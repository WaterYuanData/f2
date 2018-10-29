package com.example.yuan.app16.gestureDetector;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuan.app16.R;

//http://www.cnblogs.com/zhuangxiongwei/articles/GestureDetector.html
public class MyGesture extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private GestureDetector mGestureDetector;
    public MyGesture() {
        mGestureDetector = new GestureDetector(this);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setOnTouchListener(this);
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setLongClickable(true);
        mGestureDetector.setIsLongpressEnabled(true);
    }

    /*
     * 在onTouch()方法中，我们调用GestureDetector的onTouchEvent()方法，将捕捉到的MotionEvent交给GestureDetector
     * 来分析是否有合适的callback函数来处理用户的手势
     */
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
    public boolean onDown(MotionEvent arg0) {
        Log.i("MyGesture", "onDown");
        Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
        return true;
    }

    /*
     * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
     * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
     */
    public void onShowPress(MotionEvent e) {
        Log.i("MyGesture", "onShowPress");
        Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();
    }

    // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("MyGesture", "onSingleTapUp");
        Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return true;
    }

    // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        Log.i("MyGesture", "onFling");
//        Toast.makeText(this, "onFling", Toast.LENGTH_LONG).show();
//        return true;
//    }

    /*
    * 3. Fling事件的处理代码：除了第一个触发Fling的ACTION_DOWN和最后一个ACTION_MOVE中包含的坐标等信息外，
    * 我们还可以根据用户在X轴或者Y轴上的移动速度作为条件。
    * 比如下面的代码中我们就在用户移动超过100个像素，且X轴上每秒的移动速度大于200像素时才进行处理。
    * */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 参数解释：
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度，像素/秒
        // velocityY：Y轴上的移动速度，像素/秒

        // 触发条件 ：
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

        final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling left
            Log.i("MyGesture", "Fling left");
            Toast.makeText(this, "Fling Left", Toast.LENGTH_SHORT).show();
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            Log.i("MyGesture", "Fling right");
            Toast.makeText(this, "Fling Right", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("MyGesture", "onScroll");
        Toast.makeText(this, "onScroll", Toast.LENGTH_LONG).show();
        return true;
    }

    // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
    public void onLongPress(MotionEvent e) {
        Log.i("MyGesture", "onLongPress");
        Toast.makeText(this, "onLongPress", Toast.LENGTH_LONG).show();
    }
}