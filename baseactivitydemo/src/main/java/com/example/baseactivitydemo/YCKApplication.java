package com.example.baseactivitydemo;

import android.app.Application;
import android.content.Context;

public class YCKApplication extends Application {

    private static Context yckContext;

    /**
     * 获取到全局的Context
     * @return 返回上下文
     */
    public static Context getContext(){

        return yckContext;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        yckContext = getApplicationContext();

    }
}
