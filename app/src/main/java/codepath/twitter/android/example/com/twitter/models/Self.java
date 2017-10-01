package codepath.twitter.android.example.com.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Self {

    public String profileImageUrl;
    public String name;
    public String screenName;

    public static Self fromJson(JSONObject object) throws JSONException {
        Self self = new Self();

        self.profileImageUrl = object.getString("profile_image_url");
        self.name = object.getString("name");
        self.screenName = object.getString("screen_name");

        return self;
    }
}
