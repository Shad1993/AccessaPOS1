package com.accessa.ibora.product.items;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.accessa.ibora.login.RegistorCashor;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import static com.accessa.ibora.login.RegistorCashor.COLUMN_CASHOR_id;
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

    public void insert(String name, String desc, String price, String Category, String Barcode, float weight, String Department, String SubDepartment, String LongDescription, String Quantity, String ExpiryDate, String VAT, String AvailableForSale, String SoldBy, String Image, String Variant, String SKU, String Cost) {
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
        contentValue.put(DatabaseHelper.Weight, weight);
        contentValue.put(DatabaseHelper.AvailableForSale, AvailableForSale);
        contentValue.put(DatabaseHelper.SoldBy, SoldBy);
        contentValue.put(DatabaseHelper.Image, Image);
        contentValue.put(DatabaseHelper.Variant, Variant);
        contentValue.put(DatabaseHelper.SKU, SKU);
        contentValue.put(DatabaseHelper.Cost, Cost);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.DESC, DatabaseHelper.LongDescription, DatabaseHelper.Barcode, DatabaseHelper.Price};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean updateItem(long id,String name, String desc, String price, String Category, String Barcode, float weight, String Department, String SubDepartment, String LongDescription, String Quantity, String ExpiryDate, String VAT, String AvailableForSale, String SoldBy, String Image, String Variant, String SKU, String Cost) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.Name, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        contentValues.put(DatabaseHelper.Price, price);
        contentValues.put(DatabaseHelper.LongDescription, LongDescription);
        contentValues.put(DatabaseHelper.Quantity, Quantity);
        contentValues.put(DatabaseHelper.Category, Category);
        contentValues.put(DatabaseHelper.Department, Department);
        contentValues.put(DatabaseHelper.SubDepartment, SubDepartment);
        contentValues.put(DatabaseHelper.Barcode, Barcode);
        contentValues.put(DatabaseHelper.Weight, weight);
        contentValues.put(DatabaseHelper.ExpiryDate, ExpiryDate);
        contentValues.put(DatabaseHelper.VAT, VAT);
        contentValues.put(DatabaseHelper.SoldBy, SoldBy);
        contentValues.put(DatabaseHelper.Image, Image);
        contentValues.put(DatabaseHelper.SKU, SKU);
        contentValues.put(DatabaseHelper.Variant, Variant);
        contentValues.put(DatabaseHelper.Cost, Cost);
        contentValues.put(DatabaseHelper.AvailableForSale,AvailableForSale);

        database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + id, null);
        return true;
    }

    public boolean deleteItem(long _id) {
        String selection = DatabaseHelper._ID + "=?";
        String[] selectionArgs = { String.valueOf(_id) };
        database.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
        return true;
    }


    public Item getItemById(String id) {
        Item item = null;
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.Name,
                DatabaseHelper.DESC,
                DatabaseHelper.Price,
                DatabaseHelper.Category,
                DatabaseHelper.Barcode,
                DatabaseHelper.Department,
                DatabaseHelper.SubDepartment,
                DatabaseHelper.LongDescription,
                DatabaseHelper.Quantity,
                DatabaseHelper.ExpiryDate,
                DatabaseHelper.VAT,
                DatabaseHelper.SoldBy,
                DatabaseHelper.Weight,
                DatabaseHelper.Cost,
                DatabaseHelper.SKU,
                DatabaseHelper.AvailableForSale,
                DatabaseHelper.Variant,
                DatabaseHelper.Image
                // Add other columns as needed
        };

        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            item = new Item();
            item.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Name)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESC)));

            // Handle empty strings or invalid numbers for Price, Quantity, and Cost
            String priceString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Price));
            item.setPrice(priceString.isEmpty() ? 0.0f : Float.parseFloat(priceString));

            String quantityString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Quantity));
            item.setQuantity(quantityString.isEmpty() ? 0 : Float.parseFloat(quantityString));

            String costString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Cost));
            item.setCost(costString.isEmpty() ? 0.0f : Float.parseFloat(costString));

            String WeightString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Weight));
            item.setWeight(WeightString.isEmpty() ? 0.0f : Float.parseFloat(WeightString));

            item.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category)));
            item.setBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Barcode)));
            item.setDepartment(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Department)));
            item.setSubDepartment(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SubDepartment)));
            item.setLongDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LongDescription)));
            item.setExpiryDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ExpiryDate)));

            item.setVAT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.VAT)));

            item.setSoldBy(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SoldBy)));
            item.setAvailableForSale(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AvailableForSale))));
            item.setSKU(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SKU)));
            item.setVariant(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Variant)));
            item.setImage(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Image)));
            // Set other properties of the item
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }

    public void insertDept(String deptName, String lastModified, String userId, String deptCode) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DEPARTMENT_NAME, deptName);
        contentValue.put(DatabaseHelper.DEPARTMENT_LAST_MODIFIED, lastModified);
        contentValue.put(RegistorCashor.COLUMN_CASHOR_id, userId);
        contentValue.put(DatabaseHelper.DEPARTMENT_CODE, deptCode);

        database.insert(DatabaseHelper.DEPARTMENT_TABLE_NAME, null, contentValue);
    }

    public boolean updateDept(long id, String name, String lastmodified, String userId, String deptCode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DEPARTMENT_NAME, name);
        contentValues.put(DatabaseHelper.DEPARTMENT_LAST_MODIFIED, lastmodified);
        contentValues.put(RegistorCashor.COLUMN_CASHOR_id, userId);
        contentValues.put(DatabaseHelper.DEPARTMENT_CODE, deptCode);


        database.update(DatabaseHelper.DEPARTMENT_TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + id, null);
        return true;
    }

    public boolean deleteDept(long _id) {
        String selection = DatabaseHelper._ID + "=?";
        String[] selectionArgs = { String.valueOf(_id) };
        database.delete(DatabaseHelper.DEPARTMENT_TABLE_NAME, selection, selectionArgs);
        return true;
    }
}
