package com.example.permission_demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    private TextView tvCallphone;
    private final int CALL_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCallphone = findViewById(R.id.call_phone);
        tvCallphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.在配置文件中 去声明要申请的权限，去申请没有声明的权限会造成程序崩溃
                //2. 去检查是否申请权限了，申请了去执行业务逻辑，没有申请去申请，在回调中进行权限的再次判断与逻辑处理
                //3. ContextCompat.checkSelfPermission()返回值有两个 PackageManager.PERMISSION_GRANTED(允许了) 和 PackageManager.PERMISSION_DENIED（拒绝了）
                //权限没有申请，就去申请权限去
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CALL_PHONE)) {
                        // 当用户第一次拒绝了，这个分支在下次申请权限的时候，可以弹个dialog来提醒用户。
                        Toast.makeText(MainActivity.this,"申请吧，不要拒绝了！",Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    //权限申请了，就去执行业务逻辑吧
                    callPhone();
                }
            }
        });
    }


    /**
     * 打电话
     */
    public void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "13478493231"));
        startActivity(intent);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //检查请求码，找到了对应申请权限的请求码 再去判断
//        switch (requestCode) {
//            case CALL_PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    callPhone();
//                } else {
//                    Toast.makeText(MainActivity.this, "权限被拒绝了，就不能正常使用该应用了", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    callPhone();
                }else{
                    //用户勾选了不在提示
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CALL_PHONE)){
                        //引导用户去设置界面
                        gotoSetting();
                    }
                }
        }
    }

    /**
     * 去往设置界面
     */
    public void gotoSetting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }



}
