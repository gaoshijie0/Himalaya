package com.glandroid.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IRecomendCallback {
    /**
     * 获取推荐内容的结果
     * @param
     */
    void onRecommendListLoad(List<Album> result);

    /**
     * 加载更多的结果
     */
    void onLoadMore(List<Album> result);

    /**
     * 下拉加载更多的结果
     */
    void OnRefreshMore(List<Album> result);

    /**
     * 网络错误
     */
    void onNetWordError();

    /**
     * 数据为空
     */
    void onEmpty();

    /**
     * 正在加载
     */
    void onLoading();



}
