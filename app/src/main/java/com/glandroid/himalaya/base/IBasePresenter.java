package com.glandroid.himalaya.base;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IBasePresenter <T>{

    /**
     * 注册UI通知的接口
     * @param
     */
    void registerViewCallback(T t);

    /**
     * 删除Ui通知的接口
     * @param
     */
    void unregisterViewCallback(T t);


}

