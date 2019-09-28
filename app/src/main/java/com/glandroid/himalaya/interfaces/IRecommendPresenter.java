package com.glandroid.himalaya.interfaces;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IRecommendPresenter {
    /**
     * 获取推荐列表
     */
    void getRecommendList();

    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 加载更多
     */
    void lodeMore();
    /**
     * 用于注册UI的回调
     * @param callback
     */
    void registerViewCallback(IRecomendCallback callback);

    /**
     * 取消注册
     * @param callback
     */
    void unRegisterViewCallback(IRecomendCallback callback);
}
