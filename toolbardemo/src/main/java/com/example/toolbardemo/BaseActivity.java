package com.example.toolbardemo;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mMenuNavigationView;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;

    private FruitBean[] fruitBeans = {new FruitBean("cherry", R.drawable.cherry), new FruitBean("apple",R.drawable.apple ), new FruitBean("banana",R.drawable.banana ),new FruitBean("grape",R.drawable.grape)};
    private List<FruitBean> fruitBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initView();
        initData();
        initAdapter();

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        //默认选中第一的item
        mMenuNavigationView.setCheckedItem(R.id.friend);

        mMenuNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mMenuNavigationView.setCheckedItem(menuItem.getItemId());
                return false;
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(BaseActivity.this, "data delete", Toast.LENGTH_LONG).show();

                            }
                        }).show();
            }
        });
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        FruitAdapter fruitAdapter = new FruitAdapter(this, fruitBeanList);
        mRecyclerView.setAdapter(fruitAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i <= 50; i++) {

            int length = new Random().nextInt(fruitBeans.length);
            fruitBeanList.add(fruitBeans[length]);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mMenuNavigationView = findViewById(R.id.menu_navigation_view);
        mToolbar = findViewById(R.id.tb);
        mFloatingActionButton = findViewById(R.id.floating_action_button);
        mRecyclerView = findViewById(R.id.mRecyclerView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_backup:
                Toast.makeText(BaseActivity.this, "backup", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_delete:
                Toast.makeText(BaseActivity.this, "delete", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_setting:
                Toast.makeText(BaseActivity.this, "setting", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}
