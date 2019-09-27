package com.glandroid.himalaya.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.glandroid.himalaya.utils.Constants;
import com.glandroid.himalaya.utils.FragmentCreator;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class MainContentAdapter extends FragmentPagerAdapter {
    public MainContentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentCreator.getFragment(i);
    }

    @Override
    public int getCount() {
        return Constants.PAGE_COUNT;
    }
}
