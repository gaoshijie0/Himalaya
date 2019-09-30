package com.glandroid.himalaya.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.base.BaseApplication;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public abstract class UILoader extends FrameLayout {
    private View mLoadingView;
    private View mSuccessView;
    private View mNetWorkErroeView;
    private View mEmptyView;
    private OnRetryClickListener mOnRetryClickListener = null;
    private OnRetryClickListener mOnRetryClickListener1;

    public enum UIStatus{
        LOADING,SUCCESS,NET_ERROR,EMPTY,NONE
    }

    public UIStatus mCurrentStatus = UIStatus.NONE;
    public UILoader(@NonNull Context context) {
        super(context);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void updateStatus(UIStatus status){
        mCurrentStatus = status;
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }



    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING?VISIBLE:GONE);


        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        mSuccessView.setVisibility(mCurrentStatus == UIStatus.SUCCESS?VISIBLE:GONE);



        if (mNetWorkErroeView == null) {
            mNetWorkErroeView = getNewWorkErrorView();
            addView(mNetWorkErroeView);
        }
        mNetWorkErroeView.setVisibility(mCurrentStatus == UIStatus.NET_ERROR?VISIBLE:GONE);


        if (mEmptyView == null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY?VISIBLE:GONE);

    }

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    private View getNewWorkErrorView() {
        View networkView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);
        networkView.findViewById(R.id.network_net_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新获取数据
                if (mOnRetryClickListener != null) {
                    mOnRetryClickListener.onRetryClick();
                }

            }
        });
        return networkView;
    }

    protected abstract View getSuccessView(ViewGroup viewGroup);

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }

    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener){
        this.mOnRetryClickListener1 = onRetryClickListener;
    }
    public interface OnRetryClickListener{
       void onRetryClick();
    }
}
