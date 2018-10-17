package com.example.toolbardemo;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FruitActivity extends AppCompatActivity {

    public static final String FRUIT_NAME = "fruit_name";
    public static final String FRUIT_IMAGE_ID ="fruit_name_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        Intent intent = getIntent();
        String fruitName = intent.getStringExtra(FRUIT_NAME);
        int fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID,0);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = findViewById(R.id.fruit_image_view);
        TextView fruitContentText = findViewById(R.id.fruit_content_text);
        Toolbar mToolBar = findViewById(R.id.toolBar);

        //设置下 Toolbar
        setSupportActionBar(mToolBar);

        //设置
        ActionBar actionbar = getSupportActionBar();

        if (actionbar!=null){
            //返回按钮
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        //设置下标题
        collapsingToolbar.setTitle(fruitName);

        Glide.with(this).load(fruitImageId).into(fruitImageView);
        String contentText = generateFruitContent(fruitName);

        fruitContentText.setText(contentText);

    }

    private String generateFruitContent(String fruitName){

        StringBuilder stringBuilder = new StringBuilder();

        for(int i=00;i<5000;i++){
            stringBuilder.append(fruitName);
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
