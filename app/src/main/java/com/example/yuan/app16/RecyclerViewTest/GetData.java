package com.example.yuan.app16.RecyclerViewTest;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import static com.example.yuan.app16.RecyclerViewTest.Main7Activity.mAdapter;
//import static com.example.yuan.app16.RecyclerViewTest.Main7Activity.meizis;

/**
 * Created by yuan on 17-12-19.
 */

class GetData extends AsyncTask<String, Integer, String> {
    String TAG = "AsyncTask";
    Main7Activity m7;

    public GetData(Main7Activity m7) {
        this.m7 = m7;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: 执行前的准备工作");
        m7.swipeRefreshLayout.setRefreshing(true);
    }

    //执行execute方法传递的参数
    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: 后台联网查询 params=" + params[0]);
        return MyOkhttp.get(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute: 数据处理");
//        Log.d(TAG, "onPostExecute: 数据处理 result=" + result);
        if (!TextUtils.isEmpty(result)) {
            JSONObject jsonObject;
            Gson gson = new Gson();
            String jsonData = null;
            try {
                jsonObject = new JSONObject(result);
                jsonData = jsonObject.getString("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (m7.meizis == null || m7.meizis.size() == 0) {
                m7.meizis = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                }.getType());
            } else {
                List<Meizi> more = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                }.getType());
                m7.meizis.addAll(more);
            }
            if (m7.mAdapter == null) {
                Log.d(TAG, "onPostExecute: 初始化适配器");
                m7.mAdapter = new MyRecyclerViewAdapter(m7);
                m7.recyclerview.setAdapter(m7.mAdapter);
                m7.itemTouchHelper.attachToRecyclerView(m7.recyclerview);
            } else {
                Log.d(TAG, "onPostExecute: 适配器已存在,更新适配器数据源");
                Log.d(TAG, "onPostExecute: 数据量=" + m7.meizis.size());
                m7.mAdapter.notifyDataSetChanged();//meizis如果设为静态,所以notifyDataSetChanged()检测不到从Main7Activiy退出后再进的变化
//                mAdapter = new MyRecyclerViewAdapter();//会导致从数据扩大到后,又从头开始加载
//                recyclerview.setAdapter(mAdapter);
            }
        }
        m7.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d(TAG, "onCancelled: 取消");
    }
}
