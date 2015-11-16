package com.android.yoga.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.yoga.R;
import com.android.yoga.adapter.GoalAdapter;
import com.android.yoga.app.Applications;
import com.android.yoga.model.YogaItems;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hoang Hiep on 9/8/2015.
 */
public class MainGoal extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GoalAdapter listAdapter;
    private ArrayList<YogaItems> mYogaItem;
    TextView txtErr;
    private Context context;
    private SearchView searchView;
    private static LinearLayout noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        this.context = this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.banner).centerCrop().into(imageView);
        collapsingToolbar.setTitle("Beginner Bala Yoga for Back and Abs");
        setView();
    }

    private void setView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noResults = (LinearLayout) findViewById(R.id.noResults);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        mYogaItem = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getUrl("http://www.sieuvietesd.vn/yoga/web/api/by/beginner/goal");

                                    }
                                }
        );

        //adapter

        listAdapter = new GoalAdapter(MainGoal.this, mYogaItem);
        mRecyclerView.setAdapter(listAdapter);
        txtErr = (TextView) findViewById(R.id.txterr);

    }

    @Override
    public void onRefresh() {
        listAdapter.clear();
        fetchFeed("http://www.sieuvietesd.vn/yoga/web/api/by/beginner/goal");

    }

    private void getUrl(String url) {
        // We first check for cached request

        Cache cache = Applications.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONArray(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            fetchFeed(url);
        }
    }

    private void fetchFeed(String url) {
        swipeRefreshLayout.setRefreshing(true);
        // making fresh volley request and getting json
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                VolleyLog.d("Hiep", "Response: " + response.toString());
                if (response.length() > 0) {
                    parseJsonFeed(response);
                    swipeRefreshLayout.setRefreshing(false);
                    txtErr.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    txtErr.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error instanceof NetworkError) {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.NetworkError));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    } else if (error instanceof ServerError) {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.servererr));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    } else if (error instanceof AuthFailureError) {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.AuthFailureError));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    } else if (error instanceof ParseError) {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.ParseError));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    } else if (error instanceof NoConnectionError) {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.NoConnectionError));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    } else if (error instanceof TimeoutError) {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.TimeoutError));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        txtErr.setVisibility(View.VISIBLE);
                        txtErr.setText(getString(R.string.err));
                        mRecyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    Log.e("Hiep", "Server Error: " + error.getMessage());

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
        // Adding request to volley request queue
        Applications.getInstance().addToRequestQueue(req);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     *
     * @param response
     */
    private void parseJsonFeed(JSONArray response) {
        try {
            //JSONArray feedArray = response.getJSONArray("feed");
            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject feedObj = response.getJSONObject(i);
                    YogaItems item = new YogaItems();
                    /*int id = feedObj.getInt("id");
                    String name = feedObj.getString("name");
					String status = feedObj.getString("status");
					String profilePic = feedObj.getString("profilePic");
					String author = feedObj.getString("author");
					String pic = feedObj.getString("pic");*/

                    item.setId(feedObj.getInt("id"));
                    item.setTitle(feedObj.getString("name"));
                    String nameVn = feedObj.isNull("name_vn") ? null : feedObj.getString("name_vn");
                    item.setTitle_vn(nameVn);

                    // Image might be null sometimes
                    String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                    item.setImage(image);
                    item.setDesc(feedObj.getString("status"));

                    String status_vn = feedObj.isNull("status_vn") ? null : feedObj.getString("status_vn");
                    item.setDesc_vn(status_vn);

                    item.setProfilePic(feedObj.getString("profilePic"));
                    item.setAuthor(feedObj.getString("author"));

                    // url might be null sometimes
                    String feedUrl = feedObj.isNull("url") ? null : feedObj
                            .getString("url");
                    item.setUrl(feedUrl);
                    String url_vn = feedObj.isNull("url_vn") ? null : feedObj
                            .getString("url_vn");
                    item.setUrl_vn(url_vn);
                    String mPic = feedObj.isNull("pic") ? null : feedObj.getString("pic");
                    item.setPic(mPic);

                    String lang = Locale.getDefault().getDisplayLanguage();
                    item.setLang(lang);
                    if (!item.equals("")) {
                        mYogaItem.add(item);
                    }
                }
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }

            // notify data changes to list adapater


            listAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            ((GoalAdapter) mRecyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((GoalAdapter) mRecyclerView.getAdapter()).getFilter().filter(newText.toLowerCase());
            if (mRecyclerView.getAdapter().getItemCount() < 0) {
                noResults.setVisibility(View.VISIBLE);
            } else {
                noResults.setVisibility(View.GONE);
            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem;
        searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setResultsMessage(Boolean result) {
        if (result) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);

    }
}
