package com.example.testthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.atomic.AtomicLong;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private static final String TAG = "MainActivity";
    private static AtomicLong sAtomicLong = new AtomicLong(10000L);

    private void test() {

//这里开了1000个线程对sCount并发操作
        for (int i = 0; i < 1000; i++) {
            Log.e(TAG, "run: --" + i);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sAtomicLong.decrementAndGet();
                    Log.i(TAG, "run: " + sAtomicLong.toString());
                }
            }).start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sAtomicLong);
    }
}
