package com.example.dashingqi.yckstudy;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by dashingqi on 23/4/18.
 */

public class ToastUtils {

    private static Toast toast;
    private static long startTime;
    private static long endTime;
    private static String oldMsg=null;

    public ToastUtils() {
        throw new RuntimeException("ToastUtils is not initialization");
    }

    public static void show(Context mContext, String msg) {

        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
            toast.show();
            startTime = System.currentTimeMillis();
        } else {
            endTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (endTime - startTime > Toast.LENGTH_LONG) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
            startTime = endTime;
        }

    }

    public static void show(Context mContext, @StringRes int strRes) {
        show(mContext, mContext.getString(strRes));

    }
}
