package com.android.yoga.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.yoga.R;
import com.android.yoga.app.Applications;
import com.android.yoga.model.YogaItems;
import com.android.yoga.ui.activity.AboutsActivity;
import com.android.yoga.ui.base.FeedImageView;
import com.android.yoga.ui.base.YoutubePlay;
import com.android.yoga.util.YouTubeUtils;
import com.bumptech.glide.Glide;

import java.util.Locale;

/**
 * Created by Hoang Hiep on 8/28/2015.
 */
public class VideoDetailFragment extends AppCompatActivity {
    ImageLoader imageLoader = Applications.getInstance().getImageLoader();
    String lang = Locale.getDefault().getDisplayLanguage();
    private YogaItems mItem;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getInitialConfiguration();
        this.activity = VideoDetailFragment.this;

        TextView description = (TextView) findViewById(R.id.description);
        TextView author = (TextView) findViewById(R.id.author);
        FloatingActionButton btnPlay = (FloatingActionButton) findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Context context = v.getContext();
                Intent intent = new Intent(context, YoutubePlay.class);
                intent.putExtra(YoutubePlay.VIDEO_ID, url());
                context.startActivity(intent);*/
                YouTubeUtils.showYouTubeVideo(url(), activity);
            }
        });
        FeedImageView imgDes1 = (FeedImageView) findViewById(R.id.imgDes1);
        FeedImageView imgDes2 = (FeedImageView) findViewById(R.id.imgDes2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(mItem.getImage()).centerCrop().into(imageView);
        if (lang.equals("Tiếng Việt") && !TextUtils.isEmpty(mItem.getTitle_vn())) {
            collapsingToolbar.setTitle(mItem.getTitle_vn());
        } else {
            collapsingToolbar.setTitle(mItem.getTitle());
        }

        author.setText(mItem.getAuthor());
        if (lang.equals("Tiếng Việt") && !TextUtils.isEmpty(mItem.getDesc_vn())) {
            if (!TextUtils.isEmpty(mItem.getDesc_vn())) {
                description.setText(mItem.getDesc_vn());
            } else {
                description.setText(getResources().getString(R.string.see));
            }
        } else {
            if (!TextUtils.isEmpty(mItem.getDesc())) {
                description.setText(mItem.getDesc());
            } else {
                description.setText(getResources().getString(R.string.see));
            }
        }


        if (mItem.getImage() != null && mItem.getPic() != null) {
            imgDes1.setImageUrl(mItem.getImage(), imageLoader);
            imgDes1.setVisibility(View.VISIBLE);
            imgDes1
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
            if (!mItem.getPic().equals("http://www.sieuvietesd.vn/")) {
                imgDes2.setImageUrl(mItem.getPic(), imageLoader);
                imgDes2.setVisibility(View.VISIBLE);
                imgDes2
                        .setResponseObserver(new FeedImageView.ResponseObserver() {
                            @Override
                            public void onError() {
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });
            } else if (!mItem.getProfilePic().equals("http://www.sieuvietesd.vn/")) {
                imgDes2.setImageUrl(mItem.getProfilePic(), imageLoader);
                imgDes2.setVisibility(View.VISIBLE);
                imgDes2
                        .setResponseObserver(new FeedImageView.ResponseObserver() {
                            @Override
                            public void onError() {
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });
            }
        } else {
            imgDes1.setVisibility(View.GONE);
            imgDes2.setVisibility(View.GONE);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.abouts:
                startActivity(new Intent(getApplicationContext(), AboutsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    private void getInitialConfiguration() {
        String title = getIntent().getStringExtra("title");
        String title_vn = getIntent().getStringExtra("title_vn");
        String image = getIntent().getStringExtra("image");
        String desc = getIntent().getStringExtra("desc");
        String desc_vn = getIntent().getStringExtra("desc_vn");
        String proPic = getIntent().getStringExtra("proPic");
        String author = getIntent().getStringExtra("author");
        String url = getIntent().getStringExtra("url");
        String pic = getIntent().getStringExtra("pic");
        String url_vn = getIntent().getStringExtra("url_vn");
        mItem = new YogaItems(title, title_vn, desc, desc_vn, image, author, proPic, url, url_vn, pic);

    }

    private String url() {
        String url = null;
        if (!mItem.getUrl().equals("") || !mItem.getUrl_vn().equals("")) {
            if (lang.equals("Tiếng Việt") && !TextUtils.isEmpty(mItem.getUrl_vn())) {
                url = mItem.getUrl_vn();
            } else {
                url = mItem.getUrl();
            }
        }
        return url;
    }
}
