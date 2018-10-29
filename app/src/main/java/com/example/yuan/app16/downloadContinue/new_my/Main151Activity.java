package com.example.yuan.app16.downloadContinue.new_my;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yuan.app16.R;
import com.example.yuan.app16.util.Utils;

/**
 * Activity通过Binder调用Service的方法
 * Service再调用AsyncTask的相应方法
 * AsyncTask的进度如何显示在Activity里,通过Activity的Handler
 */
public class Main151Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main151Activity";
    String downloadUrl = "http://easynotice.cloudminds.com:81/ens/android/apk/download?id=28";
//    doInBackground: headers.toString()=Server: Apache-Coyote/1.1
//    Content-disposition: attachment; filename=easyNotice-release.apk
//    Content-Type: application/octet-stream;charset=UTF-8
//    Content-Length: 10199273
//    Date: Tue, 06 Mar 2018 10:27:59 GMT
//    OkHttp-Sent-Millis: 1520332079694
//    OkHttp-Received-Millis: 1520332079778

//    String downloadUrl = "https://github.com/googlesamples/android-testing/archive/master.zip";
//    doInBackground: headers.toString()=Transfer-Encoding: chunked
//    Access-Control-Allow-Origin: https://render.githubusercontent.com
//    Content-Security-Policy: default-src 'none'; style-src 'unsafe-inline'; sandbox
//    Strict-Transport-Security: max-age=31536000
//    Vary: Authorization,Accept-Encoding
//    X-Content-Type-Options: nosniff
//    X-Frame-Options: deny
//    X-XSS-Protection: 1; mode=block
//    ETag: "92fbbe80fc23ff05cf565f7eaa2d3e52fcb1b880"
//    Content-Type: application/zip
//    Content-Disposition: attachment; filename=android-testing-master.zip
//    X-Geo-Block-List:
//    Date: Tue, 06 Mar 2018 10:24:05 GMT
//    X-GitHub-Request-Id: 9BAA:6001:207DAD:3BA7B0:5A9E6C44
//    OkHttp-Sent-Millis: 1520331845347
//    OkHttp-Received-Millis: 1520331846545

//    String downloadUrl = "https://files.cnblogs.com/allin/allin.dev.zip";//72.9k
//    doInBackground: headers.toString()=server: Tengine
//    date: Tue, 06 Mar 2018 10:18:05 GMT
//    content-type: application/x-zip-compressed
//    content-length: 362
//    cache-control: max-age=7776000
//    last-modified: Tue, 18 Jan 2011 14:24:13 GMT
//    accept-ranges: bytes
//    etag: "80646611bb7cb1:0"
//    x-powered-by: ASP.NET
//    x-ua-compatible: IE=edge
//    content-range: bytes */74674
//    OkHttp-Sent-Millis: 1520331485304
//    OkHttp-Received-Millis: 1520331485543


    ProgressBar progressBar;// 进度条,有进度,但百分比 待优化
    Button startButton;
    Button pauseButton;
    Button pause2Button;
    Button cancelButton;
    public static final int RESET_PROGRESS_BAR = 0;
    public static final int UPDATE_PROGRESS_BAR = 1;
    public static final int TOAST = 2;
    Context applicationContext;

    // 用Handle实现AsyncTask与Activity的通信
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS_BAR:
                    progressBar.setProgress(msg.arg1);
                    break;
                case RESET_PROGRESS_BAR:
                    progressBar.setProgress(msg.arg1);
                    break;
                case TOAST:
                    Toast.makeText(applicationContext, (CharSequence) msg.obj,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    MyDownloadService.MyBinder myBinder;// Binder实现Activity与Service的通信

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyDownloadService.MyBinder) service;
            Toast.makeText(getApplicationContext(), "151服务已连接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
            Log.d(TAG, "onServiceDisconnected: 意外断开连接");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main151);
        applicationContext = getApplicationContext();

        initView();

        MyDownloadService.setHandler(handler);//Activity将Handler传递给Service

        // 绑定服务
        Intent intent = new Intent(this, MyDownloadService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

    }

    private void initView() {
        // 绑定视图
        progressBar = ((ProgressBar) findViewById(R.id.progressbar_download));
        startButton = ((Button) findViewById(R.id.btn_start));
        pauseButton = ((Button) findViewById(R.id.btn_pause));
        pause2Button = ((Button) findViewById(R.id.btn_pause2));
        cancelButton = ((Button) findViewById(R.id.btn_cancel));
        // 设置监听
        progressBar.setMax(100);
        startButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        pause2Button.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (myBinder == null) {
            Toast.makeText(getApplicationContext(), "151服务未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.btn_start:
                myBinder.startDownload(downloadUrl);
                break;
            case R.id.btn_pause:
                myBinder.pauseDownload();
                break;
            case R.id.btn_pause2:
                myBinder.pause2Download();
                break;
            case R.id.btn_cancel:
                // 如何得到当前时间
                long elapsedRealtime = SystemClock.elapsedRealtime();
                Log.d(TAG, "elapsedRealtime: " + Utils.formatTimeYMDHS(elapsedRealtime));
                long uptimeMillis = SystemClock.uptimeMillis();
                Log.d(TAG, "uptimeMillis: " + Utils.formatTimeYMDHS(uptimeMillis));
                long currentTimeMillis = System.currentTimeMillis();
                Log.d(TAG, "currentTimeMillis: " + Utils.formatTimeYMDHS(currentTimeMillis));
//                long day = 24 * 60 * 60;
//                SystemClock.setCurrentTimeMillis(currentTimeMillis + day);
//                currentTimeMillis = System.currentTimeMillis();
//                Log.d(TAG, "onClick: " + Utils.formatTimeYMDHS(currentTimeMillis));
                // 如何得到当前时间
                myBinder.cancelDownload();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 断开连接");
        unbindService(conn);
    }
}
