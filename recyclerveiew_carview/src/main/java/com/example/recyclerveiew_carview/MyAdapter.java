package com.example.recyclerveiew_carview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private final LayoutInflater mInflater;
    private List<Person> mList = new ArrayList<>();

    public MyAdapter(Context mContext, List<Person> mList) {
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_rv_card, viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Person person = mList.get(i);
        viewHolder.mName.setText(person.getName());
        viewHolder.mAge.setText(person.getAge());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mAge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.tv_name);
            mAge = itemView.findViewById(R.id.tv_age);
        }
    }
}
