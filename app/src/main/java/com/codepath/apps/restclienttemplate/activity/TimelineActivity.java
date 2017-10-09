package com.codepath.apps.restclienttemplate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.InfiniteScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.adapters.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.ComposeDialogFragment;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener, ComposeDialogFragment.ComposeDialogListener {

    TwitterClient client;
    User user;
    String screenName;

    TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.twitter_icon);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = TwitterApp.getRestClient();
        getUser();

        // get the view pager
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vp.setAdapter(tweetsPagerAdapter);

        // setup the tablayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.compose) {
            FragmentManager manager = getSupportFragmentManager();
            ComposeDialogFragment dialog = new ComposeDialogFragment();
            dialog.data(user);
            dialog.show(manager, "Filtered");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item){
        // launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        // launch other users profile view
        Intent i = new Intent(this, OthersProfileActivity.class);
        i.putExtra("screen_name", tweet.getUser().getScreenName());
        startActivity(i);
        // Toast.makeText(this, tweet.getBody(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishEditDialog(Tweet tweet) {
        // launch other users profile view
        postTweet(tweet.getBody());
        Log.d("onFinishEditDialog: ", "New Composed tweet "+ tweet.getBody());
        // Toast.makeText(this, tweet.getBody(), Toast.LENGTH_SHORT).show();
    }

    public void postTweet(String tweet){
        client.postTweet(tweet, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Tweet response object: ", response.toString());
                Tweet tweet = null;
                try {
                    tweet = Tweet.fromJSON(response);
                    HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) tweetsPagerAdapter.getItem(0);
                    homeTimelineFragment.insertTweetAtTop(tweet);
                    tweetsPagerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    public void getUser(){
        client.getAppUserSettings(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("AppUser", response.toString());
                try {
                    screenName = response.getString("screen_name");
                    Log.d("screen name", screenName);
                    getUserInformation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("AppUser", responseString);
                throwable.printStackTrace();
                //super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("AppUser", errorResponse.toString());
                throwable.printStackTrace();
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }

    public void getUserInformation(){
        client.getUserFullInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("UserInfo", response.toString());
                try {
                    user = User.fromJSON(response.getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("UserInfo", responseString);
                throwable.printStackTrace();
                //super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("UserInfo", errorResponse.toString());
                throwable.printStackTrace();
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }

}


