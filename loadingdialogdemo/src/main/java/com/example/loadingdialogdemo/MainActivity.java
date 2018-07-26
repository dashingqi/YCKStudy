package com.example.loadingdialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button show_dialog = findViewById(R.id.show_dialog);
        Button diss_dialog = findViewById(R.id.diss_dialog);

        show_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DialogPostUtils.showDialog(MainActivity.this,"提交数据中...",true);
            }
        });

    }

}
