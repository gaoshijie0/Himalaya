package com.glandroid.himalaya;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.glandroid.himalaya.adapters.IndicatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity" ;
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        iniview();

    }

    private void iniview() {
        mMagicIndicator = findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));
        //创建Indicator适配器
        IndicatorAdapter adapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this
        );
        commonNavigator.setAdapter(adapter);
        //Viewpager
        mContentPager = findViewById(R.id.content_pager);
        mMagicIndicator.setNavigator(commonNavigator);
        //把Indicator和viewpager绑定在一起
        ViewPagerHelper.bind(mMagicIndicator,mContentPager);



    }
}
