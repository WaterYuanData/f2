package com.example.testthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
// 主线程A中调用子线程B的join(6000)方法，而子线程B的run()方法中会sleep(10000)
public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        Log.i(TAG, "onCreate: sleep 开始");
//        try {
//            Thread.sleep(15000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG, "onCreate: sleep 结束");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");

        MyThread myThread = new MyThread();
        Log.i(TAG, "onResume: myThread=" + myThread.getName() + " getState=" + myThread.getState());
        myThread.start();
        Log.i(TAG, "onResume: myThread=" + myThread.getName() + " getState=" + myThread.getState());
        try {
            myThread.join(6000);
            Log.i(TAG, "onResume: myThread=" + myThread.getName() + " getState=" + myThread.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onResume: yyyy");
        /*
2019-05-29 17:37:47.803 17204-17204/com.example.testthread I/Main2Activity: onResume:
2019-05-29 17:37:47.808 17204-17223/com.example.testthread I/Main2Activity MyThread: run: sleep 开始
2019-05-29 17:37:53.805 17204-17204/com.example.testthread I/Main2Activity: onResume: yyyy
2019-05-29 17:38:02.810 17204-17223/com.example.testthread I/Main2Activity MyThread: run: sleep 结束
        */
        /*
2019-05-29 17:39:56.334 17204-17204/com.example.testthread I/Main2Activity: onResume:
2019-05-29 17:39:56.336 17204-17204/com.example.testthread I/Main2Activity: onResume: yyyy
2019-05-29 17:39:56.377 17204-17275/com.example.testthread I/Main2Activity MyThread: run: sleep 开始
         */

    }
}

class MyThread extends Thread{
    private static final String TAG = "Main2Activity MyThread";
    @Override
    public void run() {
        super.run();
        Log.i(TAG, "run: sleep 开始");
        try {
            Log.i(TAG, "run: getState=" + Thread.currentThread().getState());
            Thread.sleep(15000);
            Log.i(TAG, "run: getState=" + Thread.currentThread().getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run: sleep 结束");
    }
}
