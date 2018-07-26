package com.example.loadingdialogdemo;

import android.content.Context;
import android.os.Message;

public class DialogPostUtils {

    private static LoadingDialog loadingPostDialog;

    /**
     * 展示对话框
     *
     * @param context 上下文环境
     * @param message 提示信息
     */
    public static void showDialog(Context context, String message,boolean isShow) {

        LoadingDialog.Builder builder = new LoadingDialog.Builder(context)
                .setCanCancel(false)
                .setCanCancelOutSide(false)
                .setMessage(message)
                .setShowMessage(isShow);
        loadingPostDialog = builder.create();
        if (!loadingPostDialog.isShowing()) {
            loadingPostDialog.show();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dissDialog();
            }
        },6000);



    }

    /**
     * 关闭对话框
     */
    public static void dissDialog() {
        if (loadingPostDialog != null && loadingPostDialog.isShowing()) {
            loadingPostDialog.dismiss();
        }

    }

    private static  android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
