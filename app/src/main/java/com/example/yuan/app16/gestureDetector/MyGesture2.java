package com.example.yuan.app16.gestureDetector;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.yuan.app16.R;

//http://www.cnblogs.com/zhuangxiongwei/articles/GestureDetector.html
public class MyGesture2 extends Activity implements View.OnTouchListener {
    private GestureDetector mGestureDetector;
    public MyGesture2() {
        mGestureDetector = new GestureDetector(new MySimpleGesture());
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gesture2);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setOnTouchListener(this);
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setLongClickable(true);
    }
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i("MyGesture", "MotionEvent.ACTION_UP");
        }
        return mGestureDetector.onTouchEvent(event);
    }

    // SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener
    private class MySimpleGesture extends GestureDetector.SimpleOnGestureListener {
        // 双击的第二下Touch down时触发
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("MyGesture", "onDoubleTap");
            return super.onDoubleTap(e);
        }

        // 双击的第二下Touch down和up都会触发，可用e.getAction()区分
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("MyGesture", "onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

        // Touch down时触发
        public boolean onDown(MotionEvent e) {
            Log.i("MyGesture", "onDown");
            return super.onDown(e);
        }

        // Touch了滑动一点距离后，up时触发
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("MyGesture", "onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        // Touch了不移动一直Touch down时触发
        public void onLongPress(MotionEvent e) {
            Log.i("MyGesture", "onLongPress");
            super.onLongPress(e);
        }

        // Touch了滑动时触发
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("MyGesture", "onScroll");
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        /*
         * Touch了还没有滑动时触发
         * (1)onDown只要Touch Down一定立刻触发
         * (2)Touch Down后过一会没有滑动先触发onShowPress再触发onLongPress
         * So: Touch Down后一直不滑动，onDown -> onShowPress -> onLongPress这个顺序触发。
         */
        public void onShowPress(MotionEvent e) {
            Log.i("MyGesture", "onShowPress");
            super.onShowPress(e);
        }

        /*
         * 两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发
         * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed
         * 点击一下稍微慢点的(不滑动)Touch Up: onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
         */
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapConfirmed");
            return super.onSingleTapConfirmed(e);
        }
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapUp");
            return super.onSingleTapUp(e);
        }
    }
}