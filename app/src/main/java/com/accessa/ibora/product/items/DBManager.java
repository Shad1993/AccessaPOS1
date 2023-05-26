package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.items.DatabaseHelper.DISCOUNT_TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.Discount.Discount;
import com.accessa.ibora.product.SubDepartment.SubDepartment;
import com.accessa.ibora.product.Vendor.Vendor;

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

    public void insert(String name, String desc, String price, String Category, String Barcode, float weight, String Department, String SubDepartment, String LongDescription, String Quantity, String ExpiryDate, String VAT, String AvailableForSale, String SoldBy, String Image, String Variant, String SKU, String Cost, String UserId, String LastModified) {
        // Insert the item into the main table
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
        contentValue.put(DatabaseHelper.LastModified, LastModified);
        contentValue.put(DatabaseHelper.UserId, UserId);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

        // Insert duplicates into the cost table
        ContentValues costContentValue = new ContentValues();
        costContentValue.put(DatabaseHelper.Barcode, Barcode);
        costContentValue.put(DatabaseHelper.SKU, SKU);
        costContentValue.put(DatabaseHelper.UserId, UserId);
        costContentValue.put(DatabaseHelper.LastModified, LastModified);
        costContentValue.put(DatabaseHelper.Cost, Cost);
        database.insert(DatabaseHelper.COST_TABLE_NAME, null, costContentValue);
    }

    public Cursor Registor(String enteredPIN, String cashorlevel, String cashorname, String cashordepartment) {
        ContentValues values = new ContentValues();
        values.put("pin", enteredPIN);
        values.put("cashorlevel", cashorlevel);
        values.put("cashorname", cashorname);
        values.put("cashordepartment", cashordepartment);

        database.insert(DatabaseHelper.TABLE_NAME_Users, null, values);
        return null;
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
        contentValue.put(DatabaseHelper.COLUMN_CASHOR_id, userId);
        contentValue.put(DatabaseHelper.DEPARTMENT_CODE, deptCode);

        database.insert(DatabaseHelper.DEPARTMENT_TABLE_NAME, null, contentValue);
    }

    public boolean updateDept(long id, String name, String lastmodified, String userId, String deptCode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DEPARTMENT_NAME, name);
        contentValues.put(DatabaseHelper.DEPARTMENT_LAST_MODIFIED, lastmodified);
        contentValues.put(DatabaseHelper.COLUMN_CASHOR_id, userId);
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

    public Department getDepartmentById(String id) {
        Department department = null;
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.DEPARTMENT_ID,
                DatabaseHelper.DEPARTMENT_CODE,
                DatabaseHelper.DEPARTMENT_NAME,
                DatabaseHelper.DEPARTMENT_LAST_MODIFIED,
                DatabaseHelper.COLUMN_CASHOR_id,

                // Add other columns as needed
        };

        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.DEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            department = new Department();
            department.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID)));
            department.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME)));
            department.setDepartmentCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE)));
            department.setLastModified(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_LAST_MODIFIED)));
            department.setCashierID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id)));



        }
        if (cursor != null) {
            cursor.close();
        }
        return department;
    }
    public Department getDepartmentByCode(String id) {
        Department department = null;
        String[] columns = new String[]{
                DatabaseHelper.DEPARTMENT_CODE,
                DatabaseHelper._ID,
                DatabaseHelper.DEPARTMENT_ID,

                DatabaseHelper.DEPARTMENT_NAME,
                DatabaseHelper.DEPARTMENT_LAST_MODIFIED,
                DatabaseHelper.COLUMN_CASHOR_id,

                // Add other columns as needed
        };

        String selection = DatabaseHelper.DEPARTMENT_CODE + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.DEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            department = new Department();
            department.setDepartmentCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE)));
            department.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID)));
            department.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME)));
            department.setLastModified(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_LAST_MODIFIED)));
            department.setCashierID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id)));



        }
        if (cursor != null) {
            cursor.close();
        }
        return department;
    }
    public void insertSubDept(String subDeptName, String lastModified, String userId, String deptCode, int departmentId) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBDEPARTMENT_NAME, subDeptName);
        contentValue.put(DatabaseHelper.LastModified, lastModified);
        contentValue.put(DatabaseHelper.DEPARTMENT_CASHIER_ID, userId);
        contentValue.put(DatabaseHelper.DEPARTMENT_CODE, deptCode);
        contentValue.put(DatabaseHelper.SUBDEPARTMENT_DEPARTMENT_ID, departmentId);
        database.insert(DatabaseHelper.SUBDEPARTMENT_TABLE_NAME, null, contentValue);
    }


    public SubDepartment getSubDepartmentById(String id) {
        SubDepartment Subdepartment = null;
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.SUBDEPARTMENT_ID,
                DatabaseHelper.DEPARTMENT_CODE,
                DatabaseHelper.SUBDEPARTMENT_NAME,
                DatabaseHelper.LastModified,
                DatabaseHelper.DEPARTMENT_CASHIER_ID,

                // Add other columns as needed
        };

        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.SUBDEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Subdepartment = new SubDepartment();
            Subdepartment.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.SUBDEPARTMENT_ID)));
            Subdepartment.setSubName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SUBDEPARTMENT_NAME)));
            Subdepartment.setSubDepartmentCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE)));
            Subdepartment.setLastModified(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LastModified)));
            Subdepartment.setCashierID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CASHIER_ID)));



        }
        if (cursor != null) {
            cursor.close();
        }
        return Subdepartment;
    }




    public boolean deleteSubDept(long id) {

        String selection = DatabaseHelper._ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DatabaseHelper.SUBDEPARTMENT_TABLE_NAME, selection, selectionArgs);
        return true;
    }

    public Discount getDiscountById(String id) {
        Discount discount = null;
        String[] columns = new String[]{
                DatabaseHelper.DISCOUNT_ID,
                DatabaseHelper.DISCOUNT_NAME,
                DatabaseHelper.DISCOUNT_VALUE,
                DatabaseHelper.DISCOUNT_TIMESTAMP,
                DatabaseHelper.DISCOUNT_USER_ID,

        };

        String selection = DatabaseHelper.DISCOUNT_ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DISCOUNT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            discount = new Discount();
            discount.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DISCOUNT_ID)));
            discount.setDiscountName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISCOUNT_NAME)));
            discount.setDiscountValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISCOUNT_VALUE))));
            discount.setDiscountTimestamp(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISCOUNT_TIMESTAMP)));
            discount.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISCOUNT_USER_ID))));



        }
        if (cursor != null) {
            cursor.close();
        }
        return discount;
    }

    public Vendor getVendorById(String id) {

        Vendor vendor = null;
        String[] columns = new String[]{
                DatabaseHelper.VendorID,
                DatabaseHelper.NomFournisseur,
                DatabaseHelper.CodeFournisseur,
                DatabaseHelper.LastModified,
                DatabaseHelper.UserId,
                DatabaseHelper.PhoneNumber,
                DatabaseHelper.Street,
                DatabaseHelper.Town,
                DatabaseHelper.PostalCode,
                DatabaseHelper.Email,
                DatabaseHelper.InternalCode,
                DatabaseHelper.Salesmen,





                // Add other columns as needed
        };

        String selection = DatabaseHelper.VendorID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.VENDOR_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            vendor = new Vendor();
            vendor.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.VendorID)));
            vendor.setNomFournisseur(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NomFournisseur)));
            vendor.setCodeFournisseur(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CodeFournisseur)));
            vendor.setLastModified(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LastModified)));
            vendor.setCashierID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UserId)));
            vendor.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PhoneNumber)));
            vendor.setStreet(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Street)));
            vendor.setTown(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Town)));
            vendor.setPostalCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PostalCode)));
            vendor.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Email)));
            vendor.setInternalCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.InternalCode)));
            vendor.setSalesmen(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Salesmen)));


        }
        if (cursor != null) {
            cursor.close();
        }
        return vendor;
    }

    public boolean deleteVendor(long id) {
        String selection = DatabaseHelper.VendorID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DatabaseHelper.VENDOR_TABLE_NAME, selection, selectionArgs);
        return true;
    }

    public boolean deleteDisc(long id) {
        String selection = DatabaseHelper.DISCOUNT_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DISCOUNT_TABLE_NAME, selection, selectionArgs);
        return true;
    }

    public void insertVendor(String vendorName, String lastModified, String userId, String vendCode, String phoneNumber, String street, String town, String postalCode, String email, String internalCode, String salesmen) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NomFournisseur, vendorName);
        contentValue.put(DatabaseHelper.LastModified, lastModified);
        contentValue.put(DatabaseHelper.UserId, userId);
        contentValue.put(DatabaseHelper.CodeFournisseur, vendCode);
        contentValue.put(DatabaseHelper.PhoneNumber, phoneNumber);
        contentValue.put(DatabaseHelper.Street, street);
        contentValue.put(DatabaseHelper.Town, town);
        contentValue.put(DatabaseHelper.PostalCode, postalCode);
        contentValue.put(DatabaseHelper.Email, email);
        contentValue.put(DatabaseHelper.InternalCode, internalCode);
        contentValue.put(DatabaseHelper.Salesmen, salesmen);
        database.insert(DatabaseHelper.VENDOR_TABLE_NAME, null, contentValue);
    }

    public boolean updateVendor(long id, String vendName, String lastModified, String vendCode,
                                String updatedPhoneNumber, String updatedStreet, String updatedTown,
                                String updatedPostalCode, String updatedEmail, String updatedInternalCode,
                                String updatedSalesmen, String UserId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NomFournisseur, vendName);
        contentValues.put(DatabaseHelper.LastModified, lastModified);
        contentValues.put(DatabaseHelper.CodeFournisseur, vendCode);
        contentValues.put(DatabaseHelper.PhoneNumber, updatedPhoneNumber);
        contentValues.put(DatabaseHelper.Street, updatedStreet);
        contentValues.put(DatabaseHelper.Town, updatedTown);
        contentValues.put(DatabaseHelper.PostalCode, updatedPostalCode);
        contentValues.put(DatabaseHelper.Email, updatedEmail);
        contentValues.put(DatabaseHelper.InternalCode, updatedInternalCode);
        contentValues.put(DatabaseHelper.Salesmen, updatedSalesmen);
        contentValues.put(DatabaseHelper.UserId, UserId);
        database.update(DatabaseHelper.VENDOR_TABLE_NAME, contentValues, DatabaseHelper.VendorID + " = " + id, null);
        return true;
    }
    public boolean updateSubDept(long id, String name, String lastmodified, String userId, String deptCode) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBDEPARTMENT_NAME, name);
        contentValue.put(DatabaseHelper.LastModified, lastmodified);
        contentValue.put(DatabaseHelper.DEPARTMENT_CASHIER_ID, userId);
        contentValue.put(DatabaseHelper.DEPARTMENT_CODE, deptCode);
        contentValue.put(DatabaseHelper.SUBDEPARTMENT_DEPARTMENT_ID, id);
        database.update(DatabaseHelper.SUBDEPARTMENT_TABLE_NAME, contentValue, DatabaseHelper._ID + " = " + id, null);
        return true;
    }

    public boolean updateDisc(long id, String name, String lastmodified, String userId, String deptCode) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DISCOUNT_NAME, name);
        contentValue.put(DatabaseHelper.DISCOUNT_TIMESTAMP, lastmodified);
        contentValue.put(DatabaseHelper.DISCOUNT_USER_ID, userId);
        contentValue.put(DatabaseHelper.DISCOUNT_VALUE, deptCode);
        database.update(DISCOUNT_TABLE_NAME, contentValue, DatabaseHelper.DISCOUNT_ID + " = " + id, null);
        return true;
    }
    public boolean updateCost(long id, String lastmodified, String userId, String vendCode) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.LastModified, lastmodified);
        contentValue.put(DatabaseHelper.DEPARTMENT_CASHIER_ID, userId);
        contentValue.put(DatabaseHelper.CodeFournisseur, vendCode);

        database.update(DatabaseHelper.COST_TABLE_NAME, contentValue, DatabaseHelper.CostID + " = " + id, null);
        return true;
    }


    // Method to retrieve the count of discounts
    public int getDiscountCount() {
        String query = "SELECT COUNT(*) FROM " + DISCOUNT_TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public void insertDisc(String discName, String lastModified, String userId, String discvalue) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DISCOUNT_NAME, discName);
        contentValue.put(DatabaseHelper.DISCOUNT_TIMESTAMP, lastModified);
        contentValue.put(DatabaseHelper.DISCOUNT_USER_ID, userId);
        contentValue.put(DatabaseHelper.DISCOUNT_VALUE, discvalue);
        database.insert(DatabaseHelper.DISCOUNT_TABLE_NAME, null, contentValue);
    }



}
