package com.glandroid.himalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glandroid.himalaya.adapters.DetailListAdapter;
import com.glandroid.himalaya.base.BaseActivity;
import com.glandroid.himalaya.base.BaseApplication;
import com.glandroid.himalaya.interfaces.IAlbumDetailViewCallback;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.presenters.AlbumDetailPresenter;
import com.glandroid.himalaya.presenters.PlayerPresenter;
import com.glandroid.himalaya.utils.LogUtil;
import com.glandroid.himalaya.views.ImageBlur;
import com.glandroid.himalaya.views.RoundImageView;
import com.glandroid.himalaya.views.UILoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, DetailListAdapter.ItemClickListener, IPlayerCallback, UILoader.OnRetryClickListener {

    private static final String TAG = "DetailActivity";
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private ImageView mLargeCover;
    private RoundImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private int mCurrentPage = 1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private PlayerPresenter mPlayerPresenter;
    private ImageView mPlayControlBtn;
    private TextView mPlayControlTips;
    private List<Track> mCurrentTracks = null;
    private static final int DEFAULT_PLAY_INDEX = 0;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private long mCurrentId = -1;
    private TwinklingRefreshLayout mRefreshLayout;
    private boolean mIsLoaderMore = false;
    private String mCurrentTrackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        //专辑详情的presenter
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
        //播放器的presenter
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        updatePlayState(mPlayerPresenter.isplaying());
        initListener();

    }


    private void initListener() {
        if (mPlayControlBtn != null) {

            mPlayControlBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPlayerPresenter != null) {
                        //判断播放器是否有播放列表
                        //TODO:
                        boolean has = mPlayerPresenter.hasPlayList();
                        if (has) {
                            handlePlayControl();

                        } else {
                            handleNoPlayList();
                        }
                    }
                }
            });
        }
    }

    //   当播放器里面没有播放列表的处理
    private void handleNoPlayList() {
        mPlayerPresenter.setPlayList(mCurrentTracks, DEFAULT_PLAY_INDEX);

    }

    private void handlePlayControl() {
        //控制播放器的状态
        if (mPlayerPresenter.isplaying()) {
            //正在播放，那么暂停
            mPlayerPresenter.pause();
        } else {
            mPlayerPresenter.play();

        }
    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        if (mUiLoader == null) {

            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            //TODO:添加前先干掉所有的！！！！
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
            mUiLoader.setOnRetryClickListener(this);
        }

        LogUtil.d(TAG, "测试一下，哈哈哈哈哈");
        mLargeCover = this.findViewById(R.id.iv_large_color);
        mSmallCover = this.findViewById(R.id.riv_small_color);
        mAlbumTitle = this.findViewById(R.id.tv_album_titile);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);
        //设置播放图标
        mPlayControlBtn = findViewById(R.id.detail_play_control);
        mPlayControlTips = findViewById(R.id.play_control_text);
        mPlayControlTips.setSelected(true);


    }

    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(BaseApplication.getApContext()).inflate(R.layout.item_detail_list, container, false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        mRefreshLayout = detailListView.findViewById(R.id.refresh_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        //设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        mDetailListAdapter.setOnItemClickListener(this);

        BezierLayout headView = new BezierLayout(this);
        mRefreshLayout.setHeaderView(headView);
        mRefreshLayout.setMaxHeadHeight(140);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this, "刷新成功...", Toast.LENGTH_SHORT).show();
                        mRefreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                //TODO:加载更多
                if (mAlbumDetailPresenter != null) {
                    mAlbumDetailPresenter.lodeMore();
                    mIsLoaderMore = true;
                }
            }
        });

        return detailListView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        if (mIsLoaderMore && mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
            mIsLoaderMore = false;
        }
        if (tracks == null || tracks.size() == 0) {
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
            }
        }
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        //判断数据结果，根据结果控制Ui显示
        //        if(tracks == null ||tracks.size() == 0){
        //        }

        this.mCurrentTracks = tracks;
        mDetailListAdapter.setData(tracks);

    }

    @Override
    public void onAlbumLoaded(Album album) {
        long id = album.getId();

        //获取专辑的详情内容
        if (mAlbumDetailPresenter != null) {

            mAlbumDetailPresenter.getAlbumDetail((int) id, mCurrentPage);
        }

        //拿数据显示loading状态
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mLargeCover != null) {
            if (mLargeCover != null) {

                Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
                    @Override
                    public void onSuccess() {
                        Drawable drawable = mLargeCover.getDrawable();
                        if (drawable != null) {
                            ImageBlur.makeBlur(mLargeCover, DetailActivity.this);
                        }

                    }

                    @Override
                    public void onError() {

                    }
                });
            }


        }
        if (mSmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mSmallCover);
        }
    }

    @Override
    public void onNetWorkError(int errorCode, String errorMsg) {
        //请求发生错误，显示网络异常状态
        mUiLoader.updateStatus(UILoader.UIStatus.NET_ERROR);
    }

    @Override
    public void onLoaderMoreFinished(int size) {
        if (size>0) {
            Toast.makeText(this,"成功加载"+size+"条数据",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"没有更多的节目",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onRefreshFinished(int size) {

    }


    @Override
    public void onItemClick(List<Track> detailData, int position) {
        //设置播放器数据
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayList(detailData, position);
        //TODO跳转到播放页面
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    private void updatePlayState(boolean isplaying) {
        //修改图标为暂停的，文字修改为正在播放
        if (mPlayControlBtn != null && mPlayControlTips != null) {
            mPlayControlBtn.setImageResource(isplaying ? R.drawable.selector_play_control_pause : R.drawable.selector_play_control_play);
            if (!isplaying) {
                mPlayControlTips.setText(R.string.click_play_tips_text);
            }else{
                if (!TextUtils.isEmpty(mCurrentTrackTitle)) {
                    mPlayControlTips.setText(mCurrentTrackTitle);
                }
            }
        }
    }

    //根据播放状态修改图标和文字
    @Override
    public void onPlayStart() {
        //修改图标为暂停的，文字修改为正在播放
        updatePlayState(true);
    }


    @Override
    public void onPlayPause() {
        //修改图标为播放图标，文字修改为已暂停
        updatePlayState(false);

    }

    @Override
    public void onPlayStop() {
        //和暂停一样
        updatePlayState(false);
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        if (track != null) {
            mCurrentTrackTitle = track.getTrackTitle();
            if (!TextUtils.isEmpty(mCurrentTrackTitle) && mPlayControlTips != null) {
                mPlayControlTips.setText(mCurrentTrackTitle);

            }
        }

    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }

    @Override
    public void onRetryClick() {
        //用户网络不佳的时候去点击了重新加载
        if (mAlbumDetailPresenter != null) {

            mAlbumDetailPresenter.getAlbumDetail((int) mCurrentId, mCurrentPage);
        }

    }
}
