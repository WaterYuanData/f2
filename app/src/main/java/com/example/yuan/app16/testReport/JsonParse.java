package com.example.yuan.app16.testReport;

/**
 * Created by yuan on 17-12-20.
 */

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class JsonParse {
    /**
     * 解析Json数据
     *
     * @param urlPath
     * @return mlists
     * @throws Exception
     */

    public static List<Person> getListPerson(String urlPath) throws Exception {
        List<Person> mlists = new ArrayList<Person>();
        byte[] data = readParse(urlPath);
        JSONArray array = new JSONArray(new String(data));
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            String name = item.getString("name");
            String address = item.getString("address");
            int age = item.getInt("age");
            mlists.add(new Person(name, address, age));
        }
        return mlists;
    }

    /**
     * 从指定的url中获取字节数组
     *
     * @param urlPath
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();

        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);

        }
        inStream.close();
        return outStream.toByteArray();

    }
}

