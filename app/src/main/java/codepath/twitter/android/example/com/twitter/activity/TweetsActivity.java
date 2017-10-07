package codepath.twitter.android.example.com.twitter.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.Application;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.fragments.MentionsFragment;
import codepath.twitter.android.example.com.twitter.fragments.NewTweetFragment;
import codepath.twitter.android.example.com.twitter.fragments.TweetFragment;
import codepath.twitter.android.example.com.twitter.fragments.UserFragment;
import codepath.twitter.android.example.com.twitter.models.Self;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import codepath.twitter.android.example.com.twitter.settings.TwitterSettings;
import codepath.twitter.android.example.com.twitter.utils.Constants;
import cz.msebera.android.httpclient.Header;

public class TweetsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mActionBar;
    @BindView(R.id.sl_tweetSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.vp_tweetPager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout mtabLayout;

    private PagerAdapter mPagerAdapter;
    private TweetFragmentListener mTweetFragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        ButterKnife.bind(this);

        setSupportActionBar(mActionBar);
        setSwipeRefresh();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mtabLayout.setupWithViewPager(mViewPager);
        getSelfInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_tweet) {

            FragmentManager manager = getSupportFragmentManager();
            NewTweetFragment fragment = new NewTweetFragment();

            fragment.show(manager, Constants.NEW_TWEET_FRAGMENT);
            return true;
        }
        if(item.getItemId() == R.id.action_profileInfo) {

            TwitterSettings settings = TwitterSettings.getInstance();
            launchUserActivity(settings.getLong(Constants.SELF_USER_ID, 0),
                    Constants.USER_TYPE_PROFILE, null);
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchUserActivity(long userId, String userType, String userScreenName) {

        Intent userIntent = new Intent(this, UserActivity.class);
        userIntent.putExtra(Constants.BUNDLE_KEY_USERID, userId);
        userIntent.putExtra(Constants.BUNDLE_KEY_USERTYPE, userType);
        userIntent.putExtra(Constants.BUNDLE_KEY_USER_SCREENNAME, userScreenName);
        startActivity(userIntent);
    }

    public void setTweetFragmentListener(TweetFragmentListener listener) {

        this.mTweetFragmentListener = listener;
    }

    private void setSwipeRefresh() {
        int colorRed = getResources().getColor(R.color.colorTextRed);
        mSwipeLayout.setColorSchemeColors(colorRed);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentManager manager = getSupportFragmentManager();
                TweetFragment fragment = (TweetFragment) manager.findFragmentByTag(Constants.TWEET_FRAGMENT);
                if(fragment.mTweetsList != null && fragment.mTweetsList.size() > 0) {
                    long sinceId = fragment.mTweetsList.get(0).uid;
                    fragment.refreshTimeLineData(sinceId, 0);
                }
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    private void addTweetFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        TweetFragment fragment = new TweetFragment();
        transaction.add(R.id.fc_list, fragment, Constants.TWEET_FRAGMENT);
        transaction.commit();

    }

    public void addTweet(Tweet tweet) {

        mTweetFragmentListener.addTweetandRefresh(tweet);

        FragmentManager manager = getSupportFragmentManager();

        NewTweetFragment fragment = (NewTweetFragment) manager.findFragmentByTag(Constants.NEW_TWEET_FRAGMENT);
        manager.beginTransaction().remove(fragment).commit();

        TwitterRestClient client = Application.getRestClient();
        client.postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("TwitterApp", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("TwitterApp", responseString);
            }
        });
    }

    public interface TweetFragmentListener {

        void addTweetandRefresh(Tweet newTweet);
        void onTweetClicked(int position);

    }

    private void getSelfInformation() {
        TwitterRestClient client = new TwitterRestClient(this);
        client.getAccountInformation(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Self self = Self.fromJson(response);
                    TwitterSettings settings = TwitterSettings.getInstance();
                    settings.setLong(Constants.SELF_USER_ID, self.userId);

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

    public static class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch(position) {
                case 0:
                    fragment = new TweetFragment();
                    break;

                case 1:
                    fragment = new MentionsFragment();
                    break;

                default:
                    fragment = new TweetFragment();
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
                            getString(R.string.page_title_home);
                    break;

                case 1:
                    title = Application.getContext().
                            getResources().
                            getString(R.string.page_title_mentions);
                    break;

                default:
                    title = Application.getContext().
                            getResources().
                            getString(R.string.page_title_home);
                    break;
            }

            return title;
        }
    }
}
