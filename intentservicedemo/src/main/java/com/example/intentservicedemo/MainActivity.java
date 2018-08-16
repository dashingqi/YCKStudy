package com.example.intentservicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn_start;
    private ProgressBar mProgressBar;
    public  static final   String  ACTION_ACTIVITY = "MainActivity";
    private TextView tvShowProgress;
    private MyLocalBroadcastReceiver myLocalBroadcastReceiver;
    private LocalBroadcastManager instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        instance = LocalBroadcastManager.getInstance(this);
        myLocalBroadcastReceiver = new MyLocalBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ACTIVITY);

        instance.registerReceiver(myLocalBroadcastReceiver,intentFilter);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MyLocalIntentService.class);
                startService(intent);

            }
        });


    }

    private void initView() {
        btn_start = findViewById(R.id.btn_start);
        mProgressBar = findViewById(R.id.pb);
        tvShowProgress = findViewById(R.id.tv_show_progress);

        mProgressBar.setProgress(0);
    }



    class MyLocalBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){
                case ACTION_ACTIVITY:
                    int count = intent.getIntExtra("count", 0);
                    tvShowProgress.setText(count+"%");
                    mProgressBar.setProgress(count);

                    break;
            }


            //更新UI

        }
    }

    @Override
    protected void onDestroy() {

        instance.unregisterReceiver(myLocalBroadcastReceiver);
        super.onDestroy();

    }
}
