package com.example.mediademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private Button takePhoto;
    private ImageView ivImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        杀死当前进程
        Process.killProcess(Process.myPid());
        initView();
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferCamera();
                studyIntent();

            }
        });
        int integer = savedInstanceState.getInt("integer", -1);
    }

    /**
     * 在活动回收之前 都会调用这个方法在这里进行数据的保存 在onCreate()中进行数据的获取
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (outState!=null){
         outState.putInt("intrger",7);

        }
    }

    /**
     * 调用摄像头 拍照片  要想拍照片 就得调用摄像头，调用完摄像头，拍完的照片显示出来，
     */
    private void transferCamera() {

        //应用关联缓存目录：getExternalCacheDir() 专门用来存放当前应用缓存数据的 6.0以上读写SD卡列为危险操作，需要权限的申请
        //使用这个目录可以跳过这个步骤  /sdcard/Android/data/<package name>/cache
        File file = new File(getExternalCacheDir(), "output.jpg");
        if (file.exists()) {
            file.delete();
        }
        //7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            7.0 以后通过此方法 把file 转化成封装后的uri对象 。7.0以后认为 直接使用本地真实路径的uri是不安全的，会抛出FIleUriExposedException异常
//            而FileProvider 是特殊的内容提供者，可以有选择性的封装Uri提供给外部，需要在配置文件中进行注册
            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.dashingqi.mediatest.fileprovider", file);
//            该方法中的 第二个参数 要和注册文件中  android:authorities=""；对应的值要一致。
        } else {

            //将 file直接转化成Uri对象，这个对象标识着output.jpg 本地的真实路径
            imageUri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivImage.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private void initView() {
        ivImage = findViewById(R.id.iv_image);
        takePhoto = findViewById(R.id.take_photo);
    }


    public void studyIntent() {
        //显示Intent跳转
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);

        //隐式Intent跳转
        Intent intent1 = new Intent();
        //设置的Action要与跳转的Activity配置文件中的匹配  因为跳转的Activity设置的Category 是Default
        //在 调用startActivity的时候会把这个category自动添加到intent中 intent1.addCategory("");
        intent1.setAction("com.dashingqi.studyIntent.START_INTENT");
        startActivity(intent1);
    }

    public void callPhone(){
        Intent intent = new Intent();
        //浏览网页
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.baidu.com"));
        //拨打电话号码界面
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }
}
