package codepath.twitter.android.example.com.twitter.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.Application;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.activity.TweetsActivity;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.models.User;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import codepath.twitter.android.example.com.twitter.settings.TwitterSettings;
import codepath.twitter.android.example.com.twitter.utils.Constants;

public class UserFragment extends Fragment implements TwitterRestClient.SelfInformationListener {

    private TwitterRestClient mClient;
    private TwitterSettings mSettings;

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
        mClient.setSelfListener(this);

        mSettings = TwitterSettings.getInstance();
        if(!mSettings.getBoolean(Constants.SELF_INFO_POPULATED, false)) {
            mClient.getSelfInformation();
        } else {
            populateViews();
        }
    }

    @Override
    public void onSuccess() {
        populateViews();
    }

    @Override
    public void onFailuer() {

    }

    private void populateViews(){
        profileName.setText(mSettings.getString(Constants.SELF_NAME, null));
        profileUsername.setText(mSettings.getString(Constants.SELF_USERNAME, null));

        String followers = getResources().getString(R.string.followers,
                mSettings.getInteger(Constants.SELF_FOLLOWERS, 0));
        profileFollowers.setText(followers);

        String followings = getResources().getString(R.string.following,
                mSettings.getInteger(Constants.SELF_FOLLOWINGS, 0));

        profileFollowings.setText(followings);

        Glide.with(getActivity()).
                load(mSettings.getString(Constants.SELF_PROFILE_IMAGE, null)).
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
                load(mSettings.getString(Constants.SELF_PROFILE_BANNER, null)).
                asBitmap().
                into(profileBackground);
    }
}
