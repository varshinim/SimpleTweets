package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragments.FavoritesFragment;
import com.codepath.apps.restclienttemplate.fragments.OtherUserTweetsListFragment;

public class OtherUserTweetPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Tweets", "Favourites"};
    private Context context;
    private String screenName;

    public OtherUserTweetPagerAdapter(FragmentManager fm, Context context, String screenName){
        super(fm);
        this.context = context;
        this.screenName = screenName;
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
            return OtherUserTweetsListFragment.newInstance(screenName);
        } else if(position == 1){
            return FavoritesFragment.newInstance(screenName);
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
