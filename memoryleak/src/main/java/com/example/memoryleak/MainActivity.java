package com.example.memoryleak;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
////        //静态变量导致的内存泄露
////        mContext = this ;
//        SystemClock.sleep(30*1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                testANR();

            }
        }).start();

        SystemClock.sleep(10);
        initView();

    }

    private synchronized void initView() {

    }

    private synchronized void testANR(){
        SystemClock.sleep(30* 1000);
    }
}
