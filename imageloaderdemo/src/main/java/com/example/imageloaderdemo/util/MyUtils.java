package com.example.imageloaderdemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.IOException;

public class MyUtils {

    public static void close(Closeable closeable) {
        try {

            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    /**
     * dp 转 px
     * @param context
     * @param dp
     * @return
     */
    public  static float dp2px(Context context, float dp) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

    }

    /**
     * 判断是否是WIFI网络环境下的
     * @param context
     * @return
     */
    public static boolean isWifi(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            return true;
        }


        return false;

    }

    public static DisplayMetrics getScreenMetrics(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics;
    }
}
