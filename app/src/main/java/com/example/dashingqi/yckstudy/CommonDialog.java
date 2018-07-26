package com.example.dashingqi.yckstudy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by dashingqi on 23/4/18.
 */

public class CommonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private String content;
    private String title;
    private String positiveName;
    private String negativeName;
    private TextView cancelTv;
    private TextView submitTv;
    private CancelListener listener;
    private int themeResId;
    private TextView contentTv;
    private TextView tv_title;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    public CommonDialog(Context context, String content) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.content = content;
    }

    protected CommonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog(Context context, String content, int themeResId, CancelListener listener) {
        super(context, themeResId);
        this.content = content;
        this.mContext = context;
        this.listener = listener;
        this.themeResId = themeResId;
    }


    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }



    public CommonDialog setPositiveButton(String name) {

        this.positiveName = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        contentTv = findViewById(R.id.content);
        cancelTv = findViewById(R.id.cancel);
        tv_title = findViewById(R.id.title);
        submitTv = findViewById(R.id.submit);
        cancelTv.setOnClickListener(this);
        submitTv.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }

        if (!TextUtils.isEmpty(positiveName)) {
            cancelTv.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTv.setText(negativeName);
        }

        if (!TextUtils.isEmpty(content)){
            contentTv.setText(content);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cancel:
                if (listener != null) {
                    listener.onClicks(this, false);
                }
                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClicks(this, true);
                }
                break;

            default:

                break;
        }

    }

    public interface CancelListener {

       abstract void onClicks(Dialog dialog, boolean confirm);
    }
}
