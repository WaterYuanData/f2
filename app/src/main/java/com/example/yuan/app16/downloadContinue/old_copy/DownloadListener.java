package com.example.yuan.app16.downloadContinue.old_copy;

/**
 * Created by yuan on 18-1-23.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceld();
}
