package com.example.threaddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testScheduled();
            }
        });
    }

    /**
     * 缓存线程池
     */

    public void testCache() {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i <= 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, Thread.currentThread().getName() + " " + index);
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 定长线程池
     */

    public void testFixed() {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i <= 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, Thread.currentThread().getName() + " " + index);
                    try {

                        Thread.sleep(2 * 1000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    /**
     * 单一线程池
     */
    public void testSingle() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        for (int i = 0; i <= 10; i++) {
            final int index = i;
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, Thread.currentThread().getName() + " " + index);
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void testScheduled() {

        Log.d(TAG, " start scheduled");


        //单一延迟执行
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

//        scheduledExecutorService.schedule(new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d(TAG, Thread.currentThread().getName());
//
//            }
//        }, 2, TimeUnit.SECONDS);

//      循环有周期的执行  2 代表第一次运行程序 到第一个线程运行的开始间隔  3  代表每个线程运行的间隔。
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d(TAG,Thread.currentThread().getName());
//
//            }
//        },2,3,TimeUnit.SECONDS);
//
//        //停止线程
//        scheduledExecutorService.shutdown();

        ScheduledExecutorService scheduledExecutorService1 = Executors.newScheduledThreadPool(4);
        scheduledExecutorService1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG,Thread.currentThread().getName()+"ScheduledThreadPool");
            }
        },2,3,TimeUnit.SECONDS);


//        scheduledExecutorService1.schedule(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG,Thread.currentThread().getName()+"ScheduledThreadPool");
//
//            }
//        },2,TimeUnit.SECONDS);

    }
}
