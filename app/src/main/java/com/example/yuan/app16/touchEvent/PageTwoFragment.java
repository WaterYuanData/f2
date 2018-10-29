package com.example.yuan.app16.touchEvent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuan.app16.R;

/**
 * Created by wensefu on 17-3-11.
 */
public class PageTwoFragment extends Fragment{

    String TAG = "PageTwoFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.page2, container, false);
        Log.d(TAG, "onCreateView: container="+container);//container=android.support.v4.view.ViewPager{a5ed4a VFED..... ......I. 0,0-0,0 #7f0b005c app:id/pager}
        Log.d(TAG, "onCreateView: inflate="+inflate);//inflate=android.widget.LinearLayout{5926969 V.E...... ......ID 0,0-0,0}
        return inflate;
    }
}
