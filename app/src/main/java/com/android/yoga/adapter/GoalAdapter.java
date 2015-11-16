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

import com.android.volley.toolbox.ImageLoader;
import com.android.yoga.R;
import com.android.yoga.app.Applications;
import com.android.yoga.model.YogaItems;
import com.android.yoga.ui.activity.MainGoal;
import com.android.yoga.ui.base.FeedImageView;
import com.android.yoga.ui.fragment.VideoDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Hiep on 20/07/2015.
 */
public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.AppViewHolder> {
    private ArrayList<YogaItems> mYogaItems;
    private ArrayList<YogaItems> mSearch;
    ImageLoader imageLoader = Applications.getInstance().getImageLoader();
    private Context mContext;

    public GoalAdapter(Context context, ArrayList<YogaItems> yogaItem) {
        this.mContext = context;
        this.mYogaItems = yogaItem;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_layout, parent, false);
        return new AppViewHolder(itemView);
    }

    public void clear() {
        mYogaItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(GoalAdapter.AppViewHolder holder, int position) {
        if (imageLoader == null) {
            imageLoader = Applications.getInstance().getImageLoader();
        }
        final YogaItems item = mYogaItems.get(position);
        if (item.getLang().equals("Tiếng Việt") && !TextUtils.isEmpty(item.getTitle_vn())) {
            holder.name.setText(item.getTitle_vn());
        } else {
            holder.name.setText(item.getTitle());
        }
        holder.author.setText(item.getAuthor());

        // Feed image
        if (!TextUtils.isEmpty(item.getImage())) {
            try {
                holder.img.setImageUrl(item.getImage(), imageLoader);
                holder.img.setVisibility(View.VISIBLE);
                holder.img
                        .setResponseObserver(new FeedImageView.ResponseObserver() {
                            @Override
                            public void onError() {
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            holder.img.setVisibility(View.GONE);
        }
        CardView cardView = holder.vCard;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) mContext;
                Intent intent = new Intent(mContext, VideoDetailFragment.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("title_vn", item.getTitle_vn());
                intent.putExtra("image", item.getImage());
                intent.putExtra("desc", item.getDesc());
                intent.putExtra("desc_vn", item.getDesc_vn());
                intent.putExtra("proPic", item.getProfilePic());
                intent.putExtra("author", item.getAuthor());
                intent.putExtra("url", item.getUrl());
                intent.putExtra("pic", item.getPic());
                intent.putExtra("url_vn", item.getUrl_vn());
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mYogaItems.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView author;
        protected FeedImageView img;
        protected CardView vCard;

        public AppViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtName);
            author = (TextView) itemView.findViewById(R.id.txtAuthor);
            img = (FeedImageView) itemView.findViewById(R.id.imgIcon);
            vCard = (CardView) itemView.findViewById(R.id.app_card);
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
                            if (appInfo.getTitle().toLowerCase().contains(charSequence.toString()) || appInfo.getTitle_vn().toLowerCase().contains(charSequence.toString())) {//xem lai
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
                    MainGoal.setResultsMessage(false);
                } else {
                    MainGoal.setResultsMessage(true);
                }
                mYogaItems = (ArrayList<YogaItems>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
