package codepath.twitter.android.example.com.twitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import codepath.twitter.android.example.com.twitter.adapter.MentionsAdapter;

public class Tweet {

    public boolean favorited;
    public boolean retweeted;
    public int retweetCount;
    public int favoriteCount;
    public long uid;
    public User user;
    public String body;
    public String createdAt;
    public List<String> imageList;

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

        Set<String> mediaSet = new HashSet<>();
        JSONObject entity = jsonObject.getJSONObject("entities");
        if (entity.has("media")) {
            JSONArray mediaList = entity.getJSONArray("media");
            System.out.println("media list size " + mediaList.length());
            for (int i = 0; i < mediaList.length(); i++) {
                Media media = Media.fromJson((JSONObject) mediaList.get(i));
                if (media.mediaType.equals("photo")) {
                    mediaSet.add(media.mediaUrl);
                }
            }
        }
        if(jsonObject.has("extended_entities")) {
            JSONObject extendedEntity = jsonObject.getJSONObject("extended_entities");
            if (extendedEntity.has("media")) {
                JSONArray mediaList = extendedEntity.getJSONArray("media");
                System.out.println("extended list size " + mediaList.length());
                for (int i = 0; i < mediaList.length(); i++) {
                    Media media = Media.fromJson((JSONObject) mediaList.get(i));
                    if (media.mediaType.equals("photo")) {
                        mediaSet.add(media.mediaUrl);
                    }
                }
            }
        }
        if(mediaSet.size() > 0) {
            tweet.imageList = new ArrayList<String>(mediaSet);
        }

        return tweet;
    }
}
