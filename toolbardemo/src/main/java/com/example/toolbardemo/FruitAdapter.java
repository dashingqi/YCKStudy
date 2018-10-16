package com.example.toolbardemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<FruitBean> fruitList;
    private Context mContext;

    public FruitAdapter(Context mContext, List<FruitBean> fruitList){
        this.mContext = mContext;
        this.fruitList = fruitList;





    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fruit, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FruitBean fruitBean = fruitList.get(i);
        Glide.with(mContext).load(fruitBean.getFruitId()).into(viewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public ViewHolder(View view){
            super(view);
             mImageView = view.findViewById(R.id.iv);
        }
    }
}
