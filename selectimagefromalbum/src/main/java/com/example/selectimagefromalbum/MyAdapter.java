package com.example.selectimagefromalbum;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;

import io.reactivex.internal.operators.maybe.MaybeLift;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uri> mList;
    private ViewHolder viewHolder;
    private ChoosePicListener choosePicListener ;




    public MyAdapter(Context mContext, ArrayList<Uri> mList){
        this.mContext = mContext;
        this.mList = mList;

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Uri getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image_gride,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList!=null  && position< mList.size()) {
            Glide.with(mContext).load(mList.get(position)).into(viewHolder.mImageView);
            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = mList.get(position).getPath();
                    String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(path.toString());
                    String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
                    if (mimeTypeFromExtension != null && mimeTypeFromExtension.contains("video")) {

                    }

                }
            });

            viewHolder.btndeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;



    }

    public void setChoosePicListener(ChoosePicListener choosePicListener){
        this.choosePicListener = choosePicListener;
    }

    class ViewHolder{
        private ImageView mImageView;
        private Button btndeleteImage;
        public ViewHolder(View view){
            mImageView = view.findViewById(R.id.iv_gride_image);
            btndeleteImage = view.findViewById(R.id.bt_del);
        }
    }

    interface ChoosePicListener{
        void choosePic();
    }

}



