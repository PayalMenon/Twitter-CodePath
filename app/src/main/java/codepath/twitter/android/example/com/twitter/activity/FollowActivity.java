package codepath.twitter.android.example.com.twitter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.adapter.FollowAdapter;
import codepath.twitter.android.example.com.twitter.adapter.MentionsAdapter;
import codepath.twitter.android.example.com.twitter.models.Self;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;
import codepath.twitter.android.example.com.twitter.utils.Constants;
import cz.msebera.android.httpclient.Header;

public class FollowActivity extends AppCompatActivity {

    @BindView(R.id.rv_follow_list)
    RecyclerView mFollowView;

    private FollowAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TwitterRestClient mClient;

    private List<Self> mFollowList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        ButterKnife.bind(this);

        mClient = new TwitterRestClient(this);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new FollowAdapter(mFollowList);

        mFollowView.setAdapter(mAdapter);
        mFollowView.setLayoutManager(mLayoutManager);

        populateList();
    }

    private void populateList() {

        String followType = getIntent().getStringExtra(Constants.INTENT_USER_FOLLOW_TYPE);
        String screenName = getIntent().getStringExtra(Constants.INTENT_USER_SCREENNAME);

        if (Constants.FOLLOW_TYPE_FOLLOWER.equals(followType)) {
            getFollowersList(screenName);
        } else if (Constants.FOLLOW_TYPE_FOLLOWING.equals(followType)) {
            getFollowingList(screenName);
        }
    }

    private void getFollowersList(String screenName) {
        mClient.getFollowersList(screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                updateList(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitter Client", responseString.toString());
            }
        });
    }

    private void getFollowingList(String screenName) {
        mClient.getFollowingList(screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                updateList(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitter Client", responseString.toString());
            }
        });
    }

    private void updateList(JSONObject response) {
        try {
            JSONArray list = response.getJSONArray("users");

            for (int i = 0; i < list.length(); i++) {
                System.out.println(mFollowList.size());
                Self followInfo = Self.fromJson(list.getJSONObject(i));
                mFollowList.add(followInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.updateFollowList(mFollowList);
        mAdapter.notifyDataSetChanged();
    }
}
