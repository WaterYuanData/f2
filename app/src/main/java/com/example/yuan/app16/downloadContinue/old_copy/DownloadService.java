package com.example.yuan.app16.downloadContinue.old_copy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.yuan.app16.R;

import java.io.File;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("下载ing",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载完成时关闭前台服务通知,并新建一个通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载成功",-1));
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "下载成功: listener");
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "下载失败: listener");
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "已停止", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "已停止: listener");
        }

        @Override
        public void onCanceld() {
            downloadTask = null;
            stopForeground(true);
//            Toast.makeText(DownloadService.this, "已取消", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "已取消: listener");
        }
    };
    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class DownloadBinder extends Binder {
        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("开始下载",0));//
                Toast.makeText(DownloadService.this, "startDownload", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "startDownload: ");
            }else{
                Toast.makeText(DownloadService.this, "???", Toast.LENGTH_SHORT).show();
//                暂停或取消使doInBackground()有返回值故downloadTask会执行完变null
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                Log.d(TAG, "pauseDownload: ");
                downloadTask.pauseDownload();
            }else{
                Toast.makeText(DownloadService.this, "无效的暂停", Toast.LENGTH_SHORT).show();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                Log.d(TAG, "cancelDownload: ");
                Toast.makeText(DownloadService.this, "已取消", Toast.LENGTH_SHORT).show();
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    //取消下载时删除文件,且关闭Notification
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "已删除", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "cancelDownload: 且删除");
                }else{
                    Toast.makeText(DownloadService.this, "无效的消除", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, Main15Activity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%")
                    .setProgress(100, progress, false);
        }
        return builder.build();
    }
}
