package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.x;
import static android.icu.util.ULocale.getName;
import static org.parceler.Parcels.unwrap;

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener{

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    LinearLayoutManager linearLayoutManager;

    public interface TweetSelectedListener{
        public void onTweetSelected(Tweet tweet);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        tweets = new ArrayList<>();
         // instantiate the arratList( data source)
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);   // find the recyclerView
        tweetAdapter = new TweetAdapter(tweets, this);  // construct the adapter form this datasource
        linearLayoutManager = new LinearLayoutManager(getContext());  // set layout manager
        rvTweets.setLayoutManager(linearLayoutManager);  // attach layout manager to RecyclerView
        rvTweets.setAdapter(tweetAdapter);   // Attach the adapter to the recyclerview to populate items
        return v;
    }

    public void addItems(JSONArray response){
        for(int i=0;i<response.length();i++){
            try{
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size()-1);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener)getActivity()).onTweetSelected(tweet);
    }
}
