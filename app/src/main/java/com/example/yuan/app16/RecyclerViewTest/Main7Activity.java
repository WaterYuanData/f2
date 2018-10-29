package com.example.yuan.app16.RecyclerViewTest;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;

import com.example.yuan.app16.R;

import java.util.Collections;
import java.util.List;

public class Main7Activity extends AppCompatActivity {

    Context mContext;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerview;
    LinearLayoutManager mLayoutManager;
    ItemTouchHelper itemTouchHelper;
    MyRecyclerViewAdapter mAdapter;
    List<Meizi> meizis;
    int page = 1;
    Main7Activity m7 = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        mContext = getApplicationContext();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        recyclerview = (RecyclerView) findViewById(R.id.RecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLayoutManager);

//        new Thread() {
//            public void run() {
//                //访问网络代码
//                String result = MyOkhttp.get("http://gank.io/api/data/福利/10/1");
//                Log.d("demo", "onClick: result=" + result);
//            }
//        }.start();

//        final GetData getData = new GetData(this);
// java.lang.IllegalStateException: Cannot execute task: the task has already been executed (a task can be executed only once)
        GetData getData = new GetData(this);
        getData.execute("http://gank.io/api/data/福利/10/1");


        //处理在RecyclerView上添加拖动排序与滑动删除
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                return 0;
                int dragFlags = 0, swipeFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    //设置侧滑方向为从左到右和从右到左都可以
//                    swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    swipeFlags = ItemTouchHelper.LEFT;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(meizis, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //自写方法
                mAdapter.removeItem(viewHolder.getAdapterPosition());
            }
        });


        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                Log.d("setListener", "onRefresh: 下拉刷新");
                new GetData(m7).execute("http://gank.io/api/data/福利/10/1");
            }
        });

        //滚动监听,上拉加载
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            String TAG = "滚动";
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: 滚动状态=" + newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {
                    Log.d(TAG, "onScrollStateChanged: " + mAdapter);
                    Log.d(TAG, "onScrollStateChanged: " + meizis.size());
                    GetData getData = new GetData(m7);
                    getData.execute("http://gank.io/api/data/福利/10/" + (++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//                Log.d(TAG, "onScrolled: 滚动="+lastVisibleItem);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        meizis.removeAll(meizis);
        Log.d("Main7Activity", "onStop: 退出");
    }
}



