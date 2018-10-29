package com.example.yuan.app16.touchEvent;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by wensefu on 17-3-10.
 */
public class MyViewGroup extends FrameLayout{

    private static final String TAG = "MyViewGroup";

    private boolean mBeingDragged;
    private float mInitDownX;
    private float mInitDownY;
    private float mLastX;
    private float mLastY;
    private float mTouchSlop;
    private View mTouchView;
    private boolean mTouchOnChild;


    public MyViewGroup(Context context) {
        this(context,null);
        Log.d(TAG, "MyViewGroup: 构造 context="+context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();//?
        Log.d(TAG, "MyViewGroup: 构造 context="+context);
        Log.d(TAG, "MyViewGroup: 构造 AttributeSet="+attrs);
        Log.d(TAG, "MyViewGroup: 构造 ViewConfiguration="+vc);
        Log.d(TAG, "MyViewGroup: 构造 mTouchSlop="+mTouchSlop);//28.0
    }

    private void updateTouchView() {
        int cnt = getChildCount();
        if (cnt > 0) {
            mTouchView = getChildAt(0);
            Log.d(TAG, "updateTouchView: getChildCount()="+cnt+"    mTouchView="+mTouchView);//mTouchView=com.example.yuan.app16.example3.MyView{c2c036d V.ED..... ......I. 0,0-0,0 #7f0b0077 app:id/itemview}
        } else {
            mTouchView = null;
        }
    }

    @Override
    public void onViewAdded(View child) {
        Log.d(TAG, "onViewAdded: 添加 "+child);
        updateTouchView();
    }

    @Override
    public void onViewRemoved(View child) {
        Log.d(TAG, "onViewRemoved: 移除 "+child);
        updateTouchView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
//        return super.dispatchTouchEvent(ev);
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "MyViewGroup dispatchTouchEvent: action=" + action);
        Log.d(TAG, "派发: "+b);
        return b;
    }

    private static boolean isTouchOnView(float x, float y, View view) {
        if (view == null) {
            return false;
        }
        Rect rect = new Rect();
        view.getHitRect(rect);//
        boolean contains = rect.contains((int) x, (int) y);
        Log.d(TAG, "isTouchOnView="+contains);
        return contains;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        Log.d(TAG, "onInterceptTouchEvent: action=" + action);
        float x = ev.getX();
        float y = ev.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:{
                //down时记录下初始坐标
                mTouchOnChild = isTouchOnView(x,y,mTouchView);
                Log.d(TAG, "onInterceptTouchEvent: mTouchOnChild="+mTouchOnChild);
                if (!mTouchOnChild) {
                    return false;
                }
                //getParent()在View中
                getParent().requestDisallowInterceptTouchEvent(true);//?
                mLastX = mInitDownX = x;
                mLastY = mInitDownY = y;
                Log.d(TAG, "ACTION_DOWN: mLastX="+mLastX+"    mLastY="+mLastY);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float dx = x - mInitDownX;
                float dy = y - mInitDownY;
                Log.d(TAG, "ACTION_MOVE: dx="+dx+"    dy="+dy);
                if (Math.abs(dx) > mTouchSlop || Math.abs(dy) > mTouchSlop) {
                    mBeingDragged = true;
                    Log.d(TAG, "onInterceptTouchEvent: 开始拖拽");
                }
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_UP:{
                Log.d(TAG, "onInterceptTouchEvent: ACTION_UP");
                break;
            }
        }
        return mBeingDragged;
    }

    private void performDrag(float dx, float dy) {
        if (mTouchView == null) {
            return;
        }
        Log.d(TAG, "performDrag: dx="+dx);
        Log.d(TAG, "performDrag: dy="+dy);
        mTouchView.offsetLeftAndRight((int) dx);
        mTouchView.offsetTopAndBottom((int) dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        float x = ev.getX();
        float y = ev.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:{
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                Log.d(TAG, "onTouchEvent: ACTION_MOVE   mTouchOnChild="+mTouchOnChild+" mBeingDragged="+mBeingDragged);
                if(!mTouchOnChild){
                    break;
                }
                if (!mBeingDragged) {
                    mBeingDragged = true;
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                Log.d(TAG, "ACTION_MOVE: dx="+dx+"    dy="+dy);
                performDrag(dx,dy);
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent: ACTION_CANCEL");
            case MotionEvent.ACTION_UP:{
                Log.d(TAG, "onTouchEvent: ACTION_UP");
                mBeingDragged = false;
                break;
            }
        }
        return true;
    }
}
