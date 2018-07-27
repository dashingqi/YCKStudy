package com.example.asynctaskdemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ImageView ivShow;
    private Button btnDownLoadPic;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDownLoadPic = findViewById(R.id.btn_download_pic);
        ivShow = findViewById(R.id.iv_show);

        btnDownLoadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                new MyAsyncTask().execute("http://pic172.nipic.com/file/20180713/25812155_170649928000_2.jpg");
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String, Integer, byte[]> {

        private HttpURLConnection connection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

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
                    Log.d("contentLength= ",contentLength+"");
                    long tottal_length = 0;
                    int len = 0;
                    int progress = 0;

                    while ((len = inputStream.read(bytes)) != -1) {
                        tottal_length += len;
                        Log.d("total_length",tottal_length+"");
                        progress =(int) ((tottal_length /(float) contentLength) * 100);
                        Log.d("progress = ",progress+"");
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
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(byte[] bitmap) {
            super.onPostExecute(bitmap);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            ivShow.setImageBitmap(bitmap1);

            progressDialog.dismiss();
        }
    }

    public void showDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("下载");
        progressDialog.setMessage("正在下载，请稍后！");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
}
