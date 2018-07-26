package com.example.loadingdialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog1 extends Dialog {
    private static LoadingDialog1 mLoadingDialog;

    public LoadingDialog1(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog1(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog1(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static void showDialog(Context context, String msg, boolean isCanCancel) {

        mLoadingDialog = new LoadingDialog1(context, R.style.LoadingDialogTheme);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setTitle("");
        mLoadingDialog.setContentView(R.layout.loading_layout);
        mLoadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (msg != null || !TextUtils.isEmpty(msg)) {
            TextView tv_loading = mLoadingDialog.findViewById(R.id.loading_tv);
            tv_loading.setText(msg);
        } else {
            mLoadingDialog.findViewById(R.id.loading_tv).setVisibility(View.GONE);
        }

        //按下返回键 是否取消加载框
        mLoadingDialog.setCancelable(isCanCancel);

        mLoadingDialog.show();

    }

    public static void dissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
