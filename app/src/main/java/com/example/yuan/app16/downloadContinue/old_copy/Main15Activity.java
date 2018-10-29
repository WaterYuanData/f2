package com.example.yuan.app16.downloadContinue.old_copy;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuan.app16.R;
import com.example.yuan.app16.downloadContinue.new_my.Main151Activity;


/**
 * OKHttp实现大文件的断点续传
 * http://blog.csdn.net/program_developer/article/details/56968821
 */
public class Main15Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main15Activity";
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            Toast.makeText(getApplicationContext(), "服务已连接", Toast.LENGTH_SHORT).show();
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main15);
        Button start = (Button) findViewById(R.id.start);
        Button pause = (Button) findViewById(R.id.pause);
        Button cancel = (Button) findViewById(R.id.cancel);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        Log.d(TAG, "onCreate: 1");
        bindService(intent, connection, BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: 2");

        // 查询并申请权限
        if (ContextCompat.checkSelfPermission(Main15Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main15Activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // 断点续传法二
        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main15Activity.this, Main151Activity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (downloadBinder == null) {
            Toast.makeText(getApplicationContext(), "服务未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.start:
//                String url = "https://pan.baidu.com/s/1smQzLsL/";非直接下载页,需要点击
//                String url = "files.cnblogs.com/allin/allin.dev.zip";72.9k
                //可以
                String url = "https://github.com/googlesamples/android-testing/archive/master.zip";//7.6M
//                String url = "https://github.com/Microstrong0305/OKHttp_DownloadFile/archive/master.zip";//50.4k
                downloadBinder.startDownload(url);
                break;
            case R.id.pause:
                downloadBinder.pauseDownload();
                break;
            case R.id.cancel:
                downloadBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
