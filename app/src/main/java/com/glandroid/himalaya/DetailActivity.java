package com.glandroid.himalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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
import com.glandroid.himalaya.presenters.AlbumDetailPresenter;
import com.glandroid.himalaya.views.RoundImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, DetailListAdapter.ItemClickListener {

    private AlbumDetailPresenter mAlbumDetailPresenter;
    private ImageView mLargeCover;
    private RoundImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private int mCurrentPage = 1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);

    }

    private void initView() {
        mLargeCover = this.findViewById(R.id.iv_large_color);
        mSmallCover = this.findViewById(R.id.riv_small_color);
        mAlbumTitle = this.findViewById(R.id.tv_album_titile);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);
        mDetailList = this.findViewById(R.id.album_detail_list);
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
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover);
        }
        if (mSmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mSmallCover );
        }
    }

    @Override
    public void onItemClick(View itemView) {
        //TODO跳转到播放页面
        Intent intent = new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }
}
