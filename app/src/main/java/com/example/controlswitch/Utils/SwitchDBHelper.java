package com.example.controlswitch.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhongzhiqiang on 19-4-29.
 */

public class SwitchDBHelper extends SQLiteOpenHelper {

    private String sql="create table switch(" +
            "id integer primary key autoincrement not null," +
            "name text," +
            "port text," +
            "location text," +
            "introduction text," +
            "isChecked integer)";

    public SwitchDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
