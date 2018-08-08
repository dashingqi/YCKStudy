package com.example.tcpserverservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {

    public boolean mIsServiceDestroy = false;

    public String[] mDefinedMessagers = new String[]{
            "你好呀，哈哈！",
            "请问你叫什么名字",
            "今天北京天气不错呀，sky",
            "你知道吗？我可以和多个人进行聊天",
            "给你叫个笑话吧，爱笑的人运气都不会差的"
    };


    public TCPServerService() {
    }

    @Override
    public void onCreate() {
        new Thread(new TCPService()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroy = true;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class TCPService implements Runnable {


        @Override
        public void run() {
            ServerSocket serverSocket;

            try {
                //监控本地8808端口
                serverSocket = new ServerSocket(8808);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            //接受客户端的请求
            while (!mIsServiceDestroy) {
                try {
                    final Socket accept = serverSocket.accept();
                    Log.d("TCPServerService", "accept");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseClient(accept);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        /**
         * 用户接受客户端的消息
         *
         * @param client
         */
        public void responseClient(Socket client) throws Exception {
            //用于接受客户端的消息
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //用于发送消息到客户端

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);

            out.print("欢迎来到聊天室");

            while (!mIsServiceDestroy) {
                String str = in.readLine();
                Log.d("TCPServerService", str);

                if (str == null) {
                    Log.d("TCPServerService", "客户端断开连接");
                    return;
                }

                int i = new Random().nextInt(mDefinedMessagers.length);
                String message = mDefinedMessagers[i];
                out.print(message);
                Log.d("TCPServerService", message);
            }

            Log.d("TCPServerService", "client disconn");


            //关闭流
            in.close();
            out.close();
            client.close();
        }
    }


}
