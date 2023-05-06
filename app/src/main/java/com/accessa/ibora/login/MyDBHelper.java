package com.accessa.ibora.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Accessa.db";
    public MyDBHelper(Context context) {
        super(context, "Accessa.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(Id TEXT primary key, Name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String Id, String Name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("ID", Id);
        contentValues.put("Name", Name);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;

        else
            return true;
    }

    public Boolean checkId(String Id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where Id = ?", new String[]{Id});
        if (cursor.getCount() > 0)

            return true;
        else
            return false;
    }

    public Boolean checkIdname(String Id, String Name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Id where Id = ? and Name = ?", new String[] {Id,Name});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
