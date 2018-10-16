package com.example.gesturedetectordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

    private Scroller scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scroller = new Scroller(this);
    }

//    private void smoothScrollTo(int destX,int destY)
//    {
//       int scrollX = getScrollX();
//       int delta = destX - scrollX;
//       scrollX.startScroll(scrollX,0,delta,0,1000);
//       invalidate();
//
//
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        GestureDetector detector = new GestureDetector(this);
        //解决长按屏幕后无法拖动的现象。
        detector.setIsLongpressEnabled(false);

        //接管目标View 的 onTouchEvent方法。
        boolean consume = detector.onTouchEvent(event);

        return consume;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //手指轻轻触摸屏幕的一瞬间。
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //手指轻轻触摸屏幕，尚未松开或者拖动。

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //单击行为
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //手指按下屏幕并拖动
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长按屏幕

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //用户按下屏幕快速滑动松开。
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //严格的单击行为。
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //双击由两次连续的单击组成。
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //发生了双击的行为
        return false;
    }
}
