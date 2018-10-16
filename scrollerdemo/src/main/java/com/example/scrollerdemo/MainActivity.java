package com.example.scrollerdemo;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取到TextView
        final TextView tv = findViewById(R.id.tv);
        Button start = findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
                layoutParams.width += 100;
                layoutParams.leftMargin = 100;

                tv.setLayoutParams(layoutParams);

                //ObjectAnimator.ofFloat(tv,"translationX",0,100).setDuration(10000).start();

            }
        });


    }


}
