package com.example.velocitytrackerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //追踪当前单击事件的速度。
        VelocityTracker tracker = VelocityTracker.obtain();
        tracker.addMovement(event);
        //计算速度设置时间间隔
        tracker.computeCurrentVelocity(1000);
        //获取速度
        int xVelocity = (int) tracker.getXVelocity();
        int yVelocity = (int) tracker.getYVelocity();


        //当不需要使用的时候，需要clear来重置并且回收内存

        tracker.clear();
        tracker.recycle();

        return super.onTouchEvent(event);
    }
}
