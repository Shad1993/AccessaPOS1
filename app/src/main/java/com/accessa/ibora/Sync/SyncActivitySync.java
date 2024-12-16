package com.accessa.ibora.Sync;

import static com.accessa.ibora.product.items.DatabaseHelper.AmountDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.AvailableForSale;
import static com.accessa.ibora.product.items.DatabaseHelper.Barcode;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_ADR_1;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_ADR_2;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_ADR_3;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_BRN_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_DEPARTMENT;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_LEVEL;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_Shop;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_COMPANY_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_ADR_1;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_ADR_2;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_ADR_3;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_FAX_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Comp_TEL_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_FAX_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Logo;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_Opening_Hours;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_PIN;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_POS_Num;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_TEL_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_VAT_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.Category;
import static com.accessa.ibora.product.items.DatabaseHelper.Cost;
import static com.accessa.ibora.product.items.DatabaseHelper.Currency;
import static com.accessa.ibora.product.items.DatabaseHelper.DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.DateCreated;
import static com.accessa.ibora.product.items.DatabaseHelper.Department;
import static com.accessa.ibora.product.items.DatabaseHelper.ExpiryDate;
import static com.accessa.ibora.product.items.DatabaseHelper.ID;
import static com.accessa.ibora.product.items.DatabaseHelper.Image;
import static com.accessa.ibora.product.items.DatabaseHelper.ItemCode;
import static com.accessa.ibora.product.items.DatabaseHelper.LastModified;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.MERGED;
import static com.accessa.ibora.product.items.DatabaseHelper.MERGED_SET_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.Name;
import static com.accessa.ibora.product.items.DatabaseHelper.Nature;
import static com.accessa.ibora.product.items.DatabaseHelper.Price;
import static com.accessa.ibora.product.items.DatabaseHelper.Price2;
import static com.accessa.ibora.product.items.DatabaseHelper.Price2AfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Price3;
import static com.accessa.ibora.product.items.DatabaseHelper.Price3AfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.PriceAfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Quantity;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.RateDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Related_ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.SEAT_COUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SKU;
import static com.accessa.ibora.product.items.DatabaseHelper.STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.SoldBy;
import static com.accessa.ibora.product.items.DatabaseHelper.SubCategory;
import static com.accessa.ibora.product.items.DatabaseHelper.SubDepartment;
import static com.accessa.ibora.product.items.DatabaseHelper.SyncStatus;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_COUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.TaxCode;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount2;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount3;
import static com.accessa.ibora.product.items.DatabaseHelper.UserId;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.Variant;
import static com.accessa.ibora.product.items.DatabaseHelper.WAITER_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.Weight;
import static com.accessa.ibora.product.items.DatabaseHelper._ID;
import static com.accessa.ibora.product.items.DatabaseHelper.comment;
import static com.accessa.ibora.product.items.DatabaseHelper.hasSupplements;
import static com.accessa.ibora.product.items.DatabaseHelper.hasoptions;
import static com.accessa.ibora.product.items.DatabaseHelper.relatedSupplements;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item2;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item3;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item4;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item5;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.welcome;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class SyncActivitySync extends IntentService {
    private DatabaseHelper mDatabaseHelper;

    private Context context;

    public SyncActivitySync() {
        super("SyncActivitySync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Connection conn = start();
        if (conn != null) {
            String brn = intent.getStringExtra("BRN");
            String password = intent.getStringExtra("Password");
            String servername = intent.getStringExtra("servername");
            String dbname = intent.getStringExtra("dbname");
            String Username = intent.getStringExtra("username");

            // Check login credentials
            if (checkLogin(conn, password,servername,dbname,Username)) {

                // Login is valid, proceed with data insertion
                syncdata(conn);
            } else {
                // Display an error message or handle invalid login
                showToast("Invalid BRN or Password");

            }
        }
    }


    public Connection start() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        // Get SharedPreferences where the DB parameters are stored
        SharedPreferences preferences = getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);

        // Retrieve values from SharedPreferences (or use defaults if not set)
        String _user = preferences.getString("_user", null);
        String _pass = preferences.getString("_pass", null);
        String _DB = preferences.getString("_DB", null);
        String _server = preferences.getString("_server", null);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);

            // If the connection is successful, show a "Connection successful" toast
            showToast("Connection successful");

        } catch (SQLException se) {
            Log.e("ERROR1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR3", e.getMessage());
        }
        return conn;
    }

    private void syncdata(Connection conn) {

        try {
            // Step 1: Fetch data from the local SQLite database
            mDatabaseHelper = new DatabaseHelper(this);
            Cursor localCursor = mDatabaseHelper.getAllItems();

            getdataFromMssql(conn);
            getCategoriesFromMssql(conn);
            getSubCategoryFromMssql(conn);
          getDepartmentFromMssql(conn);
            getAndInsertSubDepartmentData(conn);
            getAndInsertVendorData(conn);
            getAndInsertOptionData(conn);
            getAndInsertSupplementData(conn);
            getAndInsertDiscountAndCouponData(conn);
             getAndInsertCostData(conn);
            getRoomsAndTablesFromMssql(conn);
            getItemsFromMssql(conn);
            getAndInsertStdAccessData(conn);
            getAndInsertBuyerData(conn);
        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
        }
    }
    public void getAndInsertBuyerData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from Buyer_Table
            String selectBuyerQuery = "SELECT * FROM Buyer_Table";
            resultSet = statement.executeQuery(selectBuyerQuery);

            // Process each buyer entry
            while (resultSet.next()) {
                int buyerId = resultSet.getInt("Buyer_Id");
                String buyerName = resultSet.getString("Buyer_Name").trim();
                String buyerOtherName = resultSet.getString("BuyerOtherName").trim();
                String companyName = resultSet.getString("companyName").trim();
                String buyerTAN = resultSet.getString("Buyer_TAN") != null ? resultSet.getString("Buyer_TAN").trim() : null;
                String buyerBRN = resultSet.getString("Buyer_BRN") != null ? resultSet.getString("Buyer_BRN").trim() : null;
                String adresse = resultSet.getString("Adresse") != null ? resultSet.getString("Adresse").trim() : null;
                String buyerType = resultSet.getString("Buyer_Type").trim();
                String buyerNIC = resultSet.getString("Buyer_NIC") != null ? resultSet.getString("Buyer_NIC").trim() : null;
                String priceLevel = resultSet.getString("PriceLevel") != null ? resultSet.getString("PriceLevel").trim() : null;
                String buyerProfile = resultSet.getString("Buyer_Profile") != null ? resultSet.getString("Buyer_Profile").trim() : null;
                int cashorId = resultSet.getInt("cashorid");
                Timestamp dateCreated = resultSet.getTimestamp("DateCreated");
                Timestamp lastModified = resultSet.getTimestamp("LastModified");

                // Insert the trimmed data into the local database
                databaseHelper.insertBuyerData(buyerId, buyerName, buyerOtherName, companyName, buyerTAN, buyerBRN, adresse, buyerType, buyerNIC, priceLevel, buyerProfile, cashorId, dateCreated, lastModified);
            }

            Log.d("BuyerSync", "Buyer data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("BuyerSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("BuyerSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("BuyerSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }

    public void getAndInsertCostData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from Cost table
            String selectCostQuery = "SELECT * FROM Cost";
            resultSet = statement.executeQuery(selectCostQuery);

            // Process each cost entry
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String barcode = resultSet.getString("Barcode").trim();
                BigDecimal sku = resultSet.getBigDecimal("SKU");
                BigDecimal cost = resultSet.getBigDecimal("Cost");
                Timestamp lastModified = resultSet.getTimestamp("LastModified");
                int userId = resultSet.getInt("UserId");
                String codeFournisseur = resultSet.getString("CodeFournisseur") != null ? resultSet.getString("CodeFournisseur").trim() : null;

                // Insert the trimmed data into the local database
                databaseHelper.insertCostData(id, barcode, sku, cost, lastModified, userId, codeFournisseur);
            }

            Log.d("CostSync", "Cost data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("CostSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("CostSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("CostSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }
    public void getAndInsertVendorData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from the Vendor table
            String selectQuery = "SELECT * FROM Vendor_pos";
            resultSet = statement.executeQuery(selectQuery);

            // Process each vendor entry
            while (resultSet.next()) {
                int vendorId = resultSet.getInt("ID");
                // Trim each field to remove leading/trailing whitespace
                String lastModified = resultSet.getString("LastModified").trim();
                int userId = resultSet.getInt("UserId");
                String codeFournisseur = resultSet.getString("CodeFournisseur").trim();
                String nomFournisseur = resultSet.getString("NomFournisseur").trim();
                String phoneNumber = resultSet.getString("PhoneNumber") != null
                        ? resultSet.getString("PhoneNumber").trim() : null;
                String street = resultSet.getString("Street") != null
                        ? resultSet.getString("Street").trim() : null;
                String town = resultSet.getString("Town") != null
                        ? resultSet.getString("Town").trim() : null;
                String postalCode = resultSet.getString("PostalCode") != null
                        ? resultSet.getString("PostalCode").trim() : null;
                String email = resultSet.getString("Email") != null
                        ? resultSet.getString("Email").trim() : null;
                String internalCode = resultSet.getString("InternalCode") != null
                        ? resultSet.getString("InternalCode").trim() : null;
                String salesmen = resultSet.getString("Salesmen") != null
                        ? resultSet.getString("Salesmen").trim() : null;

                // Insert the trimmed data into the local database
                databaseHelper.insertVendorData(vendorId, lastModified, userId, codeFournisseur, nomFournisseur,
                        phoneNumber, street, town, postalCode, email, internalCode, salesmen);
            }

            Log.d("VendorSync", "Vendor data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("VendorSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("VendorSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("VendorSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }
    public void getAndInsertDiscountAndCouponData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from Discount table
            String selectDiscountQuery = "SELECT * FROM Discount";
            resultSet = statement.executeQuery(selectDiscountQuery);

            // Process each discount entry
            while (resultSet.next()) {
                int id = resultSet.getInt("_id");
                String name = resultSet.getString("Name").trim();
                String discountValue = resultSet.getString("DiscountValue").trim();
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                int userId = resultSet.getInt("UserID");

                // Insert the trimmed data into the local database
                databaseHelper.insertDiscountData(id, name, discountValue, timestamp, userId);
            }

            // Close previous ResultSet and execute the next query for CouponTable
            resultSet.close();

            // Select all data from CouponTable
            String selectCouponQuery = "SELECT * FROM CouponTable";
            resultSet = statement.executeQuery(selectCouponQuery);

            // Process each coupon entry
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code").trim();
                String status = resultSet.getString("status").trim();
                Timestamp startDate = resultSet.getTimestamp("start_date");
                Timestamp endDate = resultSet.getTimestamp("end_date");
                int cashierId = resultSet.getInt("cashier_id");
                Timestamp dateCreated = resultSet.getTimestamp("date_created");
                Timestamp timeCreated = resultSet.getTimestamp("time_created");
                float discount = resultSet.getFloat("discount");

                // Insert the trimmed data into the local database
                databaseHelper.insertCouponData(id, code, status, startDate, endDate, cashierId, dateCreated, timeCreated, discount);
            }

            Log.d("DiscountCouponSync", "Discount and Coupon data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("DiscountCouponSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("DiscountCouponSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("DiscountCouponSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }

    public void getAndInsertSupplementData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from SupplementTable
            String selectSupplementTableQuery = "SELECT * FROM SupplementTable";
            resultSet = statement.executeQuery(selectSupplementTableQuery);

            // Process each supplement entry
            while (resultSet.next()) {
                int supplementId = resultSet.getInt("SupplementId");
                int relatedSupplementId = resultSet.getInt("RelatedSupplementID");
                String supplementDescriptions = resultSet.getString("SupplementDescriptions").trim();
                String supplementPrice = resultSet.getString("SupplementPrice").trim(); // Ensure you handle as needed
                String barcode = resultSet.getString("barcode").trim();
                Integer supplementOptionId = resultSet.getInt("SupplementOptionId"); // Handle as nullable if needed

                // Insert the trimmed data into the local database
                databaseHelper.insertSupplementData(supplementId, relatedSupplementId, supplementDescriptions,
                        supplementPrice, barcode, supplementOptionId);
            }

            // Close previous ResultSet and execute the next query for SupplementTableName
            resultSet.close();

            // Select all data from SupplementTableName
            String selectSupplementTableNameQuery = "SELECT * FROM SupplementTableName";
            resultSet = statement.executeQuery(selectSupplementTableNameQuery);

            // Process each supplement option entry
            while (resultSet.next()) {
                int supplementOptionId = resultSet.getInt("SupplementOptionId");
                String supplementOptionName = resultSet.getString("SupplementOptionName").trim();

                // Insert the trimmed data into the local database
                databaseHelper.insertSupplementOptionData(supplementOptionId, supplementOptionName);
            }

            Log.d("SupplementSync", "Supplement data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("SupplementSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("SupplementSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("SupplementSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }

    public void getAndInsertOptionData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from OptionTable
            String selectOptionTableQuery = "SELECT * FROM OptionTable";
            resultSet = statement.executeQuery(selectOptionTableQuery);

            // Process each option entry
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String optionName = resultSet.getString("optionName").trim();

                // Insert the trimmed data into the local database
                databaseHelper.insertOptionTableData(id, optionName);
            }

            // Close previous ResultSet and execute the next query for Options
            resultSet.close();

            // Select all data from Options
            String selectOptionsQuery = "SELECT * FROM Options";
            resultSet = statement.executeQuery(selectOptionsQuery);

            // Process each options entry
            while (resultSet.next()) {
                int variantOptionId = resultSet.getInt("VariantOptionId");
                int id = resultSet.getInt("id");
                int variantItemId = resultSet.getInt("variantItemId");
                String barcode = resultSet.getString("barcode").trim();
                String desc = resultSet.getString("Desc").trim();
                BigDecimal price = resultSet.getBigDecimal("Price");

                // Insert the trimmed data into the local database
                databaseHelper.insertOptionsData(variantOptionId, id, variantItemId, barcode, desc, price);
            }

            Log.d("OptionSync", "Option data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("OptionSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("OptionSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("OptionSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }

    private void getSubCategoryFromMssql(Connection conn) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            String selectQuery = "SELECT * FROM SubCategory";
            statement = conn.prepareStatement(selectQuery);
            resultSet = statement.executeQuery();

            Log.d("getSubCategoryFromMssql", selectQuery);

            while (resultSet.next()) {
                // Retrieve and trim data from the SubCategory table
                int subCategoryId = resultSet.getInt("_id");
                String subCatName = resultSet.getString("SubCatName").trim();
                String printerOption = resultSet.getString("Printer_Option").trim();
                String relatedCatId = resultSet.getString("relatedCatid").trim();

                // Insert or update data into the local database
                databaseHelper.insertSubCategoryData(subCategoryId, subCatName, printerOption, relatedCatId);
            }

            showToast("SubCategory data synced successfully.");
        } catch (SQLException se) {
            Log.e("SubCategory_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("SubCategory_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("SubCategory_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }
    public void getAndInsertSubDepartmentData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from the SubDepartment table
            String selectQuery = "SELECT * FROM SubDepartment";
            resultSet = statement.executeQuery(selectQuery);

            // Process each sub-department entry
            while (resultSet.next()) {
                int subDepartmentId = resultSet.getInt("_id");
                // Trim each field to remove leading/trailing whitespace
                String subDepartmentName = resultSet.getString("SubDepartmentName").trim();
                int departmentId = resultSet.getInt("DepartmentID");
                String departmentCode = resultSet.getString("DepartmentCode").trim();
                String lastModified = resultSet.getString("LastModified") != null
                        ? resultSet.getString("LastModified").trim() : null;
                int cashierId = resultSet.getInt("CashierID");

                // Insert the trimmed data into the local database
                databaseHelper.insertSubDepartmentData(subDepartmentId, subDepartmentName, departmentId, departmentCode, lastModified, cashierId);
            }

            Log.d("SubDepartmentSync", "SubDepartment data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("SubDepartmentSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("SubDepartmentSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("SubDepartmentSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }

    private void getCategoriesFromMssql(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        PreparedStatement insertStatement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();  // Open local SQLite database connection
            statement = conn.createStatement();  // Create MSSQL statement

            // Query to get data from MSSQL Category table
            String selectQuery = "SELECT * FROM Category";
            resultSet = statement.executeQuery(selectQuery);

            Log.d("CategoryQuery", selectQuery);

            while (resultSet.next()) {
                // Retrieve data from MSSQL Category table
                int categoryId = resultSet.getInt("_id");
                String categoryName = resultSet.getString("CatName");
                String printerOption = resultSet.getString("Printer_Option");
                String color = resultSet.getString("Color");

                // Insert or update data in the local SQLite database
                databaseHelper.insertCategoryData(categoryId, categoryName, printerOption, color);
            }

            showToast("Categories synchronized from MSSQL to local database.");

        } catch (SQLException se) {
            Log.e("Category_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("Category_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (insertStatement != null) insertStatement.close();
                dbManager.close();  // Close local SQLite database connection
            } catch (SQLException e) {
                Log.e("Category_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }
    public void getDepartmentFromMssql(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from the Department table
            String selectQuery = "SELECT * FROM Department";
            resultSet = statement.executeQuery(selectQuery);

            // Process each department entry
            while (resultSet.next()) {
                int departmentId = resultSet.getInt("_id");
                String departmentCode = resultSet.getString("DepartmentCode").trim();
                String departmentName = resultSet.getString("DepartmentName").trim();
                String dateCreated = resultSet.getString("DateCreated") != null
                        ? resultSet.getString("DateCreated").trim() : null;
                String lastModified = resultSet.getString("LastModified") != null
                        ? resultSet.getString("LastModified").trim() : null;
                int cashorId = resultSet.getInt("cashorid");

                // Insert or update data into the local database
                databaseHelper.insertDepartmentData(departmentId, departmentCode, departmentName, dateCreated, lastModified, cashorId);
            }

            Log.d("DepartmentSync", "Department data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("DepartmentSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("DepartmentSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("DepartmentSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }

    private void getRoomsAndTablesFromMssql(Connection conn) {
        ResultSet resultSet = null;
        ResultSet tablesResultSet = null;
        Statement statement = null;
        PreparedStatement tablestatement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Get total rooms
            String selectQuery = "SELECT COUNT(*) as totalrooms FROM rooms";
            resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {
                int totalItems = resultSet.getInt("totalrooms");
                if (totalItems > 0) {
                    showToast("Total Rooms in MSSQL database: " + totalItems);

                    // Get tables data
                    String tablesQuery = "SELECT * FROM tables";
                    tablestatement = conn.prepareStatement(tablesQuery);
                    tablesResultSet = tablestatement.executeQuery();

                    Log.d("tablesQuery", tablesQuery);
                    while (tablesResultSet.next()) {
                        // Retrieve data from the tables table
                        int tableId = tablesResultSet.getInt(TABLE_ID);
                        int roomids = tablesResultSet.getInt(ROOM_ID);
                        int TableNumber = tablesResultSet.getInt(TABLE_NUMBER);
                        int seatCount = tablesResultSet.getInt(SEAT_COUNT);
                        String waiterName = tablesResultSet.getString(WAITER_NAME);
                        String status = tablesResultSet.getString(STATUS);
                        String merged = tablesResultSet.getString(MERGED);
                        String mergeSetid = tablesResultSet.getString(MERGED_SET_ID);
                        int RoomNum = tablesResultSet.getInt("RoomNum");
                        int TableNum = tablesResultSet.getInt("TillNum");

                        // Insert or update data into the local database
                        databaseHelper.inserttablesDatas(tableId, roomids, TableNumber, seatCount, waiterName, status, merged, mergeSetid, RoomNum, TableNum);

                        // Process rooms data
                        String query = "SELECT * FROM rooms";
                        resultSet = statement.executeQuery(query);
                        Log.d("rooms", query);
                        while (resultSet.next()) {
                            int _id = resultSet.getInt(ID);
                            String roomname = resultSet.getString(ROOM_NAME);
                            String tablecount = resultSet.getString(TABLE_COUNT);
                            RoomNum = resultSet.getInt("RoomNum");
                            TableNum = resultSet.getInt("TillNum");

                            databaseHelper.insertroomsDatas(_id, roomname, tablecount, RoomNum, TableNum);
                        }
                    }

                    // Redirect to the Product activity
                    Intent intent = new Intent(SyncActivitySync.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    showToast("No items found in MSSQL database.");
                }
            }
        } catch (SQLException se) {
            Log.e("rooms_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("rooms_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (tablesResultSet != null) tablesResultSet.close();
                if (statement != null) statement.close();
                if (tablestatement != null) tablestatement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("rooms_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }


    private void getItemsFromMssql(Connection conn) {

        try {
            String selectQuery = "SELECT COUNT(*) as totalItems FROM Items";
            Statement statement = conn.createStatement();
            DBManager dbManager = new DBManager(this);
            dbManager.open();
            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            ResultSet resultSets = null;
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {
                int totalItems = resultSet.getInt("totalItems");
                if (totalItems > 0) {
                    showToast("Total Items in MSSQL database: " + totalItems);
                    // Execute your SQL query

                    String usersQuery = "SELECT * FROM Users ";
                    PreparedStatement usersStatement = conn.prepareStatement(usersQuery);

                    ResultSet usersResultSet = usersStatement.executeQuery();
                    Log.d("user", usersQuery);
                    while (usersResultSet.next()) {
                        // Retrieve data from the users table
                        int cashorId = usersResultSet.getInt(COLUMN_CASHOR_id);
                        String pin = usersResultSet.getString(COLUMN_PIN);
                        int cashorLevel = usersResultSet.getInt(COLUMN_CASHOR_LEVEL);
                        String cashorName = usersResultSet.getString(COLUMN_CASHOR_NAME);
                        String cashorShop = usersResultSet.getString(COLUMN_CASHOR_Shop);
                        String cashorDepartment = usersResultSet.getString(COLUMN_CASHOR_DEPARTMENT);
                        String dateCreatedUsers = usersResultSet.getString(DateCreated);
                        String lastModifiedUsers = usersResultSet.getString(LastModified);

                        // Call the method to insert this data into your target table
                        // Modify the method as needed to accept these parameters
                        databaseHelper.insertUserDatas(cashorId, pin, cashorLevel, cashorName, cashorShop, cashorDepartment, dateCreatedUsers, lastModifiedUsers);
                    }

                    String query = "SELECT * FROM Items";
                    resultSets = statement.executeQuery(query);
                    Log.d("items", query);
                    while (resultSets.next()) {
                        // Process each row of data here
                        String _id= resultSets.getString(_ID);
                        String barcode = resultSets.getString(Barcode);
                        String relateditemid = resultSets.getString(Related_ITEM_ID);
                        String itemname = resultSets.getString(Name);
                        String desc = resultSets.getString(DESC);
                        String category = resultSets.getString(Category);
                        String subcategory = resultSets.getString(SubCategory);
                        String quantity = resultSets.getString(Quantity);
                        String department = resultSets.getString(Department);
                        String longDescription = resultSets.getString(LongDescription);
                        String subDepartment = resultSets.getString(SubDepartment);
                        String price = resultSets.getString(Price);
                        String priceAfterDiscount = resultSets.getString(PriceAfterDiscount);
                        String vAT = resultSets.getString(VAT);
                        String expiryDate = resultSets.getString(ExpiryDate);
                        String availableForSale = resultSets.getString(AvailableForSale);
                        if(availableForSale.equals("1")){
                            availableForSale="true";
                        } else if (availableForSale.equals("0")) {
                            availableForSale="false";
                        }
                        String soldBy = resultSets.getString(SoldBy);
                        String image = resultSets.getString(Image);
                        String sku = resultSets.getString(SKU);
                        String variant = resultSets.getString(Variant);
                        String cost = resultSets.getString(Cost);
                        String weight = resultSets.getString(Weight);
                        String userId = resultSets.getString(UserId);
                        String nature = resultSets.getString(Nature);
                        String taxCode = resultSets.getString(TaxCode);
                        String currency = resultSets.getString(Currency);
                        String itemCode = resultSets.getString(ItemCode);
                        String totalDiscount = resultSets.getString(TotalDiscount);
                        String dateCreated = resultSets.getString(DateCreated);
                        String lastModified = resultSets.getString(LastModified);
                        String syncStatus = resultSets.getString(SyncStatus);
                        String price2 = resultSets.getString(Price2);
                        String price3 = resultSets.getString(Price3);
                        String priceAfterDiscount2 = resultSets.getString(Price2AfterDiscount);
                        String priceAfterDiscount3 = resultSets.getString(Price3AfterDiscount);
                        String rateDiscount = resultSets.getString(RateDiscount);
                        String amountDiscount = resultSets.getString(AmountDiscount);
                        String totalDiscount2 = resultSets.getString(TotalDiscount2);
                        String totalDiscount3 = resultSets.getString(TotalDiscount3);
                        String Hasoptions = resultSets.getString(hasoptions);
                        if(Hasoptions.equals("True") || Hasoptions.equals("1")){
                            Hasoptions="true";
                        } else if (Hasoptions.equals("0") || Hasoptions.equals("False")) {
                            Hasoptions="false";
                        }
                        String Comment = resultSets.getString(comment);
                        //related_item
                        String Related_item = resultSets.getString(related_item);
                        String Related_item2 = resultSets.getString(related_item2);
                        String Related_item3 = resultSets.getString(related_item3);
                        String Related_item4 = resultSets.getString(related_item4);
                        String Related_item5 = resultSets.getString(related_item5);
                        //hasSupplements
                        String HasSupplements = resultSets.getString(hasSupplements);
                        if(HasSupplements.equals("True") || HasSupplements.equals("1")){
                            HasSupplements="true";
                        } else if (HasSupplements.equals("0") || HasSupplements.equals("False")) {
                            HasSupplements="false";
                        }
                        //relatedSupplements
                        String RelatedSupplements = resultSets.getString(relatedSupplements);
                        // int ShopNums = resultSets.getInt(ShopNum);
                        //int TillNums = resultSets.getInt(TillNum);

                        Log.e("Hasoptions",Hasoptions);
                        Log.e("Related_item",Related_item);

                        databaseHelper.insertItemsDatas(_id,itemname,Comment,RelatedSupplements, desc, price,price2,price3,rateDiscount,amountDiscount, relateditemid,category, subcategory,barcode, Float.parseFloat(weight), department,
                                subDepartment, longDescription, quantity, expiryDate, vAT,
                                availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,Hasoptions,
                                nature, currency, itemCode, taxCode, totalDiscount,totalDiscount2,totalDiscount3, Double.parseDouble(priceAfterDiscount),Double.parseDouble(priceAfterDiscount2),Double.parseDouble(priceAfterDiscount3),Related_item,Related_item2,Related_item3,Related_item4,Related_item5,HasSupplements, syncStatus);


                        // dbManager.insertwithnewbarcode(itemname,Comment,RelatedSupplements, desc, price,price2,price3,rateDiscount,amountDiscount, category, barcode, Float.parseFloat(weight), department,
                        //             subDepartment, longDescription, quantity, expiryDate, vAT,
                        //           availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,Hasoptions,
                        //          nature, currency, itemCode, taxCode, totalDiscount,totalDiscount2,totalDiscount3, Double.parseDouble(priceAfterDiscount),Double.parseDouble(priceAfterDiscount2),Double.parseDouble(priceAfterDiscount3),Related_item,Related_item2,Related_item3,Related_item4,Related_item5,HasSupplements, syncStatus);

                        // Redirect to the Product activity
                        Intent intent = new Intent(SyncActivitySync.this, login.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                } else {
                    showToast("No items found in MSSQL database.");
                }
            }
            resultSets.close();

            statement.close();
            dbManager.close();
        } catch (SQLException se) {
            Log.e("ItemsREMOTE_DATA_TEST_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("ItemsREMOTE_DATA_TEST_ERROR", e.getMessage());
        }
    }
    public void getAndInsertStdAccessData(Connection conn) {
        ResultSet resultSet = null;
        Statement statement = null;
        DBManager dbManager = new DBManager(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            dbManager.open();
            statement = conn.createStatement();

            // Select all data from std_access table
            String selectStdAccessQuery = "SELECT * FROM std_access";
            resultSet = statement.executeQuery(selectStdAccessQuery);

            // Process each std_access entry
            while (resultSet.next()) {
                int stdAccessId = resultSet.getInt("std_access_id");
                String companyAddress1 = resultSet.getString("ComPanyAdress1") != null ? resultSet.getString("ComPanyAdress1").trim() : null;
                String companyAddress2 = resultSet.getString("ComPanyAdress2") != null ? resultSet.getString("ComPanyAdress2").trim() : null;
                String companyAddress3 = resultSet.getString("ComPanyAdress3") != null ? resultSet.getString("ComPanyAdress3").trim() : null;
                String companyPhoneNumber = resultSet.getString("ComPanyphoneNumber") != null ? resultSet.getString("ComPanyphoneNumber").trim() : null;
                String companyFaxNumber = resultSet.getString("ComPanyFaxNumber") != null ? resultSet.getString("ComPanyFaxNumber").trim() : null;
                String shopName = resultSet.getString("ShopName") != null ? resultSet.getString("ShopName").trim() : null;
                int shopNumber = resultSet.getInt("ShopNumber");
                String logo = resultSet.getString("Logo") != null ? resultSet.getString("Logo").trim() : null;
                String vatNo = resultSet.getString("vat_no") != null ? resultSet.getString("vat_no").trim() : null;
                String brnNo = resultSet.getString("brn_no") != null ? resultSet.getString("brn_no").trim() : null;
                String adr1 = resultSet.getString("adr_1") != null ? resultSet.getString("adr_1").trim() : null;
                String adr2 = resultSet.getString("adr_2") != null ? resultSet.getString("adr_2").trim() : null;
                String adr3 = resultSet.getString("adr_3") != null ? resultSet.getString("adr_3").trim() : null;
                String telNo = resultSet.getString("tel_no") != null ? resultSet.getString("tel_no").trim() : null;
                String faxNo = resultSet.getString("fax_no") != null ? resultSet.getString("fax_no").trim() : null;
                String openingHours = resultSet.getString("OpenningHours") != null ? resultSet.getString("OpenningHours").trim() : null;
                String companyName = resultSet.getString("company_name") != null ? resultSet.getString("company_name").trim() : null;
                int cashorId = resultSet.getInt("cashorid");
                int posNum = resultSet.getInt("pos_num");
                Timestamp lastModified = resultSet.getTimestamp("LastModified");
                Date dateCreated = resultSet.getDate("DateCreated");

                // Insert the trimmed data into the local database
                databaseHelper.insertSyncStdAccessData(stdAccessId, companyAddress1, companyAddress2, companyAddress3,
                        companyPhoneNumber, companyFaxNumber, shopName, shopNumber, logo, vatNo, brnNo,
                        adr1, adr2, adr3, telNo, faxNo, openingHours, companyName, cashorId, posNum, lastModified, dateCreated);
            }

            Log.d("StdAccessSync", "Std Access data synchronized successfully.");

        } catch (SQLException se) {
            Log.e("StdAccessSync_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("StdAccessSync_ERROR", e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                dbManager.close();
            } catch (SQLException e) {
                Log.e("StdAccessSync_ERROR", "Failed to close resources: " + e.getMessage());
            }
        }
    }


    private boolean checkLogin(Connection conn, String password, String servername, String dbname, String userName) {
        // SharedPreferences to store the database connection configurations
        SharedPreferences preferences = this.getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Set up the thread policy for allowing network operations on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connURL = null;
        boolean isConnectionSuccessful = false; // Flag to indicate the connection status

        try {
            // Load the SQL Server JDBC driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // Construct the connection URL
            connURL = "jdbc:jtds:sqlserver://" + servername + ";databaseName=" + dbname + ";user=" + userName + ";password=" + password + ";";

            // Attempt to establish the connection
            conn = DriverManager.getConnection(connURL);

            // If the connection is successful, save configurations and show a toast
            showToast("Connection successful");

            // Store the configurations in SharedPreferences
            editor.putString("_user", userName);
            editor.putString("_pass", password);
            editor.putString("_DB", dbname);
            editor.putString("_server", servername);
            editor.apply();

            isConnectionSuccessful = true; // Update the flag

        } catch (SQLException se) {
            Log.e("ERROR", "SQL Exception: " + se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", "Class Not Found: " + e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", "Exception: " + e.getMessage());
        }

        // Return the connection status
        return isConnectionSuccessful;
    }


    private void getdataFromMssql(Connection conn) {

        try {
            String selectQuery = "SELECT COUNT(*) as totalItems FROM std_access";
            Statement statement = conn.createStatement();
            DBManager dbManager = new DBManager(this);
            dbManager.open();
            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            ResultSet resultSets = null;
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {
                int totalItems = resultSet.getInt("totalItems");
                if (totalItems > 0) {




                            String stdAccessQuery =  "SELECT * FROM std_access ";
                            PreparedStatement stdAccessStatement = conn.prepareStatement(stdAccessQuery);

                            ResultSet stdAccessResultSet = stdAccessStatement.executeQuery();

                            Log.d("std", stdAccessQuery);
                            while (stdAccessResultSet.next()) {
                                String CompAd1 = stdAccessResultSet.getString(COLUMN_Comp_ADR_1);
                                String CompAd2 = stdAccessResultSet.getString(COLUMN_Comp_ADR_2);
                                String CompAd3 = stdAccessResultSet.getString(COLUMN_Comp_ADR_3);
                                String CompTel = stdAccessResultSet.getString(COLUMN_Comp_TEL_NO);
                                String brnValue = stdAccessResultSet.getString(COLUMN_BRN_NO);
                                String compFaxNo = stdAccessResultSet.getString(COLUMN_Comp_FAX_NO);
                                String shopName = stdAccessResultSet.getString(COLUMN_SHOPNAME);
                                String shopNumber = stdAccessResultSet.getString(COLUMN_SHOPNUMBER);
                                String logo = stdAccessResultSet.getString(COLUMN_Logo);
                                String vatNo = stdAccessResultSet.getString(COLUMN_VAT_NO);
                                String adr1 = stdAccessResultSet.getString(COLUMN_ADR_1);
                                String adr2 = stdAccessResultSet.getString(COLUMN_ADR_2);
                                String adr3 = stdAccessResultSet.getString(COLUMN_ADR_3);
                                String telNo = stdAccessResultSet.getString(COLUMN_TEL_NO);
                                String faxNo = stdAccessResultSet.getString(COLUMN_FAX_NO);
                                String openingHours = stdAccessResultSet.getString(COLUMN_Opening_Hours);
                                String companyName = stdAccessResultSet.getString(COLUMN_COMPANY_NAME);
                                int cashorId = stdAccessResultSet.getInt(COLUMN_CASHOR_id);
                                String lastModified = stdAccessResultSet.getString(LastModified);
                                String dateCreated = stdAccessResultSet.getString(DateCreated);
                                String posNum = stdAccessResultSet.getString(COLUMN_POS_Num);

                                // Call the insertStdAccessData method to insert data into std_access table
                                databaseHelper.insertStdAccessData(CompAd1, CompAd2, CompAd3, CompTel, brnValue, compFaxNo, shopName, shopNumber, logo, vatNo,
                                        adr1, adr2, adr3, telNo, faxNo, openingHours, companyName, cashorId, lastModified, dateCreated);


                                String usersQuery = "SELECT * FROM POSUser WHERE ShopName = ?";
                                PreparedStatement usersStatement = conn.prepareStatement(usersQuery);
                                usersStatement.setString(1, shopName);
                                ResultSet usersResultSet = usersStatement.executeQuery();
                                Log.d("user", usersQuery);
                                while (usersResultSet.next()) {
                                    // Retrieve data from the users table
                                    cashorId = usersResultSet.getInt(COLUMN_CASHOR_id);
                                    String pin = usersResultSet.getString(COLUMN_PIN);
                                    int cashorLevel = usersResultSet.getInt(COLUMN_CASHOR_LEVEL);
                                    String cashorName = usersResultSet.getString(COLUMN_CASHOR_NAME);
                                    String cashorShop = usersResultSet.getString(COLUMN_CASHOR_Shop);
                                    String cashorDepartment = usersResultSet.getString(COLUMN_CASHOR_DEPARTMENT);
                                    String dateCreatedUsers = usersResultSet.getString(DateCreated);
                                    String lastModifiedUsers = usersResultSet.getString(LastModified);

                                    // Call the method to insert this data into your target table
                                    // Modify the method as needed to accept these parameters
                                    databaseHelper.insertUserDatas(cashorId, pin, cashorLevel, cashorName, cashorShop, cashorDepartment, dateCreatedUsers, lastModifiedUsers);
                                }

                            }


                            String query = "SELECT * FROM POSITEMS";
                            resultSets = statement.executeQuery(query);
                            Log.d("Items", query);
                            while (resultSets.next()) {
                                // Process each row of data here
                                String barcode = resultSets.getString(Barcode);
                                String itemname = resultSets.getString(Name);
                                String desc = resultSets.getString(DESC);
                                String category = resultSets.getString(Category);
                                String quantity = resultSets.getString(Quantity);
                                String department = resultSets.getString(Department);
                                String longDescription = resultSets.getString(LongDescription);
                                String subDepartment = resultSets.getString(SubDepartment);
                                String price = resultSets.getString(Price);
                                //price
                                String price2 = resultSets.getString(Price2);
                                String price3 = resultSets.getString(Price3);
                                String priceAfterDiscount = resultSets.getString(PriceAfterDiscount);
                                //priceafterdisc
                                String priceAfterDiscount2 = resultSets.getString(Price2AfterDiscount);
                                String priceAfterDiscount3 = resultSets.getString(Price3AfterDiscount);
                                String vAT = resultSets.getString(VAT);
                                String expiryDate = resultSets.getString(ExpiryDate);
                                String availableForSale = resultSets.getString(AvailableForSale);
                                if (availableForSale.equals("1")) {
                                    availableForSale = "true";
                                } else if (availableForSale.equals("0")) {
                                    availableForSale = "false";
                                }
                                String soldBy = resultSets.getString(SoldBy);
                                String image = resultSets.getString(Image);
                                String sku = resultSets.getString(SKU);
                                String variant = resultSets.getString(Variant);
                                String cost = resultSets.getString(Cost);
                                String weight = resultSets.getString(Weight);
                                String userId = resultSets.getString(UserId);
                                String nature = resultSets.getString(Nature);
                                String taxCode = resultSets.getString(TaxCode);
                                String currency = resultSets.getString(Currency);
                                //rate
                                String rateDiscount = resultSets.getString(RateDiscount);
                                //AmountDiscount
                                String amountDiscount = resultSets.getString(AmountDiscount);

                                String itemCode = resultSets.getString(ItemCode);
                                String totalDiscount = resultSets.getString(TotalDiscount);
                                //totalDiscount
                                String totalDiscount2 = resultSets.getString(TotalDiscount2);
                                String totalDiscount3 = resultSets.getString(TotalDiscount3);
                                String dateCreated = resultSets.getString(DateCreated);
                                String lastModified = resultSets.getString(LastModified);
                                String syncStatus = resultSets.getString(SyncStatus);
                                //hasoptions
                                String Hasoptions = resultSets.getString(hasoptions);
                                //comment
                                String Comment = resultSets.getString(comment);
                                //related_item
                                String Related_item = resultSets.getString(related_item);
                                String Related_item2 = resultSets.getString(related_item2);
                                String Related_item3 = resultSets.getString(related_item3);
                                String Related_item4 = resultSets.getString(related_item4);
                                String Related_item5 = resultSets.getString(related_item5);
                                //hasSupplements
                                String HasSupplements = resultSets.getString(hasSupplements);
                                //relatedSupplements
                                String RelatedSupplements = resultSets.getString(relatedSupplements);

                                dbManager.insertwithnewbarcode(itemname,RelatedSupplements, Comment,desc, price,price2,price3,rateDiscount,amountDiscount, category, barcode, Float.parseFloat(weight), department,
                                        subDepartment, longDescription, quantity, expiryDate, vAT,
                                        availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,Hasoptions,
                                        nature, currency, itemCode, taxCode, totalDiscount,totalDiscount2,totalDiscount3, Double.parseDouble(priceAfterDiscount),Double.parseDouble(priceAfterDiscount2),Double.parseDouble(priceAfterDiscount3),Related_item,Related_item2,Related_item3,Related_item4,Related_item5,HasSupplements, syncStatus);

                                // Redirect to the Product activity
                                Intent intent = new Intent(SyncActivitySync.this, login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }


                } else {
                    showToast("No items found in MSSQL database.\"");
                }
            }

            resultSets.close();
            resultSet.close();
            statement.close();
            dbManager.close();
        } catch (SQLException se) {
            Log.e("REMOTE_DATA_TEST_ERROR1", se.getMessage());
        } catch (Exception e) {
            Log.e("REMOTE_DATA_TEST_ERROR2", e.getMessage());
        }
    }

    private void showToast(final String message) {
       Log.d("message",message);
    }





    public static void startSync(Context context, String brn, String password, String servername, String dbname,String username) {
        Intent intent = new Intent(context, SyncActivitySync.class);
        intent.putExtra("BRN", brn);
        intent.putExtra("Password", password);
        intent.putExtra("servername", servername);
        intent.putExtra("dbname", dbname);
        intent.putExtra("username", username);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }



}
