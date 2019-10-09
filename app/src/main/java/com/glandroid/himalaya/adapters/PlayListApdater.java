package com.glandroid.himalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.base.BaseApplication;
import com.glandroid.himalaya.views.SobPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class PlayListApdater extends RecyclerView.Adapter<PlayListApdater.InnerHolder> {
    private List<Track> mData  = new ArrayList<>();
    private int playingIndex = 0;
    private SobPopWindow.PlayListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(BaseApplication.getApContext()).inflate(R.layout.item_play_list, viewGroup, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder innerHolder, final int position) {
        innerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
        Track track = mData.get(position);
        TextView trackTitleTV = innerHolder.itemView.findViewById(R.id.track_title_tv);
        //设置字体颜色
        trackTitleTV.setTextColor(BaseApplication.getApContext().getResources().getColor(playingIndex == position?
                R.color.second_color:R.color.play_list_text_color));
        trackTitleTV.setText(track.getTrackTitle());
        View playingIconView = innerHolder.itemView.findViewById(R.id.play_icon_iv);
        playingIconView.setVisibility(playingIndex == position?View.VISIBLE:View.GONE );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Track> data) {
        //设置数据，更新列表
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();

    }

    public void setCurrentPlayPosition(int position) {
        playingIndex = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(SobPopWindow.PlayListItemClickListener playListItemClickListener) {
        this.mItemClickListener = playListItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
