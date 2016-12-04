package com.example.e560.m1126a.ToolsClass;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        httpURLConnection.setConnectTimeout(10 * 1000);
        httpURLConnection.setReadTimeout(10 * 1000);
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

    public static Bitmap BigBitmap(String img_path) throws Exception {
        return BitmapFactory.decodeStream(GetinputStream(img_path));
    }

    public static Bitmap GetBitmap(Application application, String img_path) throws Exception {
        Bitmap bitmap = null;
        String[] path_split = img_path.split("/");
        File file = new File(application.getCacheDir(), path_split[path_split.length - 1]);

        if (file.isFile()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } else {
            bitmap = BitmapFactory.decodeStream(GetinputStream(img_path));
            InputStream inputStream = GetinputStream(img_path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
        }
        return bitmap;
    }
}
