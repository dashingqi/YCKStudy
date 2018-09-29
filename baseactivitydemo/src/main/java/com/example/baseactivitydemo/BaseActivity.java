package com.example.baseactivitydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR | Window.FEATURE_NO_TITLE);

        configActionBar();
    }

    private void configActionBar() {
        getActionBarConfig();
    }

    protected void getActionBarConfig() {
        YCKActionBarConfig bar = new YCKActionBarConfig();



    }

    public enum ActionBarStyle{
        ACTION_BAR_STYLE_DEFAULT,
        ACTION_BAR_STYLE_LEFT,
        ACTION_BAR_STYLE_RIGHT,
        ACTION_BAR_STYLE_LEFT_RIGHT,
        ACTION_BAR_STYLE_CUS_RIGHT,
        ACTION_BAR_STYLE_TEXT_RIGHT,
        ACTION_BAR_STYLE_MAX
    }

    public class YCKActionBarConfig {

        private ActionBarStyle style;
        //标题
        private String title;
        //左控件id
        private int leftResId;
        //右控件id
        private int rightResId;
        //右控件描述文本
        private String rightText;
        //有控件实体view
        private View rightView;

        private YCKActionBarLeftListener leftListener;
        private YCKActionBarRightListener rightListener;

        public void init(ActionBarStyle style,String title){
            this.style = style;
            this.title = title;
        }

        public void configLeftItem(int id,YCKActionBarLeftListener listener){
            leftResId = id;
            leftListener = listener;
        }

        public void configRightItem(int id,YCKActionBarRightListener listener){
            rightResId = id;
            rightListener = listener;
        }

    }

    /**
     * 左控件点击事件的接口
     */
    public interface YCKActionBarLeftListener{
        void onClick();
    }

    /**
     * 右控件点击事件的接口
     */
    public interface YCKActionBarRightListener{
        void onClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
