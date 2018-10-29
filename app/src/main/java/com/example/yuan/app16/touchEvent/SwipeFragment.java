package com.example.yuan.app16.touchEvent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yuan.app16.R;

/**
 * Created by wensefu on 17-3-11.
 */
public class SwipeFragment extends Fragment implements View.OnClickListener{

    String TAG = "SwipeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        http://www.cnblogs.com/mengdd/archive/2013/01/08/2851368.html
//        onCreateView()中container参数代表该Fragment在Activity中的父控件；savedInstanceState提供了上一个实例的数据。
        Log.d(TAG, "onCreateView: inflater="+inflater);//
        Log.d(TAG, "onCreateView: container="+container);//container=android.support.v4.view.ViewPager{a5ed4a VFED..... ......I. 0,0-0,0 #7f0b005c app:id/pager}
        Log.d(TAG, "onCreateView: savedInstanceState="+savedInstanceState);//
//        inflate()方法的三个参数：第一个是resource ID，指明了当前的Fragment对应的资源文件；第二个参数是父容器控件；
//        第三个布尔值参数表明是否连接该布局和其父容器控件，在这里的情况设置为false，因为系统已经插入了这个布局到父控件，设置为true将会产生多余的一个View Group。
//        返回值是当前的Fragment对应的资源文件的跟标签
        View view = inflater.inflate(R.layout.page1,container,false);
        View itemView = view.findViewById(R.id.itemview);
        itemView.setOnClickListener(this);
        Log.d(TAG, "onCreateView: view="+view);//view=com.example.yuan.app16.example3.MyViewGroup{536cb1c V.E...... ......I. 0,0-0,0}
        Log.d(TAG, "onCreateView: itemView="+itemView);//itemView=com.example.yuan.app16.example3.MyView{c2c036d V.ED..C.. ......I. 0,0-0,0 #7f0b0077 app:id/itemview}
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: "+v);
        Toast.makeText(getActivity(),"item被点击",Toast.LENGTH_SHORT).show();
    }
}
