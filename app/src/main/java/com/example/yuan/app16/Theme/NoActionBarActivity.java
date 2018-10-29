package com.example.yuan.app16.Theme;

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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yuan.app16.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoActionBarActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Fruit[] fruits = {
            new Fruit("play1", R.drawable.play1),
            new Fruit("play2", R.drawable.play2),
            new Fruit("play3", R.drawable.play3),
            new Fruit("play4", R.drawable.play4),
            new Fruit("play5", R.drawable.play5),
            new Fruit("play6", R.drawable.play6),
            new Fruit("play7", R.drawable.play7),
            new Fruit("play8", R.drawable.play8),
            new Fruit("play9", R.drawable.play9),
            new Fruit("play10", R.drawable.play10),
            new Fruit("play11", R.drawable.play11),
            new Fruit("play12", R.drawable.play12),
    };

    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;

    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_action_bar);

//==================RecyclerView的使用
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);

//==================用ToolBar替代ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        //设置Toolbar中的HomeAsUp按钮打开侧滑抽屉布局,在onOptionsItemSelected()
        //抽屉,在Toolbar中添加显示抽屉的按钮,此处借用Toolbar的HomeAsUp按钮
        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        ActionBar mActionBar = getSupportActionBar();//该ActionBar的具体实现由Toolbar完成
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);//让导航按钮显示出来
            mActionBar.setHomeAsUpIndicator(R.drawable.icon);//设置导航按钮
        }

        //初始化,设置NavigationView中的点击响应
        //抽屉布局的主内容
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.call);//使该菜单默认选中
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.call:
                        Toast.makeText(getApplicationContext(), "call", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.friends:
                        Toast.makeText(getApplicationContext(), "friends", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.location:
                        Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mail:
                        Toast.makeText(getApplicationContext(), "mail", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.task:
                        Toast.makeText(getApplicationContext(), "task", Toast.LENGTH_SHORT).show();
                        break;
//NavigationView中的menu可点击,headerLayout不可点击???
                    case R.id.icon:
                        Toast.makeText(getApplicationContext(), "icon", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.username:
                        Toast.makeText(getApplicationContext(), "username", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                return true;
            }
        });

        //悬浮按钮
        //Snackbar的使用
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View floatingActionButton) {
//                Snackbar.make(mDrawerLayout,"删除数据",Snackbar.LENGTH_LONG)
//                Snackbar此处属于FloatingActionButton,而FloatingActionButton在CoordinatorLayout内,故被监听
//                而DrawerLayout在CoordinatorLayout外,故监听不到
                Snackbar.make(floatingActionButton, "删除数据", Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "已撤销", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setAction("撤销2", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "已撤销2", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

    }


    //引入Toolbar的布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //Toolbar中按钮的点击响应监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "备份", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.home:是干嘛的???
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                Toast.makeText(this, "???", Toast.LENGTH_SHORT).show();
//                break;
            case android.R.id.home://不是R.id.home
                mDrawerLayout.openDrawer(GravityCompat.START);
                Toast.makeText(this, "抽屉", Toast.LENGTH_SHORT).show();
                break;
            //NavigationView中的menu可点击,headerLayout不可点击???
            case R.id.icon:
                Toast.makeText(getApplicationContext(), "icon22", Toast.LENGTH_SHORT).show();
                break;
            case R.id.username:
                Toast.makeText(getApplicationContext(), "username22", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
