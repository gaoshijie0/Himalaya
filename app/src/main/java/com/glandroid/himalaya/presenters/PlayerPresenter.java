package com.glandroid.himalaya.presenters;

import android.util.Log;

import com.glandroid.himalaya.base.BaseApplication;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.interfaces.IPlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class PlayerPresenter implements IPlayerPresenter {
    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mXmPlayerManager;
    private PlayerPresenter(){
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getApContext());

    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getInstance() {
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
            mXmPlayerManager.play();
        }

    }

    @Override
    public void pause() {

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
    public void seekTo(int index) {

    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {

    }

    @Override
    public void unregisterViewCallback(IPlayerCallback iPlayerCallback) {

    }
}
