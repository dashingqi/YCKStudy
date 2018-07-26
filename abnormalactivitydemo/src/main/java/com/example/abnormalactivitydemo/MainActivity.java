package com.example.abnormalactivitydemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * standard:标准模式。
 *
 * singleTop:栈顶复用模式，如果某个Activity的实例是位于栈顶，当再次创建这个Activity的实例时（此时Activity的启动模式为singleTop），就会复用栈顶这个实例，
 * 并不会创建新的Activity实例，此时会调用onNewIntent()方法，onCreate()和onStart()方法不会调用。如果此时Activity的情动模式为standard,那么就会创建新的启动模式.
 *
 * singleTask:单实例模式， Activity启动的时候以这种模式启动，首先查找是否有符合入栈的任务栈，有的话再去任务栈中找 是否有创建过得实例，有的话，就将该实例放到栈顶
 * 。没有符合的任务栈，就创建新的任务栈入栈。如果任务栈中有这个实例，复用实例，并且调用 onNewIntent();
 *
 * singleInstance:  TaskAffinity:任务相关性  这个参数表示当前Activity所在任务栈的名字，默认情况下都是当前包名的名字，该参数可以自定义，但是不能喝包名名字一样，这样就没有意义了。
 * 此参数可以和singleTask配合使用，也可以和allowTaskReparenting配合使用，
 * 当ActivityC 的  allowTaskReparenting = true 时 ，应用A启动了应用B中的ActivityC ，按下Home键，打开应用B的时候，这是会启动ActivityC,应为应用A启动了ActivityC，此时C进入了应用A的任务栈，
 * 但是Activity TaskAffinity 是B的包名，当打开应用B的时候创建了 符合应用C的任务栈，所以就从任务A的栈 进入到当前B所创建的任务栈中，
 *
 *
 * //隐式意图：
 * IntentFilter 匹配规则 匹配到一组的IntentFilter的action，category和data一样，那么就可以进行跳转了。
 * 1. action：自定义的字符串或者系统自定义好的，必须的设置一个值
 * intent.setAction()
 * 2. category：可以不用添加category，因为startActivity()或者startActivityForResult()默认会把 默认的Category 设置进去
 * intent.addCategory();
 * 3. data：
 *  1）scheme :URI的模式  http  file content  URI默认值为 content和file   Uri:统一资源定位符
 *  2) host  ：主机名
 *  3) port  ：端口
 *  4) path  ：参数
 *  5)pathPattern
 *  6)pathPrefix
 *  7)mimeType：
 *  intent.setDataAndType("content://com.dashingqi","image/*");
 *
 */
public class MainActivity extends AppCompatActivity {

    public final String TAG = MainActivity.class.getSimpleName();
    private String content;
    private EditText etInput;

    /**
     *  android:configChanges="orientation|screenSize"
     *  当屏幕发生旋转的时候，不会出现重新创建Activity,而是调用onConfigurationChanged()，进而做处理
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startApplication = findViewById(R.id.btn_start_application);
        startApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //非Activity的context 去启动Activity，此时是没有任务栈的，
                // 待启动的Activity是没有任务栈可入的，需要指明启动模式为SingleTask模式启动
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                startActivity(intent);
            }
        });

        Log.d(TAG,"onCreate");
        etInput = findViewById(R.id.et_input);

        if (savedInstanceState!=null){
            String data = savedInstanceState.getString("data");
            etInput.setText(data);

        }

    }


    /**
     *  异常情况下，比如屏幕旋转，不是用户主动去销毁Activity（按Back键），可以此方法中进行数据的保存
     *  过程是这样：首先Activity回去委托Window去保存数据，然后window会去通知顶层容器去存储数据（ViewGroup 一般指的是DecorView）,然后偶顶层容器
     *  会去通知子view去保存数据，"上层委托下层，父容器指派子容器去保存数据" 异常情况下不是用户主动去销毁Activity，都会调用onSaveInstanceState()方法去保存数据
     *  此方法和onPause没有时序的调用关系，在onStop之前调用。
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState");
        content = etInput.getText().toString();
        outState.putString("data",content);

    }


    /**
     * 此方法的执行 Bundle中肯定室友数据，所以不用判断为空，onCreate中得需要判断为空，
     * 因为有时候是Activity的首次创建加载布局，不是异常情况下重新创建的，所以得判断不为空的情况下，才能去取数据的。
     * 此方法是在onStart之后调用 与onResume不存在前后调用的 顺序，有可能之前也有可能之后。
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG,"onRestoreInstanceState");

        String data = savedInstanceState.getString("data");
        Log.d(TAG,"data= "+data);
        etInput.setText(data);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG,"change");
    }


}
