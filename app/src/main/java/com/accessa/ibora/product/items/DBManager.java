package com.accessa.ibora.product.items;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc, String price, String Category, String Barcode,String Department,String SubDepartment,String LongDescription,String Quantity,String ExpiryDate,String VAT, String AvailableForSale,String SoldBy, String Image,String Variant,String SKU, String Cost) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.Name, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.Price, price);
        contentValue.put(DatabaseHelper.Category, Category);
        contentValue.put(DatabaseHelper.Barcode, Barcode);
        contentValue.put(DatabaseHelper.Department, Department);
        contentValue.put(DatabaseHelper.SubDepartment, SubDepartment);
        contentValue.put(DatabaseHelper.LongDescription, LongDescription);
        contentValue.put(DatabaseHelper.Quantity, Quantity);
        contentValue.put(DatabaseHelper.ExpiryDate, ExpiryDate);
        contentValue.put(DatabaseHelper.VAT, VAT);
        contentValue.put(DatabaseHelper.AvailableForSale, AvailableForSale);
        contentValue.put(DatabaseHelper.SoldBy, SoldBy);
        contentValue.put(DatabaseHelper.Image, Image);
        contentValue.put(DatabaseHelper.Variant, Variant);
        contentValue.put(DatabaseHelper.SKU, SKU);
        contentValue.put(DatabaseHelper.Cost, Cost);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.DESC , DatabaseHelper.LongDescription, DatabaseHelper.Barcode, String.valueOf(DatabaseHelper.Price)};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name,  String desc, String Price, String Category, String Barcode,String Department,String SubDepartment,String LongDescription,String Quantity,String ExpiryDate,String VAT) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.Name, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        contentValues.put(DatabaseHelper.Price, Price);
        contentValues.put(DatabaseHelper.Category, Category);
        contentValues.put(DatabaseHelper.Barcode, Barcode);
        contentValues.put(DatabaseHelper.Department, Department);
        contentValues.put(DatabaseHelper.SubDepartment, SubDepartment);
        contentValues.put(DatabaseHelper.LongDescription, LongDescription);
        contentValues.put(DatabaseHelper.Quantity, Quantity);
        contentValues.put(DatabaseHelper.ExpiryDate, ExpiryDate);
        contentValues.put(DatabaseHelper.VAT, VAT);
        contentValues.put(String.valueOf(DatabaseHelper.Price), 0);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);

        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
