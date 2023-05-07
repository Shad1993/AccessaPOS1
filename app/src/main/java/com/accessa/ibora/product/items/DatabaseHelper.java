package com.accessa.ibora.product.items;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.accessa.ibora.Constants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Items";

    // Table columns
    public static final String _ID = "_id";
    public static final String Name = "name";
    public static final String Category = "category";
    public static final String DESC = "description";
    public static final String Price = "price";
    public static final String Department = "Department";
    public static final String SubDepartment = "SubDepartment";
    public static final String Barcode = "Barcode";
    public static final String VAT = "VAT";

    public static final String LongDescription = "LongDescription";
    public static final String Quantity = "Quantity";
    public static final String ExpiryDate = "ExpiryDate";


    // Database Information

    private static final String DB_NAME = Constants.DB_NAME;

    // database version
    static final int DB_VERSION = 1;








    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Barcode + " TEXT UNIQUE NOT NULL, " +
            Name + " TEXT NOT NULL, " +
            DESC + " TEXT NOT NULL, " +
            Category + " TEXT NOT NULL, " +
            Quantity + " INTEGER NOT NULL, " +
            Department + " TEXT NOT NULL, " +
            LongDescription + " TEXT NOT NULL, " +
            SubDepartment + " TEXT NOT NULL, " +
            Price + " FLOAT NOT NULL, " +
            VAT + " FLOAT NOT NULL, " +
            ExpiryDate + " DATE NOT NULL);";



    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor searchItems(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { _ID, Name ,DESC,Category, Price};
        String selection = DESC + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%" };
        String sortOrder = DESC + " ASC";
        Cursor cursor1 = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor1;
    }


};
