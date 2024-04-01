package com.accessa.ibora.Sync;

import static com.accessa.ibora.product.items.DatabaseHelper.AvailableForSale;
import static com.accessa.ibora.product.items.DatabaseHelper.Barcode;
import static com.accessa.ibora.product.items.DatabaseHelper.Category;
import static com.accessa.ibora.product.items.DatabaseHelper.Cost;
import static com.accessa.ibora.product.items.DatabaseHelper.Currency;
import static com.accessa.ibora.product.items.DatabaseHelper.DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.DateCreated;
import static com.accessa.ibora.product.items.DatabaseHelper.Department;
import static com.accessa.ibora.product.items.DatabaseHelper.ExpiryDate;
import static com.accessa.ibora.product.items.DatabaseHelper.Image;
import static com.accessa.ibora.product.items.DatabaseHelper.ItemCode;
import static com.accessa.ibora.product.items.DatabaseHelper.LastModified;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.Name;
import static com.accessa.ibora.product.items.DatabaseHelper.Nature;
import static com.accessa.ibora.product.items.DatabaseHelper.Price;
import static com.accessa.ibora.product.items.DatabaseHelper.PriceAfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Quantity;
import static com.accessa.ibora.product.items.DatabaseHelper.SKU;
import static com.accessa.ibora.product.items.DatabaseHelper.SoldBy;
import static com.accessa.ibora.product.items.DatabaseHelper.SubDepartment;
import static com.accessa.ibora.product.items.DatabaseHelper.SyncStatus;
import static com.accessa.ibora.product.items.DatabaseHelper.TaxCode;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.UserId;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.Variant;
import static com.accessa.ibora.product.items.DatabaseHelper.Weight;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncService extends IntentService {
    private DatabaseHelper mDatabaseHelper;
    private static final String TAG = "SyncService";

    // Your database connection parameters
    private static final String _user = "sa";
    private static final String _pass = "Logi2131";
    private static final String _DB = "IboraPOS";
    private static final String _server = "192.168.1.89";

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // This method will run in the background when the service is started
        Connection conn = start();
        if (conn != null) {
            performBidirectionalSync(conn);
        }
    }

    public Connection start() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);

            // If the connection is successful, show a "Connection successful" toast
            showToast("Connection successful");

        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }

    private void performBidirectionalSync(Connection conn) {
        try {
            // Step 1: Fetch data from the local SQLite database
            mDatabaseHelper = new DatabaseHelper(this);
            Cursor localCursor = mDatabaseHelper.getAllItems();

            getItemsFromMssql(conn);

        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
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



                    String query = "SELECT * FROM Items";
                    resultSets = statement.executeQuery(query);
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





                            dbManager.insertwithnewbarcode(itemname, desc, price, category, barcode, Float.parseFloat(weight), department,
                                    subDepartment, longDescription, quantity, expiryDate, vAT,
                                    availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,
                                    nature, currency, itemCode, taxCode, totalDiscount, Double.parseDouble(priceAfterDiscount), syncStatus);

                        // Redirect to the Product activity
                        Intent intent = new Intent(SyncService.this, Product.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                } else {
                    showToast("No items found in MSSQL database.");
                }
            }
            resultSets.close();
            resultSet.close();
            statement.close();
            dbManager.close();
        } catch (SQLException se) {
            Log.e("REMOTE_DATA_TEST_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("REMOTE_DATA_TEST_ERROR", e.getMessage());
        }
    }

    private void showToast(String message) {
        // Display a toast (or update UI) from the background service
        Log.d(TAG, message);

    }

    public static void startSync(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}
