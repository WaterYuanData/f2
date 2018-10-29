package com.example.yuan.app16.touchEvent;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wensefu on 17-3-10.
 */
public class MyView extends View{

    private static final String TAG = "MyView";

    public MyView(Context context) {
        super(context);
        Log.d(TAG, "MyView: 构造 "+context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "MyView: 构造 context="+context);
        Log.d(TAG, "MyView: 构造 attrs="+attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: ev.getAction()=" + ev.getAction());
        Log.d(TAG, "dispatchTouchEvent: ACTION_MASK=" + MotionEvent.ACTION_MASK);
        Log.d(TAG, "dispatchTouchEvent: action=" + action);
        Log.d(TAG, "派发: "+b);
        return b;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: canvas="+canvas);
        canvas.drawColor(0xFF558B2F);
    }
}
