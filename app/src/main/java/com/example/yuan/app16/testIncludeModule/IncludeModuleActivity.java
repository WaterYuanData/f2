package com.example.yuan.app16.testIncludeModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yuan.app16.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IncludeModuleActivity extends AppCompatActivity {
    private static final String TAG = "IncludeModuleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_include_module);

//        TextView text = (TextView) findViewById(R.id.tv_ip);

        final String invalid="1.2.3.4.";
        new Thread(){
            @Override
            public void run() {
                try {
                    InetAddress.getByName(invalid);
                    Log.d(TAG, "未能识别出 case fail");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.d(TAG, "甄别出无效ip, case通过");
                }
            }
        }.start();

    }

    public void testModuleApp(View view){
        startActivity(new Intent().setAction("android.intent.action.AndroidApp"));
    }

    public void testModuleLibrary(View view){
        startActivity(new Intent().setAction("android.intent.action.AndroidLibrary"));
    }
}
