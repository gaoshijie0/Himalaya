package com.glandroid.himalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.base.BaseFragment;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class SubscriptionFragment extends BaseFragment {
    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_subscription,container,false);
        return rootView;
    }
}
