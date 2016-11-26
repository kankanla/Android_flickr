package com.example.e560.m1126a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e560.m1126a.ToolsClass.test_FlickrSQL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test_FlickrSQL();
    }

    private void test_FlickrSQL() {
        Button bt = (Button) findViewById(R.id.button1);
        bt.setText("test_FlickrSQL");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,test_FlickrSQL.class);
                intent.putExtra("name","Button1");
                startActivity(intent);
            }
        });
    }


}
