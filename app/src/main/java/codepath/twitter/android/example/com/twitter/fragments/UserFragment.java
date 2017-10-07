package codepath.twitter.android.example.com.twitter.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
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
import codepath.twitter.android.example.com.twitter.settings.TwitterSettings;
import codepath.twitter.android.example.com.twitter.utils.Constants;
import cz.msebera.android.httpclient.Header;

public class UserFragment extends Fragment {

    private TwitterRestClient mClient;

    @BindView(R.id.iv_user_image)
    ImageView profileImage;
    @BindView(R.id.rl_user_background)
    ImageView profileBackground;
    @BindView(R.id.tv_user_name)
    TextView profileName;
    @BindView(R.id.tv_user_username)
    TextView profileUsername;
    @BindView(R.id.tv_user_followers)
    TextView profileFollowers;
    @BindView(R.id.tv_user_following)
    TextView profileFollowings;
    @BindView(R.id.tv_user_tagline)
    TextView profileTagline;

    public static UserFragment newInstance(long userId, String userType, String userScreenName) {

        UserFragment fragment = new UserFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BUNDLE_KEY_USERID, userId);
        bundle.putString(Constants.BUNDLE_KEY_USERTYPE, userType);
        bundle.putString(Constants.BUNDLE_KEY_USER_SCREENNAME, userScreenName);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mClient = Application.getRestClient();

        String userType = getArguments().getString(Constants.BUNDLE_KEY_USERTYPE);
        if(Constants.USER_TYPE_PROFILE.equals(userType)) {
            getSelfInformation();
        } else {
            getUserInformation();
        }
    }

    private void populateViews(Self self){
        profileName.setText(self.name);
        String screenName = "@" + self.screenName;
        profileUsername.setText(screenName);
        profileTagline.setText(self.tagLine);

        profileFollowers.setText(Html.fromHtml(getString(R.string.followers, self.followers)));
        profileFollowings.setText(Html.fromHtml(getString(R.string.following, self.followings)));

        Glide.with(getActivity()).
                load(self.profileImageUrl).
                asBitmap().
                centerCrop().
                placeholder(R.drawable.ic_tweet).
                into(new BitmapImageViewTarget(profileImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        profileImage.setImageDrawable(circularBitmapDrawable);
                    }
                });

        Glide.with(getActivity()).
                load(self.profileBannerImageUrl).
                asBitmap().
                into(profileBackground);
    }

    public void getSelfInformation() {
        mClient.getAccountInformation(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Self self = Self.fromJson(response);

                    populateViews(self);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterApp" , responseString);
            }
        });
    }

    public void getUserInformation() {

        long userId = getArguments().getLong(Constants.BUNDLE_KEY_USERID);
        String userName = getArguments().getString(Constants.BUNDLE_KEY_USER_SCREENNAME);

        mClient.getUserInformation(userId, userName, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Self self = Self.fromJson(response);

                    populateViews(self);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterApp" , responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
