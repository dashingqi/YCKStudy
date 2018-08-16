package com.example.handlerthreaddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private ImageView mImageView;
    private HandlerThread mHandlerThread;

    private String[] urls = {"http://img.88tph.com/production/20170815/88tph-12342180-1.jpg",
                             "https://goss.veer.com/creative/vcg/veer/612/veer-300886036.jpg"
                            };


    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            Log.d(TAG, "次数=" + msg.what);
            ImageResult result = (ImageResult) msg.obj;
            Log.d(TAG, "url=" + result.url);
            mImageView.setImageBitmap(result.bitmap);
            Log.d(TAG,Thread.currentThread().getName()+" Main");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.image);

        // 创建一个HandlerThread的对象，该线程的名字是孙腾的缩写
        // HandlerThread 是继承至 Thread 重写了run()方法，在run()方法中 进行了Looper.prepare();
        //子线程中 是 没有Looper对象的，主线程是有的。
        mHandlerThread = new HandlerThread("ST");

        //开启线程   创建了一个Looper.prepare();
        // Looper.loop();循环去取MessageQueue队列中的message
        mHandlerThread.start();

        // 子线程 handler
        Handler childHandler = new Handler(mHandlerThread.getLooper(), new ChildCallback());

        for (int i = 0; i <= 1; i++) {
            childHandler.sendEmptyMessageDelayed(i,  1000);
        }


    }


    class ChildCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {

            int what = msg.what;

            //从网络中下载图片，转化成bitmap
            Bitmap bitmap = downloadBitmapFromUrl(urls[what]);

            if (bitmap != null) {

                ImageResult imageResult = new ImageResult();
                imageResult.bitmap = bitmap;
                imageResult.url = urls[what];


                Message message = Message.obtain();
                message.what = msg.what;
                message.obj = imageResult;

                Log.d(TAG,Thread.currentThread().getName()+" child");

                mUIHandler.sendMessage(message);

            }


            return false;
        }
    }


    /**
     * 从网络中下载图片
     *
     * @param url
     * @return
     */
    public Bitmap downloadBitmapFromUrl(String url) {

        Bitmap bitmap = null;

        try {

            URL mUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    class ImageResult {

        private Bitmap bitmap;
        private String url;

    }
}
