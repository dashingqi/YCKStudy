package com.example.imageloaderdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.imageloaderdemo.util.MyUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<String> mUriList = new ArrayList<>();
    private GridView mGridView;
    private boolean mIsWifi;
    private int mImageWidth;
    private boolean mCanGetBitmapFromNetWork = false;
    private MyAdapter myAdapter;
    private boolean mIsGridViewIdle = true;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        mImageLoader = ImageLoader.build(this);

    }

    private void initView() {
        mGridView = findViewById(R.id.gridVeiw);
        myAdapter = new MyAdapter(this);
        mGridView.setAdapter(myAdapter);
        mGridView.setOnScrollListener(this);

        if (!mIsWifi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("初次使用会从网络下载大概5MB的图片，确定要下载吗？");
            builder.setTitle("注意");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCanGetBitmapFromNetWork = true;
                    myAdapter.notifyDataSetChanged();

                }
            });

            builder.setNegativeButton("否", null);

            builder.show();

        }
    }

    private void initData() {


        String[] imageUrls = {

                "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
                "http://pic47.nipic.com/20140830/7487939_180041822000_2.jpg",
                "http://pic41.nipic.com/20140518/4135003_102912523000_2.jpg",
                "http://pic42.nipic.com/20140618/9448607_210533564001_2.jpg",
                "http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c0f1f6e9efff2b21192138ac0.jpg",
                "http://pic10.nipic.com/20101027/3578782_201643041706_2.jpg",
                "http://img2.3lian.com/2014/c7/51/d/26.jpg",
                "http://img3.3lian.com/2013/c1/34/d/93.jpg",
                "http://b.zol-img.com.cn/desk/bizhi/image/3/960x600/1375841395686.jpg"
        };


        for (String uri : imageUrls) {
            mUriList.add(uri);
        }

        //判断当前手机网络状态
        mIsWifi = MyUtils.isWifi(this);

        int widthPixels = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int) MyUtils.dp2px(this, 20);

        mImageWidth = (widthPixels - space) / 3;

        if (mIsWifi) {
            mCanGetBitmapFromNetWork = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mIsGridViewIdle = true;
        } else {
            mIsGridViewIdle = false;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private class MyAdapter extends BaseAdapter {

        private Drawable mDefaultBitmapDrawable;
        private LayoutInflater mInflater;

        public MyAdapter(Context mContext) {
            mInflater = LayoutInflater.from(mContext);
            mDefaultBitmapDrawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
        }

        @Override
        public int getCount() {
            return mUriList.size();
        }

        @Override
        public String getItem(int position) {
            return mUriList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageview = convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ImageView imageView = viewHolder.imageview;
            final String tag = (String) imageView.getTag();
            final String uri = getItem(position);

            if (!uri.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }

            if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
                imageView.setTag(uri);
                mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);

            }
            return convertView;
        }
    }


    class ViewHolder {

        public ViewHolder() {
        }

        public ImageView imageview;
    }
}
