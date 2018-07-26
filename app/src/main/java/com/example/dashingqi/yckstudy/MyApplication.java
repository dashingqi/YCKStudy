package com.example.dashingqi.yckstudy;

import android.app.Application;
import android.content.Context;

/**
 * Created by dashingqi on 23/4/18.
 */

public class MyApplication extends Application {

    private Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        SharedPrefsUtils.init(applicationContext);

    }
}
