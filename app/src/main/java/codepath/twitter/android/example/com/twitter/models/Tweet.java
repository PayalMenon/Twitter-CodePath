package codepath.twitter.android.example.com.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    public boolean favorited;
    public boolean retweeted;
    public int retweetCount;
    public int favoriteCount;
    public long uid;
    public User user;
    public String body;
    public String createdAt;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");

        // entities & extended_entities
        return tweet;
    }
}
