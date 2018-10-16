package com.example.scrollerdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SmoothScrollView extends LinearLayout {

    private Scroller mScroller;

    public SmoothScrollView(Context context) {
        super(context);
    }

    public SmoothScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //创建Scroller 实例
        mScroller = new Scroller(context);
    }

    public SmoothScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmoothScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



}
