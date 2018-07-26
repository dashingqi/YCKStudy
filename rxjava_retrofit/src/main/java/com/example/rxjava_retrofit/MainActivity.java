package com.example.rxjava_retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    private Subscription subscribeOn;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPostRetrofit = findViewById(R.id.btn_post_retrofit);
        btnPostRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postIpInformation("59.108.54.37");
            }
        });
    }


    public void postIpInformation(String ip){
        String url ="http://ip.taobao.com/service/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        IpServiceForPost ipServiceForPost = retrofit.create(IpServiceForPost.class);
        subscribeOn = ipServiceForPost.getIpMsg(ip).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<IpMode>() {
            @Override
            public void onCompleted() {
                Log.d("TAG", "completed");

            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG", "onError");

            }

            @Override
            public void onNext(IpMode ipMode) {

                Log.d("TAg", "" + ipMode.getCode());
                Log.d("TAG", ipMode.getData());
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_LONG).show();
            }
        });

        compositeSubscription.add(subscribeOn);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消所有的请求
        compositeSubscription.unsubscribe();
    }
}
