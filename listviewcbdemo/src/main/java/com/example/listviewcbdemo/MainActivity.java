package com.example.listviewcbdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<MyDataBean> myList = new ArrayList<>();
    private int currentNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        final MyAdapter myAdapter = new MyAdapter(MainActivity.this, myList);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (MyDataBean myDataBean : myList) {
                    myDataBean.setCheck(false);
                }
                if (currentNum == -1) {
                    myList.get(position).setCheck(true);
                    currentNum = position;
                } else if (currentNum == position) {
                    for (MyDataBean myDataBean : myList)
                        myDataBean.setCheck(false);
                    currentNum = -1;
                } else if (currentNum != position) {

                    for (MyDataBean myDataBean : myList) {
                            myDataBean.setCheck(false);
                    }
                    myList.get(position).setCheck(true);

                    currentNum = position;
                }

                //刷新下 数据
                myAdapter.notifyDataSetChanged();

//                MyDataBean myDataBean = myList.get(position);

            }
        });
    }

    private void initData() {
        for (int i = 0; i <= 20; i++) {
            myList.add(new MyDataBean("张三" + i));
        }
    }

    private void initView() {
        mListView = findViewById(R.id.my_list_view);
    }
}
