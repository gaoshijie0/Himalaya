package com.glandroid.himalaya.views;


import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.glandroid.himalaya.R;

/**
 * @author Administrator
 * @version $Rev$
 * @dex ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class LoadingView extends android.support.v7.widget.AppCompatImageView {
    //旋转的角度
    private int rotateDegree = 0;
    private boolean mNeedRotate = false;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }


    @Override
    protected void onAttachedToWindow() {
        //绑定到window的时候
            mNeedRotate  =  true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
               rotateDegree =  rotateDegree <= 360?rotateDegree:rotateDegree-360;
               invalidate();
                if (mNeedRotate) {
                    postDelayed(this,100);
                }
            }
        });
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //旋转角度
        //旋转的x坐标
        //旋转的y坐标
        canvas.rotate(rotateDegree,getWidth()/2,getHeight()/2);

        super.onDraw(canvas);
    }
}
