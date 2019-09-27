package com.glandroid.himalaya.fragments;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.base.BaseFragment;
import com.glandroid.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

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
public class RecommendFragment extends BaseFragment {
    private static final String TAG = "RecommendFragment";
    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_recommand,container,false);


      //去拿数据回来
        getRecommendData();
        return rootView;
    }

    /**
     * 获取推荐内容
     */
    private void getRecommendData() {

        //封装参数
        Map<String, String> map = new HashMap<String, String>();
        //这个参数标示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOOMEND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
        //获取数据成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        Log.d(TAG,"size---->" + albumList.size());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG,"error code-->" + i  +  "error msg--->" + s);
            }
        });

    }
}
