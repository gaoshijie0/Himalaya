package com.glandroid.himalaya;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glandroid.himalaya.adapters.IndicatorAdapter;
import com.glandroid.himalaya.adapters.MainContentAdapter;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.presenters.PlayerPresenter;
import com.glandroid.himalaya.utils.LogUtil;
import com.glandroid.himalaya.views.RoundImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.List;

public class MainActivity extends FragmentActivity implements IPlayerCallback {

    private static final String TAG = "MainActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;
    private IndicatorAdapter mIndicatorAdapter;
    private RoundImageView mRoundImageView;
    private TextView mHeadTitle;
    private TextView mSubTitle;
    private ImageView mPlayControl;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniview();
        initEvent();
        initPresenter();

    }

    private void initPresenter() {
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorClickListner(new IndicatorAdapter.OnIndicatorTabClickListner() {
            @Override
            public void onTabClick(int index) {
                mContentPager.setCurrentItem(index);
                Log.d(TAG, "index is--------->" + index);
            }
        });
        mPlayControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayerPresenter != null) {
                    if (mPlayerPresenter.isplaying()) {
                        mPlayerPresenter.pause();
                    } else {
                        mPlayerPresenter.play();
                    }
                }
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
        ViewPagerHelper.bind(mMagicIndicator, mContentPager);
        //播放控制相关的
        mRoundImageView = this.findViewById(R.id.main_track_cover);
        mHeadTitle = this.findViewById(R.id.main_head_title);
        mHeadTitle.setSelected(true);
        mSubTitle = this.findViewById(R.id.main_sub_title);
        mPlayControl = this.findViewById(R.id.main_paly_control);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {

            mPlayerPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onPlayStart() {
      updatePlayControl(true);

    }

    private void updatePlayControl(boolean isPlaying) {
        if (mPlayControl != null) {
            mPlayControl.setImageResource(isPlaying?R.drawable.selector_play_control_pause:R.drawable.selector_play_control_play);
        }
    }

    @Override
    public void onPlayPause() {
        updatePlayControl(false);

    }

    @Override
    public void onPlayStop() {
        updatePlayControl(false);

    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        if (track != null) {
            String trackTitle = track.getTrackTitle();
            String nickname = track.getAnnouncer().getNickname();
            String coverUrlMiddle = track.getCoverUrlMiddle();
            LogUtil.d(TAG, "trackTitle--->" + trackTitle);
            if (mHeadTitle != null) {
                mHeadTitle.setText(trackTitle);
            }
            LogUtil.d(TAG, "nickname--->" + nickname);
            if (mSubTitle != null) {
                mSubTitle.setText(nickname);
            }
            LogUtil.d(TAG, "coverUrlMiddle--->" + coverUrlMiddle);
            Picasso.with(this).load(coverUrlMiddle).into(mRoundImageView);
        }

    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }
}
