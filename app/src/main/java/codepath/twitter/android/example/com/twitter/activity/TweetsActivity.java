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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
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

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

  //      searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint(getResources().getString(R.string.search_text));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(TweetsActivity.this, SearchActivity.class);
                intent.putExtra(Constants.INTENT_USER_SEARCH_QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_tweet) {

            FragmentManager manager = getSupportFragmentManager();
            NewTweetFragment fragment = NewTweetFragment.getInstance(null, Constants.NEW_TWEET_MODE_NEW);

            fragment.show(manager, Constants.NEW_TWEET_FRAGMENT);
            return true;
        }
        if(item.getItemId() == R.id.action_profileInfo) {

            TwitterSettings settings = TwitterSettings.getInstance();
            launchUserActivity(settings.getLong(Constants.SELF_USER_ID, 0),
                    Constants.USER_TYPE_PROFILE, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchUserActivity(long userId, String userType, String userScreenName) {

        Intent userIntent = new Intent(this, UserActivity.class);
        userIntent.putExtra(Constants.INTENT_USER_ID, userId);
        userIntent.putExtra(Constants.INTENT_USER_TYPE, userType);
        userIntent.putExtra(Constants.INTENT_USER_SCREENNAME, userScreenName);
        startActivity(userIntent);
    }

    public void launchDetailsActivity(Tweet tweet) {

        Intent userIntent = new Intent(this, DetailsActivity.class);
        userIntent.putExtra(Constants.INTENT_TWEET, tweet);
        startActivity(userIntent);
    }

    public void postRetweet(long tweetId) {

        TwitterRestClient client = new TwitterRestClient(this);
        client.postRetweet(tweetId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
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

    public void postFavorite(long tweetId) {

        TwitterRestClient client = new TwitterRestClient(this);
        client.postFavorited(tweetId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
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

    public void postReplied(Tweet tweet) {
        FragmentManager manager = getSupportFragmentManager();
        NewTweetFragment fragment = NewTweetFragment.getInstance(tweet, Constants.NEW_TWEET_MODE_REPLY);

        fragment.show(manager, Constants.NEW_TWEET_FRAGMENT);
    }

    public void setTweetFragmentListener(TweetFragmentListener listener) {

        this.mTweetFragmentListener = listener;
    }

    public void addTweet(Tweet tweet, String type) {

        mTweetFragmentListener.addTweetandRefresh(tweet);

        FragmentManager manager = getSupportFragmentManager();

        NewTweetFragment fragment = (NewTweetFragment) manager.findFragmentByTag(Constants.NEW_TWEET_FRAGMENT);
        manager.beginTransaction().remove(fragment).commit();

        TwitterRestClient client = Application.getRestClient();

        if (Constants.NEW_TWEET_MODE_NEW.equals(type)) {
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
        } else {

            client.postReplied(tweet.user.screenName, tweet.body, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
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

    public interface TweetFragmentListener {

        void addTweetandRefresh(Tweet newTweet);
        void onTweetClicked(int position);
        void onTweetImageClicked(int position);
        void onTweetFavorited(int position);
        void onTweetRetweeted(int position);
        void onTweetReplied(int position);

    }
}
