package codepath.twitter.android.example.com.twitter.fragments;

import android.content.Context;
import android.os.Bundle;
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
import codepath.twitter.android.example.com.twitter.adapter.MentionsAdapter;
import codepath.twitter.android.example.com.twitter.adapter.TweetsAdapter;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import cz.msebera.android.httpclient.Header;

public class MentionsFragment extends Fragment{

    @BindView(R.id.lv_tweet)
    RecyclerView mListView;

    MentionsAdapter mAdapter;
    WrapContentLinearLayoutManager mLayoutManager;
    public List<Tweet> mMentionsList = new ArrayList<>();

    private TwitterRestClient mClient;

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tweet, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mClient = Application.getRestClient();

        mAdapter = new MentionsAdapter(mMentionsList);
        mListView.setAdapter(mAdapter);

        mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);

        populateMentionsTimelineData(1, 0);
    }

    private void populateMentionsTimelineData(final long sinceId, long maxId) {
        mClient.getMentionsTimelineInformation(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        System.out.println(mMentionsList.size());
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        mMentionsList.add(tweet);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.updateTweetList(mMentionsList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString.toString());
            }
        });
    }
}
