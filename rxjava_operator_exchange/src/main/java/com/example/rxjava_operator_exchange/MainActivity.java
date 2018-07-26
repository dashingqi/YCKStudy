package com.example.rxjava_operator_exchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnExchangeOperator = findViewById(R.id.btn_exchange_operator);
        btnExchangeOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mapObservable();
                //flatMapObservable();
                //contactMapObservable();
                //flatMapIterable();
                // bufferObservable();
                groupByObservable();

            }
        });
    }

    public void mapObservable() {
        final String HOST = "http://www.baidu.com/";
        rx.Observable.just("itcast").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return HOST + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("TAG_map_action", "map=" + s);

            }
        });
    }

    public void flatMapObservable() {
        final String HOST = "http://www.baidu.com";
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("itcast1");
        arrayList.add("itcast2");
        arrayList.add("itcast3");
        arrayList.add("itcast4");
        rx.Observable.from(arrayList).flatMap(new Func1<String, rx.Observable<?>>() {
            @Override
            public rx.Observable<?> call(String s) {

                return rx.Observable.just(HOST + s);
            }
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("TAG_flatmap_action", "flatmap=" + s);
            }
        });
    }

    public void contactMapObservable() {
        final String HOST = "http://www.baidu.com";
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("itcast1");
        arrayList.add("itcast2");
        arrayList.add("itcast3");
        arrayList.add("itcast4");
        arrayList.add("itcast5");
        Observable.from(arrayList).concatMap(new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String s) {
                return Observable.just(HOST + s);
            }
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("TAG_contactmap_action", "contactmap=" + s);
            }
        });
    }

    public void flatMapIterable() {
        Observable.just(1, 2, 3).flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
            @Override
            public Iterable<Integer> call(Integer integer) {

                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(integer + 1);
                return integers;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("TAG_flatmapIterable", "flatmapIterable=" + integer);
            }
        });
    }

    public void bufferObservable() {
        Observable.just(1, 2, 3, 4, 5, 6).buffer(3).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                for (Integer i : integers) {
                    Log.d("TAG_buffer_action", "buffer=" + i);
                }
                Log.d("TAG", "*****************************");
            }
        });
    }

    public void groupByObservable() {
        SwordMan s1 = new SwordMan("韦一笑", "A");
        SwordMan s2 = new SwordMan("张三丰", "S");
        SwordMan s3 = new SwordMan("周芷若", "A");
        SwordMan s4 = new SwordMan("宋远桥", "S");
        SwordMan s5 = new SwordMan("殷梨亭", "A");
        SwordMan s6 = new SwordMan("张无忌", "SS");
        SwordMan s7 = new SwordMan("何必梦", "S");
        SwordMan s8 = new SwordMan("宋青书", "A");

        Observable<GroupedObservable<String, SwordMan>> groupedObservableObservable = Observable.just(s1, s2, s3, s4, s5, s6, s7, s8).groupBy(new Func1<SwordMan, String>() {
            @Override
            public String call(SwordMan swordMan) {
                return swordMan.getLevel();
            }
        });

        Observable.concat(groupedObservableObservable).subscribe(new Action1<SwordMan>() {
            @Override
            public void call(SwordMan swordMan) {
                Log.d("TAG_group_action", "group=" + swordMan.getName() + "++++" + swordMan.getLevel());
            }
        });
    }

    public void mapObservable1(){
        final String host ="zhangqi";
        Observable.just("itcast3").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return host+s ;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("TAG","map="+s);
            }
        });
    }

    public void flatMapObservable1(){
        Observable.just(1,2,3,4,45,5,6,6).flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
            @Override
            public Iterable<Integer> call(Integer integer) {
                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(integer+1);
                return integers;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("TAG","flat_map"+integer);
            }
        });
    }

    public void bufferObservable1(){
        Observable.just(1,23,4,5,6,7,87,8,89,9).buffer(4).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                for(Integer i: integers) {
                   Log.d("TAG","buffer="+integers);
                }
                Log.d("TAG","*******************");
            }
        });
    }

    public void groupByObsrvable1(){
        SwordMan s1 = new SwordMan("韦一笑", "A");
        SwordMan s2 = new SwordMan("张三丰", "S");
        SwordMan s3 = new SwordMan("周芷若", "A");
        SwordMan s4 = new SwordMan("宋远桥", "S");
        SwordMan s5 = new SwordMan("殷梨亭", "A");
        SwordMan s6 = new SwordMan("张无忌", "SS");
        SwordMan s7 = new SwordMan("何必梦", "S");
        SwordMan s8 = new SwordMan("宋青书", "A");
        Observable<GroupedObservable<String, SwordMan>> groupedObservableObservable = Observable.just(s1, s2, s3, s4, s5, s6, s7, s8).groupBy(new Func1<SwordMan, String>() {
            @Override
            public String call(SwordMan swordMan) {
                return swordMan.getLevel();
            }
        });

        Observable.concat(groupedObservableObservable).subscribe(new Action1<SwordMan>() {
            @Override
            public void call(SwordMan swordMan) {

            }
        });
    }

    public Observable postAsyncHttp(final String ip){
        Observable<Object> objectObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody formBody = new FormBody.Builder()
                        .add("ip", ip)
                        .build();

                Request request = new Request.Builder()
                        .url("http://www.baidu.com")
                        .post(formBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        subscriber.onNext(string);
                        subscriber.onCompleted();

                    }
                });
            }
        });

        return objectObservable;
    }


    public void postObserverHttps(){

        postAsyncHttp("43").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                String str = (String)o;
                Log.d("postObserver","http="+str);
            }
        });
    }
}
