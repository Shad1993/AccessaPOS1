package com.accessa.ibora.Sync;

import static androidx.constraintlayout.widget.StateSet.TAG;
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
import android.widget.Toast;

import com.accessa.ibora.product.items.AddItemActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncAddToMssql extends IntentService {
    private DatabaseHelper mDatabaseHelper;
    private static final String TAG = "SyncService";

    // Your database connection parameters
    private static final String _user = "db_a9c818_test_admin";
    private static final String _pass = "Test1234";
    private static final String _DB = "db_a9c818_test";
    private static final String _server = "SQL8005.site4now.net";

    public SyncAddToMssql() {
        super("SyncAddToMssql");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        // This method will run in the background when the service is started
        Connection conn = start();
        if (conn != null) {
            // Retrieve data from the Intent
            String name = intent.getStringExtra("Name");
            String desc = intent.getStringExtra("Desc");
            String price = intent.getStringExtra("Price");
            String price2 = intent.getStringExtra("Price2");
            String price3 = intent.getStringExtra("Price3");
            String category = intent.getStringExtra("Category");
            String barcode = intent.getStringExtra("Barcode");
            float weight = intent.getFloatExtra("Weight", 0.0f);
            String department = intent.getStringExtra("Department");
            String subDepartment = intent.getStringExtra("SubDepartment");
            String longDescription = intent.getStringExtra("LongDescription");
            String quantity = intent.getStringExtra("Quantity");
            String expiryDate = intent.getStringExtra("ExpiryDate");
            String vat = intent.getStringExtra("VAT");
            String availableForSale = intent.getStringExtra("AvailableForSale");
            String soldBy = intent.getStringExtra("SoldBy");
            String image = intent.getStringExtra("Image");
            String variant = intent.getStringExtra("Variant");
            String sku = intent.getStringExtra("SKU");
            String cost = intent.getStringExtra("Cost");
            String userId = intent.getStringExtra("UserId");
            String dateCreated = intent.getStringExtra("DateCreated");
            String lastModified = intent.getStringExtra("LastModified");
            String selectedNature = intent.getStringExtra("SelectedNature");
            String selectedCurrency = intent.getStringExtra("SelectedCurrency");
            String itemCode = intent.getStringExtra("ItemCode");
            String vatCode = intent.getStringExtra("VATCode");
            String valueOf = intent.getStringExtra("ValueOf");
            String discountedval2 = intent.getStringExtra("Discountedamount2");
            String discountedval3 = intent.getStringExtra("Discountedamount3");
            double currentPrice = intent.getDoubleExtra("CurrentPrice", 0.0);
            double currentPrice2 = intent.getDoubleExtra("CurrentPrice2", 0.0);
            double currentPrice3 = intent.getDoubleExtra("CurrentPrice3", 0.0);
            int discountAmount = intent.getIntExtra("Discountamount", 0);

            // Insert the data into the MSSQL database using these values
            add(conn,name, desc,discountAmount, price,price2,price3, category, barcode, weight, department, subDepartment, longDescription, quantity, expiryDate, vat, availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified, selectedNature, selectedCurrency, itemCode, vatCode, valueOf,discountedval2,discountedval3, currentPrice,currentPrice2,currentPrice3);

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

    private void add(Connection conn,String name, String desc,int discamount, String price,String price2,String price3, String category, String barcode, float weight, String department, String subDepartment, String longDescription, String quantity, String expiryDate, String vat, String availableForSale, String soldBy, String image, String variant, String sku, String cost, String userId, String dateCreated, String lastModified, String selectedNature, String selectedCurrency, String itemCode, String vatCode, String valueOf, String discountedamount2, String discountedamount3, double currentPrice,double currentPrice2,double currentPrice3) {

        try {
            // Step 1: Fetch data from the local SQLite database
            mDatabaseHelper = new DatabaseHelper(this);
            Cursor localCursor = mDatabaseHelper.getAllItems();

            insertItemsFromMssql(conn,name,  desc,discamount,  price,price2,price3,  category,  barcode,  weight,  department,  subDepartment,  longDescription,  quantity,  expiryDate,  vat,  availableForSale,  soldBy,  image,  variant,  sku,  cost,  userId,  dateCreated,  lastModified,  selectedNature,  selectedCurrency,  itemCode,  vatCode,  valueOf,discountedamount2,discountedamount3, currentPrice,currentPrice2,currentPrice3
            );

        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
        }
    }
    private void insertItemsFromMssql(Connection conn ,String name, String desc,int discamount, String price,String price2, String price3, String category, String barcode, float weight, String department, String subDepartment, String longDescription, String quantity, String expiryDate, String vat, String availableForSale, String soldBy, String image, String variant, String sku, String cost, String userId, String dateCreated, String lastModified, String selectedNature, String selectedCurrency, String itemCode, String vatCode, String valueOf,String discountedamount2,String discountedamount3, double currentPrice,double currentPrice2,double currentPrice3) {


        try {
            // Construct and execute your SQL insert statement here
            String insertQuery = "INSERT INTO Items (name, description,DiscountAmount, price,price2,price3, category, Barcode, Weight, Department, SubDepartment, LongDescription, Quantity, ExpiryDate, VAT, AvailableForSale, SoldBy, Image, Variant, SKU, Cost, UserId, DateCreated, LastModified, Nature, Currency, ItemCode, TaxCode, TotalDiscount, CurrentPrice,SyncStatus) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, desc);
            preparedStatement.setString(3, price);
            preparedStatement.setString(4, price2);
            preparedStatement.setString(5, price3);
            preparedStatement.setString(6, category);
            preparedStatement.setString(7, barcode);
            preparedStatement.setFloat(8, weight);
            preparedStatement.setString(9, department);
            preparedStatement.setString(10, subDepartment);
            preparedStatement.setString(11, longDescription);
            preparedStatement.setString(12, quantity);
            preparedStatement.setString(13, expiryDate);
            preparedStatement.setString(14, vat);
            preparedStatement.setString(15, availableForSale);
            preparedStatement.setString(16, soldBy);
            preparedStatement.setString(17, image);
            preparedStatement.setString(18, variant);
            preparedStatement.setString(19, sku);
            preparedStatement.setString(20, cost);
            preparedStatement.setString(21, userId);
            preparedStatement.setString(22, dateCreated);
            preparedStatement.setString(23, lastModified);
            preparedStatement.setString(24, selectedNature);
            preparedStatement.setString(25, selectedCurrency);
            preparedStatement.setString(26, itemCode);
            preparedStatement.setString(27, vatCode);
            preparedStatement.setString(28, valueOf);
            preparedStatement.setString(29, discountedamount2);
            preparedStatement.setString(30, discountedamount3);
            preparedStatement.setDouble(31, currentPrice);
            preparedStatement.setDouble(32, currentPrice2);
            preparedStatement.setDouble(33, currentPrice3);
            preparedStatement.setInt(34, discamount);
            preparedStatement.setString(35, "Online");

            preparedStatement.executeUpdate();
            String test="test";
            // Construct and execute your SQL insert statement for the Cost table here
            String insertCostQuery = "INSERT INTO Cost (Barcode, SKU,Cost, LastModified,UserId,CodeFournisseur) " +
                    "VALUES (?, ?, ?,?, ?,?)";
            PreparedStatement preparedStatements = conn.prepareStatement(insertCostQuery);
            preparedStatements.setString(1, barcode);
            preparedStatements.setString(2, sku);
            preparedStatements.setString(3, cost);
            preparedStatements.setString(4, lastModified);
            preparedStatements.setString(5, userId);
            preparedStatements.setString(6, test);

            preparedStatements.executeUpdate();
            SyncService.startSync(this);
            // If the insertion is successful, you can show a success message or handle as needed
        } catch (SQLException se) {
            if (se.getMessage() != null) {
                Log.e("INSERT_ERROR", se.getMessage());
            } else {
                Log.e("INSERT_ERROR", "SQL Exception occurred without a message.");
            }
        } catch (Exception e) {
            Log.e("INSERT_ERROR", e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void showToast(String message) {
        // Display a toast (or update UI) from the background service
        Log.d(TAG, message);

    }

    public static void startSync(Context context, String name, String desc,int DiscountAmount, String price,String price2,String price3, String category, String barcode, float weight, String department, String subDepartment, String longDescription, String quantity, String expiryDate, String vat, String availableForSale,String options,String hascomment, String optionIds,String optionId2,String optionId3,String optionId4,String optionId5, String soldBy, String image, String variant, String sku, String cost, String userId, String dateCreated, String lastModified, String selectedNature, String selectedCurrency, String itemCode, String vatCode, String valueOf,String discountedamount2,String discountedamount3, double currentPrice,double currentPrice2,double currentPrice3) {
        Intent intent = new Intent(context, SyncAddToMssql.class);
        intent.putExtra("Name", name);
        intent.putExtra("Desc", desc);
        intent.putExtra("Price", price);
        intent.putExtra("Price2", price2);
        intent.putExtra("Price3", price3);
        intent.putExtra("Category", category);
        intent.putExtra("Barcode", barcode);
        intent.putExtra("Weight", weight);
        intent.putExtra("Department", department);
        intent.putExtra("SubDepartment", subDepartment);
        intent.putExtra("LongDescription", longDescription);
        intent.putExtra("Quantity", quantity);
        intent.putExtra("ExpiryDate", expiryDate);
        intent.putExtra("VAT", vat);
        intent.putExtra("AvailableForSale", availableForSale);
        intent.putExtra("hasoptions", options);
        intent.putExtra("hascomment", hascomment);
        intent.putExtra("related_item", optionIds);
        intent.putExtra("related_item2", optionId2);
        intent.putExtra("related_item3", optionId3);
        intent.putExtra("related_item4", optionId4);
        intent.putExtra("related_item5", optionId5);
        intent.putExtra("SoldBy", soldBy);
        intent.putExtra("Image", image);
        intent.putExtra("Variant", variant);
        intent.putExtra("SKU", sku);
        intent.putExtra("Cost", cost);
        intent.putExtra("UserId", userId);
        intent.putExtra("DateCreated", dateCreated);
        intent.putExtra("LastModified", lastModified);
        intent.putExtra("SelectedNature", selectedNature);
        intent.putExtra("SelectedCurrency", selectedCurrency);
        intent.putExtra("ItemCode", itemCode);
        intent.putExtra("VATCode", vatCode);
        intent.putExtra("ValueOf", valueOf);
        intent.putExtra("Discountedamount2", discountedamount2);
        intent.putExtra("Discountedamount3", discountedamount3);
        intent.putExtra("CurrentPrice", currentPrice);
        intent.putExtra("CurrentPrice2", currentPrice2);
        intent.putExtra("CurrentPrice3", currentPrice3);
        intent.putExtra("Discountamount", DiscountAmount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }


}
