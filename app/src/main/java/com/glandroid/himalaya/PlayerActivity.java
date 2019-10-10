package com.glandroid.himalaya;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.glandroid.himalaya.adapters.PlayTrackPagerAdapter;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.presenters.PlayerPresenter;
import com.glandroid.himalaya.utils.LogUtil;
import com.glandroid.himalaya.views.SobPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

public class PlayerActivity extends AppCompatActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

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
    private ImageView mPlayPreBtn;
    private ImageView mPlayNextBtn;
    private TextView mTrackTitleTV;
    private String mMTrackTitleText;
    private ViewPager mTrackPagerView;
    private PlayTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager = false;
    private ImageView mPlayerSwitchModeBtn;
    private XmPlayListControl.PlayMode mCurrentMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayModeRule = new HashMap<>();

    //处理播放模式的切换
    //1默认是 PLAY_MODEL_LIST
    //2列表循环 PLAY_MODEL_LIST_LOOP
    //3随机播放 PLAY_MODEL_RANDOM
    //4单曲循环 PLAY_MODEL_SINGLE_LOOP
    static {
        sPlayModeRule.put(PLAY_MODEL_LIST, PLAY_MODEL_LIST_LOOP);
        sPlayModeRule.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_RANDOM);
        sPlayModeRule.put(PLAY_MODEL_RANDOM, PLAY_MODEL_SINGLE_LOOP);
        sPlayModeRule.put(PLAY_MODEL_SINGLE_LOOP, PLAY_MODEL_LIST);
    }

    private ImageView mPlayerListBtn;
    private SobPopWindow mSobPopWindow;
    private ValueAnimator mEnterBgAnimator;
    private ValueAnimator mOutBgAnimator;
    private final int BG_ANIMATION_DURATION = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //TODO:Vie的初始化要在register前面
        initView();
        //TODO:测试播放
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        //   playerPresenter.play();
        //在界面初始化后才能获取数据
        mPlayerPresenter.getPlayList();
        intEvent();
        initBgAnimation();
    }

    private void initBgAnimation() {

        mEnterBgAnimator = ValueAnimator.ofFloat(1.0f,0.7f);
        mEnterBgAnimator.setDuration(BG_ANIMATION_DURATION);
        mEnterBgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
              float value = (float) valueAnimator.getAnimatedValue();
                //处理一点透明度
                updateBgAlpha(value);
            }
        });
        //退出的
        mOutBgAnimator = ValueAnimator.ofFloat(0.7f,1.0f);
        mOutBgAnimator.setDuration(BG_ANIMATION_DURATION);
        mOutBgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                //处理一点透明度
                updateBgAlpha(value);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unregisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void intEvent() {
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果现在的状态是播放的就暂停
                if (mPlayerPresenter.isplaying()) {
                    mPlayerPresenter.pause();
                } else {
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

        mPlayPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:播放前一首
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });

        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:播放下一首
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playnext();
                }
            }
        });
        mTrackPagerView.addOnPageChangeListener(this);
        mTrackPagerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePager = true;
                        break;
                }
                return false;
            }
        });

        mPlayerSwitchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlayMode();


            }
        });
        mPlayerListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSobPopWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

                //修改背景的透明度有一个渐变的过程
                mEnterBgAnimator.start();
            }
        });
        mSobPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //pop消失后恢复透明度
               mOutBgAnimator.start();
            }
        });
        mSobPopWindow.setPlayListItemClickListener(new SobPopWindow.PlayListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //播放列表里的Item被点击了
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playByIndex(position);
                }
            }
        });
        mSobPopWindow.setPlayListActionListener(new SobPopWindow.PlayListActionListener() {
            @Override
            public void onPlayModeClick() {
                //切换播放模式
                switchPlayMode();
            }

            @Override
            public void onOrderClick() {
                 //点击了切换顺序和逆序
             //   Toast.makeText(PlayerActivity.this,"切换列表顺序",Toast.LENGTH_SHORT).show();
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.reversPlayList();
                }
//                mSobPopWindow.updatePlayIcon(!textOrder);
//                textOrder = !textOrder;
            }
        });
    }
    private boolean textOrder = false;

    private void switchPlayMode() {
        //根据当前的MODEl获取下一个mode
        XmPlayListControl.PlayMode playMode = sPlayModeRule.get(mCurrentMode);
        //修改播放模式
        if (mPlayerPresenter != null) {
            mPlayerPresenter.switchPlayMode(playMode);

        }
    }

    private void updateBgAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }

    /**
     * 根据当前的的模式更改切换图标
     * PLAY_MODEL_LIST
     * PLAY_MODEL_LIST_LOOP
     * PLAY_MODEL_RANDOM
     * PLAY_MODEL_SINGLE_LOOP
     */
    private void updatePlayModeBtnImg() {
        int resId = R.mipmap.play_mode_list_order;
        switch (mCurrentMode) {
            case PLAY_MODEL_LIST:
                resId = R.mipmap.play_mode_list_order;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.mipmap.play_mode_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.play_mode_list_looper;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.mipmap.play_mode_single_loop;
                break;


        }
        mPlayerSwitchModeBtn.setImageResource(resId);

    }

    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuraton = this.findViewById(R.id.track_duration);
        mCurrentPositon = this.findViewById(R.id.current_position);
        mDurationBar = this.findViewById(R.id.track_seek_bar);
        mPlayPreBtn = this.findViewById(R.id.play_pre);
        mPlayNextBtn = this.findViewById(R.id.play_next);
        mTrackTitleTV = this.findViewById(R.id.track_title);
        if (!TextUtils.isEmpty(mMTrackTitleText)) {
            mTrackTitleTV.setText(mMTrackTitleText);
        }
        mTrackPagerView = this.findViewById(R.id.track_pager_view);
        mTrackPagerAdapter = new PlayTrackPagerAdapter();
        mTrackPagerView.setAdapter(mTrackPagerAdapter);
        mPlayerSwitchModeBtn = this.findViewById(R.id.player_mode_switch_btn);
        mPlayerListBtn = this. findViewById(R.id.player_list);
        mSobPopWindow = new SobPopWindow();

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
        LogUtil.d(TAG, "list------->" + list);
        if (mTrackPagerAdapter != null) {

            mTrackPagerAdapter.setData(list);
        }
        //数据回来之后也要给播放列表一份,要判空
        if (mSobPopWindow != null) {
            mSobPopWindow.setListData(list);
        }

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {
        //更换播放模式并修改UI
        mCurrentMode = playMode;
        mSobPopWindow.updatePlayMode(mCurrentMode);
        updatePlayModeBtnImg();

    }

    @Override
    public void onProgressChange(int currentDuration, int total) {
        mDurationBar.setMax(total);
        String totalDuration;
        String currentDuratoin;
        if (total > 1000 * 60 * 60) {
            totalDuration = mHourFarmat.format(total);
            currentDuratoin = mHourFarmat.format(currentDuration);

        } else {
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
        if (!mIsUserTouchProgressBar) {

            mDurationBar.setProgress(currentDuration);
        }

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        mMTrackTitleText = track.getTrackTitle();
        if (mTrackTitleTV != null) {
            //设置当前节目的标题
            mTrackTitleTV.setText(mMTrackTitleText);
        }
        //当前节目更改的时候，我们获取当前播放位置
        //d当前的节目改变时要修改图片
        if (mTrackPagerView != null) {
            mTrackPagerView.setCurrentItem(playIndex,true);
        }
        //修改播放里的播放位置
        if (mSobPopWindow != null) {
            mSobPopWindow.setCurrentPlayPosition(playIndex);
        }

    }

    @Override
    public void updateListOrder(boolean isReverse) {

        mSobPopWindow.updatePlayIcon(isReverse);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int positon) {
        //当页面选中的时候就去切换播放内容
        if (mPlayerPresenter != null && mIsUserSlidePager) {

            mPlayerPresenter.playByIndex(positon);
        }
        mIsUserSlidePager = false;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
