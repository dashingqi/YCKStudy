package com.example.dashingqi.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {

    public final String TAG = BookProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.example.dashingqi.contentproviderdemo.BookProvider";

    public static final Uri TABLE_BOOK_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri TABLE_USER_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private String tableName = null;

    static {
        uriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        //做些初始化的操作  该方法是运行在UI线程中是不能做耗时操作的。
        Log.d(TAG, "onCreate thread name = " + Thread.currentThread().getName());
        initProviderData();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        //获取到当前运行的线程名
        Log.d(TAG, "query thread name = " + Thread.currentThread().getName());
        Log.d(TAG + "Uri = ", uri.getScheme() + uri.getAuthority() + uri.getPath());
        tableName = getTableName(uri);
        if (tableName != null) {
            return db.query(tableName, strings, null, null, null, null, null);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType thread name = " + Thread.currentThread().getName());
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG, "insert thread name = " + Thread.currentThread().getName());
        tableName = getTableName(uri);
        Log.d(TAG, "table_name=" + tableName);
        db.insert(tableName, "", contentValues);

        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "delete thread name = " + Thread.currentThread().getName());
        tableName = getTableName(uri);
        int delete = db.delete(tableName, s, strings);
        if (delete > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return delete;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update thread name = " + Thread.currentThread().getName());
        tableName = getTableName(uri);
        Log.d("tableName", tableName);
        int update = db.update(tableName, contentValues, s, strings);
        if (update > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return update;

    }

    public String getTableName(Uri uri) {

        switch (uriMatcher.match(uri)) {

            case USER_URI_CODE:
                //要访问User表
                tableName = MyDbOPenHelper.USER_TABLE_NAME;
                break;
            case BOOK_URI_CODE:
                //要访问Book表
                tableName = MyDbOPenHelper.BOOK_TABLE_NAME;
                break;
        }

        return tableName;
    }

    public void initProviderData() {
        db = new MyDbOPenHelper(getContext()).getWritableDatabase();


        //在子线程中进行数据库的操作
        db.execSQL("delete from " + MyDbOPenHelper.USER_TABLE_NAME);
        db.execSQL("delete from " + MyDbOPenHelper.BOOK_TABLE_NAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.execSQL("insert into book values(9,'android');");
                db.execSQL("insert into book values(10,'java');");
                db.execSQL("insert into book values(11,'ios');");

                db.execSQL("insert into user values(1,'zhangqi',1);");
                db.execSQL("insert into user values(2,'yhn',0);");
            }
        }).start();


    }
}
