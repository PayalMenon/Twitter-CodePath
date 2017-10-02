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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.models.Tweet;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetsHolder> {

    private Context mContext;
    private List<Tweet> mTweetsList;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    public TweetsAdapter(Context context, List<Tweet> tweetsList) {
        mContext = context;
        mTweetsList = tweetsList;
    }

    @Override
    public TweetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_row_item, parent, false);
        return new TweetsHolder(view);
    }

    @Override
    public void onBindViewHolder(final TweetsHolder holder, int position) {
        Tweet tweet = mTweetsList.get(position);

        holder.nameView.setText(tweet.user.name);
        holder.usernameView.setText(tweet.user.screenName);
        holder.dateView.setText(getDateString(tweet.createdAt));
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

    }

    @Override
    public int getItemCount() {
        return mTweetsList.size();
    }

    public void updateTweetList(List<Tweet> list) {
        this.mTweetsList = list;
    }

    private String getDateString(String dateString) {

        if ("now".equals(dateString)) {
            return "now";
        }
        String displayDate = null;
        try {
            Date parsedDate = (Date) sdf.parse(dateString);
            Date today = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            SimpleDateFormat dayFormat = new SimpleDateFormat("ddMMM");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            SimpleDateFormat minFormat = new SimpleDateFormat("mm");

            displayDate = dayFormat.format(parsedDate);

            int inputDate = Integer.parseInt(dateFormat.format(parsedDate));
            int currentDate = Integer.parseInt(dateFormat.format(today));

            if (inputDate == currentDate) {
                int inputHour = Integer.parseInt(hourFormat.format(parsedDate));
                int currentHour = Integer.parseInt(hourFormat.format(today));
                if (inputHour == currentHour) {
                    displayDate = minFormat.format(parsedDate) + "m";
                } else {
                    displayDate = hourFormat.format(parsedDate) + "h";
                }
            } else {
                displayDate = dayFormat.format(parsedDate);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return displayDate;
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

        public TweetsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
