package com.example.yuan.app16.listView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yuan.app16.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//http://www.cnblogs.com/yejiurui/p/3247527.html
public class ListOnLongClickActivity extends AppCompatActivity {

    private LinearLayout myListViewlayout;
    private ListView mListView;
    SimpleAdapter adapter;
    public int MID;
    // 创建一个List对象，用来存放列表项的每一行map信息
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_test);

        myListViewlayout = (LinearLayout) findViewById(R.id.myListViewlayout);
        mListView = new ListView(this);
        // 创建布局参数
        LinearLayout.LayoutParams listviewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        // 当滑动列表上，默认显示的黑色
        mListView.setCacheColorHint(Color.WHITE);
        // 将列表添加到流式布局myListViewlayout中
        myListViewlayout.addView(mListView, listviewParams);

        FillListData();

        // 列表现的单机事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                                /*
                                 * 点击列表项时触发onItemClick方法，四个参数含义分别为
                                 * arg0：发生单击事件的AdapterView
                                 * arg1：AdapterView中被点击的View
                                 * position：当前点击的行在adapter的下标
                                 * id：当前点击的行的id
                                 */
                Toast.makeText(ListOnLongClickActivity.this,
                        "您选择的是" + list.get(position).get("name").toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Item 长按方式弹出菜单多选方式1
         * Item 长按方式弹出菜单多选方式2
         * ItemOnLongClick1()与ItemOnLongClick2()不共存，按实际需要选择
         */
                ItemOnLongClick1();
//        ItemOnLongClick2();//有bug,与可能是arrcontent为空引入的
//        java.lang.IllegalStateException: The content of the adapter has changed but ListView did not receive a notification.
// Make sure the content of your adapter is not modified from a background thread, but only from the UI thread.
// Make sure your adapter calls notifyDataSetChanged() when its content changes. [in ListView(-1, class android.widget.ListView) with Adapter(class android.widget.SimpleAdapter)]

    }

    // 填充ListView资源
    private void FillListData() {

        adapter = new SimpleAdapter(ListOnLongClickActivity.this,
                getListData(), R.layout.listviewrow, new String[] { "name",
                "price" }, new int[] { R.id.tv_name, R.id.tv_price });
        mListView.setAdapter(adapter);
    }

    private List<Map<String, Object>> getListData() {

        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("name", "小米");
        _map.put("price", "2350元");
        list.add(_map);

        _map = new HashMap<String, Object>();
        _map.put("name", "HTC G18");
        _map.put("price", "3340元");
        list.add(_map);

        _map = new HashMap<String, Object>();
        _map.put("name", "iphone 5");
        _map.put("price", "5450元");
        list.add(_map);

        _map = new HashMap<String, Object>();
        _map.put("name", "iPhone 4S");
        _map.put("price", "4650元");
        list.add(_map);

        _map = new HashMap<String, Object>();
        _map.put("name", "MOTO ME525");
        _map.put("price", "1345元");
        list.add(_map);
        return list;

    }

    private void ItemOnLongClick1() {
//注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(0, 0, 0, "购买");
                        menu.add(0, 1, 0, "收藏");
                        menu.add(0, 2, 0, "对比");

                    }
                });
    }

    // 长按菜单响应函数
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

        switch (item.getItemId()) {
            case 0:
                // 添加操作
                Toast.makeText(ListOnLongClickActivity.this,
                        "添加",
                        Toast.LENGTH_SHORT).show();
                break;

            case 1:
                // 删除操作
                break;

            case 2:
                // 删除ALL操作
                break;

            default:
                break;
        }

        return super.onContextItemSelected(item);

    }

//    CharSequence[] arrcontent = new CharSequence[]{};
    String[] arrcontent = new String[]{};

    private void ItemOnLongClick2() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                list.remove(arg2);
                new AlertDialog.Builder(ListOnLongClickActivity.this)
                        .setTitle("对Item进行操作")
//                        .setItems(R.array.arrcontent,
                        .setItems(arrcontent,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                        String[] PK = getResources()
//                                                .getStringArray(
//                                                        R.array.arrcontent);
                                        String[] PK = arrcontent;
                                        Toast.makeText(
                                                ListOnLongClickActivity.this,
                                                PK[which], Toast.LENGTH_LONG)
                                                .show();
                                        if (PK[which].equals("删除")) {
                                            // 按照这种方式做删除操作，这个if内的代码有bug，实际代码中按需操作
                                            list.remove(arg2);
                                            adapter = (SimpleAdapter) mListView
                                                    .getAdapter();
                                            if (!adapter.isEmpty()) {
                                                adapter.notifyDataSetChanged(); // 实现数据的实时刷新
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                }).show();
                return true;
            }
        });

    }

}
