package com.accessa.ibora.product.supplements;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.accessa.ibora.product.items.DatabaseHelper.DB_VERSION;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.accessa.ibora.Constants;

public class CategoryDatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Category";

    // Table columns
    public static final String _ID = "_id";
    public static final String CatName = "CatName";
    public static final String Color = "Color";

    // Database Information

    private static final String DB_NAME = Constants.DB_NAME;

    // database version


    

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CatName + " TEXT UNIQUE NOT NULL, " +
            Color + " TEXT NOT NULL);";



    public CategoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
        db.execSQL(CREATE_TABLE);
        addDefaultSupplement(db, "Supplement", "1");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    private void addDefaultSupplement(SQLiteDatabase db, String paymentMethod, String cashOrId) {
        ContentValues values = new ContentValues();
        values.put(CatName, paymentMethod);
        values.put(Color, "#EBFFD0");


        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting default item into the database");
        }
    }
    public Cursor getAllCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
         db.execSQL(CREATE_TABLE);

        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor searchCategory(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { _ID, CatName, Color };
        String selection = CatName + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%" };
        String sortOrder = CatName + " ASC";
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

};
