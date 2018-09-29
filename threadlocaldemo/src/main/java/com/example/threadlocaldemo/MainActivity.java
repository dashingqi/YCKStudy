package com.example.threadlocaldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    private ThreadLocal<Boolean> mThreadLocal = new ThreadLocal<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThreadLocal.set(true);
        Log.d("Thread1=",mThreadLocal.get()+"");

        new Thread("Thread2"){
            @Override
            public void run() {

                mThreadLocal.set(false);
                Log.d("Thread2=",mThreadLocal.get()+"");
            }
        }.start();


        new Thread("Thread3"){
            @Override
            public void run() {
               Log.d("Thread3=",mThreadLocal.get()+"");
            }
        }.start();
    }
}
