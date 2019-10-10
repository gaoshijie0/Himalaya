package com.glandroid.himalaya.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.glandroid.himalaya.base.BaseApplication;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.interfaces.IPlayerPresenter;
import com.glandroid.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {
    private static final String TAG = "PlayerPresenter";
    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();
    private XmPlayerManager mXmPlayerManager;
    private Track mCurrentTrack;
    private int mCurrentIndex = 0;
    private final SharedPreferences mPlayModeSp;
    //1默认是 PLAY_MODEL_LIST
    //2列表循环 PLAY_MODEL_LIST_LOOP
    //3随机播放 PLAY_MODEL_RANDOM
    //4单曲循环 PLAY_MODEL_SINGLE_LOOP
    public static final int PLAY_MODEL_LIST_INT = 0;
    public static final int PLAY_MODEL_LIST_LOOP_INT = 1;
    public static final int PLAY_MODEL_RANDOM_INT = 2;
    public static final int PLAY_MODEL_SINGLE_LOOP_INT = 3;
    //sp'keys and name
    public static final String PLAY_MODE_SP_NAME = "PlayMode";
    public static final String PLAY_MODE_SP_KEY = "currentPlayMode";
    private XmPlayListControl.PlayMode mCurrentPlayMode = PLAY_MODEL_LIST;
    private boolean mIsReverse = false;

    private PlayerPresenter(){
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getApContext());
        mXmPlayerManager.addAdsStatusListener(this);
        mXmPlayerManager.addPlayerStatusListener(this);
        //需要sp记录播放模式
        mPlayModeSp = BaseApplication.getApContext().getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE);

    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;

    }

    private boolean isPlayListSet = false;

    public void setPlayList(List<Track> list, int playIndex) {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.setPlayList(list, playIndex);
            isPlayListSet = true;
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
        } else {
            Log.v(TAG, "PlayerPresenter is null ");
        }

    }

    @Override
    public void play() {
        if (isPlayListSet) {
            mXmPlayerManager.play();

        }

    }

    @Override
    public void pause() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.pause();
        }

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.playPre();
        }

    }

    @Override
    public void playnext() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.playNext();
        }

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        if (mXmPlayerManager != null) {
            mCurrentPlayMode = mode;
            mXmPlayerManager.setPlayMode(mode);
            //通知UI更换播放模式
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onPlayModeChange(mode);
            }
            //保存到sp里去
            SharedPreferences.Editor edit = mPlayModeSp.edit();
            edit.putInt(PLAY_MODE_SP_KEY,getIntByPlayMode(mode));
            edit.commit();

        }

    }

    /**
     * 根据mode拿到int值用于存储到sp
     * @param mode
     * @return
     */
    private int getIntByPlayMode(XmPlayListControl.PlayMode mode){
        switch(mode){
            case PLAY_MODEL_SINGLE_LOOP:
                return PLAY_MODEL_SINGLE_LOOP_INT;
            case PLAY_MODEL_LIST_LOOP:
                return PLAY_MODEL_LIST_LOOP_INT;
            case PLAY_MODEL_RANDOM:
                return PLAY_MODEL_RANDOM_INT;
            case PLAY_MODEL_LIST:
                return PLAY_MODEL_LIST_INT;
        }
        return PLAY_MODEL_LIST_INT;
    }

    /**
     * 根据INT拿到mode值用于从sp拿到mode
     * @param index
     * @return
     */
    private XmPlayListControl.PlayMode getModeByPlay(int index){
        switch(index){
            case PLAY_MODEL_SINGLE_LOOP_INT:
                return PLAY_MODEL_SINGLE_LOOP;
            case PLAY_MODEL_LIST_LOOP_INT:
                return PLAY_MODEL_LIST_LOOP;
            case PLAY_MODEL_RANDOM_INT:
                return PLAY_MODEL_RANDOM;
            case PLAY_MODEL_LIST_INT:
                return PLAY_MODEL_LIST;
        }
        return PLAY_MODEL_LIST;
    }

    @Override
    public void getPlayList() {
        if (mXmPlayerManager != null) {

            List<Track> playList = mXmPlayerManager.getPlayList();
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onListLoaded(playList);
            }
        }

    }

    @Override
    public void playByIndex(int index) {
        mXmPlayerManager.play(index);
    }

    @Override
    public void seekTo(int progress) {
        //更新播放进度
        mXmPlayerManager.seekTo(progress);

    }

    @Override
    public boolean isplaying() {
        //返回当前是否正在播放
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void reversPlayList() {
        //把播放器内容翻转
        List<Track> playList = mXmPlayerManager.getPlayList();
        Collections.reverse(playList);
        mIsReverse = !mIsReverse;
        //参数一：播放列表，参数二：播放下标
        mCurrentIndex = playList.size()-1-mCurrentIndex;
        mXmPlayerManager.setPlayList(playList,mCurrentIndex);
        //更新UI
        mCurrentTrack = (Track) mXmPlayerManager.getCurrSound();
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onListLoaded(playList);
            iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
            iPlayerCallback.updateListOrder(mIsReverse);
        }
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
        //从sp里拿
        int modelIndex = mPlayModeSp.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST_INT);
        mCurrentPlayMode = getModeByPlay(modelIndex);

        iPlayerCallback.onPlayModeChange(mCurrentPlayMode);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unregisterViewCallback(IPlayerCallback iPlayerCallback) {
            mIPlayerCallbacks.remove(iPlayerCallback);
    }

    //================广告相关的回调方法 start===============//

    @Override
    public void onStartGetAdsInfo() {
      Log.v(TAG,"onStartGetAdsInfo");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        Log.v(TAG,"onGetAdsInfo");
    }

    @Override
    public void onAdsStartBuffering() {
        Log.v(TAG,"onAdsStartBuffering");
    }

    @Override
    public void onAdsStopBuffering() {
        Log.v(TAG,"onAdsStopBuffering");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        Log.v(TAG,"onStartPlayAds");
    }

    @Override
    public void onCompletePlayAds() {
        Log.v(TAG,"onCompletePlayAds");
    }

    @Override
    public void onError(int what, int extra) {
        Log.v(TAG,"onError what---->"+what+"  extra"+extra);

    }
    //================广告相关的回调方法 end===============//


    //================播放相关的回调方法 start===============//

    @Override
    public void onPlayStart() {
        Log.v(TAG,"onPlayStart");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStart();
        }

    }

    @Override
    public void onPlayPause() {
        Log.v(TAG,"onPlayPause");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayPause();
        }

    }

    @Override
    public void onPlayStop() {
        Log.v(TAG,"onPlayStop");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStop();
        }

    }

    @Override
    public void onSoundPlayComplete() {
        Log.v(TAG,"onSoundPlayComplete");

    }

    @Override
    public void onSoundPrepared() {
        Log.v(TAG,"onSoundPrepared");
        mXmPlayerManager.setPlayMode(mCurrentPlayMode);
        if (mXmPlayerManager.getPlayerStatus() == PlayerConstants.STATE_PREPARED) {
            //TODO:播放器准备完了，可以播放了!!
            mXmPlayerManager.play();
        }

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel currentModel) {
        LogUtil.d(TAG,"onSoundSwitch....");
        if (lastModel != null) {
            LogUtil.d(TAG,"lastModel..."+ lastModel.getKind());
        }
        LogUtil.d(TAG,"currentModel..."+ currentModel.getKind());
//        if ("track".equals(currentModel.getKind())) {
//            Track currentTrack = (Track) currentModel;
//            LogUtil.d(TAG,"title---->"+ currentTrack.getTrackTitle());
//        }
        mCurrentIndex = mXmPlayerManager.getCurrentIndex();
        if (currentModel instanceof Track) {
            Track currentTrack = (Track) currentModel;
            //        LogUtil.d(TAG,"title---->"+ currentTrack.getTrackTitle());
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onTrackUpdate(currentTrack,mCurrentIndex);
            }

        }

    }

    @Override
    public void onBufferingStart() {
        Log.v(TAG,"onBufferingStart");
    }

    @Override
    public void onBufferingStop() {
        Log.v(TAG,"onBufferingStop");

    }

    @Override
    public void onBufferProgress(int progress) {
        Log.v(TAG,"onBufferProgress");

    }

    @Override
    public void onPlayProgress(int current, int duration) {
        //单位是毫秒
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onProgressChange(current,duration);
        }

    }

    @Override
    public boolean onError(XmPlayerException e) {
        Log.v(TAG,"error e----->"+e);

        return false;
    }

    public boolean hasPlayList() {
        return isPlayListSet;

    }
    //================播放相关的回调方法 end===============//
}
