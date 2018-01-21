package com.masst.memo;

/**
 * Created by Dell on 1/19/2018.
 */

import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE ="memos.db";
    public static final String TABLE = "memo";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE+" (date INTEGER PRIMARY KEY, memo TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
