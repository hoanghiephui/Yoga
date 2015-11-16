package com.android.yoga.ui.fragment.type.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.yoga.ui.fragment.MainFragment;

/**
 * Created by Hoang Hiep on 9/6/2015.
 */
public class ViewPagerAdapterType extends FragmentStatePagerAdapter {
    int mNumOfTab;

    public ViewPagerAdapterType(FragmentManager fm, int numOfTab) {
        super(fm);
        this.mNumOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return MainFragment.newInstance("Backbends");
            case 1:
                return MainFragment.newInstance("Balances");
            case 2:
                return MainFragment.newInstance("Forward Bends");
            case 3:
                return MainFragment.newInstance("Inverted");
            case 4:
                return MainFragment.newInstance("Lying On The Back");
            case 5:
                return MainFragment.newInstance("Lying On The Stomach");
            case 6:
                return MainFragment.newInstance("Sitting and Twisting");
            case 7:
                return MainFragment.newInstance("Standing");
            default:
                return MainFragment.newInstance("Backbends");
        }
    }

    @Override
    public int getCount() {
        return mNumOfTab;
    }
}
