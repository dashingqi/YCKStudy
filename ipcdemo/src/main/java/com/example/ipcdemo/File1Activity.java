package com.example.ipcdemo;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class File1Activity extends AppCompatActivity {

    private Button writeData;
    private ObjectOutputStream objectOutputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file1);
        writeData = findViewById(R.id.write_data);

        writeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData();
            }
        });

    }

    /**
     * 写数据
     */
    private void writeData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                File file = new File(Environment.getExternalStorageDirectory(), "file.txt");
                if (!file.exists()) {
                    // mkdir()表示在指定目录下创建目录
                    // mkdirs() 表示创建所有指定的目录
                    //createNewFile()创建文件
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                User user = new User("zhangqi", 18);


                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(user);

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (objectOutputStream!=null){
                        try {
                            objectOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            gotoActivity();
                        }
                    }
                }

            }
        }).start();

    }


    public void gotoActivity(){
        Intent intent = new Intent();
        intent.setClass(File1Activity.this,File2Activity.class);
        startActivity(intent);
    }
}
