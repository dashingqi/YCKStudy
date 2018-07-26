package com.example.rxjavademo;

import android.icu.util.TimeUnit;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clickRxJava = findViewById(R.id.btn_click_rxjava);
        clickRxJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //订阅事件
                //getObservable().subscribe(onNextAction,onErrorAction,onCompletetdAction);
                timeObservable();
            }
        });

    }

    /**
     * 创建监听者
     */
    public Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onStart() {
            Log.d("TAG_Main", "start");
        }


        @Override
        public void onCompleted() {
            Log.d("TAG_Main", "completetd");

        }

        @Override
        public void onError(Throwable e) {
            Log.d("TAG_Main", e.getMessage());

        }

        @Override
        public void onNext(String s) {
            Log.d("TAG_Main", s);

        }
    };

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {
            Log.d("TAG_Main", "completed");

        }

        @Override
        public void onError(Throwable e) {
            Log.d("TAG_Main", e.getMessage());

        }

        @Override
        public void onNext(String s) {
            Log.d("TAG_Main", s);

        }
    };


    /**
     * 创建被观察者
     */
    public Observable createObservable() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("zhangqi");
                subscriber.onNext("zhangsan");
                subscriber.onCompleted();

            }
        });
        return observable;
    }

    public Observable<String> getObservable() {
        String[] nameArray = {"张三", "李思", "王五"};
        Observable<String> observable = Observable.from(nameArray);
        return observable;
    }


    Action1<String> onNextAction = new Action1<String>(){
        @Override
        public void call(String s) {
            Log.d("Action",s);

        }
    };

    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {

            Log.d("Action", throwable.getMessage());
        }
    };

   Action0 onCompletetdAction = new Action0() {
       @Override
       public void call() {

           Log.d("Action","completetdAction");
       }
   };

   public  void timeObservable(){
       Observable.interval(3, java.util.concurrent.TimeUnit.SECONDS).subscribe(new Action1<Long>() {
           @Override
           public void call(Long aLong) {
               Log.d("Time_Action","intValue = "+aLong.intValue());
           }
       });
   }

    //    private Observable<String> getObservable(final String ip) {
//        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(final Subscriber<? super String> subscriber) {
//                OkHttpClient okHttpClient = new OkHttpClient();
//
//                FormBody formBody = new FormBody.Builder()
//                        .add("ip", ip)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url("http://ip.taobao.com/service/getIpInfo.php")
//                        .post(formBody)
//                        .build();
//                Call call = okHttpClient.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(new Exception("error"));
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String str = response.body().string();
//                        subscriber.onNext(str);
//                        subscriber.onCompleted();
//                    }
//                });
//            }
//        });
//
//        return observable;
//    }
//
//
//    private void postAsyncHttp(String size) {
//        getObservable(size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                Log.d("TAG_Main", "onCompleted");
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("TAG_Main", e.getMessage());
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d("TAG_Main", s);
//                Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

}
