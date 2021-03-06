package codepath.twitter.android.example.com.twitter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.activity.TweetsActivity;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.utils.Utils;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetsHolder> {

    private Context mContext;
    private List<Tweet> mTweetsList;
    private TweetsActivity.TweetFragmentListener mListener;

    public TweetsAdapter(List<Tweet> tweetsList, TweetsActivity.TweetFragmentListener listener) {
        mTweetsList = tweetsList;
        mListener = listener;
    }

    @Override
    public TweetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_row_item, parent, false);
        return new TweetsHolder(view);
    }

    @Override
    public void onBindViewHolder(final TweetsHolder holder, final int position) {
        Tweet tweet = mTweetsList.get(position);

        holder.nameView.setText(tweet.user.name);
        String userName = "@" + tweet.user.screenName;
        holder.usernameView.setText(userName);
        holder.dateView.setText(Utils.getDateString(tweet.createdAt));
        holder.tweetView.setText(tweet.body);

        if (tweet.favoriteCount != 0) {
            holder.favortieView.setText(tweet.favoriteCount + "");
        }
        if (tweet.favorited) {
            holder.favortieView.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
            holder.favortieView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorited, 0, 0, 0);
        }

        if (tweet.retweetCount != 0) {
            holder.retweetView.setText(tweet.retweetCount + "");
        }
        if (tweet.retweeted) {
            holder.retweetView.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
            holder.retweetView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_retweeted, 0, 0, 0);
        }

        Glide.with(mContext).
                load(tweet.user.profileImageUrl).
                asBitmap().
                centerCrop().
                placeholder(R.drawable.ic_tweet).
                into(new BitmapImageViewTarget(holder.profileImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.profileImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

        if (tweet.imageList != null && tweet.imageList.size() > 0) {
            holder.bannerImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).
                    load(tweet.imageList.get(0)).
                    asBitmap().
                    placeholder(R.drawable.ic_tweet).
                    into(holder.bannerImageView);
        } else {
            holder.bannerImageView.setVisibility(View.GONE);
        }

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTweetImageClicked(position);
            }
        });

        holder.tweetContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTweetClicked(position);
            }
        });

        holder.favortieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTweetFavorited(position);
                holder.favortieView.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
                holder.favortieView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorited, 0, 0, 0);
            }
        });

        holder.retweetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTweetRetweeted(position);
                holder.retweetView.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
                holder.retweetView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_retweeted, 0, 0, 0);
            }
        });

        holder.replyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTweetReplied(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTweetsList.size();
    }

    public void updateTweetList(List<Tweet> list) {
        this.mTweetsList = list;
    }

    public class TweetsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView nameView;
        @BindView(R.id.tv_tweet)
        TextView tweetView;
        @BindView(R.id.tv_username)
        TextView usernameView;
        @BindView(R.id.tv_date)
        TextView dateView;
        @BindView(R.id.iv_profileImage)
        ImageView profileImage;
        @BindView(R.id.tv_reply)
        TextView replyView;
        @BindView(R.id.tv_favorite)
        TextView favortieView;
        @BindView(R.id.tv_retweet)
        TextView retweetView;
        @BindView(R.id.iv_bannerImage)
        ImageView bannerImageView;
        @BindView(R.id.item_card_view)
        CardView tweetContainerView;

        public TweetsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
