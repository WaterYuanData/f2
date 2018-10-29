package com.example.yuan.app16.testReport;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yuan.app16.R;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main9Activity extends Activity implements View.OnClickListener {

    String TAG = "Main9Activity";

    EditText user;
    EditText password;
    Button login;
    Button reset;
    EditText inputCode;
    ImageView randomImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        init();
    }

    public void init() {
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        reset = (Button) findViewById(R.id.reset);
        inputCode = (EditText) findViewById(R.id.randomCode);
        randomImage = (ImageView) findViewById(R.id.randomImage);
        String st = "http://202.99.59.96/oa/loginAction.do?userName=luru&passWord=luru";
        //http://202.99.59.96/oa/login/image.jsp为验证码网址
        Log.d(TAG, "init: 随机数=" + Math.random());
        //原始加载
        String imgSrc = "http://202.99.59.96/oa/login/image.jsp?" + Math.random();
        //http://202.99.59.96/authImage也是验证码
        Log.d(TAG, "init: 日期=" + new Date());
        String imgSrc2 = "http://202.99.59.96/authImage?data=" + new Date();

        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            String user = this.user.getText().toString();
            String password = this.password.getText().toString();
            String inputCode = this.inputCode.getText().toString();
            Log.d(TAG, "onClick: " + user + " " + password + " " + password);

            new Thread() {
                @Override
                public void run() {
                    String httpUrl = "http://202.99.59.96/oa/loginAction.do?userName=luru&passWord=luru";
//                    httpUrl = "http://202.99.59.96";
                    httpUrl = "http://202.99.59.96/oa/login/image.jsp";
                    OkHttpClient client = new OkHttpClient();
                    client.newBuilder().connectTimeout(10000, TimeUnit.MILLISECONDS);
                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .build();
                    Call call = client.newCall(request);
                    //
//                    call.enqueue(new Callback() {
//
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            Log.d(TAG, "onFailure: ");
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            Log.d(TAG, "onResponse: ");
//                        }
//                    });
                    //java.lang.IllegalStateException: Already Executed
                    try {
                        Response response = call.execute();
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onClick: 结果=" + response);
                            String string = response.body().string();
                            Log.d(TAG, "onClick: 结果=" + string);
                            byte[] bytes = response.body().bytes();
                            Log.d(TAG, "onClick: 结果=" + bytes);
                        } else {
                            throw new IOException("Unexpected code " + response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }

        if (v.getId() == R.id.reset) {
            user.setText("");
            password.setText("");
        }
    }
}
