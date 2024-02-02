package com.accessa.ibora.product.options;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.accessa.ibora.product.items.DatabaseHelper;

public class OptionsDBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public OptionsDBManager(Context c) {
        context = c;
    }

    public OptionsDBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String CatName) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.OPTION_NAME, CatName);


        database.insert(DatabaseHelper.OPTIONS_TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.OPTION_ID, DatabaseHelper.OPTION_NAME };
        Cursor cursor = database.query(DatabaseHelper.OPTIONS_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String CatName) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.OPTION_NAME, CatName);



        int i = database.update(DatabaseHelper.OPTIONS_TABLE_NAME, contentValues, DatabaseHelper.OPTION_ID + " = " + _id, null);

        return i;
    }

    public void delete(long _id) {
        database.delete(CategoryDatabaseHelper.TABLE_NAME, DatabaseHelper.OPTION_ID + "=" + _id, null);
    }

}
