package com.glandroid.himalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glandroid.himalaya.adapters.DetailListAdapter;
import com.glandroid.himalaya.base.BaseActivity;
import com.glandroid.himalaya.interfaces.IAlbumDetailViewCallback;
import com.glandroid.himalaya.interfaces.IPlayerCallback;
import com.glandroid.himalaya.presenters.AlbumDetailPresenter;
import com.glandroid.himalaya.presenters.PlayerPresenter;
import com.glandroid.himalaya.utils.LogUtil;
import com.glandroid.himalaya.views.ImageBlur;
import com.glandroid.himalaya.views.RoundImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, DetailListAdapter.ItemClickListener, IPlayerCallback {

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

                        }else{
                            handleNoPlayList();
                        }
                    }
                }
            });
        }
    }

    //   当播放器里面没有播放列表的处理
    private void handleNoPlayList() {
      mPlayerPresenter.setPlayList(mCurrentTracks,DEFAULT_PLAY_INDEX);

    }

    private void handlePlayControl() {
        //控制播放器的状态
        if (mPlayerPresenter.isplaying()) {
            //正在播放，那么暂停
            mPlayerPresenter.pause();
        }else{
            mPlayerPresenter.play();

        }
    }

    private void  initView() {
        LogUtil.d(TAG,"测试一下，哈哈哈哈哈");
        mLargeCover = this.findViewById(R.id.iv_large_color);
        mSmallCover = this.findViewById(R.id.riv_small_color);
        mAlbumTitle = this.findViewById(R.id.tv_album_titile);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);
        mDetailList = this.findViewById(R.id.album_detail_list);
        //设置播放图标
        mPlayControlBtn = findViewById(R.id.detail_play_control);
        mPlayControlTips = findViewById(R.id.play_control_text);

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
        mDetailListAdapter.setOnItemClickListener(this);
        mDetailList.setAdapter(mDetailListAdapter);

    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
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
        mAlbumDetailPresenter.getAlbumDetail((int)id,mCurrentPage);

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
                            ImageBlur.makeBlur(mLargeCover,DetailActivity.this);
                        }

                    }

                    @Override
                    public void onError() {

                    }
                });
            }


        }
        if (mSmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mSmallCover );
        }
    }


    @Override
    public void onItemClick(List<Track> detailData, int position) {
        //设置播放器数据
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayList(detailData,position);
        //TODO跳转到播放页面
        Intent intent = new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }

    private void updatePlayState(boolean isplaying) {
        //修改图标为暂停的，文字修改为正在播放
        if (mPlayControlBtn != null && mPlayControlTips != null) {
            mPlayControlBtn.setImageResource(isplaying?R.drawable.selector_play_control_pause:R.drawable.selector_play_control_play );
            mPlayControlTips.setText(isplaying?R.string.playing_text:R.string.pause_text);
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

    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }
}
