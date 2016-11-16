package com.example.java.m1115a.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by java on 2016/11/15.
 */

public class GetJson extends HttpTools {
    public GetJson(String url_path) {
        super(url_path);
    }

    public HashMap<Integer, HashMap> jsonMap() throws Exception {
        String jsonString = getjsonString();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject jsonObject1 = jsonObject.getJSONObject("photos");
        JSONArray jsonArray = jsonObject1.getJSONArray("photo");
        HashMap<Integer, HashMap> jMap = new HashMap<Integer, HashMap>();

        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            Iterator<String> iterator = jsonObject2.keys();
            while (iterator.hasNext()) {
                String temp = iterator.next();
                item.put(temp, jsonObject2.getString(temp));
            }
            jMap.put(i, item);
        }
        return jMap;
    }

    public static String img_url(HashMap<String, String> item_hashMap) throws Exception {
        /*
        *
        * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
        * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{o-secret}_o.(jpg|gif|png)
        *
        * String farmid = item_hashMap.get("farm");
        * String Serverid = item_hashMap.get("server");
        * String id = item_hashMap.get("id");
        * String secret = item_hashMap.get("secret");
        * String img_url = "https://farm" + farmid + ".staticflickr.com/" + Serverid + "/" + id + "_" + secret + ".jpg";
        * System.out.println(img_url);
        */

        HashMap<String, String> temp_map = item_hashMap;
        return "https://farm" + item_hashMap.get("farm") + ".staticflickr.com/" + item_hashMap.get("server") + "/" + item_hashMap.get("id") + "_" + item_hashMap.get("secret") + ".jpg";
    }

    public static Bitmap getBitmap(String img_url) throws Exception {
        URL url = new URL(img_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        Bitmap bitmap = null;
        if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                byte[] buf = new byte[1024];
                int len = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while ((len = inputStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                    byteArrayOutputStream.flush();
                }
                bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            }
        }
        return bitmap;
    }
}
