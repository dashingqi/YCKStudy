package com.example.dashingqi.yckstudy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dashingqi on 23/4/18.
 */

public class MyAdapter extends BaseAdapter {

    private List<String> list;
    private Context mContext;

    public MyAdapter(List<String> list, Context mContext) {

        this.list = list;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.activity_main, parent);
            holder.tvName = convertView.findViewById(R.id.top);
            holder.tvSex = convertView.findViewById(R.id.top);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvSex.setText("你比");
        holder.tvName.setText("什么鬼东西呀");
        return convertView;
    }

    class ViewHolder {
        private TextView tvName;
        private TextView tvSex;
    }
}
