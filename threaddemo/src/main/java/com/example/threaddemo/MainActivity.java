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

    /**
     * 线程池优势：
     * 1、重复利用线程池中闲置的线程，避免线程的创建和销毁所带来的性能上的开销。（缓存线程）
     * 2、可设置最大的线程并发数，可避免线程争抢资源造成的线程阻塞。（定长线程，单一线程）
     * 3、可设置定时的有时间间隔的循环的执行任务。（ScheduledThreadPool）
     *
     * 线程池使用上的规则
     * 1、如果线程池中的核心线程数大于最大线程数，那么执行任务就用核心线程。
     * 2、如果线程池中的核心线程都在工作，但是还有任务，那么就会插入到任务队列中去等待。
     * 3、在情况2的条件下，如果任务没有能够插入到任务队列中，那么就是任务队列已经满了，这个时候如果线程数量没有达到线程池最大数量
     * 就会重新启用一个非核心线程去执行该任务。
     * 4、在情况3的条件下，如果线程数量已经达到最大线程池中容纳的线程数量，那么就拒绝去执行该任务
     */

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
