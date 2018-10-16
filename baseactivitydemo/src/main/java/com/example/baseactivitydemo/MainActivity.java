package com.example.baseactivitydemo;

import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected YCKActionBarConfig getActionBarConfig() {
        YCKActionBarConfig bar = new YCKActionBarConfig();
        bar.init(ActionBarStyle.ACTION_BAR_STYLE_TEXT_RIGHT,getString(R.string.action_bar_title_own_vehicle));
        bar.setRightText(getString(R.string.action_bar_title_default));
        bar.setRightListener(new YCKActionBarRightListener() {
           @Override
           public void onClick() {
               Toast.makeText(MainActivity.this,getString(R.string.actionbar_right_text_toast),Toast.LENGTH_LONG).show();
           }
       });
        return bar;
    }
}
