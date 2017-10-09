package com.codepath.apps.restclienttemplate.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.OtherUserTweetPagerAdapter;
import com.codepath.apps.restclienttemplate.adapters.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.OtherUserTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.OtherUserTweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class OthersProfileActivity extends AppCompatActivity {

    TwitterClient client;
    OtherUserTweetPagerAdapter otherUserTweetPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);

        String screenName = getIntent().getStringExtra("screen_name");

        // set the adapter for the pager
        otherUserTweetPagerAdapter = new OtherUserTweetPagerAdapter(getSupportFragmentManager(), this, screenName);
        vp.setAdapter(otherUserTweetPagerAdapter);

        // setup the tablayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vp);

        client = TwitterApp.getRestClient();
        client.getUserFullInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("OtherUserInfo: ", response.toString());
                try {
                    User user = User.fromJSON(response.getJSONObject(0));
                    populateOtherUserHeadline(user);
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

    public void populateOtherUserHeadline(User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
