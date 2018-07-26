package com.example.listviewcbdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {


    private Context context;
    private List<MyDataBean> myDataBeans;
    private MyViewHolder myViewHolder;

    public MyAdapter(Context context, List<MyDataBean> myDataBeans) {
        this.context = context;
        this.myDataBeans = myDataBeans;
    }

    @Override
    public int getCount() {
        return myDataBeans.size();
    }

    @Override
    public MyDataBean getItem(int position) {
        return myDataBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cb_layout, null);
            myViewHolder = new MyViewHolder(convertView);
           convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        if (myDataBeans.get(position).isCheck()){
            myViewHolder.cbCheck.setChecked(true);
        }else{
            myViewHolder.cbCheck.setChecked(false);
        }

        myViewHolder.tvTitle.setText(myDataBeans.get(position).getTitle());

        return convertView;
    }


    class MyViewHolder {
        public TextView tvTitle;
        public CheckBox cbCheck;
        public MyViewHolder(View view){
            tvTitle = view.findViewById(R.id.tv_title);
            cbCheck = view.findViewById(R.id.cb_check);

        }
    }
}
