package codepath.twitter.android.example.com.twitter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import twitter.android.example.com.twitter.Application;
import twitter.android.example.com.twitter.R;
import twitter.android.example.com.twitter.fragments.NewTweetFragment;
import twitter.android.example.com.twitter.fragments.TweetFragment;
import twitter.android.example.com.twitter.models.Tweet;
import twitter.android.example.com.twitter.restClient.TwitterRestClient;
import twitter.android.example.com.twitter.utils.Constants;

public class TweetsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mActionBar;

    private TweetFragmentListener mTweetFragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        ButterKnife.bind(this);

        setSupportActionBar(mActionBar);

        addListFragment();
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
            FragmentTransaction transaction = manager.beginTransaction();
            NewTweetFragment fragment = new NewTweetFragment();


            transaction.addToBackStack(Constants.TWEET_FRAGMENT);
            transaction.replace(R.id.fc_list, fragment, Constants.NEW_TWEET_FRAGMENT);
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            TweetFragment listFragment = (TweetFragment) manager.findFragmentByTag(Constants.TWEET_FRAGMENT);
            manager.beginTransaction().remove(listFragment).commit();
        }
    }

    public void setTweetFragmentListener(TweetFragmentListener listener) {

        this.mTweetFragmentListener = listener;
    }

    private void addListFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        TweetFragment fragment = (TweetFragment) manager.findFragmentByTag(Constants.TWEET_FRAGMENT);

        if (fragment == null) {

            fragment = new TweetFragment();
            transaction.add(R.id.fc_list, fragment, Constants.TWEET_FRAGMENT);
            transaction.commit();
        }
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

    }
}
