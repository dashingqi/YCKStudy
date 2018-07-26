package com.example.selectimagefromalbum;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnSelectImage;
    private int REQUEST_GET_IMAGE = 0;
    ArrayList<Uri> mSelected;
    private GridView grideLayout;
    private ArrayList<Uri> mUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSelectImage = (Button) findViewById(R.id.btn_select_single_image);
        grideLayout = (GridView) findViewById(R.id.grid_layout);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
    }

    public void selectImage() {
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .theme(R.style.Matisse_Dracula)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_GET_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GET_IMAGE) {
                if (data != null) {

                    mSelected = (ArrayList<Uri>) Matisse.obtainResult(data);
                    for (Uri uri : mSelected) {
                        if (!mUri.contains(uri)) {
                            mUri.add(uri);
                        }
                    }
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this, mUri);

                    grideLayout.setAdapter(myAdapter);

                }
                Log.d("TAg", "Tag=" + mSelected);
            }
        }
    }


}
