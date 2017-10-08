package codepath.twitter.android.example.com.twitter.fragments;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.Application;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.activity.TweetsActivity;
import codepath.twitter.android.example.com.twitter.models.Self;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.models.User;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import codepath.twitter.android.example.com.twitter.utils.Constants;
import cz.msebera.android.httpclient.Header;

public class NewTweetFragment extends DialogFragment {

    @BindView(R.id.bt_tweet)
    FloatingActionButton mSendTweet;
    @BindView(R.id.et_tweet)
    EditText mAddNewTweet;
    @BindView(R.id.iv_new_profileImage)
    ImageView mProfileImageView;
    @BindView(R.id.tv_new_name)
    TextView mUserName;

    private TwitterRestClient mClient;
    private Self mSelfInformation;

    public static NewTweetFragment getInstance(Tweet tweet, String mode) {
        NewTweetFragment fragment = new NewTweetFragment();

        Bundle args = new Bundle();
        args.putString("mode", mode);
        if(tweet != null) {
            args.putParcelable("tweet", tweet);
        }

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_tweet, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mClient = Application.getRestClient();
        getSelfInformation();

        if(Constants.NEW_TWEET_MODE_REPLY.equals(getArguments().getString("mode"))) {
            Tweet tweet = getArguments().getParcelable("tweet");
            String replyTo = getString(R.string.reply_to, tweet.user.screenName);
            mUserName.setText(replyTo);
            mUserName.setVisibility(View.VISIBLE);
        } else {
            mUserName.setVisibility(View.GONE);
        }

        mSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tweet tweet = new Tweet();
                tweet.user = new User();

                if(Constants.NEW_TWEET_MODE_NEW.equals(getArguments().getString("mode"))) {
                    tweet.user.name = mSelfInformation.name;
                    tweet.user.screenName = mSelfInformation.screenName;
                    tweet.user.profileImageUrl = mSelfInformation.profileImageUrl;
                    tweet.body = mAddNewTweet.getText().toString();
                    tweet.createdAt = "now";
                    ((TweetsActivity) getActivity()).addTweet(tweet, (getArguments().getString("mode")));
                } else {
                    Tweet userTweet = getArguments().getParcelable("tweet");
                    tweet.user.name = mSelfInformation.name;
                    tweet.user.screenName = mSelfInformation.screenName;
                    tweet.user.profileImageUrl = mSelfInformation.profileImageUrl;
                    String body = "@" + userTweet.user.screenName + " " + mAddNewTweet.getText().toString();
                    tweet.body = body;
                    tweet.createdAt = "now";
                    ((TweetsActivity) getActivity()).addTweet(tweet, (getArguments().getString("mode")));
                }

            }
        });
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.75), (int) (size.y * 0.65));
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    private void getSelfInformation() {
        mClient.getAccountInformation(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    mSelfInformation = Self.fromJson(response);
                    Glide.with(getActivity()).
                            load(mSelfInformation.profileImageUrl).
                            asBitmap().
                            centerCrop().
                            placeholder(R.drawable.ic_tweet).
                            into(new BitmapImageViewTarget(mProfileImageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    mProfileImageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
