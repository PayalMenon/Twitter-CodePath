package codepath.twitter.android.example.com.twitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;

import com.codepath.oauth.OAuthLoginActionBarActivity;

import twitter.android.example.com.twitter.R;
import twitter.android.example.com.twitter.restClient.TwitterRestClient;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterRestClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, TweetsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(Exception e) {
        System.out.println("failed in login task");
    }

    public void loginToRest(View view) {
        getClient().connect();
    }
}
