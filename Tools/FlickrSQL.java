package com.example.e560.m1126a.ToolsClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by E560 on 2016/11/26.
 */

public class FlickrSQL {
    /**
     *
     */
    private Context context;
    private final String TAG = "FlickrSQL";
    private final String JSON_PATH = "http://cdefgab.web.fc2.com/idolsname.json";
    private final String JLSON_FILTER = "http://cdefgab.web.fc2.com/filter.json";
    private final String DB_NAME = "IdolAPP.db";
    private final int DB_VERSION = 1;
    private mSQL mSQL_db;

    /**
     * @param context
     */
    public FlickrSQL(Context context) {
        this.context = context;
        mSQL_db = new mSQL(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 檢查Name是否存在。count = 1 存在，0 不存在。
     *
     * @param name
     * @return
     * @throws Exception
     */

    public int check_name(String name) throws Exception {
        int count = 0;
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String sql_cmd = "select name from toplist where name = ?";
        Cursor cursor = db.rawQuery(sql_cmd, new String[]{name});
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 返回Name個數。
     *
     * @return
     */
    public int db_count() {
        int count = 0;
        SQLiteDatabase db = mSQL_db.getReadableDatabase();
        String sql_cmd = "select count(*) from toplist";
        Cursor cursor = db.rawQuery(sql_cmd, null);
        while (cursor.moveToNext()) {
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count(*)")));
        }
        cursor.close();
        Log.i(TAG, "db_idols_count: " + count);
        db.close();
        return count;
    }

    /**
     * 返回所有Name條目。
     *
     * @return
     */
    public Cursor SelectallIdol() {
        Cursor cursor = null;
        SQLiteDatabase db = mSQL_db.getReadableDatabase();
//        String sql_cmd = "select * from toplist WHERE active NOT IN(?) ORDER BY up_date DESC ";
        String sql_cmd = "select * from toplist WHERE active NOT IN(?)";
        cursor = db.rawQuery(sql_cmd, new String[]{"false"});
        return cursor;
    }

    /**
     * 從JSON_PATH 更新客戶端數據庫。
     * String JSON_PATH = "http://cdefgab.web.fc2.com/idolsname.json";
     *
     * @throws Exception
     */

    public void Synchronize_toplist() throws Exception {
        int insertcount = 0;
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String json = FlickrHttp.GetResultstring(JSON_PATH);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("idolnames");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            if (check_name(jsonObject1.getString("name")) == 1) {
                update_active(jsonObject1.getString("name"), jsonObject1.getString("active"));
            } else {
                insertcount++;
                insert(jsonObject1.getString("name"), jsonObject1.getString("img"));
                update_active(jsonObject1.getString("name"), jsonObject1.getString("active"));
            }
        }
        Log.i(TAG, "sysdb: new Insert " + insertcount);
        db.close();
    }

    /**
     * 更新過濾器 filter_ID。
     */

    public void Synchronize_filter() throws Exception {
        String json = FlickrHttp.GetResultstring(JLSON_FILTER);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (!jsonArray.getString(i).isEmpty()) {
                if (chk_filterid(jsonArray.getString(i)) < 1) {
                    addidto_filter(jsonArray.getString(i));
                }
            }
        }
    }

    /**
     * 檢查 filter_ID 是否存在。
     * =1 存在
     * @param id
     * @return
     */
    public int chk_filterid(String id) {
        int chk = 0;
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String sql_cmd = "select id from filter_id where id = ?";
        Cursor cursor = db.rawQuery(sql_cmd, new String[]{id});
        chk = cursor.getCount();
        db.close();
        cursor.close();
        return chk;
    }

    /**
     * 添加FilterID
     *
     * @param id
     */
    public void addidto_filter(String id) {
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String sql_cmd = "INSERT INTO filter_id(id)values(?)";
        db.execSQL(sql_cmd, new String[]{id});
        db.close();
    }

    /**
     * @param name
     * @param img_url
     */
    public void insert(String name, String img_url) {
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String sql_cmd = "insert into toplist(name,img_url) values (? ,?)";
        db.execSQL(sql_cmd, new String[]{name, img_url});
        db.close();
    }

    /**
     * @param name
     * @param img_url
     */
    public void update_imgurl(String name, String img_url) {
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String sql_cmd = "update toplist set img_url = ? where name = ?";

        db.execSQL(sql_cmd, new String[]{img_url, name});
        db.close();
    }

    /**
     * @param name
     * @param active
     */
    public void update_active(String name, String active) {
        SQLiteDatabase db = mSQL_db.getWritableDatabase();
        String sql_cmd = "update toplist set active = ? where name = ?";
        db.execSQL(sql_cmd, new String[]{active, name});
        db.close();
    }


    public class mSQL extends SQLiteOpenHelper {

        /**
         * mSQL_db = new mSQL(context, db_pak_name, null, db_version);
         * https://farm8.staticflickr.com/7566/15949681296_b6a869bdfd_q.jpg
         * https://farm8.staticflickr.com/7569/15975428145_c85aae84bd_s.jpg
         */

        /**
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        public mSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String topidoltable = "create table toplist (rowid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE NOT NULL,up_date TEXT default current_timestamp,img_url TEXT NOT NULL, accc INTEGER DEFAULT 0, Active TEXT DEFAULT 'ture')";
            String filter_id = "create table filter_id(rowid INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT UNIQUE NOT NULL)";
            String filter_owner = "create table filter_owner (rowid INTEGER PRIMARY KEY AUTOINCREMENT,owner TEXT UNIQUE NOT NULL)";
            String setimgurl = "update toplist set img_url = ?";
            db.execSQL(topidoltable);
            db.execSQL(filter_id);
            db.execSQL(filter_owner);
            db.execSQL(setimgurl, new String[]{"https://farm8.staticflickr.com/7569/15975428145_c85aae84bd_s.jpg"});
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
