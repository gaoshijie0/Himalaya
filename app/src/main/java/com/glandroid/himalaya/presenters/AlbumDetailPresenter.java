package com.glandroid.himalaya.presenters;

import com.glandroid.himalaya.interfaces.IAlbumDetailPresenter;
import com.glandroid.himalaya.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    private Album mTargetAlbum = null;

    private AlbumDetailPresenter(){}
    private static AlbumDetailPresenter instance = null;
    public static AlbumDetailPresenter getInstance(){
        if (instance == null) {
           synchronized (AlbumDetailPresenter.class){
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

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {

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

    public  void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum = targetAlbum;

    }
}
