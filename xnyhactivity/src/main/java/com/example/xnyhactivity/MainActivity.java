package com.example.xnyhactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvText = findViewById(R.id.tv_text);
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ViewStub 的加载方式
                //findViewById(R.id.button_view).setVisibility(View.VISIBLE);
                ((ViewStub) findViewById(R.id.button_view)).inflate();
            }
        });
    }
}
