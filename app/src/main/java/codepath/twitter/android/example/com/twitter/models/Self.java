package codepath.twitter.android.example.com.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Self {

    public String profileImageUrl;
    public String name;
    public String screenName;
    public String profileBannerImageUrl;
    public long userId;
    public int followers;
    public int followings;


    public static Self fromJson(JSONObject object) throws JSONException {
        Self self = new Self();

        self.profileImageUrl = object.getString("profile_image_url");
        self.name = object.getString("name");
        self.screenName = object.getString("screen_name");
        self.userId = object.getLong("id");
        self.followers = object.getInt("followers_count");
        self.followings = object.getInt("friends_count");
        self.profileBannerImageUrl = object.getString("profile_banner_url");

        return self;
    }
}
