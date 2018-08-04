package com.example.ipcdemo;

import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * IPC: Intent Process Communication 进程间通信  跨进程通信
 * 多进程：1）配置文件中 给活动设置属性 process = "进程名"，2）在JNI层的native fork一份新进程，但是不常用。
 * 进程是CPU最小的调度单位，一个进程中包含多个线程。包含和被包含的关系
 * adb shell ps 在Terminal中查看进程
 * <p>
 * 多进程：
 * 1）静态成员变量与单例模式都会失效
 * 2）线程的同步机制失效
 * 3）SharedPreferences的可靠性下降：底层是 读写xml文件的，
 * 4）会多次启动Application
 * <p>
 * Serializable:序列化 接口，是Java提供的，谁想序列化就实现这个接口。序列化的是对象，不是类，静态成员变量是属于类的不属于对象的，不参加序列化。
 * serialVersionUID:是在反序列化的时候用的，序列化的时候把系统当前类的serialVersionUID写入到文件中,在反序列化的时候，系统去检测文件中的
 * serialVersionUID 是否和当前类的 serialVersionUID 一样，一样的话就反序列化成功，不一样就抛出 java.io.InvalidClassException异常
 *
 * <p>
 *     Serializable:使用简便，但是效率低，有很多I/O 操作
 *     Parcelable:使用复杂，但是效率高，是Android平台提供的，优先考虑使用这个。
 *
 *
 * Android IPC 方式
 *
 * 1. 使用Bundle
 *
 * Bundle 实现了Parcelable接口，可以在不同的进程间进行通信
 * Bundle 之所以已键值对存储数据，是因为其内部维护了一个ArrayMap()，ArrayMap内部维护了两个数组，一个int数组，是存储对象数据对应的角标
 * 一个对象数组，用来保存Key和value，内部使用二分法进行key的排序，在进行取值的时候也是使用二分法进行取值的。适合于小数据量的传递。使用Bundle
 * 来传递数据可以保证更快的速度和更少的内存占用。
 *
 * Bundle 是使用Parcelable 进行序列化，但是HashMap是使用Serializable进行序列化，
 * 都知道Parcelable是Android平台推荐使用，虽然写法复杂，但是效率高，省内存。
 *
 * Intent.putExtras()和Intent.putString();
 *
 * A传给B B传给C   如果用Bundle 可以在B中直接传递Bundle对象，在传递给C  （在B中可以添加新的Key和Value，在C中也是可以取到的）
 * 如果使用 Intent.putString(),需要在B中把值取出来，在设置到putString中，在传递给C。
 *
 * 2. 使用文件共享
 * 两个进程通过读写同一个文件进行进程间通信。
 *
 */
public class MainActivity extends AppCompatActivity {

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvStartSecond = findViewById(R.id.tv_start_second);

        //序列化过程  写数据的过程
        User user = new User("zhangqi", 14);
        File file = new File(Environment.getExternalStorageDirectory(), "cache.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //反序列化过程  读数据的过程
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            User mUser = (User) objectInputStream.readObject();
            if (mUser != null) {
                Log.d("name = ", mUser.getName());
                Log.d("age = ", String.valueOf(mUser.getAge()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
//        本身就开启了两个进程，意味着开辟了两个内存块和开启了两个虚拟机，不同进程访问同一个类会产生不同的副本，一个进程操作一个副本，所以改变这个public 的 静态变量 在同一个进程
//        中是有所影响的，在不同进程中 改变一个进程中的静态变量，不会影响另外一个独立进程中的静态变量的
        UserManager.sUerId = 2;
        Log.d("sUserId = ", String.valueOf(UserManager.sUerId));
        tvStartSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                ParcelUser parcelUser = new ParcelUser("zhangqi", 12, "dalian");
                Bundle bundle = new Bundle();
                bundle.putParcelable("user",parcelUser);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}
