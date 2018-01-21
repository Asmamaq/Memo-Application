package com.masst.memo;

/**
 * Created by Dell on 1/19/2018.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private DatabaseHelper openHelper;
    private static volatile DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void save(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("date", memo.getTime());
        values.put("memo", memo.getText());
        database.insert(DatabaseHelper.TABLE, null, values);
    }

    public void update(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("memo", memo.getText());
        String date = Long.toString(memo.getTime());
        database.update(DatabaseHelper.TABLE, values, "date = ?", new String[]{date});
    }

    public void delete(Memo memo) {
        String date = Long.toString(memo.getTime());
        database.delete(DatabaseHelper.TABLE, "date = ?", new String[]{date});
    }

    public ArrayList<Memo> getAllMemos() {
        ArrayList<Memo> memos = new ArrayList<Memo>();
        Cursor cursor = database.rawQuery("SELECT * From memo ORDER BY date DESC", null);
        if(cursor.moveToNext()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long time = cursor.getLong(0);
                String text = cursor.getString(1);
                memos.add(new Memo(time, text));
                cursor.moveToNext();
            }
            cursor.close();
            return memos;
        }
        else
        {
            return memos;
        }
    }
}
