package com.example.yuan.app16.example2;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.yuan.app16.R;

/*
http://www.cnblogs.com/liangstudyhome/p/3798638.html
 */

public class Main2Activity extends Activity implements View.OnTouchListener {

    String TAG = "MainLibraryActivity";

    private ImageView img;

    private int lastX, lastY;

    private int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        img = (ImageView) findViewById(R.id.img);
        // 获取屏幕的宽和高
        DisplayMetrics dm = getResources().getDisplayMetrics();//?
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
//        System.out.println("screenWidth-->>" + screenWidth + "    screenHeight-->>" + screenHeight);
        Log.d(TAG, "onCreate: " + "screenWidth-->>" + screenWidth + "    screenHeight-->>" + screenHeight);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("这是ImageView的onTouch");
                Log.d(TAG, "onTouch: 这是ImageView的onTouch");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        /**
                         * 如果这里返回true 或者让ImageView
                         * 的android:onClick设置成true(表示down时间处理成功了)
                         * 在结合时间传递机制就知道为什么了Down-->>move-->>up
                         */
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);

                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

                        //超出边界的处理
                        if (left <= 0) {
                            // 到达父View的左边界
                            left = 0;
                            right = left + v.getWidth();//
                        }
                        if (right >= screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top <= 0) {
                            top = 0;
                            bottom = top + v.getHeight();//
                        }
                        if (bottom >= screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }

                        v.layout(left, top, right, bottom);//?

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        Log.e(TAG, "onTouch: 左" + left + "上" + top + "右" + right
                                + "下" + bottom + " (" + lastX + "," + lastY);
                    case MotionEvent.ACTION_UP:
//                        System.out.println("释放后的位置-->>event.getX():" + event.getX()
//                                + "   event.getY():" + event.getY()
//                                + "  event.getRawX():" + event.getRawX()
//                                + "   event.getRawY():" + event.getRawY());
                        //getX()与getRawX()的区别?
                        Log.d(TAG, "onTouch: " + "释放后的位置-->>event.getX():" + event.getX()
                                + "   event.getY():" + event.getY()
                                + "  event.getRawX():" + event.getRawX()
                                + "   event.getRawY():" + event.getRawY());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.btn).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("这是Button的onTouch");
                Log.d(TAG, "onTouch: " + "                                          这是Button的onTouch             ");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        System.out.println("ACTION_DOWNACTION_DOWNACTION_DOWNACTION_DOWNACTION_DOWN");
                        Log.d(TAG, "onTouch: ACTION_DOWN");
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
//                        System.out.println("MOVEMOVE");
                        Log.d(TAG, "onTouch: MOVEMOVE");
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);

                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

                        if (left <= 0) {
                            // 到达父View的左边界
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right >= screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top <= 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom >= screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }

                        v.layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        Log.e(TAG, "onTouch: 左" + left + "上" + top + "右" + right
                                + "下" + bottom + " (" + lastX + "," + lastY);
                        break;
                    case MotionEvent.ACTION_UP:
//                        System.out.println("释放后的位置-->>event.getX():" + event.getX()
//                                + "   event.getY():" + event.getY()
//                                + "  event.getRawX():" + event.getRawX()
//                                + "   event.getRawY():" + event.getRawY());
                        Log.d(TAG, "onTouch: " + "释放后的位置-->>event.getX():" + event.getX()
                                + "   event.getY():" + event.getY()
                                + "  event.getRawX():" + event.getRawX()
                                + "   event.getRawY():" + event.getRawY());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        System.out.println("这是Activity的onTouch");
        Log.d(TAG, "onTouch: 这是Activity的onTouch");
        return false;
    }

}
