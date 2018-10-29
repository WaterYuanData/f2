package com.example.yuan.app16.Theme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yuan.app16.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

//参考第一行代码的12章节
public class ThemeActivity extends AppCompatActivity {

    @OnClick(R.id.bt1)
    public void NoActionBar(View view){
        startActivity(new Intent(this,NoActionBarActivity.class));
    }

    @OnClick(R.id.bt2)
    public void NoActionBar2(View view){
        startActivity(new Intent(this,NoActionBarActivity2.class));
    }

    @OnClick(R.id.bt3)
    public void Main19Activity(View view){
        startActivity(new Intent(this,Main19Activity.class));
    }

    @OnClick(R.id.bt4)
    public void FruitActivity(View view){
        startActivity(new Intent(this,FruitActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ViewUtils.inject(this);
    }
}
