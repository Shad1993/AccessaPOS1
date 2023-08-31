package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.items.DatabaseHelper.BUYER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_ADR_1;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_ADR_2;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_ADR_3;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_FAX_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_TEL_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Opening_Hours;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_ADR_1;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_ADR_2;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_ADR_3;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_BRN_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_COMPANY_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_FAX_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Logo;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_PAYMENT_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_QR_CODE_NUM;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_TEL_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_VAT_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.DEPARTMENT_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.DEPARTMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.DISCOUNT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.DateCreated;
import static com.accessa.ibora.product.items.DatabaseHelper.ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.LastModified;
import static com.accessa.ibora.product.items.DatabaseHelper.OpenDrawer;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_PAYMENTBYQY;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_STD_ACCESS;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.accessa.ibora.Admin.cashier;
import com.accessa.ibora.Buyer.Buyer;
import com.accessa.ibora.QR.QR;
import com.accessa.ibora.Settings.PaymentFragment.payment;
import com.accessa.ibora.company.Company;
import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.Discount.Discount;
import com.accessa.ibora.product.SubDepartment.SubDepartment;
import com.accessa.ibora.product.Vendor.Vendor;

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private static SQLiteDatabase database;

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

    public void insert(String name, String desc, String price, String Category, String Barcode, float weight, String Department, String SubDepartment, String LongDescription, String Quantity, String ExpiryDate, String VAT, String AvailableForSale, String SoldBy, String Image, String Variant, String SKU, String Cost, String UserId, String DateCreated, String LastModified, String selectedNature, String selectedCurrency, String itemCode, String vatCode, String selectedDiscount, double currentPrice) {
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
        contentValue.put(DatabaseHelper.DateCreated, DateCreated);
        contentValue.put(DatabaseHelper.LastModified, LastModified);
        contentValue.put(DatabaseHelper.UserId, UserId);
        contentValue.put(DatabaseHelper.Nature, selectedNature);
        contentValue.put(DatabaseHelper.Currency, selectedCurrency);
        contentValue.put(DatabaseHelper.ItemCode, itemCode);
        contentValue.put(DatabaseHelper.TaxCode, vatCode);
        contentValue.put(DatabaseHelper.TotalDiscount, selectedDiscount);
        contentValue.put(DatabaseHelper.PriceAfterDiscount, currentPrice);
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
    public void insertUser(String pin, String cashorname, String cashierLevel, String cashordepartment, String ShopName, String dateCreated, String lastModified, DatabaseHelper databaseHelper) {
        // Insert the item into the main table
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.COLUMN_PIN, pin);
        contentValue.put(DatabaseHelper.COLUMN_CASHOR_NAME, cashorname);
        contentValue.put(DatabaseHelper.COLUMN_CASHOR_LEVEL, cashierLevel);
        contentValue.put(DatabaseHelper.COLUMN_CASHOR_DEPARTMENT, cashordepartment);
        contentValue.put(DatabaseHelper.COLUMN_CASHOR_Shop, ShopName);
        contentValue.put(DateCreated, dateCreated);
        contentValue.put(LastModified, lastModified);

        database.insert(DatabaseHelper.TABLE_NAME_Users, null, contentValue);

        // Auto-increment the department code
        String departmentCode = getAutoIncrementedDepartmentCode(database);

        // Check if the department name already exists
        Cursor departmentCursor = databaseHelper.getDepartmentByName(cashordepartment);
        if (departmentCursor.moveToFirst()) {
            // Department already exists, update the department code instead of inserting
            departmentCode = departmentCursor.getString(departmentCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE));
        } else {
            // Department doesn't exist, insert it into the department table
            ContentValues departmentContentValue = new ContentValues();
            departmentContentValue.put(DatabaseHelper.DEPARTMENT_NAME, cashordepartment);
            departmentContentValue.put(DatabaseHelper.DEPARTMENT_CODE, departmentCode);
            departmentContentValue.put(DatabaseHelper.DEPARTMENT_LAST_MODIFIED, lastModified);

            database.insert(DEPARTMENT_TABLE_NAME, null, departmentContentValue);
        }

        // Continue with inserting the user record with the department code
        contentValue.put(DatabaseHelper.DEPARTMENT_CODE, departmentCode);
        database.insert(DatabaseHelper.TABLE_NAME_Users, null, contentValue);
    }


    public static String getAutoIncrementedDepartmentCode(SQLiteDatabase database) {
        String lastDepartmentCode = ""; // Initialize with an empty string

        // Query the database to retrieve the last department code
        String query = "SELECT MAX(" + DatabaseHelper.DEPARTMENT_CODE + ") FROM " + DEPARTMENT_TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            lastDepartmentCode = cursor.getString(0); // Get the last department code from the cursor
        }



        // Increment the department code
        String nextDepartmentCode = ""; // Initialize with an empty string

        if (lastDepartmentCode != null && !lastDepartmentCode.isEmpty()) {
            String numericPart = lastDepartmentCode.substring(1); // Extract the numeric part of the department code
            if (!numericPart.isEmpty()) {
                int numericValue = Integer.parseInt(numericPart);
                int nextNumericValue = numericValue + 1;
                nextDepartmentCode = lastDepartmentCode.charAt(0) + String.valueOf(nextNumericValue); // Reconstruct the department code
            } else {
                nextDepartmentCode = lastDepartmentCode; // Keep the same department code if the numeric part is empty
            }
        } else {
            lastDepartmentCode = "D0"; // Default department code if there is no previous code
            nextDepartmentCode = lastDepartmentCode;
        }

        if (cursor != null) {
            cursor.close();
        }
        return nextDepartmentCode;

    }

    public Cursor Registor(String enteredPIN, String cashorlevel, String cashorname, String cashordepartment,String DateCreated,String LastModified) {
        ContentValues values = new ContentValues();
        values.put("pin", enteredPIN);
        values.put("cashorlevel", cashorlevel);
        values.put("cashorname", cashorname);
        values.put("cashorDepartment", cashordepartment);
        values.put("DateCreated", DateCreated);
        values.put("LastModified", LastModified);

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
    } public Cursor fetchBuyers() {
        String[] columns = new String[]{DatabaseHelper.BUYER_ID, DatabaseHelper.BUYER_NAME,DatabaseHelper.BUYER_Company_name, DatabaseHelper.BUYER_BRN, DatabaseHelper.BUYER_TAN, DatabaseHelper.BUYER_NIC, DatabaseHelper.BUYER_BUSINESS_ADDR};
        Cursor cursor = database.query(DatabaseHelper.BUYER_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchUser() {
        String[] columns = new String[]{COLUMN_CASHOR_id, DatabaseHelper.COLUMN_CASHOR_NAME, DatabaseHelper.COLUMN_CASHOR_LEVEL};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_Users, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean updateTransItem(long id,String quantity,String price,  String longDesc, String lastModified) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.QUANTITY, quantity);
        contentValues.put(DatabaseHelper.TOTAL_PRICE, price);
        contentValues.put(DatabaseHelper.TRANSACTION_DATE, lastModified);
        contentValues.put(DatabaseHelper.LongDescription, longDesc);
        database.update(TRANSACTION_TABLE_NAME, contentValues, ITEM_ID + " = " + id, null);
        return true;
    }
    public boolean updateUser(long id, String name, String enteredPIN, String dept, String level,String lastmodified) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_CASHOR_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PIN,enteredPIN);
        contentValues.put(DatabaseHelper.COLUMN_CASHOR_DEPARTMENT,dept);
        contentValues.put(DatabaseHelper.COLUMN_CASHOR_LEVEL,level);
        contentValues.put(LastModified,lastmodified);


        database.update(DatabaseHelper.TABLE_NAME_Users, contentValues, COLUMN_CASHOR_id + " = " + id, null);
        return true;

    }
    public boolean updateItem(long id,String name, String desc, String price, String Category, String Barcode, float weight, String Department, String SubDepartment, String LongDescription, String Quantity, String ExpiryDate, String VAT, String AvailableForSale, String SoldBy, String Image, String Variant, String SKU, String Cost,String lastmodified) {
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
        contentValues.put(LastModified,lastmodified);

        database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + id, null);
        return true;
    }

    public boolean deleteItem(long _id) {
        String selection = DatabaseHelper._ID + "=?";
        String[] selectionArgs = { String.valueOf(_id) };
        database.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
        return true;
    }
    public boolean deleteUser(long id) {
        String selection = COLUMN_CASHOR_id + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DatabaseHelper.TABLE_NAME_Users, selection, selectionArgs);
        return true;
    }

    public cashier getUserById(String id) {
        cashier user = null;
        String[] columns = new String[]{
                COLUMN_CASHOR_id,
                DatabaseHelper.COLUMN_PIN,
                DatabaseHelper.COLUMN_CASHOR_LEVEL,
                DatabaseHelper.COLUMN_CASHOR_NAME,
                DatabaseHelper.COLUMN_CASHOR_DEPARTMENT,

        };

        String selection = COLUMN_CASHOR_id + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_Users, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user = new cashier();
            user.setcashorid((int) cursor.getLong(cursor.getColumnIndex(COLUMN_CASHOR_id)));
            user.setcashorname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME)));
            user.setcashorlevel(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_LEVEL)));
            user.setpin(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PIN)));
            user.setcashorDepartment(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_DEPARTMENT)));

        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
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
                DatabaseHelper.Image,
                DatabaseHelper.Nature,
                DatabaseHelper.ItemCode,
                DatabaseHelper.Currency,
                DatabaseHelper.TaxCode,
                DatabaseHelper.TotalDiscount
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
            item.setNature(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Nature)));
            item.setItemCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ItemCode)));
            item.setCurrency(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Currency)));
            item.setTaxCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TaxCode)));
            item.setTotalDiscount(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TotalDiscount))));
            // Set other properties of the item
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }
    public Item getItemByBarcode(String barcode) {
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

        String selection = DatabaseHelper.Barcode + " = ?";
        String[] selectionArgs = new String[]{barcode};

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

            String weightString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Weight));
            item.setWeight(weightString.isEmpty() ? 0.0f : Float.parseFloat(weightString));

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

    public String getVATById(String id) {
        String vatValue = null;
        String[] columns = new String[]{DatabaseHelper.VAT}; // Specify the VAT column

        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            vatValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.VAT));
        }

        if (cursor != null) {
            cursor.close();
        }
        return vatValue;
    }

    public void insertDept(String deptName,String DateCreated, String lastModified, String userId, String deptCode) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DEPARTMENT_NAME, deptName);
        contentValue.put(DatabaseHelper.DEPARTMENT_DATE_CREATED, DateCreated);
        contentValue.put(DatabaseHelper.DEPARTMENT_LAST_MODIFIED, lastModified);
        contentValue.put(COLUMN_CASHOR_id, userId);
        contentValue.put(DEPARTMENT_CODE, deptCode);

        database.insert(DEPARTMENT_TABLE_NAME, null, contentValue);
    }



    public boolean updateDept(long id, String name, String lastmodified, String userId, String deptCode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DEPARTMENT_NAME, name);
        contentValues.put(DatabaseHelper.DEPARTMENT_LAST_MODIFIED, lastmodified);
        contentValues.put(COLUMN_CASHOR_id, userId);
        contentValues.put(DEPARTMENT_CODE, deptCode);


        database.update(DEPARTMENT_TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + id, null);
        return true;
    }

    public boolean deleteDept(long _id) {
        String selection = DatabaseHelper._ID + "=?";
        String[] selectionArgs = { String.valueOf(_id) };
        database.delete(DEPARTMENT_TABLE_NAME, selection, selectionArgs);
        return true;
    }

    public Department getDepartmentById(String id) {
        Department department = null;
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.DEPARTMENT_ID,
                DEPARTMENT_CODE,
                DatabaseHelper.DEPARTMENT_NAME,
                DatabaseHelper.DEPARTMENT_LAST_MODIFIED,
                COLUMN_CASHOR_id,

                // Add other columns as needed
        };

        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            department = new Department();
            department.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID)));
            department.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME)));
            department.setDepartmentCode(cursor.getString(cursor.getColumnIndex(DEPARTMENT_CODE)));
            department.setLastModified(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_LAST_MODIFIED)));
            department.setCashierID(cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id)));



        }
        if (cursor != null) {
            cursor.close();
        }
        return department;
    }
    public Department getDepartmentByCode(String id) {
        Department department = null;
        String[] columns = new String[]{
                DEPARTMENT_CODE,
                DatabaseHelper._ID,
                DatabaseHelper.DEPARTMENT_ID,

                DatabaseHelper.DEPARTMENT_NAME,
                DatabaseHelper.DEPARTMENT_LAST_MODIFIED,
                COLUMN_CASHOR_id,

                // Add other columns as needed
        };

        String selection = DEPARTMENT_CODE + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DEPARTMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            department = new Department();
            department.setDepartmentCode(cursor.getString(cursor.getColumnIndex(DEPARTMENT_CODE)));
            department.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID)));
            department.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME)));
            department.setLastModified(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_LAST_MODIFIED)));
            department.setCashierID(cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id)));



        }
        if (cursor != null) {
            cursor.close();
        }
        return department;
    }
    public void insertSubDept(String subDeptName, String lastModified, String userId, String deptCode, int departmentId) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBDEPARTMENT_NAME, subDeptName);
        contentValue.put(LastModified, lastModified);
        contentValue.put(DatabaseHelper.DEPARTMENT_CASHIER_ID, userId);
        contentValue.put(DEPARTMENT_CODE, deptCode);
        contentValue.put(DatabaseHelper.SUBDEPARTMENT_DEPARTMENT_ID, departmentId);
        database.insert(DatabaseHelper.SUBDEPARTMENT_TABLE_NAME, null, contentValue);
    }


    public SubDepartment getSubDepartmentById(String id) {
        SubDepartment Subdepartment = null;
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.SUBDEPARTMENT_ID,
                DEPARTMENT_CODE,
                DatabaseHelper.SUBDEPARTMENT_NAME,
                LastModified,
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
            Subdepartment.setSubDepartmentCode(cursor.getString(cursor.getColumnIndex(DEPARTMENT_CODE)));
            Subdepartment.setLastModified(cursor.getString(cursor.getColumnIndex(LastModified)));
            Subdepartment.setCashierID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CASHIER_ID)));



        }
        if (cursor != null) {
            cursor.close();
        }
        return Subdepartment;
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
    public Buyer getBuyerById(String id) {
        Buyer buyer = null;
        String[] columns = new String[]{
                DatabaseHelper.BUYER_ID,
                DatabaseHelper.BUYER_NAME,
                DatabaseHelper.BUYER_Other_NAME,
                DatabaseHelper.BUYER_TAN,
                DatabaseHelper.BUYER_BRN,
                DatabaseHelper.BUYER_BUSINESS_ADDR,
                DatabaseHelper.BUYER_TYPE,
                DatabaseHelper.BUYER_NIC,
                DatabaseHelper.BUYER_Company_name,
                DatabaseHelper.BUYER_Profile


        };
        String selection = DatabaseHelper.BUYER_ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(BUYER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            buyer = new Buyer();
            buyer.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.BUYER_ID)));
            buyer.setNames(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_NAME)));
            buyer.setBuyerOtherName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_Other_NAME)));
            buyer.setTan(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_TAN)));
            buyer.setBrn(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_BRN)));
            buyer.setBusinessAddr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_BUSINESS_ADDR)));
            buyer.setBuyerType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_TYPE)));
            buyer.setNic(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_NIC)));
            buyer.setCompanyName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_Company_name)));
            buyer.setProfile(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BUYER_Profile)));




        }
        if (cursor != null) {
            cursor.close();
        }
        return buyer;
    }
    public Vendor getVendorById(String id) {

        Vendor vendor = null;
        String[] columns = new String[]{
                DatabaseHelper.VendorID,
                DatabaseHelper.NomFournisseur,
                DatabaseHelper.CodeFournisseur,
                LastModified,
                DatabaseHelper.UserId,
                DatabaseHelper.PhoneNumber,
                DatabaseHelper.Street,
                DatabaseHelper.Town,
                DatabaseHelper.PostalCode,
                DatabaseHelper.Email,
                DatabaseHelper.InternalCode,
                DatabaseHelper.Salesmen,

        };

        String selection = DatabaseHelper.VendorID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(DatabaseHelper.VENDOR_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            vendor = new Vendor();
            vendor.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.VendorID)));
            vendor.setNomFournisseur(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NomFournisseur)));
            vendor.setCodeFournisseur(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CodeFournisseur)));
            vendor.setLastModified(cursor.getString(cursor.getColumnIndex(LastModified)));
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

    public boolean deleteSubDept(long id) {

        String selection = DatabaseHelper._ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DatabaseHelper.SUBDEPARTMENT_TABLE_NAME, selection, selectionArgs);
        return true;
    }
    public boolean deleteTransacItem(long itemId) {

        String selection = ITEM_ID + "=?";
        String[] selectionArgs = { String.valueOf(itemId) };
       database.delete(DatabaseHelper.TRANSACTION_TABLE_NAME, selection, selectionArgs);
        return true;
    }


    public void insertVendor(String vendorName, String lastModified, String userId, String vendCode, String phoneNumber, String street, String town, String postalCode, String email, String internalCode, String salesmen) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NomFournisseur, vendorName);
        contentValue.put(LastModified, lastModified);
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
        contentValues.put(LastModified, lastModified);
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
        contentValue.put(LastModified, lastmodified);
        contentValue.put(DatabaseHelper.DEPARTMENT_CASHIER_ID, userId);
        contentValue.put(DEPARTMENT_CODE, deptCode);
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
    public boolean updateBuyer(long id, String name,String OtherNames, String compname, String tan, String brn,String add, String type, String Buyerprofile,String nic) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.BUYER_NAME, name);
        contentValue.put(DatabaseHelper.BUYER_Other_NAME, OtherNames);
        contentValue.put(DatabaseHelper.BUYER_Company_name, compname);
        contentValue.put(DatabaseHelper.BUYER_TAN, tan);
        contentValue.put(DatabaseHelper.BUYER_BRN, brn);
        contentValue.put(DatabaseHelper.BUYER_BUSINESS_ADDR, add);
        contentValue.put(DatabaseHelper.BUYER_TYPE, type);
        contentValue.put(DatabaseHelper.BUYER_NIC, nic);
        contentValue.put(DatabaseHelper.BUYER_Profile, Buyerprofile);


        database.update(BUYER_TABLE_NAME, contentValue, DatabaseHelper.BUYER_ID + " = " + id, null);
        return true;
    }
    public boolean updateCost(long id, String lastmodified, String userId, String vendCode) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(LastModified, lastmodified);
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
    public int getCompanyCount() {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_STD_ACCESS;
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


    public void insertQr(String qrName, String dateCreated, String lastModified, String userId, String qrCode) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.COLUMN_PAYMENT_METHOD, qrName);
        contentValue.put(LastModified, lastModified);
        contentValue.put(COLUMN_CASHOR_id, userId);
        contentValue.put(DateCreated, dateCreated);
        contentValue.put(COLUMN_QR_CODE_NUM, qrCode);
        database.insert(TABLE_NAME_PAYMENTBYQY, null, contentValue);
    }

    public boolean deleteQR(long id) {
        String selection = COLUMN_PAYMENT_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(TABLE_NAME_PAYMENTBYQY, selection, selectionArgs);
        return true;
    }

    public boolean updateQR(long id, String name, String lastmodified, String userId, String qrCode) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.COLUMN_PAYMENT_METHOD, name);
        contentValue.put(LastModified, lastmodified);
        contentValue.put(COLUMN_CASHOR_id, userId);
        contentValue.put(COLUMN_QR_CODE_NUM, qrCode);

        database.update(TABLE_NAME_PAYMENTBYQY, contentValue, DatabaseHelper.COLUMN_PAYMENT_ID + " = " + id, null);
        return true;
    }

    public QR getQRById(String id) {
        QR Qr = null;
        String[] columns = new String[]{
                DatabaseHelper.COLUMN_PAYMENT_ID,
                LastModified,
                DatabaseHelper.COLUMN_PAYMENT_METHOD,
                COLUMN_CASHOR_id,
                COLUMN_QR_CODE_NUM,

                // Add other columns as needed
        };

        String selection = DatabaseHelper.COLUMN_PAYMENT_ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(TABLE_NAME_PAYMENTBYQY, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Qr = new QR();
            Qr.setId(String.valueOf((int) cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_ID))));
            Qr.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_METHOD)));
            Qr.setQRcode(cursor.getString(cursor.getColumnIndex(COLUMN_QR_CODE_NUM)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return Qr;
    }

    public void insertPaymentMethod(String methodName, String dateCreated, String lastModified, String userId, String icon, String drawerValue) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME, methodName);
        contentValue.put(DatabaseHelper.PAYMENT_METHOD_COLUMN_LAST_MODIFIED, lastModified);
        contentValue.put(PAYMENT_METHOD_COLUMN_CASHOR_ID, userId);
        contentValue.put(DatabaseHelper.PAYMENT_METHOD_COLUMN_DATE_CREATED, dateCreated);
        contentValue.put(PAYMENT_METHOD_COLUMN_ICON, icon);
        contentValue.put(OpenDrawer, drawerValue);
        database.insert(PAYMENT_METHOD_TABLE_NAME, null, contentValue);
    }

    public payment getpaymentById(String id) {
        payment payments = null;
        String[] columns = new String[]{
                PAYMENT_METHOD_COLUMN_ID,
                DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME,
                DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON,
                DatabaseHelper.OpenDrawer,
                PAYMENT_METHOD_COLUMN_CASHOR_ID,

                // Add other columns as needed
        };

        String selection = PAYMENT_METHOD_COLUMN_ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = database.query(PAYMENT_METHOD_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            payments = new payment();
            payments.setId(cursor.getColumnIndex(PAYMENT_METHOD_COLUMN_ID));
            payments.setPaymentMethodName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME)));
            payments.setDrawerOpen(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(OpenDrawer))));
            payments.setDPaymentMethodIcon(cursor.getString(cursor.getColumnIndex(PAYMENT_METHOD_COLUMN_ICON)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return payments;
    }

    public boolean updatePayment(long id, String paymentName, String lastmodified, String userId, String icon, String drawer) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME, paymentName);
        contentValue.put(DatabaseHelper.PAYMENT_METHOD_COLUMN_LAST_MODIFIED, lastmodified);
        contentValue.put(DatabaseHelper.PAYMENT_METHOD_COLUMN_CASHOR_ID, userId);
        contentValue.put(PAYMENT_METHOD_COLUMN_ICON, icon);
        contentValue.put(OpenDrawer, drawer);

        database.update(PAYMENT_METHOD_TABLE_NAME, contentValue, PAYMENT_METHOD_COLUMN_ID + " = " + id, null);
        return true;
    }

    public boolean deletepaymentMethod(long id) {
        String selection = PAYMENT_METHOD_COLUMN_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(PAYMENT_METHOD_TABLE_NAME, selection, selectionArgs);
        return true;
    }

    public Company getCompanyInfo() {
        Company company = null;

        String[] columns = new String[]{
                COLUMN_SHOPNAME,
                COLUMN_SHOPNUMBER,
                COLUMN_VAT_NO,
                COLUMN_BRN_NO,
                COLUMN_ADR_1,
                COLUMN_ADR_2,
                COLUMN_ADR_3,
                COLUMN_Comp_ADR_1,
                COLUMN_Comp_ADR_2,
                COLUMN_Comp_ADR_3,
                COLUMN_TEL_NO,
                COLUMN_FAX_NO,
                COLUMN_Comp_TEL_NO,
                COLUMN_Comp_FAX_NO,
                COLUMN_COMPANY_NAME,
                COLUMN_CASHOR_id,
                LastModified,
                COLUMN_Opening_Hours,
                COLUMN_Logo
        };

        Cursor cursor = database.query(TABLE_NAME_STD_ACCESS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            company = new Company(); // Instantiate the company object
            company.setShopName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOPNAME)));
            company.setShopNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOPNUMBER)));
            company.setVATNo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VAT_NO)));
            company.setBRNNo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRN_NO)));
            company.setADR1(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADR_1)));
            company.setADR2(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADR_2)));
            company.setADR3(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADR_3)));
            company.setCompADR(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Comp_ADR_1)));
            company.setCompADR2(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Comp_ADR_2)));
            company.setCompADR3(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Comp_ADR_3)));
            company.setTelNo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEL_NO)));
            company.setFaxNo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAX_NO)));
            company.setCompanyName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPANY_NAME)));
            company.setCashorId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASHOR_id)));
            company.setLastModified(cursor.getString(cursor.getColumnIndexOrThrow(LastModified)));
            company.setOpeninghours(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Opening_Hours)));
            company.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Logo)));
            company.setComptel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Comp_TEL_NO)));
            company.setCompFax(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Comp_FAX_NO)));
            company.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Logo)));

        }

        if (cursor != null) {
            cursor.close();
        }

        return company;
    }






    public boolean updateCompanyInfo(String abv, String shopnumber, String lastModified, String userId, String vatNo, String brnNo, String adr1, String adr2, String adr3, String compAdr, String compAdr2, String compAdr3, String telNo, String faxNo, String companyName, String image, String openhour, String comptel, String compfax) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_SHOPNAME, abv);
            values.put(COLUMN_SHOPNUMBER, shopnumber);
            values.put(LastModified, lastModified);
            values.put(COLUMN_CASHOR_id, userId);
            values.put(COLUMN_VAT_NO, vatNo);
            values.put(COLUMN_BRN_NO, brnNo);
            values.put(COLUMN_ADR_1, adr1);
            values.put(COLUMN_ADR_2, adr2);
            values.put(COLUMN_ADR_3, adr3);
            values.put(COLUMN_Comp_ADR_1, compAdr);
            values.put(COLUMN_Comp_ADR_2, compAdr2);
            values.put(COLUMN_Comp_ADR_3, compAdr3);
            values.put(COLUMN_TEL_NO, telNo);
            values.put(COLUMN_FAX_NO, faxNo);
            values.put(COLUMN_COMPANY_NAME, companyName);
            values.put(COLUMN_Logo, image);
            values.put(COLUMN_Opening_Hours, openhour);
             values.put(COLUMN_Comp_TEL_NO, comptel);
          values.put(COLUMN_Comp_FAX_NO, compfax);

            int rowsAffected = database.update(TABLE_NAME_STD_ACCESS, values, null, null);
            return rowsAffected > 0;}

    public boolean deleteBuyer(long id) {
        String selection = DatabaseHelper.BUYER_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(BUYER_TABLE_NAME, selection, selectionArgs);
        return true;
    }
}
