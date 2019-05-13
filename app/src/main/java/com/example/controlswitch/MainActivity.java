package com.example.controlswitch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.controlswitch.Utils.WiFiDBHelper;

public class MainActivity extends AppCompatActivity {

    private Button buttonConnect;
    private Button buttonSwitch;
    private Button buttonSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WiFiDBHelper wiFiDBHelper =new WiFiDBHelper(MainActivity.this,"WiFiInfo.db",null,1);
        SQLiteDatabase database = wiFiDBHelper.getWritableDatabase();
        Cursor cursor = database.query("wifi",null,null,null,null,null,null);
        Log.d("Test", "Count: " + cursor.getCount());
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("number", "1");
            values.put("Ssid", "Null");
            values.put("Password", "Null");
            values.put("isChecked", 0);
            database.insert("wifi", null, values);
        }
//        Cursor cursor = database.rawQuery("select name from sqlite_master where type='table';", null);
//        while(cursor.moveToNext()){
//            //遍历出表名
//            String name = cursor.getString(0);
//            Log.i("System.out", name);
//        }

        buttonConnect = (Button) findViewById(R.id.button_connect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConnectActivity.class));
            }
        });

        buttonSwitch = (Button) findViewById(R.id.button_switch);
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SwitchActivity.class));
            }
        });



        buttonSecurity = (Button) findViewById(R.id.button_security);
        buttonSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecurityActivity.class));
            }
        });
    }
}
