package com.glandroid.himalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glandroid.himalaya.R;
import com.squareup.picasso.Picasso;
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
public class PlayTrackPagerAdapter extends PagerAdapter {
    private List<Track> mData = new ArrayList<>();
    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_pager,container,false);
        container.addView(itemView);
        ImageView item = itemView.findViewById(R.id.track_pager_item);
        Track track = mData.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();
        Picasso.with(container.getContext()).load(coverUrlLarge).into(item);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object ;
    }

    public void setData(List<Track> list) {
       mData.clear();
       mData.addAll(list);
       notifyDataSetChanged();

    }
}
