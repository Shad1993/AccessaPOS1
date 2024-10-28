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
import static com.accessa.ibora.product.items.DatabaseHelper.SEAT_COUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SKU;
import static com.accessa.ibora.product.items.DatabaseHelper.STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.ShopNum;
import static com.accessa.ibora.product.items.DatabaseHelper.SoldBy;
import static com.accessa.ibora.product.items.DatabaseHelper.SubDepartment;
import static com.accessa.ibora.product.items.DatabaseHelper.SyncStatus;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_COUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.TaxCode;
import static com.accessa.ibora.product.items.DatabaseHelper.TillNum;
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

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.Rooms.RoomsFragment;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncServiceroomstable extends IntentService {
    private DatabaseHelper mDatabaseHelper;
    private static final String TAG = "SyncService";
    private static final String CHANNEL_ID = "SyncServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    // Your database connection parameters
    private static final String _user = "sa";
    private static final String _pass = "Logi2131";
    private static final String _DB = "IboraResto";
    private static final String _server = "192.168.1.89";

    public SyncServiceroomstable() {
        super("SyncService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Sync Service")
                    .setContentText("Running sync service")
                    .setSmallIcon(R.drawable.iboralogo) // Replace with your app's icon
                    .build();
        }

        startForeground(1, notification);
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
           // mDatabaseHelper = new DatabaseHelper(this);
          //  Cursor localCursor = mDatabaseHelper.getAllItems();


            getRoomsAndTablesFromMssql(conn);
        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
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
                    returnHome();
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

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Rooms_fragment");
        startActivity(home_intent1);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sync Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    private Notification buFildNotification() {
        Intent notificationIntent = new Intent(this, Product.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sync Service")
                .setContentText("Running sync service")
                .setSmallIcon(R.drawable.iboralogo) // Replace with your app's icon
                .setContentIntent(pendingIntent);

        return builder.build();
    }
    private void showToast(String message) {
        // Display a toast (or update UI) from the background service
        Log.d(TAG, message);

    }

    public static void startSync(Context context) {
        Intent intent = new Intent(context, SyncServiceroomstable.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}
