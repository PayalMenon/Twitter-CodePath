package codepath.twitter.android.example.com.twitter;

import android.content.Context;

import codepath.twitter.android.example.com.twitter.restClient.TwitterRestClient;

public class Application extends android.app.Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        this.sContext = getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }

    public static TwitterRestClient getRestClient() {
        return (TwitterRestClient) TwitterRestClient.getInstance(TwitterRestClient.class, sContext);
    }

}
