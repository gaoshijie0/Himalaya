package com.glandroid.himalaya.presenters;

import android.util.Log;

import com.glandroid.himalaya.base.BaseApplication;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.interfaces.IPlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

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
    private PlayerPresenter(){
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getApContext());
        mXmPlayerManager.addAdsStatusListener(this);
        mXmPlayerManager.addPlayerStatusListener(this);

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
        } else {
            Log.v(TAG, "PlayerPresenter is null ");
        }

    }

    @Override
    public void play() {
        if (isPlayListSet) {
            Log.v(TAG,"play");
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

    }

    @Override
    public void playnext() {

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {
        //更新播放进度
        mXmPlayerManager.seekTo(progress);

    }

    @Override
    public boolean isPlay() {
        //返回当前是否正在播放
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
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

    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        Log.v(TAG,"onSoundSwitch");
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
    //================播放相关的回调方法 end===============//
}
