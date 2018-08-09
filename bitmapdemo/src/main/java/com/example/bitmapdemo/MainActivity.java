package com.example.bitmapdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private LruCache<String, Bitmap> mLruCatch;
    private final long DISK_LRU_CATCH = 1024*1024*50;//50mb
    private DiskLruCache mDiskLruCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //高效的加载Bitmap
        decodeSampleBitmapFromResource(getResources(), R.mipmap.ic_launcher, 480, 800);
    }

    /**
     * 利用采样率
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // 创建BitmapFactory.Options 对象
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只会解析图片的原始宽高信息，并不会去加载图片的
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, resId, options);

        //去计算采样率
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //从true设置为false
        options.inJustDecodeBounds = false;

        //计算完采样率，就去重新加载图片  通常采样率都会采用2的倍数
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);


        return bitmap;
    }


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        //获取到当前图片的实际高度
        int height = options.outHeight;

        //获取到当前图片的实际宽度
        int width = options.outWidth;

        int inSampleSize = 1;

        //将图片的实际宽度和高度，与需要的宽度和高度进行比较
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }


        return inSampleSize;
    }


    /**
     * lruCatch 是用作 去内存中的缓存  是Android3.1加入的，为了兼容3.1 之前的版本，需要用到v4包中的LruCache
     */
    public void useLruCache() {

        //获取到当前进程最大的内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //设置当前缓存为最大的 1/8
        int catchMemory = maxMemory / 8;

        //计算缓存对象的大小,大小是KB(除以了1024)
        mLruCatch = new LruCache<String, Bitmap>(catchMemory) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                //计算缓存对象的大小 大小是KB(除以了1024)
                return value.getRowBytes() * value.getHeight() / 1024;
            }

            /**
             * 移除旧缓存对象调用的方法
             * @param evicted
             * @param key
             * @param oldValue
             * @param newValue
             */
            @Override
            protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };

        mLruCatch.get("");//获取到缓存中的对象
        //mLruCatch.put("","bitmap");//将对象存储到内存中
        mLruCatch.remove("");//删除缓存中的对象；
    }

    public void useDiskLruCache(){


        /**
         * args1:文件存储路径
         * args2:app版本号，一般都是1；如果当前填写的是app的版本号，当应用更新的话，那么就会删除上个版本的缓存了。
         * args3:表示的是单个节点大小，一般都是为1的；
         * args4:表示的是缓存的最大容量
         */

        File file = new File(Environment.getExternalStorageDirectory(), "bitmap");
        try {
            //获取到DiskLruCache引用。
            mDiskLruCache = DiskLruCache.open(file, 1, 1, DISK_LRU_CATCH);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
