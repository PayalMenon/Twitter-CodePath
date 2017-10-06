package codepath.twitter.android.example.com.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Media {

    public String mediaUrl;
    public String mediaType;

    public static Media fromJson(JSONObject json) throws JSONException {
        Media media = new Media();

        media.mediaUrl = json.getString("media_url");
        media.mediaType = json.getString("type");

        return media;
    }
}
