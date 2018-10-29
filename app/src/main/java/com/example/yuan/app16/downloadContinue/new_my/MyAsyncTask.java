package com.example.yuan.app16.downloadContinue.new_my;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.yuan.app16.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yuan on 18-3-5.
 * AsyncTask定义了三种泛型类型 Params，Progress和Result。
 * 参数一 Params 启动任务执行的输入参数，比如HTTP请求的URL。
 * 参数二 Progress 后台任务执行的百分比。
 * 参数三 Result 后台执行任务最终返回的结果，比如String。控制doInBackground()的返回值类型及onPostExecute()的参数类型
 */

public class MyAsyncTask extends AsyncTask<String, Integer, Integer> implements MyInterface {

    private static final String TAG = "MyAsyncTask";
    Context context;
    Handler handler;//AsyncTask借助Handler与Activity通信
    boolean isPaused = false;
    static boolean isPaused2 = true;
    boolean isCanceled = false;
    File file;
    public static final int SUCESS = 0;
    public static final int PAUSE = 1;
    public static final int CANCEL = 2;
    public static final int FAIL = 3;


    public MyAsyncTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void pauseDownload() {
        Log.d(TAG, "暂停: ");
        isPaused = true;
    }

    @Override
    public void pause2Download() {
        isPaused2 = !isPaused2;
        if (isPaused2) {
            Log.d(TAG, "人为暂停: ");
            sendMessage(Main151Activity.TOAST, "人为暂停");
        } else {
            Log.d(TAG, "人为暂停 已取消: ");
            sendMessage(Main151Activity.TOAST, "人为暂停 已取消");
        }
    }

    @Override
    public void cancelDownload() {
        Log.d(TAG, "取消并删除: ");
        isCanceled = true;
        // 重置进度条
        Message message = new Message();
        message.what = Main151Activity.RESET_PROGRESS_BAR;
        message.arg1 = 0;
        handler.sendMessage(message);
        // 删除文件
        if (file != null && file.exists()) {
            file.delete();
            sendMessage(Main151Activity.TOAST, "已删除");
        }
    }

    public void sendMessage(int what, String msg) {
        Message message = new Message();
        message.what = what;
        message.obj = msg;
        handler.sendMessage(message);
    }

    /**
     * 此方法在主线程执行，用于显示任务执行的进度。
     * 参数由publishProgress()方法传递过来
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        int type = values[1];
        Message message = new Message();
        if (type == 1) {
            if (progress == PAUSE) {
            }
            if (progress == CANCEL) {
                message.what = Main151Activity.RESET_PROGRESS_BAR;
                message.arg1 = 0;
                handler.sendMessage(message);
            }
            if (progress == FAIL) {
            }
        } else {
            message.what = Main151Activity.UPDATE_PROGRESS_BAR;
            message.arg1 = progress;
            handler.sendMessage(message);
        }
    }

    /**
     * 参数为doInBackground()的返回值,更新UI,在主线程执行
     */
    @Override
    protected void onPostExecute(Integer s) {
        switch (s) {
            case SUCESS:
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show();
                break;
            case PAUSE:
                Toast.makeText(context, "暂停", Toast.LENGTH_SHORT).show();
                break;
            case CANCEL:
                Toast.makeText(context, "取消并删除", Toast.LENGTH_SHORT).show();
                break;
            case FAIL:
                Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    /**
     * 在后台执行的比较耗时的操作,注意这里不能直接操作UI
     * 参数为execute()方法传递过来的参数
     */
    @Override
    protected Integer doInBackground(String... params) {
//        isPaused = false;
//        isCanceled = false;
        // 参数
        String downloadUrl = params[0];
        // 路径包含文件名时 不包含的情况呢???
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        // 路径=/storage/emulated/0    文件名=/master.zip
//        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        String directory = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d(TAG, "doInBackground: 路径=" + directory + "    文件名=" + fileName);

        InputStream inputStream = null;
        RandomAccessFile savedFile = null;
        long totalLength = Utils.getContentLength(downloadUrl);
        long downloadedLength = 0;

        file = new File(directory + fileName);
        if (file.exists()) {
            downloadedLength = file.length();
//            Log.d(TAG, "doInBackground: 已下载=" + downloadedLength);
        }
        if (downloadedLength >= totalLength) {
            return SUCESS;
        }
        // okhttp的使用
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //断点续传,告诉服务器从何处开始下载
                .addHeader("RANGE", "bytes=" + downloadedLength + "-" + totalLength)
                .url(downloadUrl)
                .build();
        Log.d(TAG, "doInBackground: 请求=" + request.headers().toString());

        Call call = null;
        try {
            call = client.newCall(request);
            Response response = call.execute();
            if (response != null) {

                //==========================
                //如何从Headers中获取filename ???
                Headers headers = response.headers();
                Log.d(TAG, "doInBackground: 响应  headers.toString()=" + headers.toString());//有时包含路径不含有的文件名

                String disposition = response.header("Content-disposition");
                Log.d("URL预处理", "getContentLength: disposition="+disposition);

//                响应headers中content-length与response.body().contentLength()的区别???
                //==========================

//                long totalLength = response.body().contentLength();//文件总长
//                Log.d(TAG, "doInBackground: 总长=" + totalLength);
                inputStream = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);//跳过已下载的

                byte[] b = new byte[2048];
                int total = 0;
//                int thisTimeTotal = 0;
//                int total = ((int) downloadedLength);
                int len;
                while ((len = inputStream.read(b)) != -1) {
                    if (isPaused) {
                        publishProgress(PAUSE, 1);
                        Log.d(TAG, "doInBackground: 暂停时已写=" + (downloadedLength + total));
                        return PAUSE;
                    }
                    if (isCanceled) {
                        Log.d(TAG, "doInBackground: 取消并删除");
                        if (file.exists()) {
                            file.delete();
                        }
                        publishProgress(CANCEL, 1);
                        return CANCEL;
                    }
                    total += len;
                    savedFile.write(b, 0, len);
                    Log.d(TAG, "doInBackground: " + len);
                    int progress = (int) ((downloadedLength + total) * 100 / totalLength);
                    // 进度条显示
//                    Log.d(TAG, "doInBackground: " + progress);
                    publishProgress(progress, 0);

                    // 人为暂停 测试用 因为在savedFile.write(b, 0, len)语句后,所以每次还是会写一些
                    if (isPaused2) {
                        if (progress > 60) {
                            Log.d(TAG, "doInBackground: 使能人为暂停");
                            isPaused = true;
                        }
                    }
                }
                Log.d(TAG, "doInBackground: 本次写了=" + total);
            }
            return SUCESS;
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(FAIL, 1);
            return FAIL;
        } finally {
            Log.d(TAG, "doInBackground: return后仍会执行");
            try {
                if (call != null) {
                    call.cancel();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
