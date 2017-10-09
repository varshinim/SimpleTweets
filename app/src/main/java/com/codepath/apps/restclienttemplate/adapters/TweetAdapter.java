package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.Date;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    Context context;
    private List<Tweet> tweets;
    private TweetAdapterListener listener;

    // define an interface required by the view holder
    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }

    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener){
        this.tweets = tweets;
        this.listener = listener;
    }

    // create ViewHolder class (findViewById lookups)
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTimeStamp;

        public ViewHolder(View tweet){
            super(tweet);
            // perform findViewById lookups
            ivProfileImage = (ImageView) tweet.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) tweet.findViewById(R.id.tvUserName);
            tvBody = (TextView) tweet.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) tweet.findViewById(R.id.tvTimeStamp);

            ivProfileImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        // get position of row  element
                        int position = getAdapterPosition();
                        // fire the listener call back
                        listener.onItemSelected(view, position);
                    }
                }
            }); // handle row click event
        }
    }

    // for each row inflate the layout and cache references to ViewHolder
    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView;
        ViewHolder viewHolder;
        tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(TweetAdapter.ViewHolder holder, int position) {
        //get data according to position
        Tweet tweet = tweets.get(position);
        //populate the view according to the position
        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvBody.setText(tweet.getBody());
        holder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(Date date) {
        String relativeDate = "";

        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        // relativeDate = date.toString();

        return relativeDate;
    }

}