package codepath.twitter.android.example.com.twitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable{


    public long uid;
    public String name;
    public String screenName;
    public String profileImageUrl;
    public String tagLine;
    public String profileBannerImageUrl;

    public User() {}

    protected User(Parcel in) {
        uid = in.readLong();
        name = in.readString();
        screenName = in.readString();
        profileImageUrl = in.readString();
        tagLine = in.readString();
        profileBannerImageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // deserialize json
    public static User fromJson(JSONObject json) throws JSONException {
        User user = new User();

        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        user.tagLine = json.getString("description");
        if(json.has("profile_banner_url")) {
            user.profileBannerImageUrl = json.getString("profile_banner_url");
        }

        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(uid);
        parcel.writeString(name);
        parcel.writeString(screenName);
        parcel.writeString(profileImageUrl);
        parcel.writeString(tagLine);
        parcel.writeString(profileBannerImageUrl);
    }
}
