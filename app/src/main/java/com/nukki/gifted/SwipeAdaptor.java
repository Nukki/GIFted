package com.nukki.gifted;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;

/**
 * Created by NUKE on 1/15/17.
 */

public class SwipeAdaptor extends FragmentStatePagerAdapter {
    private final Bundle fragData;
    public SwipeAdaptor(FragmentManager fm, Bundle bun) {
        super(fm);
        fragData = bun;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PageFragment();
        fragData.putInt("count",position);
        fragment.setArguments(fragData);
        return fragment;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 20;
    }
}
