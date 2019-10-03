package com.glandroid.himalaya.interfaces;

import com.glandroid.himalaya.base.IBasePresenter;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IRecommendPresenter extends IBasePresenter<IRecomendCallback> {
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

}
