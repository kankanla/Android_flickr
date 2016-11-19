package com.example.e560.m1117a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e560.m1117a.Tools.GetJson;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            private int cont;

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Mlistview.class);
                startActivity(intent);
//                new img().execute("http://cdefgab.web.fc2.com/song.json", String.valueOf(cont));
//                cont++;
            }
        });
    }

    public class img extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            GetJson json = new GetJson(params[0]);
            int cont = Integer.parseInt(params[1]);
            Bitmap bitmap = null;
            try {
                bitmap = GetJson.getBitmap(GetJson.img_url(json.jsonMap().get(cont),"q"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) {
                Toast.makeText(MainActivity.this, "noImg", Toast.LENGTH_SHORT).show();
            }else {
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(bitmap);
            }
        }
    }
}

