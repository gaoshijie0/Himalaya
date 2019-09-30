package com.glandroid.himalaya.fragments;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.adapters.RecommendListAdapter;
import com.glandroid.himalaya.base.BaseFragment;
import com.glandroid.himalaya.interfaces.IRecomendCallback;
import com.glandroid.himalaya.presenters.RecommendPresenter;
import com.glandroid.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class RecommendFragment extends BaseFragment implements IRecomendCallback, UILoader.OnRetryClickListener {
    private static final String TAG = "RecommendFragment";
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;
    private View mRootView;

    @Override
    protected View onSubViewLoad(final LayoutInflater inflater, ViewGroup container) {

        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(inflater, container);
            }
        };


        // getRecommendData();
        mRecommendPresenter = RecommendPresenter.getInstance();
        mRecommendPresenter.registerViewCallback(this);
        /**
         * 获取推荐列表
         */
        mRecommendPresenter.getRecommendList();

        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        mUiLoader.setOnRetryClickListener(this);
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_recommand, container, false);

        //RecyclerView的使用
        //  1.找到控件
        mRecommendRv = mRootView.findViewById(R.id.recommen_list);
        // 2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);

            }
        });
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(mRecommendListAdapter);

        return mRootView;
    }

    private void upRecommendUI(List<Album> albumList) {
        //把数据设置给适配器，并且更新UI
        mRecommendListAdapter.setData(albumList);

    }

    @Override
    public void onRecommendListLoad(List<Album> result) {
        mRecommendListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onLoadMore(List<Album> result) {


    }

    @Override
    public void OnRefreshMore(List<Album> result) {

    }

    @Override
    public void onNetWordError() {
        mUiLoader.updateStatus(UILoader.UIStatus.NET_ERROR);
    }

    @Override
    public void onEmpty() {
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRetryClick() {
        //网络不加的时候用户点击重试
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }
}
