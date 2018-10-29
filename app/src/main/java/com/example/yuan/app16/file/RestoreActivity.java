package com.example.yuan.app16.file;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.yuan.app16.R;

public class RestoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
    }

    public void onClick(View view){
        if(view == null){
            return;
        }
        view.setEnabled(false);
        Intent intent;
        if(view.getId() == R.id.btn_restore_data){
            intent = new Intent(this, RecoverActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.btn_restore_app){
            intent = new Intent(this, RecoverAppActivity.class);
            startActivity(intent);
        }
        view.setEnabled(true);
    }
}
