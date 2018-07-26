package com.example.packinglistview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class ListActivity<T> extends AppCompatActivity implements AdapterView.OnItemClickListener, BaseRefreshListener {

    public PullToRefreshLayout mPullRefreshLayout;
    private ListView mListView;
    public ListAdapter<ListItem> listItemAdapter;
    List<ListItem> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        listItemAdapter = new ListAdapter<>(this);
        mListView.setAdapter(listItemAdapter);
    }

    /**
     * 初始化控件
     */
    public void initView() {
        mPullRefreshLayout = findViewById(R.id.pullToRefreshLayout);
        mListView = findViewById(R.id.lv_review);
    }

    @Override
    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);


    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }

    public interface ListItem {
        long getId();

        View getView(LayoutInflater layoutInflater);

        View fillView(View view);
    }

    public class ListAdapter<T extends ListItem> extends BaseAdapter {

        private Context mContext;
        private LayoutInflater inflater;
        private List<T> items = new ArrayList<>();

        public ListAdapter(Context mContext) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                items.get(position).getView(inflater);
            } else {
                items.get(position).fillView(convertView);
            }
            return convertView;
        }
    }
}
