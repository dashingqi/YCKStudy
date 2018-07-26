package com.example.wheelview_demo;

import android.app.VoiceInteractor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OptionsPickerBuilder pvOptions;

    private List<String> options1Items ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initWhell();
        initOptionView();
    }

    public void initOptionView() {
        options1Items = new ArrayList();
        options1Items.add("1");
        options1Items.add("2");
        options1Items.add("3");

        OptionsPickerView pvOptions = new OptionsPickerBuilder(MainActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String s = options1Items.get(options1);
                Log.d("Tag",s+options1);


            }
        }).setSubmitText("确定")
                .setCancelText("取消")
                .setTitleText("请选择")
                .build();
        pvOptions.setPicker(options1Items, null, null);
        pvOptions.show();


    }

    private void initWhell() {
        WheelView wheelView = findViewById(R.id.wheelview);
        wheelView.setCyclic(false);


        final List<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("item0");
        mOptionsItems.add("item1");
        mOptionsItems.add("item2");

        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(MainActivity.this, "" + mOptionsItems.get(index), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ArrayWheelAdapter implements WheelAdapter {
        private List<String> list;

        public ArrayWheelAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        public String getItem(int index) {
            return list.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

    }
}
