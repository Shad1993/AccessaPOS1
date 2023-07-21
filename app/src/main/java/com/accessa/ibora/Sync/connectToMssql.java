package com.accessa.ibora.Sync;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;

import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class connectToMssql extends AppCompatActivity {
    private DatabaseHelper mDatabaseHelper;

    @SuppressLint("NewApi")
    String _user = "db_a9c818_test_admin";
    String _pass = "Test1234";
    String _DB = "db_a9c818_test";
    String _server = "SQL8005.site4now.net";

private  List<Item> localDataList ;
       /*     String _user = "reshad";
    String _pass = "reshad1234";
    String _DB = "TPCentralDB";
    String _server = "192.168.1.97\\ACC_ASST_INT";
  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Connection conn = start();
        if (conn != null) {
            performBidirectionalSync(conn);
        }

       // startSync(null); // Since we are not using the 'view' parameter, pass null
    }



    public void startSync(View view) {
        // Start the synchronization process in the background
        new SyncTask().execute();
    }

    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Perform the synchronization here
            Connection conn = start();
            if (conn != null) {
                performBidirectionalSync(conn);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update UI or show a toast when the synchronization is completed
            showToast("Sync completed");
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
            // Process the localCursor to retrieve data from the local SQLite database
            // You may need to convert the data from the Cursor to a suitable format (e.g., List of POJOs)./


            // Step 2: Fetch data from the remote MSSQL database
            String selectMssqlQuery = "SELECT * FROM Items";
            Statement statement = conn.createStatement();
            ResultSet mssqlResultSet = statement.executeQuery(selectMssqlQuery);
            // Process the mssqlResultSet to retrieve data from the remote MSSQL database
            // You may need to convert the data from the ResultSet to a suitable format (e.g., List of POJOs).
            // Test if there is data in the remote MSSQL database
            testRemoteData(conn);
            // Step 3: Compare data between the databases and identify differences
            List<Item> localDataList = convertCursorToLocalDataList(localCursor);
            List<Item> remoteDataList = convertResultSetToRemoteDataList(mssqlResultSet);

            // Assuming Item.getId() returns an int
            Set<Integer> localIds = new HashSet<>();
            Set<Integer> remoteIds = new HashSet<>();

            // Add unique identifiers to the sets
            for (Item localData : localDataList) {
                localIds.add(localData.getId());
            }

            for (Item remoteData : remoteDataList) {
                remoteIds.add(remoteData.getId());
            }

            // Find differences
            Set<Integer> recordsToUpdateOnLocal = new HashSet<>(remoteIds);
            recordsToUpdateOnLocal.removeAll(localIds);

            Set<Integer> recordsToUpdateOnRemote = new HashSet<>(localIds);
            recordsToUpdateOnRemote.removeAll(remoteIds);

            // Now you have two sets containing unique identifiers of records to update in each database
            // You can use these sets to retrieve the actual records that need updating in Steps 4 and 5

            // Step 4: Update both databases with the latest changes
            for (Item localData : localDataList) {
                if (recordsToUpdateOnLocal.contains(localData.getId())) {
                    // Update localData in the local SQLite database
                    mDatabaseHelper.updateLocalData(localData);
                }
            }

            for (Item remoteData : remoteDataList) {
                if (recordsToUpdateOnRemote.contains(remoteData.getId())) {
                    // Update remoteData in the remote MSSQL database
                    updateRemoteDataInMssql(remoteData);
                }
            }


            // Step 5: Handle conflicts and ensure data integrity
            for (Item localData : localDataList) {
                for (Item remoteData : remoteDataList) {
                    if (localData.getId() == remoteData.getId()) {
                        // Conflict resolution logic
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault());

                        try {
                            Date localDate = sdf.parse(localData.getLastModified());
                            Date remoteDate = sdf.parse(remoteData.getLastModified());

                            if (localDate.after(remoteDate)) {
                                // Update remoteData in the remote MSSQL database with localData
                                updateRemoteDataInMssql(localData);
                            } else {
                                // Update localData in the local SQLite database with remoteData
                                mDatabaseHelper.updateLocalData(remoteData);
                            }
                        } catch (ParseException e) {
                            Log.e("SYNC_ERROR", "Error parsing date: " + e.getMessage());
                        }

                        break; // Exit inner loop to avoid unnecessary comparisons
                    }
                }
            }


            // Close the ResultSet and statement
            if (mssqlResultSet != null) {
                mssqlResultSet.close();
            }
            statement.close();

        } catch (SQLException se) {
            Log.e("SYNC_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
        }
    }

    private void updateRemoteDataInMssql(Item remoteData) {
        Connection conn = null;
        Statement statement = null;

        try {
            // Establish the connection to the MSSQL database
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);
            statement = conn.createStatement();

            // Assuming your DataObject class has getters for each column (e.g., getColumnName())
            String updateQuery = "UPDATE Items SET "
                    + "Barcode='" + remoteData.getBarcode() + "', "
                    + "Name='" + remoteData.getName() + "', "
                    + "Description='" + remoteData.getDescription() + "', "
                    + "Category='" + remoteData.getCategory() + "', "
                    + "Quantity=" + remoteData.getQuantity() + ", "
                    + "Department='" + remoteData.getDepartment() + "', "
                    + "LongDescription='" + remoteData.getLongDescription() + "', "
                    + "SubDepartment='" + remoteData.getSubDepartment() + "', "
                    + "Price=" + remoteData.getPrice() + ", "
                    + "VAT='" + remoteData.getVAT() + "', "
                    + "ExpiryDate='" + remoteData.getExpiryDate() + "', "
                    + "AvailableForSale=" + (remoteData.getAvailableForSale() ? 1 : 0) + ", "
                    + "SoldBy='" + remoteData.getSoldBy() + "', "
                    + "Image='" + remoteData.getImage() + "', "
                    + "SKU='" + remoteData.getSKU() + "', "
                    + "Variant='" + remoteData.getVariant() + "', "
                    + "Cost=" + remoteData.getCost() + ", "
                    + "Weight=" + remoteData.getWeight() + ", "
                    + "UserId=" + remoteData.getUserId() + ", "
                    + "DateCreated='" + remoteData.getDateCreated() + "', "
                    + "LastModified='" + remoteData.getLastModified() + "' "
                    + "WHERE ID=" + remoteData.getId();

            // Execute the update query
            statement.executeUpdate(updateQuery);

            // Close the statement and connection
            statement.close();
            conn.close();

            // Log that the remote data has been updated
            Log.d("REMOTE_UPDATE", "Remote data updated successfully: " + remoteData.getId());

        } catch (ClassNotFoundException e) {
            Log.e("REMOTE_UPDATE_ERROR", "JDBC Driver not found");
        } catch (SQLException se) {
            Log.e("REMOTE_UPDATE_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("REMOTE_UPDATE_ERROR", e.getMessage());
        } finally {
            try {
                // Close the statement and connection in the finally block
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                Log.e("REMOTE_UPDATE_ERROR", se.getMessage());
            }
        }
    }


    private List<Item> convertCursorToLocalDataList(Cursor cursor) {
        List<Item> dataList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String longDescription = cursor.getString(cursor.getColumnIndex("longDescription"));
                float quantity = cursor.getFloat(cursor.getColumnIndex("quantity"));
                String department = cursor.getString(cursor.getColumnIndex("department"));
                String subDepartment = cursor.getString(cursor.getColumnIndex("subDepartment"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String barcode = cursor.getString(cursor.getColumnIndex("barcode"));
                float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                String expiryDate = cursor.getString(cursor.getColumnIndex("expiryDate"));
                String VAT = cursor.getString(cursor.getColumnIndex("VAT"));
                String soldBy = cursor.getString(cursor.getColumnIndex("soldBy"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String SKU = cursor.getString(cursor.getColumnIndex("SKU"));
                String variant = cursor.getString(cursor.getColumnIndex("variant"));
                String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
                String DateCreated = cursor.getString(cursor.getColumnIndex("DateCreated"));
                String LastModified = cursor.getString(cursor.getColumnIndex("LastModified"));
                float cost = cursor.getFloat(cursor.getColumnIndex("cost"));
                boolean AvailableForSale = cursor.getInt(cursor.getColumnIndex("AvailableForSale")) == 1;

                // Create a DataObject and add it to the list
                Item dataObject = new Item(id, name, description, price, longDescription, quantity, category, department, subDepartment, barcode, weight, expiryDate, VAT, soldBy, AvailableForSale, UserId, DateCreated, LastModified);
                dataObject.setImage(image);
                dataObject.setSKU(SKU);
                dataObject.setVariant(variant);
                dataObject.setCost(cost);

                dataList.add(dataObject);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    private void testRemoteData(Connection conn) {

        try {
            String selectQuery = "SELECT COUNT(*) as totalItems FROM Items";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {
                int totalItems = resultSet.getInt("totalItems");
                if (totalItems > 0) {
                    showToast("Total Items in MSSQL database: " + totalItems);
                } else {
                    showToast("No items found in MSSQL database.");
                    // If no items found in the remote database, insert data from the local SQLite database
                    for (Item localData : localDataList) {
                        insertDataIntoMssql(conn, localData);
                    }
                    showToast("Data inserted into MSSQL database.");
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException se) {
            Log.e("REMOTE_DATA_TEST_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("REMOTE_DATA_TEST_ERROR", e.getMessage());
        }
    }
    private void insertDataIntoMssql(Connection conn, Item localData) {
        try {
            // Create the SQL INSERT query
            String insertQuery = "INSERT INTO Items ("
                    + "Barcode, Name, Description, Category, Quantity, Department, "
                    + "LongDescription, SubDepartment, Price, VAT, ExpiryDate, "
                    + "AvailableForSale, SoldBy, Image, SKU, Variant, Cost, Weight, "
                    + "UserId, DateCreated, LastModified"
                    + ") VALUES ("
                    + "'" + localData.getBarcode() + "', "
                    + "'" + localData.getName() + "', "
                    + "'" + localData.getDescription() + "', "
                    + "'" + localData.getCategory() + "', "
                    + localData.getQuantity() + ", "
                    + "'" + localData.getDepartment() + "', "
                    + "'" + localData.getLongDescription() + "', "
                    + "'" + localData.getSubDepartment() + "', "
                    + localData.getPrice() + ", "
                    + "'" + localData.getVAT() + "', "
                    + "'" + localData.getExpiryDate() + "', "
                    + (localData.getAvailableForSale() ? 1 : 0) + ", "
                    + "'" + localData.getSoldBy() + "', "
                    + "'" + localData.getImage() + "', "
                    + "'" + localData.getSKU() + "', "
                    + "'" + localData.getVariant() + "', "
                    + localData.getCost() + ", "
                    + localData.getWeight() + ", "
                    + localData.getUserId() + ", "
                    + "'" + localData.getDateCreated() + "', "
                    + "'" + localData.getLastModified() + "'"
                    + ")";

            // Create a statement and execute the INSERT query
            Statement statement = conn.createStatement();
            statement.executeUpdate(insertQuery);
            statement.close();

            Log.d("INSERT_DATA", "Data inserted successfully into MSSQL database: " + localData.getId());

        } catch (SQLException se) {
            Log.e("INSERT_DATA_ERROR", se.getMessage());
        } catch (Exception e) {
            Log.e("INSERT_DATA_ERROR", e.getMessage());
        }
    }

    private List<Item> convertResultSetToRemoteDataList(ResultSet resultSet) throws SQLException {
        List<Item> dataList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            float price = resultSet.getFloat("price");
            String longDescription = resultSet.getString("longDescription");
            float quantity = resultSet.getFloat("quantity");
            String department = resultSet.getString("department");
            String subDepartment = resultSet.getString("subDepartment");
            String category = resultSet.getString("category");
            String barcode = resultSet.getString("barcode");
            float weight = resultSet.getFloat("weight");
            String expiryDate = resultSet.getString("expiryDate");
            String VAT = resultSet.getString("VAT");
            String soldBy = resultSet.getString("soldBy");
            String image = resultSet.getString("image");
            String SKU = resultSet.getString("SKU");
            String variant = resultSet.getString("variant");
            String UserId = resultSet.getString("UserId");
            String DateCreated = resultSet.getString("DateCreated");
            String LastModified = resultSet.getString("LastModified");
            float cost = resultSet.getFloat("cost");
            boolean AvailableForSale = resultSet.getInt("AvailableForSale") == 1;

            // Create a DataObject and add it to the list
            Item dataObject = new Item(id, name, description, price, longDescription, quantity, category, department, subDepartment, barcode, weight, expiryDate, VAT, soldBy, AvailableForSale, UserId, DateCreated, LastModified);
            dataObject.setImage(image);
            dataObject.setSKU(SKU);
            dataObject.setVariant(variant);
            dataObject.setCost(cost);

            dataList.add(dataObject);
        }
        resultSet.close();
        return dataList;
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
