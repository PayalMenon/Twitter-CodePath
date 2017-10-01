package codepath.twitter.android.example.com.twitter.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.Application;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.activity.TweetsActivity;
import codepath.twitter.android.example.com.twitter.adapter.TweetsAdapter;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import cz.msebera.android.httpclient.Header;

public class TweetFragment extends Fragment implements TweetsActivity.TweetFragmentListener {

    @BindView(R.id.lv_tweet)
    RecyclerView mListView;

    TweetsAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    List<Tweet> mTweetsList = new ArrayList<>();

    TwitterRestClient mClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tweet, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mAdapter = new TweetsAdapter(getActivity(), mTweetsList);
        mListView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);

        ((TweetsActivity) getActivity()).getSupportActionBar().show();
        ((TweetsActivity) getActivity()).setTweetFragmentListener(this);

        mClient = Application.getRestClient();

        if (mTweetsList.size() == 0) {

            populateTImelineData(1, 0);
        }

        mListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (currentPosition == (mTweetsList.size() - 1)) {
                    Tweet tweet = mTweetsList.get(mTweetsList.size() - 1);
                    populateTImelineData(0, (tweet.uid - 1));
                }
            }
        });
    }

    @Override
    public void addTweetandRefresh(Tweet newTweet) {
        mTweetsList.add(0, newTweet);
        mAdapter.updateTweetList(mTweetsList);
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    private void populateTImelineData(long sinceId, long maxId) {
        mClient.getHomeTimeline(sinceId, maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        mTweetsList.add(tweet);
                        mAdapter.updateTweetList(mTweetsList);
                        mAdapter.notifyItemInserted(mTweetsList.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString.toString());
            }
        });
    }
}
