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
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Name + " TEXT NOT NULL, " + DESC + " TEXT NOT NULL, " + Category + " NOT NULL , " + Quantity + " NOT NULL , "+ Department + " NOT NULL , " + Barcode+" NOT NULL , "+LongDescription + " NOT NULL, "+SubDepartment + " NOT NULL, " + Price + " TEXT NOT NULL ,"+ VAT + "  NOT NULL, " +ExpiryDate + " NOT NULL );";

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
    public List<Item> getAllItems1() {
        List<Item> itemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int itemId = cursor.getInt(cursor.getColumnIndex(_ID));
                String itemName = cursor.getString(cursor.getColumnIndex(Name));
                double itemPrice = cursor.getDouble(cursor.getColumnIndex(Price));

                // Create an Item object and add it to the list
                Item item = new Item(_ID, Name, Price);
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return itemList;
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
    public Cursor searchID(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { _ID, Name ,DESC,Category, Price,ExpiryDate,Department,SubDepartment,};
        String selection = _ID + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%" };

        Cursor cursor1 = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        return cursor1;
    }
    public Cursor getDataById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { _ID, Name ,DESC, Category, Price,ExpiryDate,Department,SubDepartment};
        String selection = _ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
};
