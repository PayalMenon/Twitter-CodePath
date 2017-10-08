package codepath.twitter.android.example.com.twitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.Application;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.fragments.UserDataFragment;
import codepath.twitter.android.example.com.twitter.fragments.UserFavoritesFragment;
import codepath.twitter.android.example.com.twitter.fragments.UserFragment;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.utils.Constants;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.fl_info_layout)
    FrameLayout mInfoLayout;
    @BindView(R.id.fl_data_layout)
    LinearLayout mDataLayout;
    @BindView(R.id.vp_user_tweetPager)
    ViewPager mViewPager;
    @BindView(R.id.user_sliding_tabs)
    TabLayout mtabLayout;

    private UserPagerAdapter mPagerAdapter;
    private static long userId;
    private static String userScreenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        mPagerAdapter = new UserPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mtabLayout.setupWithViewPager(mViewPager);

        if (getIntent() != null) {

            userId = getIntent().getLongExtra(Constants.BUNDLE_KEY_USERID, 0);
        }

        initializeFragments();
    }

    public void launchFollowsActivity(String followType, String screenName) {
        Intent intent = new Intent(this, FollowActivity.class);
        intent.putExtra(Constants.INTENT_USER_SCREENNAME, screenName);
        intent.putExtra(Constants.INTENT_USER_FOLLOW_TYPE, followType);
        startActivity(intent);
    }

    private void initializeFragments() {

        String userType = getIntent().getStringExtra(Constants.BUNDLE_KEY_USERTYPE);
        userScreenName = getIntent().getStringExtra(Constants.BUNDLE_KEY_USER_SCREENNAME);

        UserFragment userFragment = UserFragment.newInstance(userId, userType, userScreenName);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().
                add(R.id.fl_info_layout, userFragment).
                commit();

    }

    public static class UserPagerAdapter extends FragmentPagerAdapter {

        public UserPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch(position) {
                case 0:
                    fragment = UserDataFragment.newInstance(userId, userScreenName);
                    break;

                case 1:
                    fragment = new UserFavoritesFragment().newInstance(userId, userScreenName);
                    break;

                default:
                    fragment = UserDataFragment.newInstance(userId, userScreenName);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;

            switch(position) {
                case 0:
                    title = Application.getContext().
                            getResources().
                            getString(R.string.page_title_tweets);
                    break;

                case 1:
                    title = Application.getContext().
                            getResources().
                            getString(R.string.page_title_favorite);
                    break;

                default:
                    title = Application.getContext().
                            getResources().
                            getString(R.string.page_title_tweets);
                    break;
            }

            return title;
        }
    }
}
