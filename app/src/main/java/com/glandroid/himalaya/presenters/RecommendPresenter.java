package com.glandroid.himalaya.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.glandroid.himalaya.interfaces.IRecomendCallback;
import com.glandroid.himalaya.interfaces.IRecommendPresenter;
import com.glandroid.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

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
public class RecommendPresenter implements IRecommendPresenter {
    private List<IRecomendCallback> mCallbacks = new ArrayList<>();
    private static final String TAG = "RecommendPresenter";

    /**
     * 获取单例对象
     */
    private RecommendPresenter() {
    }

    private static RecommendPresenter instance = null;

    public static RecommendPresenter getInstance() {
        if (instance == null) {
            synchronized (RecommendPresenter.class) {
                if (instance == null) {
                    instance = new RecommendPresenter();
                }
            }
        }
        return instance;
    }

    /**
     * 获取推荐内容
     */
    @Override
    public void getRecommendList() {

        updateLoading();
        //封装参数
        Map<String, String> map = new HashMap<String, String>();
        //这个参数标示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                //获取数据成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        //数据回来后我们要更新UI
                        //   upRecommendUI(albumList);
                        handlerRecommendResult(albumList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "error code-->" + i + "error msg--->" + s);
                handleError();
            }
        });

    }

    private void handleError() {

        //通知UI
        if (mCallbacks != null) {
            for (IRecomendCallback callback : mCallbacks) {
                callback.onNetWordError();

            }
        }
    }


    private void handlerRecommendResult(List<Album> albumList) {
        if (albumList != null) {
            if (albumList.size() == 0) {
                for (IRecomendCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                //通知UI
                for (IRecomendCallback callback : mCallbacks) {

                    callback.onRecommendListLoad(albumList);
                }
            }
        }

    }

    private void updateLoading() {
        //通知UI
        for (IRecomendCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }


    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void lodeMore() {

    }

    @Override
    public void registerViewCallback(IRecomendCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }

    }

    @Override
    public void unRegisterViewCallback(IRecomendCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }

    }
}
