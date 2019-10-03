package com.glandroid.himalaya.interfaces;

import com.glandroid.himalaya.base.IBasePresenter;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {

    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 加载更多
     */
    void lodeMore();

    /**
     * 获取专辑详情
     */
    void getAlbumDetail(int albumId,int page);

}
