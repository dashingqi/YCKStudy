package com.example.textevaluatordemo;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_eng;
    private ValueAnimator valueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button start_anim = findViewById(R.id.start_anim);
        Button stop_anim = findViewById(R.id.stop_anim);
        start_anim.setOnClickListener(this);
        stop_anim.setOnClickListener(this);
        tv_eng = findViewById(R.id.btn_view_set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_anim:
                //开启动画
                startAnim();
                break;
            case R.id.stop_anim:
                //停止动画
                stopAnim();
                break;
        }
    }

    private void stopAnim() {
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }


    }

    private void startAnim() {
        valueAnimator = ValueAnimator.ofObject(new MyEvaluator(), new Character('A'), new Character('Z'));
        valueAnimator.setDuration(40000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final Character animatedValue = (Character) animation.getAnimatedValue();
                
                        tv_eng.setText(String.valueOf(animatedValue));

            }
        });

        valueAnimator.start();


    }
}
