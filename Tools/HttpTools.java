package com.example.java.m1115a.Tools;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by java on 2016/11/15.
 */

public class HttpTools {
    private String url_path;
    private Context context;

    public HttpTools(String url_path) {
        this.url_path = url_path;
    }

    public HttpTools(String url_path, Context context) {
        this.url_path = url_path;
        this.context = context;
    }

    public InputStream getnetInputStream() throws Exception {
        URL url = new URL(url_path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");

        InputStream inputStream = null;
        if (httpURLConnection.getResponseCode() == 200) {
            inputStream = httpURLConnection.getInputStream();
        }
        return inputStream;
    }

    public ByteArrayOutputStream getbyteArrayOutputStream() throws Exception {
        InputStream inputStream = getnetInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buff)) != -1) {
            byteArrayOutputStream.write(buff, 0, len);
        }
        inputStream.close();
        return byteArrayOutputStream;
    }

    public String getjsonString() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = getbyteArrayOutputStream();
        return byteArrayOutputStream.toString();
    }
}
