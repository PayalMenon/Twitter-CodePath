package codepath.twitter.android.example.com.twitter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.adapter.MentionsAdapter;
import codepath.twitter.android.example.com.twitter.adapter.TweetsAdapter;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import codepath.twitter.android.example.com.twitter.utils.Constants;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.rv_search_list)
    RecyclerView mSearchView;

    private MentionsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TwitterRestClient mClient;

    private List<Tweet> mSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        mClient = new TwitterRestClient(this);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MentionsAdapter(mSearchList);

        mSearchView.setAdapter(mAdapter);
        mSearchView.setLayoutManager(mLayoutManager);

        if (getIntent() != null) {
            getSearchList(getIntent().getStringExtra(Constants.INTENT_USER_SEARCH_QUERY));
        }
    }

    private void getSearchList(String query) {

        mClient.searchTweet(query, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        System.out.println(mSearchList.size());
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        mSearchList.add(tweet);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.updateTweetList(mSearchList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray tweetArray = response.getJSONArray("statuses");
                    for (int i = 0; i < tweetArray.length(); i++) {

                        System.out.println(mSearchList.size());
                        Tweet tweet = Tweet.fromJSON(tweetArray.getJSONObject(i));
                        mSearchList.add(tweet);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.updateTweetList(mSearchList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }
        });
    }
}
