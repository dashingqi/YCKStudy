package com.example.intentservicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyLocalIntentService extends IntentService {


    private boolean isRunning = true;



    private static  final String TAG = MyLocalIntentService.class.getSimpleName();
    private LocalBroadcastManager instance;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyLocalIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        instance = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"onHandleIntent");

        int count =0;

        while(isRunning){

            SystemClock.sleep(1000);

            count++;

            if (count>100){
                isRunning = false;
            }

            SystemClock.sleep(500);

            sendCountToFront(count);

        }

    }

    private void sendCountToFront(int count) {

        Intent intent = new Intent(MainActivity.ACTION_ACTIVITY);
        intent.putExtra("count",count);
        instance.sendBroadcast(intent);

    }


    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }
}
