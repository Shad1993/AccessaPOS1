package com.accessa.ibora.product.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDBManager {

    private CategoryDatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CategoryDBManager(Context c) {
        context = c;
    }

    public CategoryDBManager open() throws SQLException {
        dbHelper = new CategoryDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String CatName, String Color ) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(CategoryDatabaseHelper.CatName, CatName);
        contentValue.put(CategoryDatabaseHelper.Color, Color);

        database.insert(CategoryDatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { CategoryDatabaseHelper._ID, CategoryDatabaseHelper.CatName, CategoryDatabaseHelper.Color };
        Cursor cursor = database.query(CategoryDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String CatName,  String Color) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CategoryDatabaseHelper.CatName, CatName);
        contentValues.put(CategoryDatabaseHelper.Color, Color);


        int i = database.update(CategoryDatabaseHelper.TABLE_NAME, contentValues, CategoryDatabaseHelper._ID + " = " + _id, null);

        return i;
    }

    public void delete(long _id) {
        database.delete(CategoryDatabaseHelper.TABLE_NAME, CategoryDatabaseHelper._ID + "=" + _id, null);
    }

}
