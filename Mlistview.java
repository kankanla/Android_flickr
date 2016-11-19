package com.example.e560.m1117a;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.e560.m1117a.Tools.GetJson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by E560 on 2016/11/19.
 */

public class Mlistview extends AppCompatActivity {
    private GridView gridView;
    private final String imgsize = "w";
    private final String test_url = "http://cdefgab.web.fc2.com/song.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mlistview);
        new getlistmap().execute(test_url);
    }

    protected class getlistmap extends AsyncTask<String, Integer, List<String>> {
        /*
        * 返回所以縮圖，最長邊為 100的URL;
         */
        @Override
        protected List<String> doInBackground(String... params) {
            GetJson getJson = new GetJson(params[0]);
            List<String> listurl = new ArrayList<String>();
            try {
                HashMap<Integer, HashMap> items_map = getJson.jsonMap();
                for (int i = 0; i < items_map.size(); i++) {
                    listurl.add(GetJson.img_url(items_map.get(i), imgsize));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listurl;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            gridView = (GridView) findViewById(R.id.mlistview);
            myAdapter my = new myAdapter(strings);
            my.notifyDataSetChanged();
            gridView.setAdapter(my);
        }
    }

    protected class myAdapter extends BaseAdapter {
        private List<String> list;

        public myAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            View v = new View(getApplication());
            if (convertView == null) {
                v = LayoutInflater.from(getApplication()).inflate(R.layout.ladapter, null);
            } else {
                v = convertView;
            }
            ImageView iv = (ImageView) v.findViewById(R.id.imageView2);
            iv.setImageBitmap(bitmap);
            GG gg = new GG(iv);
            gg.execute(list.get(position));
            return v;
        }

        public class GG extends AsyncTask<String, Integer, Bitmap> {
            private Bitmap bitmap;
            private ImageView iv;

            public GG(ImageView iv) {
                this.iv = iv;
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
//                     https://farm8.staticflickr.com/7385/26957386592_7f0aa84802_s.jpg
                    String[] sa = params[0].split("/");
                    File file = new File(getApplication().getCacheDir(), sa[sa.length - 1]);
                    if (file.isFile()) {
                        FileInputStream fin = new FileInputStream(file);
                        bitmap = BitmapFactory.decodeStream(fin);
                        fin.close();
                    } else {
                        bitmap = GetJson.getBitmap(params[0]);
                        FileOutputStream fon = new FileOutputStream(file);
                        ByteArrayOutputStream bas = GetJson.getBAOS(params[0]);
                        fon.write(bas.toByteArray());
                        fon.flush();
                        fon.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                iv.setImageBitmap(bitmap);
            }
        }
    }
}
