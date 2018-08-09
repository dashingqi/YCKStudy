package com.example.imageloaderdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;

public class ImageLoader {


    private  LruCache<String, Bitmap> mLruCache;
    private final long DISK_CACHE_MEMORY = 1024 * 1024 * 50;
    private  DiskLruCache mDiskLruCache;
    private boolean mIsDiskLruCacheCreate = false;


    public ImageLoader(Context mContext) {
        //初始化，LruCache 和 DiskLruCache (内存缓存和磁盘缓存)

        //获取到该进程获得最大内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //为内存缓存 分配大小
        int cacheMemory = maxMemory / 8;

        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //获取到内存缓存对象的大小
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }


        //判断存储缓存的地方是否够用
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_MEMORY) {

            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_MEMORY);

                mIsDiskLruCacheCreate = true;


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * 获取到磁盘缓存的路径
     *
     * @param context
     * @param name
     * @return
     */
    private File getDiskCacheDir(Context context, String name) {

        boolean equals = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (equals) {
            cachePath = context.getExternalCacheDir().getPath();

        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + name);
    }

    /**
     * 获取到磁盘缓存目录的大小 和指定的磁盘缓存大小进行比较
     *
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            return path.getUsableSpace();

        StatFs statFs = new StatFs(path.getPath());
        return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();

    }
}
