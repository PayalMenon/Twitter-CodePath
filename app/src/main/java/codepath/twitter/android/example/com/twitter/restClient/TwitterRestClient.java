package codepath.twitter.android.example.com.twitter.restClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import codepath.twitter.android.example.com.twitter.R;
import codepath.twitter.android.example.com.twitter.models.Self;
import codepath.twitter.android.example.com.twitter.models.Tweet;
import codepath.twitter.android.example.com.twitter.settings.TwitterSettings;
import codepath.twitter.android.example.com.twitter.utils.Constants;
import cz.msebera.android.httpclient.Header;

public class TwitterRestClient extends OAuthBaseClient {

    public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "0UHPilPkqhpxEidkKhExHXIvu";       // Change this
    public static final String REST_CONSUMER_SECRET = "W1nigFyqYhuYxhqhO61380VB6DOSq73yllBbqgszPYaFQ4fDpt"; // Change this

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public TwitterRestClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    public void getHomeTimeline(long sinceId, long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (sinceId != 0) {
            params.put("since_id", sinceId);
        }
        if (maxId != 0) {
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getAccountInformation(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");

        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }

    public void searchTweet(String query, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("search/tweets.json");

        RequestParams params = new RequestParams();
        params.add("q", query);
        client.get(apiUrl, params, handler);
    }

    public void getUserInformation(long userId, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimelineInformation(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        RequestParams params = new RequestParams();

        client.get(apiUrl, params, handler);
    }

    public void getUserTimelineInformation(long userId, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);

        client.get(apiUrl, params, handler);
    }

    public void getFollowingList(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friends/list.json");

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);

        client.get(apiUrl, params, handler);
    }

    public void getFavoritesList(long userId, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/list.json");

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);

        client.get(apiUrl, params, handler);
    }

    public void getFollowersList(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("followers/list.json");

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);

        client.get(apiUrl, params, handler);
    }

    public void postTweet(Tweet tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.add("status", tweet.body);
        client.post(apiUrl, params, handler);
    }

    public void postRetweet(long tweetId, AsyncHttpResponseHandler handler) {
        String url = "statuses/retweet/" + tweetId +".json";
        String apiUrl = getApiUrl(url);

        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        client.post(apiUrl, params, handler);
    }

    public void postFavorited(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");

        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        client.post(apiUrl, params, handler);
    }

    public void postReplied(String screenName, String tweetReply, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("direct_messages/new.json");

        RequestParams params = new RequestParams();
        params.put("text", tweetReply);
        params.put("screen_name", screenName);
        client.post(apiUrl, params, handler);
    }
}
