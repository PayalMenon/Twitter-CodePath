package codepath.twitter.android.example.com.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {


    public long uid;
    public String name;
    public String screenName;
    public String profileImageUrl;
    public String profileBannerImageUrl;

    // deserialize json
    public static User fromJson(JSONObject json) throws JSONException {
        User user = new User();

        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");

        return user;
    }
}
