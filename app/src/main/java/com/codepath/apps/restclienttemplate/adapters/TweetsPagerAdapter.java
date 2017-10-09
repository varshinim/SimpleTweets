package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    private HomeTimelineFragment homeTimelineFragment;
    private MentionsTimelineFragment mentionsTimelineFragment;
    public TweetsPagerAdapter(FragmentManager fm, Context context){
         super(fm);
        this.context = context;
    }

    // return toal no.of fragments
    @Override
    public int getCount() {
        return 2;
    }

    // return the fragment to use depending on the position
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            if (homeTimelineFragment == null) {
                homeTimelineFragment = new HomeTimelineFragment();
            }
            return homeTimelineFragment;
        } else if(position == 1){
            if (mentionsTimelineFragment == null) {
                mentionsTimelineFragment = new MentionsTimelineFragment();
            }
            return mentionsTimelineFragment;
        } else {
            return null;
        }
    }

    // return title
    @Override
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabTitles[position];
    }

}
