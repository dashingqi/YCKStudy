package com.example.getandsetmethod;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnViewSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnViewSet = findViewById(R.id.btn_view_set);
        btnViewSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==btnViewSet)
                    performAnimate();

            }
        });
    }

    public void performAnimate(){
        ViewWrapper viewWrapper = new ViewWrapper(btnViewSet);
        ObjectAnimator.ofInt(viewWrapper,"width",500).setDuration(500).start();
    }


    public static  class ViewWrapper{
        private View mTarget;

        public ViewWrapper(View target){
            this.mTarget = target;
        }

        public int getWidth(){
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width){
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}
