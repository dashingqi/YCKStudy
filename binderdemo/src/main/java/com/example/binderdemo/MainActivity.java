package com.example.binderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * Binder 是Android中的类，它实现了IBinder接口
 * Binder主要用在Service中，包括AIDL和Messenger(信使)
 *
 * 一般的Service当中不涉及到跨进程通信，而Messenger底层也是AIDL的原理。
 *
 * 用AIDL来分析Binder
 *
 * AIDL 分析
 *
 * 编译器生成的对应AIDL的类
 * 1）继承自IInterface接口，本身也是个接口，所有在Binder进行跨进程通信的都是要继承IInterface接口。
 * 2）DESCRIPTOR:Binder唯一标示，一般用当前类名标示 com.dashingqi.binderdemo.IBookManager
 * 3) asInterface(android.os.IBinder obj)
 * 该方法是将服务端的Binder对象转化成客户端所需的AIDL类型的对象，这种转化过程
 * 是分进程的，如果服务端和客户端是处在同一个进程中，那么此方法返回的是Stub对象本身，
 * 否则返回的是系统封装后的Stub.proxy对象。
 * 4）asBinder:返回系统当前的Binder对象。
 * 5）onTransact(int code,android.os.parcel data,android.os.Parcel replay,int flags);
 *  该方法是运行在服务端的Binder线程池中，通过code  来判别客户端所请求的目标方法是什么。
 *  接着从data中取出目标方法中所需的参数（如果目标方法中有参数的话），然后执行目标方法，目标方法执行完毕后
 *  就将目标方法返回的返回值，写入到replay中（目标方法有放回的时候）。
 *  如果onTransact()方法返回false 那么标示客户端的请求失败了。
 * 6）Proxy # getBookList()
 *
 */
//TODO

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
