package com.example.packinglistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity<T> extends ListActivity<T> {

    private boolean isLastPage = false;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void loadMore() {
        if (isLastPage) {
            Toast.makeText(MainActivity.this, "不能再加载了。。。", Toast.LENGTH_LONG).show();
            return;
        } else {
            initData();
        }
    }

    @Override
    public void refresh() {
        isLastPage = false;
        currentPage = 1;
        initData();
    }

    private void initData() {

        List<Person> dataList = new ArrayList();

        for (int i = 0; i < currentPage; i++) {
            dataList.add(new Person("zhangsan" + i, i));
        }

        List<DataItem> dataItemList = new ArrayList<>();
        for (Person p : dataList) {
            dataItemList.add(new DataItem(p));
        }

        listItems.addAll(dataItemList);
        listItemAdapter.notifyDataSetChanged();
        currentPage++;
        mPullRefreshLayout.finishRefresh();
        mPullRefreshLayout.finishLoadMore();

    }


    class DataItem implements ListItem {
        private Person p;
        private ViewHolder viewHolder;

        public DataItem(Person p) {
            this.p = p;
        }


        @Override
        public long getId() {
            return 0;
        }

        @Override
        public View getView(LayoutInflater layoutInflater) {
            View view = layoutInflater.inflate(R.layout.item_data, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.tv_name);
            viewHolder.tvAge = view.findViewById(R.id.tv_age);
            viewHolder.tvName.setText(p.getName());
            viewHolder.tvAge.setText(p.getAge());
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public View fillView(View view) {
            if (view != null) {

                viewHolder = (ViewHolder) view.getTag();
                viewHolder.tvName.setText(p.getName());
                viewHolder.tvAge.setText(p.getAge());
            }
            return view;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvAge;
        }
    }
}
