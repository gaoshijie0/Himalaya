package com.glandroid.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IAlbumDetailViewCallback {
    /**
     * 专辑内容加载出来了。。
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 吧Album传给Ui
     * @param album
     */
    void onAlbumLoaded(Album album);

    /**
     * 网络错误
     */
    void onNetWorkError(int errorCode, String errorMsg);
}
