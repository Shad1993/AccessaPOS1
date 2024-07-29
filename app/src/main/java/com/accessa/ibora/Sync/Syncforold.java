package com.accessa.ibora.Sync;

import static com.accessa.ibora.product.items.DatabaseHelper.AmountDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.AvailableForSale;
import static com.accessa.ibora.product.items.DatabaseHelper.Barcode;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_DEPARTMENT;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_LEVEL;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_Shop;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_PIN;
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
import static com.accessa.ibora.product.items.DatabaseHelper.Name;
import static com.accessa.ibora.product.items.DatabaseHelper.Nature;
import static com.accessa.ibora.product.items.DatabaseHelper.Price;
import static com.accessa.ibora.product.items.DatabaseHelper.Price2;
import static com.accessa.ibora.product.items.DatabaseHelper.Price2AfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Price3;
import static com.accessa.ibora.product.items.DatabaseHelper.Price3AfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.PriceAfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Quantity;
import static com.accessa.ibora.product.items.DatabaseHelper.RateDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.SKU;
import static com.accessa.ibora.product.items.DatabaseHelper.SoldBy;
import static com.accessa.ibora.product.items.DatabaseHelper.SubDepartment;
import static com.accessa.ibora.product.items.DatabaseHelper.SyncStatus;
import static com.accessa.ibora.product.items.DatabaseHelper.TaxCode;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount2;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount3;
import static com.accessa.ibora.product.items.DatabaseHelper.UserId;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.Variant;
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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.accessa.ibora.Admin.AdminMenuFragment;
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

public class Syncforold extends IntentService {
    private DatabaseHelper mDatabaseHelper;
    private static final String TAG = "SyncService";

    // Your database connection parameters
    private static final String _user = "sa";
    private static final String _pass = "Logi2131";
    private static final String _DB = "IboraResto";
    private static final String _server = "192.168.1.89";

    public Syncforold() {
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
            String selectQuery = "SELECT COUNT(*) as totalItems FROM ITEMS";
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

                    String query = "SELECT * FROM ITEMS";
                    resultSets = statement.executeQuery(query);
                    Log.d("items", query);
                    while (resultSets.next()) {
                        // Process each row of data here
                        String _id= resultSets.getString(_ID);
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
                        String price2 = resultSets.getString(Price2);
                        String price3 = resultSets.getString(Price3);
                        String priceAfterDiscount2 = resultSets.getString(Price2AfterDiscount);
                        String priceAfterDiscount3 = resultSets.getString(Price3AfterDiscount);
                        String rateDiscount = resultSets.getString(RateDiscount);
                        String amountDiscount = resultSets.getString(AmountDiscount);
                        String totalDiscount2 = resultSets.getString(TotalDiscount2);
                        String totalDiscount3 = resultSets.getString(TotalDiscount3);
                        String Hasoptions = resultSets.getString(hasoptions);
                        if(Hasoptions.equals("1")){
                            Hasoptions="true";
                        } else if (Hasoptions.equals("0")) {
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
                        //relatedSupplements
                        String RelatedSupplements = resultSets.getString(relatedSupplements);

                        Log.e("Hasoptions",Hasoptions);
                        Log.e("Related_item",Related_item);

                        databaseHelper.insertItemsDatas(_id,itemname,Comment,RelatedSupplements, desc, price,price2,price3,rateDiscount,amountDiscount, category, barcode, Float.parseFloat(weight), department,
                                subDepartment, longDescription, quantity, expiryDate, vAT,
                                availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,Hasoptions,
                                nature, currency, itemCode, taxCode, totalDiscount,totalDiscount2,totalDiscount3, Double.parseDouble(priceAfterDiscount),Double.parseDouble(priceAfterDiscount2),Double.parseDouble(priceAfterDiscount3),Related_item,Related_item2,Related_item3,Related_item4,Related_item5,HasSupplements, syncStatus);


                        // dbManager.insertwithnewbarcode(itemname,Comment,RelatedSupplements, desc, price,price2,price3,rateDiscount,amountDiscount, category, barcode, Float.parseFloat(weight), department,
                        //             subDepartment, longDescription, quantity, expiryDate, vAT,
                        //           availableForSale, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,Hasoptions,
                        //          nature, currency, itemCode, taxCode, totalDiscount,totalDiscount2,totalDiscount3, Double.parseDouble(priceAfterDiscount),Double.parseDouble(priceAfterDiscount2),Double.parseDouble(priceAfterDiscount3),Related_item,Related_item2,Related_item3,Related_item4,Related_item5,HasSupplements, syncStatus);

                        // Redirect to the Product activity
                        Intent intent = new Intent(Syncforold.this, Product.class);

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

    private void showToast(String message) {
        // Display a toast (or update UI) from the background service
        Log.d(TAG, message);

    }

    public static void startSync(Context context) {
        Intent intent = new Intent(context, Syncforold.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}