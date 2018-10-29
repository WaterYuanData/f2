package com.example.yuan.app16.RecyclerViewTest;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuan.app16.R;
import com.squareup.picasso.Picasso;


//import static com.example.yuan.app16.RecyclerViewTest.Main7Activity.meizis;

/**
 * Created by yuan on 17-12-19.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    String TAG = "适配器";
    Context context;
    Main7Activity m7;

    public MyRecyclerViewAdapter(Main7Activity m7) {
        this.m7 = m7;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: " + parent);//android.support.v7.widget.RecyclerView{481fdac VFED..... ......ID 56,56-1384,2210 #7f0b006b app:id/myRecyclerView}
        Log.d(TAG, "onCreateViewHolder: 创建" + parent.getContext());//com.example.yuan.app16.myRecyclerView.Main7Activity@6c27eaa
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        context = parent.getContext();
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: 绑定位置=" + position);
        holder.tv.setText("图" + position + " " + m7.meizis.get(position).getDesc());
        Picasso.with(context).load(m7.meizis.get(position).getUrl()).into(holder.iv);
        //给item的根布局添加监听
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击第" + position + "个", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return m7.meizis.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;
        private LinearLayout ll;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.line_item_iv);
            tv = (TextView) view.findViewById(R.id.line_item_tv);
            ll = (LinearLayout) view.findViewById(R.id.ll);
        }
    }

    //自写方法
    public void removeItem(final int position) {
        final Meizi removed=m7.meizis.get(position);
        m7.meizis.remove(position);
        notifyItemRemoved(position);
        SnackbarUtil.ShortSnackbar(m7.swipeRefreshLayout,"你删除了第"+position+"个item",SnackbarUtil.Warning).setAction("撤销", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(removed,position);
                SnackbarUtil.ShortSnackbar(m7.swipeRefreshLayout,"撤销了删除第"+position+"个item",SnackbarUtil.Confirm).show();
            }
        }).setActionTextColor(Color.WHITE).show();
    }
    public void addItem(Meizi meizi, int position) {
        m7.meizis.add(position, meizi);
        notifyItemInserted(position);
        m7.recyclerview.scrollToPosition(position);
    }
}