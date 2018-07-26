package com.example.loadingdialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{

        private Context mContext;
        private String message;
        private boolean isShow;
        private boolean isCanCancel;
        private boolean isCanCancelOutSide;

        public Builder(Context context){
            mContext = context;
        }

        /**
         * 设置提醒的消息
         * @param message
         * @return
         */
        public Builder setMessage(String message){
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示消息
         * @param isShow
         * @return
         */
        public Builder setShowMessage(boolean isShow){
            this.isShow = isShow;
            return this;
        }

        /**
         * 设置按返回键是否取消dialog
         * @param isCanCancel
         * @return
         */
        public Builder setCanCancel(boolean isCanCancel){
            this.isCanCancel = isCanCancel;
            return this;
        }

        /**
         * 设置点击屏幕之外是否能取消dialog
         * @param isCanCanelOutSide
         * @return
         */
        public Builder setCanCancelOutSide(boolean isCanCanelOutSide){
            this.isCanCancelOutSide = isCanCanelOutSide;
            return this;
        }

        /**
         * 建造者模式，展示dialog
         * @return
         */
        public LoadingDialog create(){

            LayoutInflater from = LayoutInflater.from(mContext);
            View view = from.inflate(R.layout.loading_dialog, null);

            LoadingDialog loadingPostDialog = new LoadingDialog(mContext, R.style.MyDialogStyle);
            loadingPostDialog.setContentView(view);

            loadingPostDialog.setCancelable(isCanCancel);
            loadingPostDialog.setCanceledOnTouchOutside(isCanCancelOutSide);
            TextView tipTextView = view.findViewById(R.id.tipTextView);
            if (isShow){

                tipTextView.setText(message);
            }else{
                tipTextView.setVisibility(View.GONE);
            }

            return loadingPostDialog;
        }
    }
}
