package com.example.e560.m1126a.ToolsClass;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by E560 on 2016/11/26.
 */

public class FlickrHttp {

    public static InputStream GetinputStream(String url_path) throws Exception {
        URL url = new URL(url_path);
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(5 * 1000);
        httpURLConnection.setReadTimeout(5 * 1000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        if (httpURLConnection.getResponseCode() == 200) {
            inputStream = httpURLConnection.getInputStream();
        }
        return inputStream;
    }

    public static String GetResultstring(String url_path) throws Exception {
        InputStream inputStream = GetinputStream(url_path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }
        String GetResultstring = byteArrayOutputStream.toString();
        inputStream.close();
        byteArrayOutputStream.close();
        return GetResultstring;
    }

    public static Bitmap GetBitmap(String img_path) throws Exception {
        InputStream inputStream = GetinputStream(img_path);
        return BitmapFactory.decodeStream(inputStream);
    }
}
