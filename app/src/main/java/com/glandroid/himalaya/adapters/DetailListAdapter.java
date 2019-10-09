package com.glandroid.himalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glandroid.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {
    List<Track> mDetailData  = new ArrayList<>();
    private SimpleDateFormat mUpdateDateFromat = new SimpleDateFormat("yy-mm-dd");
    private SimpleDateFormat mDurationFormat= new SimpleDateFormat("mm:dd");
    private ItemClickListener mItemClicklistener = null;

    @NonNull
    @Override
    public DetailListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_layout,parent,false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailListAdapter.InnerHolder innerHolder, final int position) {
           View itemView = innerHolder.itemView;
        TextView orderView = itemView.findViewById(R.id.order_text);
        TextView titleTV = itemView.findViewById(R.id.detail_item_title);
        TextView playCountTV = itemView.findViewById(R.id.detail_item_play_count);
        TextView durationTV = itemView.findViewById(R.id.detail_item_duration);
        TextView updateTimeTV = itemView.findViewById(R.id.detail_item_update_time);

        Track track = mDetailData.get(position);

        orderView.setText((position+1)+"");
        titleTV.setText(track.getTrackTitle());
        playCountTV.setText(track.getPlayCount()+"");
        int durationMil = track.getDuration()*1000;
        String durantion = mDurationFormat.format(durationMil);
        durationTV.setText(durantion);
        String updateTimeText = mUpdateDateFromat.format(track.getUpdatedAt());
        updateTimeTV.setText(updateTimeText);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //条目的点击事件
                if (mItemClicklistener != null) {

                    mItemClicklistener.onItemClick(mDetailData,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        mDetailData.clear();
        mDetailData.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClicklistener =  listener;
    }
    public interface ItemClickListener{

        void onItemClick(List<Track> detailData, int position);
    }
}
