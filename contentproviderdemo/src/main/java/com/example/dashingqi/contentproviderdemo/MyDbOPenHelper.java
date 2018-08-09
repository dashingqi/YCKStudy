package com.example.dashingqi.contentproviderdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbOPenHelper extends SQLiteOpenHelper {

    public static final String DB_Name= "book_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    public static final int VERSION = 1;

    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS "+BOOK_TABLE_NAME+"(id INT PRIMARY KEY,name TEXT)";
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "+USER_TABLE_NAME+"(id INT PRIMARY KEY,name TEXT,sex INT)";




    public MyDbOPenHelper(Context context) {
        super(context, DB_Name, null, VERSION);
        //创建数据库， 设置等级
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //做些初始化的操作  创建表的操作 只会运行一次
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //升级数据库的时候，进行调用
    }
}
