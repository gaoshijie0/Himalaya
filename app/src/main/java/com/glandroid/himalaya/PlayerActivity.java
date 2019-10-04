package com.glandroid.himalaya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements IPlayerCallback {

    private static final String TAG = "PlayerActivity";
    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    SimpleDateFormat mHourFarmat = new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalDuraton;
    private TextView mCurrentPositon;
    private SeekBar mDurationBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //TODO:测试播放
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
//        playerPresenter.play();
        initView();
        intEvent();
        startPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unregisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayerPresenter != null) {

            mPlayerPresenter.play();
        }
    }

    private void intEvent() {
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果现在的状态是播放的就暂停
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                }else{
                    //如果现在的状态是暂停的就让播放器播放
                    mPlayerPresenter.play();
                }


            }
        });
        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {
                    mCurrentProgress = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
            //手离开拖动进度条更新
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });
    }

    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuraton = this.findViewById(R.id.track_duration);
        mCurrentPositon = this. findViewById(R.id.current_position);
        mDurationBar = this.findViewById(R.id.track_seek_bar);
    }

    @Override
    public void onPlayStart() {
        //开始播放，修改Ui成暂停按钮
        if (mControlBtn != null) {

            mControlBtn.setImageResource(R.mipmap.stop);
        }

    }

    @Override
    public void onPlayPause() {
        //要判空，不然可能控件还没初始化完成，造成空指针
        if (mControlBtn != null) {

            mControlBtn.setImageResource(R.mipmap.play);
        }

    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {

            mControlBtn.setImageResource(R.mipmap.play);
        }

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
    public void onProgressChange(int currentDuration, int total) {
        mDurationBar.setMax(total);
        String totalDuration;
        String currentDuratoin;
        if (total>1000*60*60) {
            totalDuration = mHourFarmat.format(total);
            currentDuratoin = mHourFarmat.format(currentDuration);

        }else{
            totalDuration = mMinFormat.format(total);
            currentDuratoin = mMinFormat.format(currentDuration);
        }
        if (mTotalDuraton != null) {

            mTotalDuraton.setText(totalDuration);
        }
        //更新当前时间
        if (mCurrentPositon != null) {
            mCurrentPositon.setText(currentDuratoin);
        }

        //更新进度
        //计算当前的进度
        if (mIsUserTouchProgressBar) {

            mDurationBar.setProgress(currentDuration);
        }

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }
}
