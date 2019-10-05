package com.glandroid.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IPlayerCallback {
    void onPlayStart();
    void onPlayPause();
    void onPlayStop();
    void onPlayError();
    void nextPlay(Track track);
    void onPrePlay(Track track);

    /**
     * 播放列表数据加载完成
     * @param list 播放列表数据
     */
    void onListLoaded(List<Track> list);

    /**
     * 播放模式改变了
     * @param playMode
     */
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    /**
     * 进度条改变
     * @param currentProgress
     * @param total
     */
    void onProgressChange(int currentProgress,int total);
    /**
     * 广告正在加载
     */
    void onAdLoading();
    /**
     * 广告结束
     */
    void onAdFinished();

    /**
     *更新当前节目
     * @param
     */
    void onTrackUpdate(Track track,int playIndex);

}
