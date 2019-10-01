package com.glandroid.himalaya;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.glandroid.himalaya.base.BaseActivity;
import com.glandroid.himalaya.interfaces.IAlbumDetailViewCallback;
import com.glandroid.himalaya.presenters.AlbumDetailPresenter;
import com.glandroid.himalaya.views.RoundImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private AlbumDetailPresenter mAlbumDetailPresenter;
    private ImageView mLargeCover;
    private RoundImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        init();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }

    private void init() {
        mLargeCover = this.findViewById(R.id.iv_large_color);
        mSmallCover = this.findViewById(R.id.riv_small_color);
        mAlbumTitle = this.findViewById(R.id.tv_album_titile);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);

    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {
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
}
