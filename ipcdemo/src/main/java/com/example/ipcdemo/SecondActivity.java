package com.example.ipcdemo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d("sUserId_second= ",String.valueOf(UserManager.sUerId));
        Button btnStartThrid = findViewById(R.id.btn_start_third);

        ParcelUser user = getIntent().getExtras().getParcelable("user");
        Log.d("name",user.getName());
        Log.d("age",user.getAge()+"");
        Log.d("address",user.getAddress());

        btnStartThrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);

            }
        });
    }
}
