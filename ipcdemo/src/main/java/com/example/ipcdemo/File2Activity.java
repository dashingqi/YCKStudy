package com.example.ipcdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class File2Activity extends AppCompatActivity {

    private Button readData;
    private ObjectInputStream objectInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file2);
        readData = findViewById(R.id.read_data);

        readData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });
    }

    /**
     * 读数据
     */
    private void readData() {

        new Thread(new Runnable() {

            private User mUser;

            @Override
            public void run() {

                File file = new File(Environment.getExternalStorageDirectory(), "file.txt");

                if (!file.exists()) {
                    Toast.makeText(File2Activity.this, "文件不存在", Toast.LENGTH_LONG).show();
                    return;
                }
                try{

                    objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    mUser = (User) objectInputStream.readObject();

                    if (mUser!=null){
                        Log.d("user_name=",mUser.getName());
                        Log.d("user_age = ",mUser.getAge()+"");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                    if (objectInputStream!=null){
                        try {
                            objectInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }).start();

    }
}
