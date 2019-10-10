package com.glandroid.himalaya.interfaces;

import com.glandroid.himalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {
    void play();

    void pause();

    void stop();

    void playPre();

    void playnext();

    /**
     * 切换播放模式
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目位置进行播放
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     * @param index
     */
    void seekTo(int index);

    /**
     * 判断播放器是否在播放
     * @return
     */
    boolean isplaying();

    /**
     * 把播放器列表内容反转
     */
    void reversPlayList();

}
