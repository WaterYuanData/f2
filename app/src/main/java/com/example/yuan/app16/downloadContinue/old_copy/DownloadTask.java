package com.example.yuan.app16.downloadContinue.old_copy;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yuan on 18-1-23.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final String TAG = "DownloadTask";
    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSED = 2;
    private static final int TYPE_CANCLED = 3;
    private DownloadListener listener;
    private boolean isCancled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadedLength = 0;//已下载文件的长度
            String downloadUrl = params[0];
            Log.d(TAG, "doInBackground: downloadUrl=" + downloadUrl);
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            Log.d(TAG, "doInBackground: directory=" + directory);
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();//
                Log.d(TAG, "doInBackground: 断点续传 downloadedLength=" + downloadedLength);
            }
            long contentLength = getContentLength(downloadUrl);//文件总长度
            Log.d(TAG, "doInBackground: 文件总长 contentLength=" + contentLength);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点续传,告诉服务器从何处开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
//                    .header("RANGE", "bytes=" + downloadedLength + "-")
                    //无效可能是所下载的网站的服务器不支持断点续传
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();//IOException
            if (response != null) {

                String header = response.header("Content-Encoding");
                Log.d(TAG, "doInBackground: Content-Encoding=" + header);
                header = response.header("Content-Language");
                Log.d(TAG, "doInBackground: Content-Language=" + header);
                header = response.header("Content-Length");//7945075
                Log.d(TAG, "doInBackground: Content-Length=" + header);
                header = response.header("Accept-Ranges");//null
                Log.d(TAG, "doInBackground: Accept-Ranges=" + header);
                header = response.header("Accept-Range");//null
                Log.d(TAG, "doInBackground: Accept-Range=" + header);
                header = response.header("RANGE");//null
                Log.d(TAG, "doInBackground: RANGE=" + header);
                header = response.header("Accept-Type");
                Log.d(TAG, "doInBackground: Accept-Type=" + header);

                Headers headers = response.headers();
                headers.toString();
                Log.d(TAG, "doInBackground: headers.toString()="+headers.toString());


                is = response.body().byteStream();
                Log.d(TAG, "doInBackground: is.toString()=" + is.toString());
                savedFile = new RandomAccessFile(file, "rw");
//                因为断点续传功能失效所以不用跳过,即注释跳过
                savedFile.seek(downloadedLength);//跳过已下载的
                Log.d(TAG, "doInBackground: 注释seek");
                byte[] b = new byte[2048];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCancled) {
                        response.body().close();//
                        return TYPE_CANCLED;
                    } else if (isPaused) {
                        response.body().close();//
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);//
                        Log.d(TAG, "doInBackground: progress=" + progress);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCancled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCLED:
                listener.onCanceld();
            default:
                break;
        }
    }

    public void pauseDownload() {
        Log.d(TAG, "pauseDownload: ");
        isPaused = true;
    }

    public void cancelDownload() {
        isCancled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
//            response.close();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
