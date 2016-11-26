package com.example.e560.m1126a.ToolsClass;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.e560.m1126a.R;

/**
 * Created by E560 on 2016/11/26.
 */

public class test_FlickrSQL extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        new Th().start();
    }

    class Th extends Thread {
        @Override
        public void run() {
            try {
                FlickrSQL flickrSQL = new FlickrSQL(getApplicationContext());
//                flickrSQL.sysdb();
//                flickrSQL.db_count();

                Cursor cursor = flickrSQL.Selectall();

                while (cursor.moveToNext()) {
                    String rowid = cursor.getString(cursor.getColumnIndex("rowid"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String up_date = cursor.getString(cursor.getColumnIndex("up_date"));
                    String img_url = cursor.getString(cursor.getColumnIndex("img_url"));
                    String accc = cursor.getString(cursor.getColumnIndex("accc"));
                    String Active = cursor.getString(cursor.getColumnIndex("Active"));

                    System.out.println(rowid);
                    System.out.println(name);
                    System.out.println(up_date);
                    System.out.println(img_url);
                    System.out.println(accc);
                    System.out.println(Active);
                }
//                cursor.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



