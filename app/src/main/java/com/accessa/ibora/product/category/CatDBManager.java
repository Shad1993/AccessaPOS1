package com.accessa.ibora.product.category;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CatDBManager {

    private CatDatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CatDBManager(Context c) {
        context = c;
    }

    public CatDBManager open() throws SQLException {
        dbHelper = new CatDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(CatDatabaseHelper.SUBJECT, name);
        contentValue.put(CatDatabaseHelper.DESC, desc);
        database.insert(CatDatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { CatDatabaseHelper._ID, CatDatabaseHelper.SUBJECT, CatDatabaseHelper.DESC };
        Cursor cursor = database.query(CatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CatDatabaseHelper.SUBJECT, name);
        contentValues.put(CatDatabaseHelper.DESC, desc);
        int i = database.update(CatDatabaseHelper.TABLE_NAME, contentValues, CatDatabaseHelper._ID + " = " + _id, null);

        return i;
    }

    public void delete(long _id) {
        database.delete(CatDatabaseHelper.TABLE_NAME, CatDatabaseHelper._ID + "=" + _id, null);
    }

}

