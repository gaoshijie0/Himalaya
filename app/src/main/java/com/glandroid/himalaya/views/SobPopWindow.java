package com.glandroid.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.adapters.PlayListApdater;
import com.glandroid.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class SobPopWindow extends PopupWindow {

    private final View mPopView;
    private TextView mCloseBtn;
    private PlayListApdater mPlayListApdater;
    private RecyclerView mTrackslist;
    private TextView mMPlayModeTv;
    private ImageView mMPlayModeiv;
    private View mPlayModeContainer;
    private PlayListPlayModeClickListener mPlayModeClickListener = null;

    public SobPopWindow() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //TODO:这里要注意，在setOusideTouchable之前要先设置setBackgoundDrawble
        //否则点击外部无法关闭POP
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        //要载进来的View
        mPopView = LayoutInflater.from(BaseApplication.getApContext()).inflate(R.layout.pop_play_list, null);
        setContentView(mPopView);
        //设置窗口进入和退出的动画
        //TODO:
        setAnimationStyle(R.style.pop_animation);
        initView();
        intEvent();
    }

    private void intEvent() {
        //点击窗口消失
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        mPlayModeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换播放模式
                if (mPlayModeClickListener != null) {
                    mPlayModeClickListener.onPlayModeClick();
                }
            }
        });
    }

    private void initView() {
        mCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);
        mTrackslist = mPopView.findViewById(R.id.play_list_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getApContext());
        mTrackslist.setLayoutManager(linearLayoutManager);
        //TODO:设置适配器
        mPlayListApdater = new PlayListApdater();
        mTrackslist.setAdapter(mPlayListApdater);
        mMPlayModeTv = mPopView.findViewById(R.id.play_list_play_mode_tv);
        mMPlayModeiv = mPopView.findViewById(R.id.play_list_play_mode_iv);
        mPlayModeContainer = mPopView.findViewById(R.id.play_list_play_mode_container);


    }

    /**
     * 给适配器设置数据
     *
     * @param data
     */
    public void setListData(List<Track> data) {
        if (mPlayListApdater != null) {

            mPlayListApdater.setData(data);
        }
    }
    public void setCurrentPlayPosition(int position){
        if (mPlayListApdater != null) {
            mPlayListApdater.setCurrentPlayPosition(position);
            mTrackslist.scrollToPosition(position);
        }

    }
    //TODO：不用持有，给adapter持有
    public void setPlayListItemClickListener(PlayListItemClickListener playListItemClickListener){
        mPlayListApdater.setOnItemClickListener(playListItemClickListener);

    }

    /**
     * 更新播放列表的播放模式
     * @param currentMode
     */
    public void updatePlayMode(XmPlayListControl.PlayMode currentMode) {
        updatePlayModeBtnImg(currentMode);
    }

    /**
     * 根据当前的的模式更改切换图标
     * PLAY_MODEL_LIST
     * PLAY_MODEL_LIST_LOOP
     * PLAY_MODEL_RANDOM
     * PLAY_MODEL_SINGLE_LOOP
     */
    private void updatePlayModeBtnImg(XmPlayListControl.PlayMode playMode) {
        int resId = R.mipmap.play_mode_list_order;
        int textId= R.string.play_mode_order_text;
        switch (playMode) {
            case PLAY_MODEL_LIST:
                resId = R.mipmap.play_mode_list_order;
                textId= R.string.play_mode_order_text;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.mipmap.play_mode_random;
                textId= R.string.play_mode_random_text;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.play_mode_list_looper;
                textId= R.string.play_mode_list_play_text;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.mipmap.play_mode_single_loop;
                textId= R.string.play_mode_single_loop_text;
                break;


        }
        mMPlayModeiv.setImageResource(resId);
        mMPlayModeTv.setText(textId);

    }

    public interface PlayListItemClickListener{
        void onItemClick(int position);
    }

    public void setPlayListPlayModeClickListener(PlayListPlayModeClickListener playModeListener){
        mPlayModeClickListener = playModeListener;

    }

    public interface  PlayListPlayModeClickListener{
        void onPlayModeClick();
    }

}
