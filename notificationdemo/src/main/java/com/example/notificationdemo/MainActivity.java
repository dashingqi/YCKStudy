package com.example.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView sendNotification = findViewById(R.id.send_notification);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //折叠式通知
                showFoldNotification();
                //悬挂式通知
                showScreenNotification();

            }
        });
    }

    /**
     * 悬挂式通知栏
     */
    private void showScreenNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.csdn.net/dashingqi"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("悬挂式通知");
        //设置点击跳转
        Intent hangIntent = new Intent();
        hangIntent.setClass(this,NotificationActivity.class);
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent hangPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //设置悬浮通知
        builder.setFullScreenIntent(hangPendingIntent,true);

        Notification notification = builder.build();
        notificationManager.notify(2,notification);

    }

    /**
     * 展示折叠的通知  使用RemoveViews ：因为创建视图的进程和展示视图的进程不在一个进程里面
     */
    private void showFoldNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remoteview);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://blog.csdn.net/dashingqi"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        builder.setContentTitle("折叠式通知");
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        //指定展开时的视图  bigContentView 只有在api16 以上才好用。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            notification.bigContentView = remoteViews;
        notificationManager.notify(1, notification);
    }


}
