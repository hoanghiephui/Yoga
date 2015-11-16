package com.android.yoga.ui.fragment.level.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.yoga.ui.fragment.MainFragment;

/**
 * Created by Hoang Hiep on 9/7/2015.
 */
public class ViewPagerAdapterLevel extends FragmentStatePagerAdapter {
    int mNumOfTab;

    public ViewPagerAdapterLevel(FragmentManager fm, int numOfTab) {
        super(fm);
        this.mNumOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return MainFragment.newInstance("Beginner");
            case 1:
                return MainFragment.newInstance("Intermediate");
            case 2:
                return MainFragment.newInstance("Advanced");
            default:
                return MainFragment.newInstance("Beginner");
        }
    }

    @Override
    public int getCount() {
        return mNumOfTab;
    }
}
