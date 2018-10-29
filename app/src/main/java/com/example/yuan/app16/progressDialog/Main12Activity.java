package com.example.yuan.app16.progressDialog;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yuan.app16.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main12Activity extends AppCompatActivity {

    private ActivityManager mActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);

        //实例化ActivityManager
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        ProgressDialog mydialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);//5
        mydialog.setMessage(getString(R.string.connecting));//
        mydialog.setMessage(String.format(getString(R.string.connecting)));
        mydialog.show();
        {
            ProgressDialog mydialog2 = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);//4
            mydialog2.setMessage("4");
            mydialog2.show();
        }
        {
            ProgressDialog mydialog2 = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);//3
            mydialog2.setMessage("3");
            mydialog2.show();
        }
        {
            ProgressDialog mydialog2 = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);//2
            mydialog2.setMessage("2");
            mydialog2.show();
        }
        {
            ProgressDialog mydialog2 = new ProgressDialog(this, AlertDialog.THEME_TRADITIONAL);//1
            mydialog2.setMessage("1");
            mydialog2.show();
        }
        {
            ProgressDialog mydialog2 = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);//0
            mydialog2.setMessage("0");//
            mydialog2.show();
        }
        {
            ProgressDialog mydialog2 = new ProgressDialog(this, ProgressDialog.STYLE_HORIZONTAL);//1
            mydialog2.setMessage("11");
            mydialog2.show();
        }


    }


}
