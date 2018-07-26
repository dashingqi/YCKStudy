package com.example.rxjava_operator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定时器的操作符
                //timeObservable();
                //rangeObservable();
                repeatObservable();
            }
        });
    }

    public void timeObservable(){
        Observable.interval(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d("TAG_time_action","interval="+aLong.intValue());

            }
        });
    }

    /**
     * 发射指定范围的整数数值的Observable 可以代替for循环
     */
    public void rangeObservable(){
        Observable.range(0,7).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("TAG_int_action","range="+integer.intValue());

            }
        });
    }

    public void repeatObservable(){
        Observable.range(0,3).repeat(3).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("TAG_repeat_action","repeat="+integer.intValue());
            }
        });
    }
}
