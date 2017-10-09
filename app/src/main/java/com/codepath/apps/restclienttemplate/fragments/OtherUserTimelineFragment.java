package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.InfiniteScrollListener;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class OtherUserTimelineFragment extends TweetsListFragment {

    TwitterClient client;
    private InfiniteScrollListener scrollListener;

    public static OtherUserTimelineFragment newInstance(String screenName){
        OtherUserTimelineFragment otherUserTimelineFragment = new OtherUserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        otherUserTimelineFragment.setArguments(args);
        return otherUserTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient();
        populateTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        scrollListener = new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                Log.d("onLoadMore: ", "page = "+page);
                Log.d("onLoadMore: ", "totalItemsCount = "+totalItemsCount);
                loadNextDataFromApi(totalItemsCount);
                return true;
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        return v;
    }

    public void loadNextDataFromApi(int offset) {
        populateTimeline();
        // tweetAdapter.notifyItemInserted(tweets.size()-1);
    }

    private void populateTimeline(){
        // comes from the activity
        String screenName = getArguments().getString("screen_name");
        Log.d("Out_screenname", screenName);
        long maxId = tweets != null && !tweets.isEmpty() ? tweets.get(tweets.size()-1).getUid(): -1;
        client.getOtherUserHomeTimeline(screenName, maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("OuTimeline: ", response.toString());
                addItems(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
