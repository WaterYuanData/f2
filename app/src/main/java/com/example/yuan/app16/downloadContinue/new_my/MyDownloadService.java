package com.example.yuan.app16.downloadContinue.new_my;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MyDownloadService extends Service {

    private static final String TAG = "MyDownloadService";
    MyAsyncTask task;
    static Handler handler;//因为在静态方法里被调用

    public static void setHandler(Handler hd) {
        handler = hd;
    }

    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class MyBinder extends Binder implements MyInterface {
        public void startDownload(String downloadUrl) {
            task = new MyAsyncTask(getApplicationContext(), handler);//Service将Handler传递给AsyncTask
            task.execute(downloadUrl);
        }

        // Service与AsyncTask的通信用Interface进行约束

        @Override
        public void pauseDownload() {
            if (task != null) {
                task.pauseDownload();
            } else {
                Log.d(TAG, "pauseDownload: 尚未实例AsyncTask");
            }
        }

        @Override
        public void pause2Download() {
            if (task != null) {
                task.pause2Download();
            } else {
                Log.d(TAG, "pause2Download: 尚未实例AsyncTask");
            }
        }

        @Override
        public void cancelDownload() {
            if (task != null) {
                task.cancelDownload();
            } else {
                Log.d(TAG, "cancelDownload: 尚未实例AsyncTask");
            }
        }

    }
}
