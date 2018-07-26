package com.example.singletopactivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startSecond = findViewById(R.id.btn_start_second);
        startSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.dashingqi.secondactivity");
                intent.setClass(MainActivity.this, SecondMainActivity.class);
                //隐式意图跳转前的匹配检查，有符合条件的就进行跳转
                //此方法是返回全部符合匹配的 Activity信息
//                List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo resolveInfo : resolveInfos) {
//                    String name = resolveInfo.activityInfo.name;
//                    Log.d(TAG,name);
//                }
                //返回一个最符合此匹配的活动的信息
//                ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                String name = resolveInfo.activityInfo.name;
//                Log.d(TAG,name);

                ComponentName componentName = intent.resolveActivity(getPackageManager());
                if (componentName == null) {
                    Log.d(TAG, "componentname");
                    return;
                }


                String className = componentName.getClassName();
                Log.d(TAG+"com", className);

//                if (resolveInfos.size() > 0) {
                startActivity(intent);
//                }
            }
        });
    }
}
