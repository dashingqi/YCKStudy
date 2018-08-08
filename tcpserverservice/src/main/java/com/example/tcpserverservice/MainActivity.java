package com.example.tcpserverservice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tvMessageCOntainer;

    private static final int MESSAGE_PECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;
    private Socket mSocket;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_PECEIVE_NEW_MSG:
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    break;
            }
        }
    };
    private PrintWriter out;
    private BufferedReader in;
    private EditText et_msg;
    private Button btnSendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //展示信息
        tvMessageCOntainer = findViewById(R.id.tvMessageContainer);
        //写信息
        et_msg = findViewById(R.id.et_msg);
        //发送消息
        btnSendMsg = findViewById(R.id.btn_send_msg);


        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                connectService();
            }
        }).start();
    }

    /**
     * 连接服务端的，给服务端发消息
     */
    private void connectService() {

        Socket socket = null;

        while (socket == null) {
            try {

                socket = new Socket("localhost", 8808);
                mSocket = socket;

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //异步消息机制，通过消息，来去更新UI
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);


            } catch (Exception e) {
                SystemClock.sleep(1000);
                e.printStackTrace();

            }
        }

        try{
            //接受服务端消息
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(!this.isFinishing()){
                String msg = in.readLine();
                if (msg!= null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy:MM:dd HH:mm::ss");
                    String date = simpleDateFormat.format(System.currentTimeMillis());
                    String finalMsg = "server"+date+":"+msg;
                    mHandler.obtainMessage(MESSAGE_PECEIVE_NEW_MSG,finalMsg).sendToTarget();
                }
            }

            Log.d("MainActivity:","quit...");
            in.close();
            out.close();
            socket.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
