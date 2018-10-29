package com.example.yuan.app16.path;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yuan.app16.R;

import java.io.File;


//https://www.jianshu.com/p/4affa241a71d
public class PathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        File dataDirectory = Environment.getDataDirectory();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }
}
