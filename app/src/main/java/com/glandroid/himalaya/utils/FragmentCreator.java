package com.glandroid.himalaya.utils;

import com.glandroid.himalaya.base.BaseFragment;
import com.glandroid.himalaya.fragments.HistoryFragment;
import com.glandroid.himalaya.fragments.RecommendFragment;
import com.glandroid.himalaya.fragments.SubscriptionFragment;

import java.util.HashMap;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class FragmentCreator {

//    public final static int INDEX_RECOMMEND = 0;
//    public final static int INDEX_SUBSCIPTION = 1;
//    public final static int INDEX_HISTORY = 2;
//    public final static int PAGE_COUNT = 3;

        private static HashMap<Integer, BaseFragment> sCaChe  = new HashMap<>();
        public static BaseFragment getFragment(int index){
            BaseFragment baseFragment = sCaChe.get(index);
            if (baseFragment != null) {
                return baseFragment;
            }
            switch (index){
                case Constants.INDEX_RECOMMEND:
                    baseFragment = new RecommendFragment();
                    break;
                case Constants.INDEX_SUBSCIPTION:
                    baseFragment = new SubscriptionFragment();
                    break;
                case Constants.INDEX_HISTORY:
                    baseFragment = new HistoryFragment();
                    break;
            }
            sCaChe.put(index,baseFragment);
                return  baseFragment;
        }
}
