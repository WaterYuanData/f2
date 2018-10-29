package com.example.yuan.app16.example1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.yuan.app16.R;

/*
http://www.cnblogs.com/liangstudyhome/p/3798638.html
 */

public class MyActivity extends Activity {

    String TAG = "MyLinearLayout";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        setContentView(R.layout.activity_my);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ============================================");
        Log.d("d", "【总统】任务<" + Util.actionToString(ev.getAction()) + "> : 需要分派");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean bo = false;
        Log.d("d", "【总统】任务<" + Util.actionToString(ev.getAction()) + "> : 下面都解决不了，下次再也不能靠你们了，哼…只能自己尝试一下啦。能解决？" + bo);
        return bo;
    }
}

class Util {
    static String actionToString(int ii) {
        String ss = "" + ii + " ";
        switch (ii) {
            case MotionEvent.ACTION_DOWN:
                ss += "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                ss += "ACTION_UP";
                break;
            case MotionEvent.ACTION_MOVE:
                ss += "ACTION_MOVE";
                break;
            default:
                ss += "default";
                break;
        }
        return ss;
    }
}
