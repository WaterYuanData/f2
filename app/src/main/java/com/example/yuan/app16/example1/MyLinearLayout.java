package com.example.yuan.app16.example1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by yuan on 17-10-17.
 */

public class MyLinearLayout extends LinearLayout {


    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("d", "【市长】任务<" + Util.actionToString(ev.getAction()) + "> : 需要分派");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean bo = false;
        Log.d("d", "【市长】任务<" + Util.actionToString(ev.getAction()) + "> : 拦截吗？" + bo);
        return bo;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean bo = false;
        Log.d("d", "【市长】任务<" + Util.actionToString(ev.getAction()) + "> : 农民真没用，下次再也不找你了，我自己来尝试一下。能解决？" + bo);
        return bo;
    }
}
