package com.example.controlswitch.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhongzhiqiang on 19-4-29.
 */

public class WiFiDBHelper extends SQLiteOpenHelper {

    private String sql="create table wifi(" +
            "id integer primary key autoincrement not null," +
            "number text," +
            "Ssid text," +
            "Password text," +
            "isChecked integer)";

    public WiFiDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
