package com.masst.memo.Database;

/**
 * Created by Dell on 1/19/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.masst.memo.Globals;
import com.masst.memo.Models.Memo;
import com.masst.memo.Models.SessionManagment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DatabaseAccess {

    private SQLiteDatabase database;
    private DatabaseHelper openHelper;
    private static volatile DatabaseAccess instance;

    // Session Manager Class
    SessionManagment session;
    String username;
    Context mcontext;

    public DatabaseAccess(Context context) {

        this.openHelper = new DatabaseHelper(context);
        mcontext=context;

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

    public long save(Memo memo) {
        // Session class instance
        session = new SessionManagment(mcontext);

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name of user
        username = user.get(SessionManagment.KEY_NAME);
        ContentValues values = new ContentValues();
        values.put("Title",memo.getTitle());
        values.put("date", new Date().getTime());
        values.put("memo", memo.getText());
        values.put("user", username);
        long i = database.insert(DatabaseHelper.TABLE, null, values);
        return i;
    }

    public void update(Memo memo) {
        // Session class instance
        session = new SessionManagment(mcontext);

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name of user
        username = user.get(SessionManagment.KEY_NAME);
        ContentValues values = new ContentValues();
        values.put("Title",memo.getTitle());
        values.put("date", new Date().getTime());
        values.put("memo", memo.getText());
        values.put("user", username);
        String id = String.valueOf(memo.getId());
        database.update(DatabaseHelper.TABLE, values, "ID = ?", new String[]{id});
    }

    public void delete(Memo memo) {
        String id = String.valueOf(memo.getId());
       // Log.d("Memo Time",date);
       int i= database.delete(DatabaseHelper.TABLE, "Id = ?", new String[]{id});
          Log.d("Memo del",i+"");
    }

    public ArrayList<Memo> getAllMemos(String username) {
        ArrayList<Memo> memos = new ArrayList<Memo>();
        Cursor cursor =  database.rawQuery("SELECT * From memo WHERE user ="+'"username"+"'+"ORDER BY date DESC", null);
                //database.query("memo", new String[]{Globals.COL_1,Globals.COL_2,Globals.COL_3,Globals.COL_4}, null, null, null, null, null);
        Log.e("db","hey db ");
        if(cursor!=null) {
            if (cursor.moveToNext()) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    long time = cursor.getLong(2);
                    String text = cursor.getString(3);
                    memos.add(new Memo(id, title, time, text));
                    cursor.moveToNext();
                }
                cursor.close();
                return memos;
            } else {
                return memos;
            }
        }
        else
        {
            return null;
        }
    }
}
