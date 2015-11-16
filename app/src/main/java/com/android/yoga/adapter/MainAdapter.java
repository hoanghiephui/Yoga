package com.android.yoga.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.yoga.R;
import com.android.yoga.app.Applications;
import com.android.yoga.model.YogaItems;
import com.android.yoga.ui.base.FeedImageView;
import com.android.yoga.ui.fragment.MainFragment;
import com.android.yoga.ui.fragment.VideoDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Hiep on 8/28/2015.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.AppViewHolder> {
    private List<YogaItems> mYogaItems;
    private List<YogaItems> mSearch;
    private Context mContext;
    ImageLoader imageLoader = Applications.getInstance().getImageLoader();

    public MainAdapter(Context mContext, ArrayList<YogaItems> mYogaItems) {
        this.mContext = mContext;
        this.mYogaItems = mYogaItems;
    }

    public void clear() {
        if (mYogaItems.size() > 0) {
            mYogaItems.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.yoga_items, viewGroup, false);
        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        final YogaItems items = mYogaItems.get(i);
        if (imageLoader == null) {
            imageLoader = Applications.getInstance().getImageLoader();
        }
        if (items.getLang().equals("Tiếng Việt") && !TextUtils.isEmpty(items.getTitle_vn())) {
            appViewHolder.txtTitle.setText(items.getTitle_vn());
        } else {
            appViewHolder.txtTitle.setText(items.getTitle());
        }

        appViewHolder.txtAuthor.setText(items.getAuthor());

        if (!items.getProfilePic().equals("http://www.sieuvietesd.vn/") || !items.getPic().equals("http://www.sieuvietesd.vn/") || !items.getImage().equals("http://www.sieuvietesd.vn/")) {
            if (!items.getProfilePic().equals("http://www.sieuvietesd.vn/")) {
                appViewHolder.profilePic.setImageUrl(items.getProfilePic(), imageLoader);
                appViewHolder.profilePic.setVisibility(View.VISIBLE);
            } else if (!items.getPic().equals("http://www.sieuvietesd.vn/")) {
                appViewHolder.profilePic.setImageUrl(items.getPic(), imageLoader);
                appViewHolder.profilePic.setVisibility(View.VISIBLE);
            } else if (items.getProfilePic().equals("http://www.sieuvietesd.vn/") && items.getPic().equals("http://www.sieuvietesd.vn/")) {
                appViewHolder.profilePic.setImageUrl(items.getImage(), imageLoader);
                appViewHolder.profilePic.setVisibility(View.VISIBLE);
            }
            if (!items.getImage().equals("http://www.sieuvietesd.vn/")) {
                appViewHolder.imageView.setImageUrl(items.getImage(), imageLoader);
                appViewHolder.imageView.setVisibility(View.VISIBLE);
                appViewHolder.imageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                    @Override
                    public void onError() {
                        Toast.makeText(mContext, "null image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
            }
        } else {
            appViewHolder.profilePic.setVisibility(View.GONE);
            appViewHolder.imageView.setVisibility(View.GONE);
        }

        CardView cardView = appViewHolder.mCardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) mContext;
                Intent intent = new Intent(mContext, VideoDetailFragment.class);
                intent.putExtra("title", items.getTitle());
                intent.putExtra("title_vn", items.getTitle_vn());
                intent.putExtra("image", items.getImage());
                intent.putExtra("desc", items.getDesc());
                intent.putExtra("desc_vn", items.getDesc_vn());
                intent.putExtra("proPic", items.getProfilePic());
                intent.putExtra("author", items.getAuthor());
                intent.putExtra("url", items.getUrl());
                intent.putExtra("pic", items.getPic());
                intent.putExtra("url_vn", items.getUrl_vn());
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });
    }

    @Override
    public int getItemCount() throws NullPointerException {
        return (null != mYogaItems ? mYogaItems.size() : 0);
    }


    public class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle, txtAuthor;
        protected NetworkImageView profilePic;
        protected FeedImageView imageView;
        protected CardView mCardView;

        public AppViewHolder(View itemView) {
            super(itemView);
            this.txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            this.txtAuthor = (TextView) itemView.findViewById(R.id.txtAuthor);
            this.profilePic = (NetworkImageView) itemView.findViewById(R.id.profilePic);
            this.imageView = (FeedImageView) itemView.findViewById(R.id.imageView);
            mCardView = (CardView) itemView.findViewById(R.id.app_cards);

        }
    }

    //tìm kiếm
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<YogaItems> results = new ArrayList<>();
                if (mSearch == null) {
                    mSearch = mYogaItems;
                }
                if (charSequence != null) {
                    if (mSearch != null && mSearch.size() > 0) {
                        for (final YogaItems appInfo : mSearch) {
                            if (appInfo.getTitle().toLowerCase().contains(charSequence.toString())) {
                                results.add(appInfo);
                            }
                        }

                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    MainFragment.setResultsMessage(false);
                } else {
                    MainFragment.setResultsMessage(true);
                }
                mYogaItems = (ArrayList<YogaItems>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
