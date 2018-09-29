package com.example.asynctaskdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView ivShow;
    private Button btnDownLoadPic;
    private ProgressBar mProgressBar;
    private TextView tvShowProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDownLoadPic = findViewById(R.id.btn_download_pic);
        ivShow = findViewById(R.id.iv_show);
        mProgressBar = findViewById(R.id.pb);
        tvShowProgress = findViewById(R.id.tv_show_progress);

        mProgressBar.setProgress(0);

        btnDownLoadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask().execute("http://pic172.nipic.com/file/20180713/25812155_170649928000_2.jpg");
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String, Integer, byte[]> {

        private HttpURLConnection connection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setProgress(0);
        }


        //运行在线程池当中的。
        @Override
        protected byte[] doInBackground(String... strings) {
            byte[] bytes = new byte[1024];
            byte[] image = new byte[]{};
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(500);
                int responseCode = connection.getResponseCode();

                Log.d("responseCode = ", responseCode + "");
                if (responseCode == 200) {
                    InputStream inputStream = connection.getInputStream();
                    long contentLength = connection.getContentLength();
                    Log.d("contentLength= ", contentLength + "");
                    long total_length = 0;
                    int len;
                    int progress;

                    while ((len = inputStream.read(bytes)) != -1) {
                        total_length += len;
                        Log.d("total_length", total_length + "");
                        progress = (int) ((total_length / (float) contentLength) * 100);
                        Log.d("progress = ", progress + "");
                        publishProgress(progress);
                        byteArrayOutputStream.write(bytes, 0, len);
                    }

                    image = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return image;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            tvShowProgress.setText(values[0] + "%");
        }

        @Override
        protected void onPostExecute(byte[] bitmap) {
            super.onPostExecute(bitmap);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            ivShow.setImageBitmap(bitmap1);
        }
    }
}
