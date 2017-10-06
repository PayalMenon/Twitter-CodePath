package codepath.twitter.android.example.com.twitter.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

        getSelfInformation();
    }

    private void populateViews(Self self){
        profileName.setText(self.name);
        profileUsername.setText(self.screenName);
        profileTagline.setText(self.tagLine);

        String followers = getResources().getString(R.string.followers, self.followers);
        profileFollowers.setText(followers);

        String followings = getResources().getString(R.string.following, self.followings);
        profileFollowings.setText(followings);

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
}
