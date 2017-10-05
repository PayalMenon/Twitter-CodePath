package codepath.twitter.android.example.com.twitter.settings;

import android.content.Context;
import android.content.SharedPreferences;

import codepath.twitter.android.example.com.twitter.Application;
import codepath.twitter.android.example.com.twitter.utils.Constants;

public class TwitterSettings {

    private static TwitterSettings sInstance;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private TwitterSettings(Context context) {
        mPreferences = context.getSharedPreferences(Constants.TWEET_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static synchronized TwitterSettings getInstance() {
        if (sInstance == null) {
            sInstance = new TwitterSettings(Application.getContext());
        }
        return sInstance;
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setInteger(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public String getString(String key, String defaultVlaue) {
        return mPreferences.getString(key, defaultVlaue);
    }

    public int getInteger(String key, int defaultVlaue) {
        return mPreferences.getInt(key, defaultVlaue);
    }

    public Boolean getBoolean(String key, Boolean defaultVlaue) {
        return mPreferences.getBoolean(key, defaultVlaue);
    }

    public void clearAll() {
        mEditor.clear();
        mEditor.commit();
    }
}
