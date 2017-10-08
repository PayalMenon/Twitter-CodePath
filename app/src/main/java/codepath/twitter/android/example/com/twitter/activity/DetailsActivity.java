package codepath.twitter.android.example.com.twitter.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.adapter.ImagesAdapter;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.utils.Constants;

public class DetailsActivity extends AppCompatActivity{

    @BindView(R.id.iv_detail_image)
    ImageView mProfileImage;
    @BindView(R.id.iv_detail_background)
    ImageView mBannderImage;
    @BindView(R.id.tv_detail_name)
    TextView mName;
    @BindView(R.id.tv_detail_username)
    TextView mUserName;
    @BindView(R.id.tv_detail_body)
    TextView mBody;
    @BindView(R.id.tv_detail_favorite)
    TextView mFavorite;
    @BindView(R.id.tv_detail_retweet)
    TextView mRetweet;
    @BindView(R.id.rv_detail_tweet_images)
    RecyclerView mImagesList;

    private Tweet mTweet;
    private ImagesAdapter mAdapter;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        mTweet = getIntent().getParcelableExtra(Constants.INTENT_TWEET);
        if(mTweet != null) {
            populateViews();
        }
    }

    private void populateViews(){

        mName.setText(mTweet.user.name);
        String userName = "@" + mTweet.user.screenName;
        mUserName.setText(userName);
        mBody.setText(mTweet.body);
        mFavorite.setText(mTweet.favoriteCount + "");
        mRetweet.setText(mTweet.retweetCount + "");

        Glide.with(this).
                load(mTweet.user.profileImageUrl).
                asBitmap().
                centerCrop().
                placeholder(R.drawable.ic_tweet).
                into(new BitmapImageViewTarget(mProfileImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mProfileImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

        Glide.with(this).
                load(mTweet.user.profileBannerImageUrl).
                asBitmap().
                into(mBannderImage);

        mAdapter = new ImagesAdapter(mTweet.imageList);
        mImagesList.setAdapter(mAdapter);

        mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mImagesList.setLayoutManager(mManager);
    }
}
