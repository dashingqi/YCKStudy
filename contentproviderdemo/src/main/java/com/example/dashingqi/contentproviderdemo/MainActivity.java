package com.example.dashingqi.contentproviderdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = Uri.parse("content://com.example.dashingqi.contentproviderdemo.BookProvider");
//        //调用了三次 这三次都是运行在不同Binder线程中。
//        getContentResolver().query(uri,null,null,null,null);
//        getContentResolver().query(uri,null,null,null,null);
//        getContentResolver().query(uri,null,null,null,null);

        Uri bookUri = Uri.parse("content://com.example.dashingqi.contentproviderdemo.BookProvider/book");

        ContentValues contentValues = new ContentValues();
        contentValues.put("id",24);
        contentValues.put("name","C++");
        getContentResolver().insert(bookUri,contentValues);

        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"id", "name"}, null, null, null);

        while (bookCursor.moveToNext()){
            int anInt = bookCursor.getInt(0);
            String string = bookCursor.getString(1);

            Log.d(TAG,"id = "+anInt+", name = "+string);

        }

        bookCursor.close();

    }
}
