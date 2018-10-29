package com.example.yuan.app16.file;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import com.example.yuan.app16.R;

public class RecoverAppActivity extends ListActivity implements View.OnClickListener {

    String TAG = "恢复应用";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_recover_app);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }


}
