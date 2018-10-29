package com.example.yuan.app16.touchEvent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.yuan.app16.R;


//http://www.jianshu.com/p/328ab7c84ca0

public class Main3Activity extends AppCompatActivity {//AppCompatActivity继承与FragmentActivity

    private static final String TAG = "Main3Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //ViewPager继承自ViewGroup
        ViewPager pager = (ViewPager) findViewById(R.id.pager);//pager是activity_main3的id
        Log.d(TAG, "onCreate: pager="+pager);
        //把片段作为适配器的参数引入Activity
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));//getSupportFragmentManager()在FragmentActivity中
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
//            Log.d(TAG, "MyPagerAdapter: ");//必须放在调用父类构造方法后,否则报错
            super(fm);
            Log.d(TAG, "MyPagerAdapter: 构造 "+fm);
        }

//        适配器中getItem(),getCount()的调用机制?

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = position == 0 ? new SwipeFragment() : new PageTwoFragment();
            Log.d(TAG, "getItem: position=" + position+"    fragment="+fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            Log.d(TAG, "getCount: ");
            return 2;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        Log.d(TAG, "dispatchTouchEvent: action=" + action);
        Log.d(TAG, " "+ev.getAction()+"  --  "+MotionEvent.ACTION_MASK);
//        return  super.dispatchTouchEvent(ev);
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: " + b);
        return b;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        Log.d(TAG, "onTouchEvent: action=" + action);
        Log.d(TAG, " "+ev.getAction()+"  --  "+MotionEvent.ACTION_MASK);
        boolean b = super.onTouchEvent(ev);
        Log.d(TAG, "处理: " + b);
        return b;
    }
}
