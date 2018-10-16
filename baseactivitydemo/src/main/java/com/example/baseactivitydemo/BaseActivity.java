package com.example.baseactivitydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class BaseActivity extends AppCompatActivity {

    private View mActionBarView;
    private ImageView backBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR | Window.FEATURE_NO_TITLE);
        configActionBar();
    }


    /**
     * 配置的几个主题样式
     */
    public enum ActionBarStyle {
        ACTION_BAR_STYLE_DEFAULT,
        ACTION_BAR_STYLE_LEFT,
        ACTION_BAR_STYLE_RIGHT,
        ACTION_BAR_STYLE_LEFT_RIGHT,
        ACTION_BAR_STYLE_CUS_RIGHT,
        ACTION_BAR_STYLE_TEXT_RIGHT,
        ACTION_BAR_STYLE_MAX
    }

    //子类需要重写这个方法，提供YKCActionBarConfig实例
    protected YCKActionBarConfig getActionBarConfig() {
        YCKActionBarConfig bar = new YCKActionBarConfig();
        bar.init(ActionBarStyle.ACTION_BAR_STYLE_DEFAULT, getString(R.string.action_bar_title_default));
        return bar;
    }


    public void configActionBar() {

        YCKActionBarConfig config = getActionBarConfig();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        //加载布局
        if (config.getActionBarStyle() == ActionBarStyle.ACTION_BAR_STYLE_DEFAULT) {
            mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_common_default_layout, null);
        } else if (config.getActionBarStyle() == ActionBarStyle.ACTION_BAR_STYLE_LEFT_RIGHT) {
            mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_common_left_right_layout, null);
        } else if (config.getActionBarStyle() == ActionBarStyle.ACTION_BAR_STYLE_TEXT_RIGHT) {
            mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_common_right_text_layout, null);
        }

        ActionBar actionBar = getSupportActionBar();


        //基本的ActionBar配置
        if (actionBar != null && mActionBarView != null) {
            //设置自定义视图
            actionBar.setCustomView(mActionBarView, lp);
            //展示自定义视图
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //是否显示自定义视图
            actionBar.setDisplayShowCustomEnabled(true);
            //左上角图标是否显示（系统默认自带的）
            actionBar.setDisplayShowHomeEnabled(false);
            // 显示标题 （系统）
            actionBar.setDisplayShowTitleEnabled(false);
            //标题的配置
            initTitleTv(mActionBarView, config);
            //返回按钮的点击事件
            initButtonAction(mActionBarView);

            //根据配置项 去初始化各个点击事件
            if (config.getActionBarStyle() == ActionBarStyle.ACTION_BAR_STYLE_DEFAULT) {

            } else if (config.getActionBarStyle() == ActionBarStyle.ACTION_BAR_STYLE_LEFT_RIGHT) {

                if (config.rightResId == 0 || config.rightListener == null) {
                    throw new RuntimeException("need rightResId and rightListener");
                } else {
                    initRightImageButton(mActionBarView, config.getRightResId(), config.getRightListener());

                }
            }else if (config.getActionBarStyle() == ActionBarStyle.ACTION_BAR_STYLE_TEXT_RIGHT){
                initRightTextButton(mActionBarView,config.getRightText(),config.getRightListener());

            }

        }
    }

    private void initRightTextButton(View mActionBarView, String rightText, final YCKActionBarRightListener rightListener) {
        TextView tvCommonRight = mActionBarView.findViewById(R.id.tv_common_right);
        tvCommonRight.setText(rightText);
        tvCommonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightListener.onClick();
            }
        });
    }

    /**
     * right按钮的点击事件
     *
     * @param parentView
     * @param rightResId
     * @param rightListener
     */
    private void initRightImageButton(View parentView, int rightResId, final YCKActionBarRightListener rightListener) {
        ImageView rightImageButton = parentView.findViewById(R.id.iv_common_right);
        rightImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightListener.onClick();
            }
        });

        rightImageButton.setImageResource(rightResId);


    }

    /**
     * 初始化返回按钮的事件
     *
     * @param parentView
     */
    private void initButtonAction(View parentView) {
        backBtn = parentView.findViewById(R.id.iv_common_left);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //销毁该Activity
                finish();

            }
        });

    }

    /**
     * 初始化标题栏
     *
     * @param mActionBarView
     * @param config
     */
    private void initTitleTv(View mActionBarView, YCKActionBarConfig config) {
        TextView tvTitle = mActionBarView.findViewById(R.id.common_title);
        tvTitle.setText(config.getTitle());
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

        public void init(ActionBarStyle style, String title) {
            this.style = style;
            this.title = title;
        }

        public void configLeftItem(int id, YCKActionBarLeftListener listener) {
            leftResId = id;
            leftListener = listener;
        }

        public void configRightItem(int id, YCKActionBarRightListener listener) {
            rightResId = id;
            rightListener = listener;
        }

        public String getTitle() {
            return title;
        }

        public int getLeftResId() {
            return leftResId;
        }

        public int getRightResId() {
            return rightResId;
        }

        public YCKActionBarLeftListener getLeftListener() {
            return leftListener;
        }

        public YCKActionBarRightListener getRightListener() {
            return rightListener;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ActionBarStyle getActionBarStyle() {
            return style;
        }

        public void setStyle(ActionBarStyle style) {
            this.style = style;
        }

        public void setLeftResId(int leftResId) {
            this.leftResId = leftResId;
        }

        public void setRightResId(int rightResId) {
            this.rightResId = rightResId;
        }

        public String getRightText() {
            return rightText;
        }

        public void setRightText(String rightText) {
            this.rightText = rightText;
        }

        public View getRightView() {
            return rightView;
        }

        public void setRightView(View rightView) {
            this.rightView = rightView;
        }

        public void setLeftListener(YCKActionBarLeftListener leftListener) {
            this.leftListener = leftListener;
        }

        public void setRightListener(YCKActionBarRightListener rightListener) {
            this.rightListener = rightListener;
        }
    }

    /**
     * 左控件点击事件的接口
     */
    public interface YCKActionBarLeftListener {
        void onClick();
    }

    /**
     * 右控件点击事件的接口
     */
    public interface YCKActionBarRightListener {
        void onClick();
    }


}
