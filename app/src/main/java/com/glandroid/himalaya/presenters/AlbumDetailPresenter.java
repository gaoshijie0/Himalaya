package com.glandroid.himalaya.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.glandroid.himalaya.interfaces.IAlbumDetailPresenter;
import com.glandroid.himalaya.interfaces.IAlbumDetailViewCallback;
import com.glandroid.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {
    private static final String TAG = "AlbumDetailPresenter";
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    private List<Track> mTracks  = new ArrayList<>();
    private Album mTargetAlbum = null;
    //当前的专辑ID
    private int mCurrentAlbumId = -1;
    //当前页
    private int mcurrentPageIndex = 0;

    private AlbumDetailPresenter() {
    }

    private static AlbumDetailPresenter instance = null;

    public static AlbumDetailPresenter getInstance() {
        if (instance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (instance == null) {
                    instance = new AlbumDetailPresenter();
                }
            }
        }
        return instance;

    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void lodeMore() {
        //去加载更多内容
        mcurrentPageIndex++;
        //传入true,表示结果会追加到结果后方
        doLoaded(true);

    }
    private void doLoaded(final boolean isLoaderMore){
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, mCurrentAlbumId+"");
        map.put(DTransferConstants.PAGE, mcurrentPageIndex + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    Log.v(TAG,"tracks size------>" +tracks.size());
                    if (isLoaderMore) {
                    //TODO:上拉加载，结果放到后面去
                        mTracks.addAll(tracks);
                        int size = tracks.size();
                        handleLoadMoreResult(size);

                    }else{
                        //TODO:下拉加载，结果放到前面去
                        mTracks.addAll(0,tracks);
                    }
                    //TODO：就因为把mTrakcs写成tracks导致莫名其妙的错误！！！！
                    handlerAlbumDetailResult(mTracks);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (isLoaderMore) {
                    mcurrentPageIndex--;
                }
                handleError(errorCode,errorMsg);
            }
        });
    }

    /**
     * 处理加载更多的结果
     * @param size
     */
    private void handleLoadMoreResult(int size) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onLoaderMoreFinished(size);
        }
    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
        mTracks.clear();
        this.mCurrentAlbumId = albumId;
        this.mcurrentPageIndex = page;
        //根据页码和专辑ID获取列表
        doLoaded(false);


    }

    /**
     * 如果网络错误就通知Ui
     * @param errorCode
     * @param errorMsg
     */
    private void handleError(int errorCode, String errorMsg) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onNetWorkError(errorCode,errorMsg);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onDetailListLoaded(tracks);
        }

    }


    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        if (!mCallbacks.contains(detailViewCallback)) {
            mCallbacks.add(detailViewCallback);
            if (mTargetAlbum != null) {

                detailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        mCallbacks.remove(detailViewCallback);
    }

    public void setTargetAlbum(Album targetAlbum) {
        this.mTargetAlbum = targetAlbum;

    }
}
