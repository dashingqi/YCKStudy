package com.example.imageloaderdemo;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

public class ImageResizer {

    public static final String TAG = "IamgeResizer";

    public ImageResizer() {

    }

    /**
     * 从资源中去加载图片
     * @param res
     * @param resId
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqHeight, int reqWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        return bitmap;
    }


    public static Bitmap decodeSampleBitmapFromFileDecsriptor(FileDescriptor fd,int reqHeight,int reqWidth){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqHeight,reqWidth);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);

        return bitmap;
    }


    /**
     * 计算采样率
     *
     * @param options
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {

        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }

        //获取到照片原始的大小
        int height = options.outHeight;
        int width = options.outWidth;

        Log.d(TAG, "Height = " + height + ",width = " + width);

        int sampleSize = 1;


        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth = width / 2;
            Log.d(TAG, "halfHeight = " + halfHeight + ",halfWidth = " + halfWidth);

            while ((halfHeight / sampleSize) >= reqHeight && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }
}
