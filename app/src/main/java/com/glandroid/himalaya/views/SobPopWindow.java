package com.glandroid.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.glandroid.himalaya.R;
import com.glandroid.himalaya.base.BaseApplication;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class SobPopWindow extends PopupWindow {
    public SobPopWindow() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //TODO:这里要注意，在setOusideTouchable之前要先设置setBackgoundDrawble
        //否则点击外部无法关闭POP
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        //要载进来的View
        View popView = LayoutInflater.from(BaseApplication.getApContext()).inflate(R.layout.pop_play_list, null);
        setContentView(popView);
        //设置窗口进入和退出的动画
        //TODO:
        setAnimationStyle(R.style.pop_animation);
    }


}
