package com.glandroid.himalaya;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.glandroid.himalaya.adapters.IndicatorAdapter;
import com.glandroid.himalaya.adapters.MainContentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends FragmentActivity {

    private static final String TAG ="MainActivity" ;
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        iniview();
        initEvent();

    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorClickListner(new IndicatorAdapter.OnIndicatorTabClickListner() {
            @Override
            public void onTabClick(int index) {
                mContentPager.setCurrentItem(index);
                Log.d(TAG,"index is--------->"+index);
            }
        });
    }

    private void iniview() {
        mMagicIndicator = findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.second_color));
        //创建Indicator适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);
        //Viewpager
        mContentPager = findViewById(R.id.content_pager);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);
        mContentPager.setAdapter(mainContentAdapter);


        mMagicIndicator.setNavigator(commonNavigator);
        //把Indicator和viewpager绑定在一起
        ViewPagerHelper.bind(mMagicIndicator,mContentPager);



    }
}
