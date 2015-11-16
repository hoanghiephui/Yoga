package com.android.yoga.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.yoga.R;
import com.android.yoga.adapter.MainAdapter;
import com.android.yoga.app.Applications;
import com.android.yoga.model.YogaItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hoang Hiep on 8/17/2015.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    View rootView;
    @Bind(R.id.txterr)
    TextView mTxtErr;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private static LinearLayout noResults;
    private MainAdapter adapter;
    private static String url = "";
    private Context mContext;
    String lang = Locale.getDefault().getDisplayLanguage();
    private ArrayList<YogaItems> mYogaItems;

    public static MainFragment newInstance(String text) {
        MainFragment fragment = new MainFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        fragment.setArguments(mBundle);
        return fragment;
    }

    //một hàm với kiểu trả về kiểu string khi truyền vào một tham số phù hợp
    private String setUrl(String url) {
        try {
            if ("Backbends".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/backbends/type";
            } else if ("Balances".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/balances/type";
            } else if ("Forward Bends".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/forward/type";
            } else if ("Inverted".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/inverted/type";
            } else if ("Lying On The Back".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/lyingb/type";
            } else if ("Lying On The Stomach".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/lyings/type";
            } else if ("Sitting and Twisting".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/sitting/type";
            } else if ("Standing".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/standing/type";
            } else if ("All".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://sieuvietesd.vn/yoga/web/api/all";
            } else if ("Beginner".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/beginner/level";
            } else if ("Intermediate".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/inter/level";
            } else if ("Advanced".equals(getArguments().getString(TEXT_FRAGMENT))) {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/advanced/level";
            } else if ("duration".equals(getArguments().getString(TEXT_FRAGMENT))) {
                if (lang.equals("Tiếng Việt")) {
                    url = "http://www.sieuvietesd.vn/yoga/web/api/by/pro/full";
                } else {
                    url = "http://www.sieuvietesd.vn/yoga/web/api/by/full/eng";
                }
            } else {
                url = "http://www.sieuvietesd.vn/yoga/web/api/by/pro/full";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this.getActivity();
        mYogaItems = new ArrayList<>();
    }

    //Khai báo các layout cũng như các đối tượng trong layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        noResults = (LinearLayout) rootView.findViewById(R.id.noResults);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    //hàm dùng để gọi đến một hàm xử lý việc parse JSON, và xuất dữ liệu sang adapter
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         getUrl(setUrl(url));

                                     }
                                 }
        );

        //adapter
        adapter = new MainAdapter(mContext, mYogaItems);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
        mSwipeRefreshLayout.setRefreshing(true);
        // making fresh volley request and getting json
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                VolleyLog.d("Yoga", "Response: " + response.toString());
                if (response.length() > 0) {
                    parseJsonFeed(response);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mTxtErr.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mTxtErr.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (getActivity() != null) {
                        if (error instanceof NetworkError) {
                            mTxtErr.setVisibility(View.VISIBLE);
                            mTxtErr.setText(getString(R.string.NetworkError));
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else if (error instanceof ServerError) {
                            mTxtErr.setVisibility(View.VISIBLE);
                            mTxtErr.setText(getString(R.string.servererr));
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else if (error instanceof AuthFailureError) {
                            mTxtErr.setVisibility(View.VISIBLE);
                            mTxtErr.setText(getString(R.string.AuthFailureError));
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else if (error instanceof ParseError) {
                            mTxtErr.setVisibility(View.VISIBLE);
                            mTxtErr.setText(getString(R.string.ParseError));
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else if (error instanceof TimeoutError) {
                            mTxtErr.setVisibility(View.VISIBLE);
                            mTxtErr.setText(getString(R.string.TimeoutError));
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            mTxtErr.setVisibility(View.VISIBLE);
                            mTxtErr.setText(getString(R.string.err));
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                    Log.e("Yoga", "Server Error: " + error.getMessage());

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        // Adding request to volley request queue
        Applications.getInstance().addToRequestQueue(req);
    }


    private void parseJsonFeed(JSONArray response) {
        try {
            //JSONArray feedArray = response.getJSONArray("feed");
            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject feedObj = response.getJSONObject(i);
                    YogaItems item = new YogaItems();
                    item.setId(feedObj.getInt("id"));
                    item.setTitle(feedObj.getString("name"));
                    String nameVn = feedObj.isNull("name_vn") ? null : feedObj.getString("name_vn");
                    item.setTitle_vn(nameVn);

                    // Image might be null sometimes
                    String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                    item.setImage(image);
                    item.setDesc(feedObj.getString("status"));

                    String desc_vn = feedObj.isNull("status_vn") ? null : feedObj.getString("status_vn");
                    item.setDesc_vn(desc_vn);

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
                    if (feedObj.getInt("id") > 0) {
                        mYogaItems.add(item);
                    }
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            // notify data changes to list adapater
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setResultsMessage(Boolean result) {
        if (result) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        getUrl(setUrl(url));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            ((MainAdapter) mRecyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((MainAdapter) mRecyclerView.getAdapter()).getFilter().filter(newText.toLowerCase());
        }
        return false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //Select search item
        MenuItem item = menu.findItem(R.id.share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Yoga");
        String share = "Yoga Everyone \n https://play.google.com/store/apps/details?id=com.yoga.video.tutor";
        intent.putExtra(Intent.EXTRA_TEXT, share);
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(intent);
        } else {
            Toast.makeText(getActivity(), " null", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                menuSearch(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void menuSearch(MenuItem item) {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }
}
