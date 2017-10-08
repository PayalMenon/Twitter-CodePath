package codepath.twitter.android.example.com.twitter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.models.Self;
import codepath.twitter.android.example.com.twitter.models.Tweet;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder>{

    private List<Self> mFollowList;
    private Context mContext;

    public FollowAdapter(List<Self> followList){
        this.mFollowList = followList;
    }

    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_row_item, parent, false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowViewHolder holder, int position) {
        Self followInfo = mFollowList.get(position);

        holder.mFollowName.setText(followInfo.name);
        String username = "@" + followInfo.screenName;
        holder.mFollowUsername.setText(username);
        holder.mFollowTagline.setText(followInfo.tagLine);

        Glide.with(mContext).
                load(followInfo.profileImageUrl).
                asBitmap().
                centerCrop().
                placeholder(R.drawable.ic_tweet).
                into(new BitmapImageViewTarget(holder.mProfileImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.mProfileImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return mFollowList.size();
    }

    public void updateFollowList(List<Self> list) {
        this.mFollowList = list;
    }

    public class FollowViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_followImage)
        ImageView mProfileImage;
        @BindView(R.id.tv_follow_name)
        TextView mFollowName;
        @BindView(R.id.tv_follow_username)
        TextView mFollowUsername;
        @BindView(R.id.tv_follow_tagline)
        TextView mFollowTagline;

        public FollowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
