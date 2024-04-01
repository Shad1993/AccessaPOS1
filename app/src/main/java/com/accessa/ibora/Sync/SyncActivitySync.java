package com.accessa.ibora.Sync;

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

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncActivitySync extends IntentService {
    private DatabaseHelper mDatabaseHelper;
    private static final String TAG = "SyncService";

    // Your database connection parameters
    private static final String _user = "sa";
    private static final String _pass = "Logi2131";
    private static final String _DB = "IboraPOS";
    private static final String _server = "192.168.1.89";
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
            String shopnum = intent.getStringExtra("shopnum");
            String tillnum = intent.getStringExtra("tillnum");

            // Check login credentials
            if (checkLogin(conn, brn, password)) {

                // Login is valid, proceed with data insertion
                syncdata(conn, brn, password,shopnum,tillnum);
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

    private void syncdata(Connection conn,String brn, String password,String shopnum,String tillnum ) {

        try {
            // Step 1: Fetch data from the local SQLite database
            mDatabaseHelper = new DatabaseHelper(this);
            Cursor localCursor = mDatabaseHelper.getAllItems();

            getdataFromMssql(conn,brn,shopnum,tillnum);

        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
        }
    }
    private boolean checkLogin(Connection conn, String brn, String password) {
        try {
            // Construct and execute an SQL query to check login credentials
            String query = "SELECT * FROM std_access WHERE brn_no = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, brn);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the query returned any rows (login is valid)
            return resultSet.next();

        } catch (SQLException se) {
            Log.e("LOGIN_ERROR", se.getMessage());
        }
        return false; // Login check failed
    }

    private void getdataFromMssql(Connection conn,String brn,String shopnum,String tillnum) {

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
// Define your SQL query to check for the presence of the POS number in the POS table
                    String checkPosQuery = "SELECT COUNT(*) FROM POSTable WHERE TerminalNumber = ?";
                    PreparedStatement checkPosStatement = conn.prepareStatement(checkPosQuery);
                    checkPosStatement.setString(1, tillnum);

// Execute the query to check if the POS number exists
                    ResultSet checkPosResultSet = checkPosStatement.executeQuery();

                    if (checkPosResultSet.next()) {
                        int count = checkPosResultSet.getInt(1);

                        if (count > 0) {
                            String stdAccessQuery = "SELECT * FROM std_access WHERE brn_no = ? AND ShopNumber = ? ";
                            PreparedStatement stdAccessStatement = conn.prepareStatement(stdAccessQuery);
                            stdAccessStatement.setString(1, brn);
                            stdAccessStatement.setString(2, shopnum);
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
                                        adr1, adr2, adr3, telNo, faxNo, openingHours, companyName, cashorId, lastModified, dateCreated, tillnum);


                                String usersQuery = "SELECT * FROM Users WHERE ShopName = ?";
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
                                Intent intent = new Intent(SyncActivitySync.this, login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }showToast("POS Number not  found .");
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
            Log.e("REMOTE_DATA_TEST_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("REMOTE_DATA_TEST_ERROR", e.getMessage());
        }
    }

    private void showToast(final String message) {
       Log.d("message",message);
    }





    public static void startSync(Context context, String brn, String password, String shopnum, String tillnum) {
        Intent intent = new Intent(context, SyncActivitySync.class);
        intent.putExtra("BRN", brn);
        intent.putExtra("Password", password);
        intent.putExtra("shopnum", shopnum);
        intent.putExtra("tillnum", tillnum);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }



}
