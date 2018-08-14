package com.example.imageloaderdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ImageLoader 的意义所在是高效的加载图片以及减少用户流量的消耗，先从内存中加载缓存，内存中没有，再从磁盘中加载，磁盘没有再从
 * 网络中加载。
 */
public class ImageLoader {

    private final String TAG = "ImageLoader";
    private LruCache<String, Bitmap> mLruCache;
    private static final int DISK_CACHE_MEMORY = 1024 * 1024 * 50;
    private DiskLruCache mDiskLruCache;
    private boolean mIsDiskLruCacheCreate = false;
    private HttpURLConnection mHttpUrlConnection;
    private BufferedInputStream bufferedInputStream;
    private static final int IO_BUFFER_SIZE = 1024 * 8;
    private BufferedOutputStream bufferedOutputStream;
    private HttpURLConnection urlConnection;
    private static final int DISK_CATCH_INDEX = 0;
    private Bitmap bitmap;
    private static final int TAG_KEY_URI = 1;
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;


    private static final ThreadFactory sThreadFactory = new ThreadFactory() {


        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r,"ImageLoader#"+mCount.getAndIncrement());
        }
    };


    public static  final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
           CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),sThreadFactory
            );



    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            LoaderResult loaderResult = (LoaderResult) msg.obj;
            ImageView imageView = loaderResult.imageView;
            imageView.setImageBitmap(loaderResult.bitmap);
            String uri = (String)imageView.getTag(TAG_KEY_URI);

            if (uri.equals(loaderResult.uri))
                imageView.setImageBitmap(loaderResult.bitmap);
            else
                Log.d(TAG,"set image bitmap,but url has changed,ignored!");

        }
    };



    public ImageLoader(Context mContext) {
        //初始化，LruCache 和 DiskLruCache (内存缓存和磁盘缓存)

        //获取到该进程获得最大内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //为内存缓存 分配大小
        int cacheMemory = maxMemory / 8;

        //内存缓存
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //获取到内存缓存对象的大小  bitmap.getRowBytes()   API:1   bitmap.getRawCount()  API:12
                //为了兼容性的原因，使用API版本1的。
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
                //磁盘缓存
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_MEMORY);
                mIsDiskLruCacheCreate = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }


    /**
     * 获取到磁盘缓存的路径
     *
     * @param context
     * @param name
     * @return
     */
    private File getDiskCacheDir(Context context, String name) {

        //获取到当前手机的SD卡是不是正常挂载。
        boolean equals = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //获取到当前SD卡是否可移动
        boolean externalStorageRemovable = Environment.isExternalStorageRemovable();
        String cachePath;
        if (equals || !externalStorageRemovable) {
            //  SDCard/Android/data/<application package>/cache/目录  放些临时的缓存数据。
            cachePath = context.getExternalCacheDir().getPath();

        } else {
            // /data/data/<application package>/cache
            cachePath = context.getCacheDir().getPath();
        }
        // File.separator 在不同系统中目录分隔符都不一样  win:"\",linux:"/",这样会报跨平台错误
        return new File(cachePath + File.separator + name);
    }

    /**
     * 获取到磁盘缓存目录的大小 和指定的磁盘缓存大小进行比较
     *
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        // android 2.3 以后 直接调用 file.getUsableSpace() 来获取当前可用空间
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            return path.getUsableSpace();
        //Android 2.3 以前 计算获取
        StatFs statFs = new StatFs(path.getPath());
        return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();

    }


    /**
     * 内存缓存添加： 判断 key对应的value是否存在，存在的话就不添加。
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    /**
     * 内存缓存读取 ： 根据key 来获取内存缓存中存储的Bitmap
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    private Bitmap loadBitmapFromMemCache(String uri) {

        String key = hashKeyFromUrl(uri);

        return getBitmapFromMemCache(key);
    }


    /**
     * 从网络中获取Bitmap
     *
     * @param url       图片链接地址
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    private Bitmap loadBitmapFromHttp(String url, int reqHeight, int reqWidth) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread");
        }

        if (mDiskLruCache == null)
            return null;

        if (url != null) {
            String key = hashKeyFromUrl(url);
            try {
                //磁盘缓存 存储数据
                DiskLruCache.Editor edit = mDiskLruCache.edit(key);
                if (edit != null) {
                    OutputStream outputStream = edit.newOutputStream(DISK_CACHE_MEMORY);
                    if (downloadUrlToStream(url, outputStream)) {
                        edit.commit();
                    } else {
                        //
                        edit.abort();
                    }
                }
                mDiskLruCache.flush();

                return loadBitmapFromDiskLruCache(url, reqHeight, reqWidth);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    /**
     * 从磁盘中加载缓存
     *
     * @param url
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    private Bitmap loadBitmapFromDiskLruCache(String url, int reqHeight, int reqWidth) throws IOException {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException(" can not visit network in main thread ");
        }

        if (mDiskLruCache == null)
            return null;

        String key = hashKeyFromUrl(url);

        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);

        if (snapshot != null) {
            FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(DISK_CATCH_INDEX);
            FileDescriptor fd = inputStream.getFD();
            //获取到压缩完的Bitmap
            bitmap = ImageReasizer.decodeSampleBitmapFromFileDescriptor(fd, reqHeight, reqWidth);

            //接下来就是获取到Bitmap

            if (bitmap != null)
                addBitmapToMemoryCache(key, bitmap);
        }


        return bitmap;

    }


    /**
     * 将Url进行MD5加密处理
     *
     * @param url
     * @return
     */
    private String hashKeyFromUrl(String url) {

        String cacheKey = null;
        try {

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesHexToString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(url.hashCode());
        }
        return cacheKey;
    }


    private String bytesHexToString(byte[] bytes) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String toHexString = Integer.toHexString(0xFF & bytes[i]);

            if (toHexString.length() == 1) {
                stringBuilder.append('0');
            }

            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }


    /**
     * 从服务器上获取照片流
     *
     * @param url
     * @param out
     * @return
     */
    private boolean downloadUrlToStream(String url, OutputStream out) {

        try {
            URL mUrl = new URL(url);
            mHttpUrlConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpUrlConnection.setRequestMethod("GET");
            mHttpUrlConnection.setConnectTimeout(5000);
            mHttpUrlConnection.setReadTimeout(5000);
            int responseCode = mHttpUrlConnection.getResponseCode();
            if (responseCode == 200) {
                bufferedInputStream = new BufferedInputStream(mHttpUrlConnection.getInputStream());
                bufferedOutputStream = new BufferedOutputStream(out, IO_BUFFER_SIZE);

                int b = 0;
                while ((b = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(b);
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (mHttpUrlConnection != null)
                    mHttpUrlConnection.disconnect();

                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }

                if (bufferedInputStream != null)
                    bufferedInputStream.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * 将网络中的输入流转化成Bitmap
     *
     * @param url
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String url) {

        try {
            URL mUrl = new URL(url);

            urlConnection = (HttpURLConnection) mUrl.openConnection();
            bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);

            return bitmap;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (bufferedInputStream != null)
                    bufferedInputStream.close();
                if (urlConnection != null)
                    urlConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }


    /**
     * 同步加载 不能放在UI线程中去执行，因为这是个耗时的操作。
     *
     * @param uri
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    public Bitmap loadBitmap(String uri, int reqHeight, int reqWidth) {

        //获取到缓存中的bitmap
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "LoadBitmapFromMeCache : " + uri);
            return bitmap;
        }


        //缓存中没有，再去磁盘中获取
        try {
            bitmap = loadBitmapFromDiskLruCache(uri, reqHeight, reqWidth);
            if (bitmap != null)
                return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (bitmap == null && !mIsDiskLruCacheCreate)
            bitmap = downloadBitmapFromUrl(uri);


        return bitmap;
    }


    public void bindBitmap(String uri,ImageView imageView){
        bindBitmap(uri,imageView,0,0);
    }


    /**
     * 异步加载:要学习11章了，线程的部分
     *
     * @param uri
     * @param imageView
     * @param reqHeight
     * @param reqWidth
     */
    public void bindBitmap(final String uri, final ImageView imageView, final int reqHeight, final int reqWidth) {


        imageView.setTag(TAG_KEY_URI, uri);

        //在内存中找到缓存了，就去加载这个内存中的缓存
        final Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }


        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {

                Bitmap loadBitmap = loadBitmap(uri, reqHeight, reqWidth);
                if (loadBitmap != null) {

                    LoaderResult result = new LoaderResult(imageView, uri, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();


                }

            }
        };

        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }




    class LoaderResult {

        private ImageView imageView;
        private String uri;
        private Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public String getUri() {
            return uri;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }


}
