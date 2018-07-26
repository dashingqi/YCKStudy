package com.example.photocompress;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private BufferedOutputStream bufferedOutputStream;
    private Bitmap bitmapQuality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView compressQuality = findViewById(R.id.compress_quality);
        compressQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //申请权限
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    } else {
                        //compressQuality();
                        //compressSize();
                        bilinearCompress();
                    }
                } else {
                    // compressQuality();
                    //compressSize();
                    bilinearCompress();
                }
            }
        });
        //compressSize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //compressQuality();
                    //compressSize();
                    bilinearCompress();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //跳转到设置界面
                        gotoSettingActivity();
                    }
                }
                break;
        }
    }

    /**
     * 跳转到设置界面
     */
    private void gotoSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getApplication().getPackageName(), null));
        startActivity(intent);

    }

    /**
     * 尺寸压缩：临近采样
     */
    private void compressSize() {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            //获取到Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timo, options);
            //设置文件存储路径
            String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/timo_option.jpg";
            ImageUtils(savePath, bitmap);
        } catch (OutOfMemoryError e) {
            //此处捕获是个error 不是Exception 如果单纯写成Exception e 那么是捕获不到OutOfMemoryError这个错误
            e.printStackTrace();
        }

    }

    /**
     * 图片的质量压缩
     */
    private void compressQuality() {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "timo_compress.jpg");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取到bitmap
        bitmapQuality = BitmapFactory.decodeResource(getResources(), R.drawable.timo);
        BufferedOutputStream ous = null;
        try {
            ous = new BufferedOutputStream(new FileOutputStream(file));
            //参数1：压缩格式 PNG,JPEG,WEBP,正常情况下都是选用JPEG格式，JPEG是有损压缩，PNG的无损压缩（也就是没有压缩），WEBP是Google
            //自己推出的图片格式，相比JPEG会节约30%，但是从兼容性和整体的压缩大小来看，都是选择JPEG格式的。
            boolean compress = bitmapQuality.compress(Bitmap.CompressFormat.JPEG, 50, ous);
            //Bitmap 引用的建立 是包括 Java层面和C层面的 Java层面可用垃圾回收器进行回收，C层面的，只能调用bitmap.recycle()来进行内存的释放
            // recycle()内部是调用JNI层的代码来进行内存的释放。
            //调用垃圾回收器进行回收，调用这个并不会立刻进行回收，而是加快垃圾回收器的到来，来进行回收
            //recycle()方法调用的时机，是不在使用此bitmap引用的情况下，如果回收了再次使用就会抛出异常，所以在Activity中回收的话就在onStop或者onDestroy()中进行调用

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 双线性采样压缩:是通过一个像素点周围的2x2 个点的值，根据周围点值来计算取对应的权重，计算得到目标图像
     */
    public void bilinearCompress() {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/bili_tomo.jpg";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timo);
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap mBitmap = Bitmap.createBitmap(bitmap, 0, 0, (bitmap.getWidth()), (bitmap.getHeight()), matrix, true);
        ImageUtils(filePath, mBitmap);

    }


    /**
     * 图片工具类
     *
     * @param filePath
     * @param bitmap
     */
    public void ImageUtils(String filePath, Bitmap bitmap) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
            }

            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bufferedOutputStream);
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        if (bitmapQuality != null && !bitmapQuality.isRecycled()) {
            bitmapQuality.recycle();
            bitmapQuality = null;
        }

        System.gc();
    }

//    @TargetApi(19)
//    public void handleImageOnKitKat(Intent data) {
//        String imagePath = null;
//    Uri 统一资源定位符   [scheme:] [//authority(//host:port)] /path   content://com.android.support
//        Uri uri = data.getData();
//
//        if (DocumentsContract.isDocumentUri(MainActivity.this, uri)) {
//            //如果是document类型的Uri，那么通过document的id来处理
//            String documentId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {

//                String id = documentId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(uri,selection);
//            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
//                imagePath = getImagePath(contentUri,null);
//            }
//              equalsIgnoreCase()是不区分大小写，equals()是区分大小写的
//        }else if ("content".equalsIgnoreCase(uri.getScheme())){
//            //如果是content类型的uri那么使用正常的uri
//            imagePath = getImagePath(uri,null);
//        }else if ("file".equalsIgnoreCase(uri.getScheme())){
//            //如果uri是file 那么直接获取文件的路径
//           imagePath = uri.getPath();
//        }
//
//        displayImage(imagePath);
//
//    }
}
