package com.accessa.ibora.Sync.MasterSync;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.accessa.ibora.product.items.DatabaseHelper.AmountDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.AvailableForSale;
import static com.accessa.ibora.product.items.DatabaseHelper.Barcode;
import static com.accessa.ibora.product.items.DatabaseHelper.CASH_REPORT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COST_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COUNTING_REPORT_CASHIER_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.COUNTING_REPORT_DATETIME;
import static com.accessa.ibora.product.items.DatabaseHelper.COUNTING_REPORT_DIFFERENCE;
import static com.accessa.ibora.product.items.DatabaseHelper.COUNTING_REPORT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COUNTING_REPORT_TOTALIZER_TOTAL;
import static com.accessa.ibora.product.items.DatabaseHelper.COUNTING_REPORT_TOTAL_VALUE;
import static com.accessa.ibora.product.items.DatabaseHelper.Category;
import static com.accessa.ibora.product.items.DatabaseHelper.CodeFournisseur;
import static com.accessa.ibora.product.items.DatabaseHelper.Cost;
import static com.accessa.ibora.product.items.DatabaseHelper.Currency;
import static com.accessa.ibora.product.items.DatabaseHelper.DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.DateCreated;
import static com.accessa.ibora.product.items.DatabaseHelper.Department;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CURRENT_TIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_DATETIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_PAYMENT;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_POSNUM;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_SHOP_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTAL;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTALIZER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TRANSACTION_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.Image;
import static com.accessa.ibora.product.items.DatabaseHelper.ItemCode;
import static com.accessa.ibora.product.items.DatabaseHelper.LastModified;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.MERGED;
import static com.accessa.ibora.product.items.DatabaseHelper.MERGED_SET_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.Name;
import static com.accessa.ibora.product.items.DatabaseHelper.Nature;
import static com.accessa.ibora.product.items.DatabaseHelper.Price;
import static com.accessa.ibora.product.items.DatabaseHelper.PriceAfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.Quantity;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOMS;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.RateDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.SEAT_COUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.SKU;
import static com.accessa.ibora.product.items.DatabaseHelper.SKUCost;
import static com.accessa.ibora.product.items.DatabaseHelper.SUPPLEMENTS_OPTIONS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SUPPLEMENT_OPTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.SUPPLEMENT_OPTION_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SoldBy;
import static com.accessa.ibora.product.items.DatabaseHelper.SubDepartment;
import static com.accessa.ibora.product.items.DatabaseHelper.SyncStatus;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLES;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_COUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_SHIFT_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.TaxCode;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount2;
import static com.accessa.ibora.product.items.DatabaseHelper.TotalDiscount3;
import static com.accessa.ibora.product.items.DatabaseHelper.UserId;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANTS_OPTIONS_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANTS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_BARCODE;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.Variant;
import static com.accessa.ibora.product.items.DatabaseHelper.WAITER_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.Weight;
import static com.accessa.ibora.product.items.DatabaseHelper.comment;
import static com.accessa.ibora.product.items.DatabaseHelper.hasSupplements;
import static com.accessa.ibora.product.items.DatabaseHelper.hasoptions;
import static com.accessa.ibora.product.items.DatabaseHelper.relatedSupplements;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item2;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item3;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item4;
import static com.accessa.ibora.product.items.DatabaseHelper.related_item5;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.accessa.ibora.Sync.SyncService;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MssqlDataSync {
    private DatabaseHelper mDatabaseHelper;

    // Constructor
    public MssqlDataSync() {
    }
    public  Connection start(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        // Get SharedPreferences where the DB parameters are stored
        SharedPreferences preferences = context.getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);

        // Retrieve values from SharedPreferences (or use defaults if not set)
        String _user = preferences.getString("_user", null);
        String _pass = preferences.getString("_pass", null);
        String _DB = preferences.getString("_DB", null);
        String _server = preferences.getString("_server", null);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
             ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password=" + _pass + ";";
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
    private void showToast(String message) {
        // Display a toast (or update UI) from the background service
        Log.d(TAG, message);

    }
    public void syncTransactionsFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        try {
            // Start database connection
            mDatabaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            // Query SQLite database to retrieve relevant data
            Cursor cursor = db.rawQuery(
                    "SELECT t.* FROM " + TRANSACTION_TABLE_NAME + " t " +
                            "JOIN " + TRANSACTION_HEADER_TABLE_NAME + " h " +
                            "ON t." + TRANSACTION_ID + " = h." + TRANSACTION_TICKET_NO + " " +
                            "WHERE h." + TRANSACTION_STATUS + " = 'Completed'",
                    null);

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    int tableid = cursor.getInt(cursor.getColumnIndex("id"));
                    int roomid = cursor.getInt(cursor.getColumnIndex("room_id"));
                    String transcationId = cursor.getString(cursor.getColumnIndex("TranscationId"));
                    int itemId = cursor.getInt(cursor.getColumnIndex("ItemId"));
                    String transactionDate = cursor.getString(cursor.getColumnIndex("TransactionDate"));
                    int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                    double totalPrice = cursor.getDouble(cursor.getColumnIndex("TotalPrice"));
                    double vat = cursor.getDouble(cursor.getColumnIndex("VAT"));
                    String vatType = cursor.getString(cursor.getColumnIndex("VatType"));
                    String longDescription = cursor.getString(cursor.getColumnIndex("LongDescription"));
                    // Additional fields
                    int paidstatus = cursor.getInt(cursor.getColumnIndex("Paidstatus"));
                    String nature = cursor.getString(cursor.getColumnIndex("Nature"));
                    String taxCode = cursor.getString(cursor.getColumnIndex("TaxCode"));
                    String currency = cursor.getString(cursor.getColumnIndex("Currency"));
                    String itemCode = cursor.getString(cursor.getColumnIndex("ItemCode"));
                    int shopNo = cursor.getInt(cursor.getColumnIndex("ShopNo"));
                    double totaldisc = cursor.getDouble(cursor.getColumnIndex("TotalDisc"));
                    int terminalNo = cursor.getInt(cursor.getColumnIndex("TerminalNo"));
                    String datecreated = cursor.getString(cursor.getColumnIndex("DateCreated"));
                    String dateModified = cursor.getString(cursor.getColumnIndex("DateModified"));
                    String timeCreated = cursor.getString(cursor.getColumnIndex("TimeCreated"));
                    String timeModified = cursor.getString(cursor.getColumnIndex("TimeModified"));
                    String code = cursor.getString(cursor.getColumnIndex("Code"));
                    double unitPrice = cursor.getDouble(cursor.getColumnIndex("UnitPrice"));
                    int qte = cursor.getInt(cursor.getColumnIndex("Qte"));
                    double discount = cursor.getDouble(cursor.getColumnIndex("Discount"));
                    double vatBeforeDisc = cursor.getDouble(cursor.getColumnIndex("VAT_Before_Disc"));
                    double vatAfterDisc = cursor.getDouble(cursor.getColumnIndex("VAT_After_Disc"));
                    double totalHT_A = cursor.getDouble(cursor.getColumnIndex("TOTALHT_A"));
                    double totalTTC = cursor.getDouble(cursor.getColumnIndex("TotalTTC"));
                    int isTaxable = cursor.getInt(cursor.getColumnIndex("IsTaxable"));
                    String dateTransaction = cursor.getString(cursor.getColumnIndex("DateTransaction"));
                    String timeTransaction = cursor.getString(cursor.getColumnIndex("TimeTransaction"));
                    String barcode = cursor.getString(cursor.getColumnIndex("Barcode"));
                    double weights = cursor.getDouble(cursor.getColumnIndex("Weights"));
                    double totalHT_B = cursor.getDouble(cursor.getColumnIndex("TotalHT_B"));
                    String typeTax = cursor.getString(cursor.getColumnIndex("TYPETAX"));
                    String rayon = cursor.getString(cursor.getColumnIndex("Rayon"));
                    double currentPrice = cursor.getDouble(cursor.getColumnIndex("CurrentPrice"));
                    String famille = cursor.getString(cursor.getColumnIndex("Famille"));
                    String idSalesD = cursor.getString(cursor.getColumnIndex("IDSalesD"));
                    String totalizer = cursor.getString(cursor.getColumnIndex("Totalizer"));
                    String comment = cursor.getString(cursor.getColumnIndex("Comment"));
                    Log.d("select", "transaction selected");
                    // Insert data into MSSQL
                    insertTransactionIntoMSSQL(conn, _id,tableid, roomid,paidstatus, transcationId, itemId, transactionDate, quantity, totalPrice, vat, vatType, longDescription, nature, taxCode, currency, itemCode, totaldisc, shopNo, terminalNo, datecreated, dateModified, timeCreated, timeModified, code, unitPrice, qte, discount, vatBeforeDisc, vatAfterDisc, totalHT_A, totalTTC, isTaxable, dateTransaction, timeTransaction, barcode, weights, totalHT_B, typeTax, rayon, currentPrice, famille, idSalesD, totalizer, comment);

                } while (cursor.moveToNext());
            }

            // Close the cursor and database connection after use
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("SYNC_ERROR_Transactions", e.getMessage());
        }
    }
    public void insertTransactionIntoMSSQL(Connection conn, int _id, int tableid, int Room_id, int PaidStatus, String TranscationId, int ItemId, String TransactionDate, int Quantity, double TotalPrice, double VAT, String VatType, String LongDescription, String Nature, String TaxCode, String Currency, String ItemCode, double TotalDisc, int ShopNo, int TerminalNo, String DateCreated, String DateModified, String TimeCreated, String TimeModified, String Code, double UnitPrice, long Qte, double Discount, double VAT_Before_Disc, double VAT_After_Disc, double TOTALHT_A, double TotalTTC, int IsTaxable, String DateTransaction, String TimeTransaction, String Barcode, double Weights, double TotalHT_B, String TypeTax, String Rayon, double CurrentPrice, String Famille, String IDSalesD, String Totalizer, String Comment) {
        try {
            // Start a transaction
            conn.setAutoCommit(false);
            // Check if the record with the same _id already exists
            String checkExistenceQuery = "SELECT TranscationHeader_Ref FROM Transactions WHERE TranscationHeader_Ref = ? AND Barcode = ? ";

            PreparedStatement checkExistenceStatement = conn.prepareStatement(checkExistenceQuery);
            checkExistenceStatement.setString(1, TranscationId); // Set TranscationId
            checkExistenceStatement.setString(2, Barcode); // Set Barcode

            ResultSet resultSet = checkExistenceStatement.executeQuery();


            if (resultSet.next()) {
                // If the record exists, perform an update
                String updateSql = "UPDATE Transactions SET   ItemId = ?, TransactionDate = ?,  TotalPrice = ?, VAT = ?, VatType = ?, LongDescription = ?, Nature = ?, TaxCode = ?, Currency = ?, ItemCode = ?, TotalDisc = ?, ShopNo = ?, TerminalNo = ?, DateCreated = ?, DateModified = ?, TimeCreated = ?, TimeModified = ?,  UnitPrice = ?,  Discount = ?, VAT_Before_Disc = ?, VAT_After_Disc = ?, TOTALHT_A = ?, TotalTTC = ?, IsTaxable = ?, DateTransaction = ?, TimeTransaction = ?,  Weights = ?, TotalHT_B = ?, TypeTax = ?, Rayon = ?, CurrentPrice = ?, Famille = ?,  Totalizer = ?, Comment = ?,Paidstatus=?,Quantity=? WHERE TranscationHeader_Ref = ? AND Barcode = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateSql);

                updateStatement.setInt(1, ItemId);
                updateStatement.setString(2, TransactionDate);
                updateStatement.setDouble(3, TotalPrice);
                updateStatement.setDouble(4, VAT);
                updateStatement.setString(5, VatType);
                updateStatement.setString(6, LongDescription);
                updateStatement.setString(7, Nature);
                updateStatement.setString(8, TaxCode);
                updateStatement.setString(9, Currency);
                updateStatement.setString(10, ItemCode);
                updateStatement.setDouble(11, TotalDisc);
                updateStatement.setInt(12, ShopNo);
                updateStatement.setInt(13, TerminalNo);
                updateStatement.setString(14, DateCreated);
                updateStatement.setString(15, DateModified);
                updateStatement.setString(16, TimeCreated);
                updateStatement.setString(17, TimeModified);
                updateStatement.setDouble(18, UnitPrice);
                updateStatement.setDouble(19, Discount);
                updateStatement.setDouble(20, VAT_Before_Disc);
                updateStatement.setDouble(21, VAT_After_Disc);
                updateStatement.setDouble(22, TOTALHT_A);
                updateStatement.setDouble(23, TotalTTC);
                updateStatement.setInt(24, IsTaxable);
                updateStatement.setString(25, DateTransaction);
                updateStatement.setString(26, TimeTransaction);
                updateStatement.setDouble(27, Weights);
                updateStatement.setDouble(28, TotalHT_B);
                updateStatement.setString(29, TypeTax);
                updateStatement.setString(30, Rayon);
                updateStatement.setDouble(31, CurrentPrice);
                updateStatement.setString(32, Famille);
                updateStatement.setString(33, Totalizer);
                updateStatement.setString(34, Comment);
                updateStatement.setInt(35, PaidStatus);
                updateStatement.setInt(36, Quantity);
                updateStatement.setString(37, TranscationId);
                updateStatement.setString(38, Barcode); // Assuming 'barcode' is your variable holding the barcode value



                int rowsUpdated = updateStatement.executeUpdate();
                updateStatement.close();

                if (rowsUpdated > 0) {
                    Log.i("updateTrans_Success", "Record updated successfully");
                } else {
                    Log.i("updateTrans_Failure", "No records updated");
                }
            } else {
                // Enable IDENTITY_INSERT for the Transactions table
                String enableIdentityInsert = "SET IDENTITY_INSERT Transactions ON";
                PreparedStatement enableIdentityStatement = conn.prepareStatement(enableIdentityInsert);
                enableIdentityStatement.executeUpdate();
                enableIdentityStatement.close();
                // If the record doesn't exist, perform an insert
                String insertSql = "INSERT INTO Transactions (TranscationHeader_Ref, ItemId, TransactionDate, TotalPrice, VAT, VatType, LongDescription, Nature, TaxCode, Currency, ItemCode, TotalDisc, ShopNo, TerminalNo, DateCreated, DateModified, TimeCreated, TimeModified,  UnitPrice,  Discount, VAT_Before_Disc, VAT_After_Disc, TOTALHT_A, TotalTTC, IsTaxable, DateTransaction, TimeTransaction, Barcode, Weights, TotalHT_B, TypeTax, Rayon, CurrentPrice, Famille, Totalizer, Comment, Paidstatus,Quantity) VALUES ( ?,?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertSql);

                insertStatement.setString(1, TranscationId);
                insertStatement.setInt(2, ItemId);
                insertStatement.setString(3, TransactionDate);
                insertStatement.setDouble(4, TotalPrice);
                insertStatement.setDouble(5, VAT);
                insertStatement.setString(6, VatType);
                insertStatement.setString(7, LongDescription);
                insertStatement.setString(8, Nature);
                insertStatement.setString(9, TaxCode);
                insertStatement.setString(10, Currency);
                insertStatement.setString(11, ItemCode);
                insertStatement.setDouble(12, TotalDisc);
                insertStatement.setInt(13, ShopNo);
                insertStatement.setInt(14, TerminalNo);
                insertStatement.setString(15, DateCreated);
                insertStatement.setString(16, DateModified);
                insertStatement.setString(17, TimeCreated);
                insertStatement.setString(18, TimeModified);
                insertStatement.setDouble(19, UnitPrice);
                insertStatement.setDouble(20, Discount);
                insertStatement.setDouble(21, VAT_Before_Disc);
                insertStatement.setDouble(22, VAT_After_Disc);
                insertStatement.setDouble(23, TOTALHT_A);
                insertStatement.setDouble(24, TotalTTC);
                insertStatement.setInt(25, IsTaxable);
                insertStatement.setString(26, DateTransaction);
                insertStatement.setString(27, TimeTransaction);
                insertStatement.setString(28, Barcode);
                insertStatement.setDouble(29, Weights);
                insertStatement.setDouble(30, TotalHT_B);
                insertStatement.setString(31, TypeTax);
                insertStatement.setString(32, Rayon);
                insertStatement.setDouble(33, CurrentPrice);
                insertStatement.setString(34, Famille);
                insertStatement.setString(35, Totalizer);
                insertStatement.setString(36, Comment);
                insertStatement.setInt(37, PaidStatus);
                insertStatement.setInt(38, Quantity);


                int rowsUpdated = insertStatement.executeUpdate();
                insertStatement.close();

                if (rowsUpdated > 0) {
                    Log.i("insertEtrans_Success", "Record inserted successfully");
                } else {
                    Log.i("inserttrans_Failure", "No records inserted");
                }
            }

            // Commit the transaction
            conn.commit();

            resultSet.close();
            checkExistenceStatement.close();

        } catch (SQLException e) {
            try {
                // Rollback the transaction in case of any error
                conn.rollback();
            } catch (SQLException rollbackEx) {
                Log.e("ROLLBACK_ERROR", rollbackEx.getMessage());
            }
            Log.e("INSERT_ERROR_Transaction", e.getMessage());
        } finally {
            try {
                // Reset auto-commit to true
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                Log.e("AUTO_COMMIT_ERROR", e.getMessage());
            }
        }
    }

    public void syncTransactionHeaderFromMSSQLToSQLite(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        try {
            // Start database connection
            mDatabaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            // Query SQLite database to retrieve relevant data
            Cursor cursor = db.rawQuery("SELECT * FROM " + TRANSACTION_HEADER_TABLE_NAME +
                    " WHERE " + TRANSACTION_STATUS + " = 'Completed'", null);

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    long _id = cursor.getLong(cursor.getColumnIndex("_id"));
                    long roomId = cursor.getLong(cursor.getColumnIndex("room_id"));
                    long tableId = cursor.getLong(cursor.getColumnIndex("id"));
                    String splitType = cursor.getString(cursor.getColumnIndex("SplitType"));
                    String transactionId = cursor.getString(cursor.getColumnIndex("TranscationId"));
                    String shopNo = cursor.getString(cursor.getColumnIndex("ShopNo"));
                    int terminalNo = cursor.getInt(cursor.getColumnIndex("TerminalNo"));
                    String dateCreated = cursor.getString(cursor.getColumnIndex("DateCreated"));
                    String dateModified = cursor.getString(cursor.getColumnIndex("DateModified"));
                    String timeCreated = cursor.getString(cursor.getColumnIndex("TimeCreated")); // Added field
                    String timeModified = cursor.getString(cursor.getColumnIndex("TimeModified")); // Added field
                    String previousHash = cursor.getString(cursor.getColumnIndex("PreviousHash")); // Added field
                    String memberCard = cursor.getString(cursor.getColumnIndex("MemberCard")); // Added field
                    double subTotal = cursor.getDouble(cursor.getColumnIndex("SubTotal")); // Added field
                    String cashierCode = cursor.getString(cursor.getColumnIndex("CashierCode")); // Added field
                    String dateTransaction = cursor.getString(cursor.getColumnIndex("DateTransaction")); // Added field
                    String timeTransaction = cursor.getString(cursor.getColumnIndex("TimeTransaction")); // Added field
                    double totalHT_A = cursor.getDouble(cursor.getColumnIndex("TOTALHT_A")); // Added field
                    double totalTTC = cursor.getDouble(cursor.getColumnIndex("TotalTTC")); // Added field
                    double tenderAmount = cursor.getDouble(cursor.getColumnIndex("TenderAmount")); // Added field
                    double cashReturn = cursor.getDouble(cursor.getColumnIndex("CashReturn")); // Added field
                    double vatBeforeDisc = cursor.getDouble(cursor.getColumnIndex("VAT_Before_Disc")); // Added field
                    double vatAfterDisc = cursor.getDouble(cursor.getColumnIndex("VAT_After_Disc")); // Added field
                    double total_Tx_1 = cursor.getDouble(cursor.getColumnIndex("Total_Tx_1")); // Added field
                    double total_Tx_2 = cursor.getDouble(cursor.getColumnIndex("Total_Tx_2")); // Added field
                    double total_Tx_3 = cursor.getDouble(cursor.getColumnIndex("Total_Tx_3")); // Added field
                    double totalDisc = cursor.getDouble(cursor.getColumnIndex("TotalDisc")); // Added field
                    int qtyItem = cursor.getInt(cursor.getColumnIndex("QtyItem")); // Added field
                    double totalHT_B = cursor.getDouble(cursor.getColumnIndex("TotalHT_B")); // Added field
                    String clientName = cursor.getString(cursor.getColumnIndex("ClientName")); // Added field
                    String clientOtherName = cursor.getString(cursor.getColumnIndex("ClientOtherName")); // Added field
                    String clientNIC = cursor.getString(cursor.getColumnIndex("ClientNIC")); // Added field
                    String clientAdr1 = cursor.getString(cursor.getColumnIndex("ClientAdr1")); // Added field
                    String clientAdr2 = cursor.getString(cursor.getColumnIndex("ClientAdr2")); // Added field
                    String transactionStatus = cursor.getString(cursor.getColumnIndex("TransactionStatus"));
                    String clientVATRegNo = cursor.getString(cursor.getColumnIndex("ClientVATRegNo")); // Added field
                    String clientBRN = cursor.getString(cursor.getColumnIndex("ClientBRN")); // Added field
                    String clientTel = cursor.getString(cursor.getColumnIndex("ClientTel")); // Added field
                    String invoiceRef = cursor.getString(cursor.getColumnIndex("InvoiceRef")); // Added field
                    String isCashCredit = cursor.getString(cursor.getColumnIndex("IsCash_Credit")); // Added field
                    String idSalesH = cursor.getString(cursor.getColumnIndex("IDSalesH")); // Added field
                    String clientCode = cursor.getString(cursor.getColumnIndex("ClientCode")); // Added field
                    String loyalty = cursor.getString(cursor.getColumnIndex("Loyalty")); // Added field
                    String MRA_Response = cursor.getString(cursor.getColumnIndex("MRA_Response")); // Added field
                    String MRA_IRN = cursor.getString(cursor.getColumnIndex("MRA_IRN")); // Added field
                    int invoiceCounter = cursor.getInt(cursor.getColumnIndex("Invoice_counter")); // Added field
                    String MRA_Method = cursor.getString(cursor.getColumnIndex("MRA_Method")); // Added field

                    // Pass all retrieved data to the insertOrUpdateTransactionHeaderIntoMSSQL method
                    insertOrUpdateTransactionHeaderIntoMSSQL(
                            conn, _id, roomId, tableId, splitType, transactionId, shopNo,
                            terminalNo, dateCreated, dateModified, timeCreated, timeModified,
                            previousHash, memberCard, subTotal, cashierCode, dateTransaction,
                            timeTransaction, totalHT_A, totalTTC, tenderAmount, cashReturn,
                            vatBeforeDisc, vatAfterDisc, total_Tx_1, total_Tx_2, total_Tx_3,
                            totalDisc, qtyItem, totalHT_B, clientName, clientOtherName,
                            clientNIC, clientAdr1, clientAdr2, transactionStatus, clientVATRegNo,
                            clientBRN, clientTel, invoiceRef, isCashCredit, idSalesH, clientCode,
                            loyalty, MRA_Response, MRA_IRN, invoiceCounter, MRA_Method
                    );

                } while (cursor.moveToNext());
            }

            // Close the cursor and database connection after use
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("SYNC_ERROR_TransactionsHeader", e.getMessage());
        }
    }


    public void insertOrUpdateTransactionHeaderIntoMSSQL(Connection conn, long _id, long roomId, long id, String splitType, String transactionId, String shopNo, int terminalNo, String dateCreated, String dateModified, String timeCreated, String timeModified, String previousHash, String memberCard, double subTotal, String cashierCode, String dateTransaction, String timeTransaction, double totalHT_A, double totalTTC, double tenderAmount, double cashReturn, double vatBeforeDisc, double vatAfterDisc, double total_Tx_1, double total_Tx_2, double total_Tx_3, double totalDisc, int qtyItem, double totalHT_B, String clientName, String clientOtherName, String clientNIC, String clientAdr1, String clientAdr2, String transactionStatus, String clientVATRegNo, String clientBRN, String clientTel, String invoiceRef, String isCashCredit, String idSalesH, String clientCode, String loyalty, String MRA_Response, String MRA_IRN, int invoiceCounter, String MRA_Method) {
        try {
            // Check if the record with the same _id already exists
            String checkExistenceQuery = "SELECT TransactionRef  FROM TransactionHeader WHERE TransactionRef  = ?";
            PreparedStatement checkExistenceStatement = conn.prepareStatement(checkExistenceQuery);
            checkExistenceStatement.setString(1, transactionId);
            ResultSet resultSet = checkExistenceStatement.executeQuery();

            if (resultSet.next()) {
                // If the record exists, perform an update
                String updateSql = "UPDATE TransactionHeader SET room_id = ?, table_id = ?, SplitType = ?, ShopNo = ?, TerminalNo = ?, DateCreated = ?, DateModified = ?, TimeCreated = ?, TimeModified = ?, PreviousHash = ?, MemberCard = ?, SubTotal = ?, CashierCode = ?, DateTransaction = ?, TimeTransaction = ?, TOTALHT_A = ?, TotalTTC = ?, TenderAmount = ?, CashReturn = ?, VAT_Before_Disc = ?, VAT_After_Disc = ?, Total_Tx_1 = ?, Total_Tx_2 = ?, Total_Tx_3 = ?, TotalDisc = ?, QtyItem = ?, TotalHT_B = ?, ClientName = ?, ClientOtherName = ?, ClientNIC = ?, ClientAdr1 = ?, ClientAdr2 = ?, TransactionStatus = ?, ClientVATRegNo = ?, ClientBRN = ?, ClientTel = ?, InvoiceRef = ?, IsCash_Credit = ?, IDSalesH = ?, ClientCode = ?, Loyalty = ?, MRA_Response = ?, MRA_IRN = ?, Invoice_counter = ?, MRA_Method = ? WHERE TransactionRef  = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateSql);
                updateStatement.setLong(1, roomId);
                updateStatement.setLong(2, id);
                updateStatement.setString(3, splitType);
                updateStatement.setString(4, shopNo);
                updateStatement.setInt(5, terminalNo);
                updateStatement.setString(6, dateCreated);
                updateStatement.setString(7, dateModified);
                updateStatement.setString(8, timeCreated);
                updateStatement.setString(9, timeModified);
                updateStatement.setString(10, previousHash);
                updateStatement.setString(11, memberCard);
                updateStatement.setDouble(12, subTotal);
                updateStatement.setString(13, cashierCode);
                updateStatement.setString(14, dateTransaction);
                updateStatement.setString(15, timeTransaction);
                updateStatement.setDouble(16, totalHT_A);
                updateStatement.setDouble(17, totalTTC);
                updateStatement.setDouble(18, tenderAmount);
                updateStatement.setDouble(19, cashReturn);
                updateStatement.setDouble(20, vatBeforeDisc);
                updateStatement.setDouble(21, vatAfterDisc);
                updateStatement.setDouble(22, total_Tx_1);
                updateStatement.setDouble(23, total_Tx_2);
                updateStatement.setDouble(24, total_Tx_3);
                updateStatement.setDouble(25, totalDisc);
                updateStatement.setInt(26, qtyItem);
                updateStatement.setDouble(27, totalHT_B);
                updateStatement.setString(28, clientName);
                updateStatement.setString(29, clientOtherName);
                updateStatement.setString(30, clientNIC);
                updateStatement.setString(31, clientAdr1);
                updateStatement.setString(32, clientAdr2);
                updateStatement.setString(33, transactionStatus);
                updateStatement.setString(34, clientVATRegNo);
                updateStatement.setString(35, clientBRN);
                updateStatement.setString(36, clientTel);
                updateStatement.setString(37, invoiceRef);
                updateStatement.setString(38, isCashCredit);
                updateStatement.setString(39, idSalesH);
                updateStatement.setString(40, clientCode);
                updateStatement.setString(41, loyalty);
                updateStatement.setString(42, MRA_Response);
                updateStatement.setString(43, MRA_IRN);
                updateStatement.setInt(44, invoiceCounter);
                updateStatement.setString(45, MRA_Method);
                updateStatement.setString(46, transactionId);



                int rowsUpdated = updateStatement.executeUpdate();
                updateStatement.close();

                if (rowsUpdated > 0) {
                    Log.i("UPDATE_Success", "Record updated successfully");
                } else {
                    Log.i("UPDATE_Failure", "No records updated");
                }
            } else {
                // If the record doesn't exist, perform an insert
                String insertSql = "INSERT INTO TransactionHeader ( room_id, table_id, SplitType, TransactionRef , ShopNo, TerminalNo, DateCreated, DateModified, TimeCreated, TimeModified, PreviousHash, MemberCard, SubTotal, CashierCode, DateTransaction, TimeTransaction, TOTALHT_A, TotalTTC, TenderAmount, CashReturn, VAT_Before_Disc, VAT_After_Disc, Total_Tx_1, Total_Tx_2, Total_Tx_3, TotalDisc, QtyItem, TotalHT_B, ClientName, ClientOtherName, ClientNIC, ClientAdr1, ClientAdr2, TransactionStatus, ClientVATRegNo, ClientBRN, ClientTel, InvoiceRef, IsCash_Credit, IDSalesH, ClientCode, Loyalty, MRA_Response, MRA_IRN, Invoice_counter, MRA_Method) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement insertStatement = conn.prepareStatement(insertSql);

                insertStatement.setLong(1, roomId);
                insertStatement.setLong(2, id);
                insertStatement.setString(3, splitType);
                insertStatement.setString(4, transactionId);
                insertStatement.setString(5, shopNo);
                insertStatement.setInt(6, terminalNo);
                insertStatement.setString(7, dateCreated);
                insertStatement.setString(8, dateModified);
                insertStatement.setString(9, timeCreated);
                insertStatement.setString(10, timeModified);
                insertStatement.setString(11, previousHash);
                insertStatement.setString(12, memberCard);
                insertStatement.setDouble(13, subTotal);
                insertStatement.setString(14, cashierCode);
                insertStatement.setString(15, dateTransaction);
                insertStatement.setString(16, timeTransaction);
                insertStatement.setDouble(17, totalHT_A);
                insertStatement.setDouble(18, totalTTC);
                insertStatement.setDouble(19, tenderAmount);
                insertStatement.setDouble(20, cashReturn);
                insertStatement.setDouble(21, vatBeforeDisc);
                insertStatement.setDouble(22, vatAfterDisc);
                insertStatement.setDouble(23, total_Tx_1);
                insertStatement.setDouble(24, total_Tx_2);
                insertStatement.setDouble(25, total_Tx_3);
                insertStatement.setDouble(26, totalDisc);
                insertStatement.setInt(27, qtyItem);
                insertStatement.setDouble(28, totalHT_B);
                insertStatement.setString(29, clientName);
                insertStatement.setString(30, clientOtherName);
                insertStatement.setString(31, clientNIC);
                insertStatement.setString(32, clientAdr1);
                insertStatement.setString(33, clientAdr2);
                insertStatement.setString(34, transactionStatus);
                insertStatement.setString(35, clientVATRegNo);
                insertStatement.setString(36, clientBRN);
                insertStatement.setString(37, clientTel);
                insertStatement.setString(38, invoiceRef);
                insertStatement.setString(39, isCashCredit);
                insertStatement.setString(40, idSalesH);
                insertStatement.setString(41, clientCode);
                insertStatement.setString(42, loyalty);
                insertStatement.setString(43, MRA_Response);
                insertStatement.setString(44, MRA_IRN);
                insertStatement.setInt(45, invoiceCounter);
                insertStatement.setString(46, MRA_Method);


                int rowsInserted = insertStatement.executeUpdate();
                insertStatement.close();

                if (rowsInserted > 0) {
                    Log.i("INSERT_Success", "Record inserted successfully");
                } else {
                    Log.i("INSERT_Failure", "No records inserted");
                }
            }

            resultSet.close();
            checkExistenceStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_UPDATE_ERROR_TransactionHeader", e.getMessage());
        }
    }




    public void syncInvoiceSettlementFromMSSQLToSQLite(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        try {
            // Start database connection
            mDatabaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            // Query SQLite database to retrieve relevant data
            Cursor cursor = db.rawQuery("SELECT * FROM " + INVOICE_SETTLEMENT_TABLE_NAME, null);

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    // Extract data from the cursor
                    int settlementId = cursor.getInt(cursor.getColumnIndex("SettlementId"));
                    int roomid = cursor.getInt(cursor.getColumnIndex("room_id"));
                    int tableid = cursor.getInt(cursor.getColumnIndex("id"));
                   // int transcationId = cursor.getInt(cursor.getColumnIndex("TranscationId"));
                    int shopNo = cursor.getInt(cursor.getColumnIndex("ShopNo"));
                    int terminalNo = cursor.getInt(cursor.getColumnIndex("TerminalNo"));
                    String transactionDate = cursor.getString(cursor.getColumnIndex("DateTransaction"));
                    int CodePaiement = cursor.getInt(cursor.getColumnIndex("CodeModeDePaiement"));
                    double amount = cursor.getDouble(cursor.getColumnIndex("Amount"));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndex("TotalAmount"));
                    long giftVoucherNo = cursor.getLong(cursor.getColumnIndex("GiftVoucherNo"));
                    String IdInvoiceSett = cursor.getString(cursor.getColumnIndex("IDInvoiceSettlement"));
                    String remarks = cursor.getString(cursor.getColumnIndex("Remark"));
                    String datecreated = cursor.getString(cursor.getColumnIndex("DateCreated"));
                    String paymentName = cursor.getString(cursor.getColumnIndex("PaymentName"));
                    long chequeNo = cursor.getLong(cursor.getColumnIndex("ChequeNo"));

// Insert data into MSSQL
                    insertOrUpdateSettlementIntoMSSQLAndSaveToCSV(conn, settlementId, roomid, tableid, shopNo, terminalNo, transactionDate, CodePaiement, amount, totalAmount, giftVoucherNo, IdInvoiceSett, remarks, datecreated, paymentName, chequeNo);

                } while (cursor.moveToNext());
            }

            // Close the cursor and database connection after use
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("SYNC_ERROR_Transactionssettlement", e.getMessage());
        }
    }





    public void insertOrUpdateSettlementIntoMSSQLAndSaveToCSV(Connection conn, int settlementId, int roomId, int id, int shopNo, int terminalNo, String dateTransaction, int codeModeDePaiement, double amount, double totalAmount, long giftVoucherNo, String idInvoiceSettlement, String remark, String dateCreated, String paymentName, long chequeNo) {
        try {
            // Check if the record with the same idInvoiceSettlement already exists
            String checkExistenceQuery = "SELECT IDInvoiceSettlement FROM InvoiceSettlement WHERE IDInvoiceSettlement = ?";
            PreparedStatement checkExistenceStatement = conn.prepareStatement(checkExistenceQuery);
            checkExistenceStatement.setString(1, idInvoiceSettlement);
            ResultSet resultSet = checkExistenceStatement.executeQuery();

            if (resultSet.next()) {
                // If the record exists, perform an update
                String updateSql = "UPDATE InvoiceSettlement SET room_id = ?, table_id = ?, ShopNo = ?, TerminalNo = ?, DateTransaction = ?, CodeModeDePaiement = ?, Amount = ?, TotalAmount = ?, GiftVoucherNo = ?, Remark = ?, DateCreated = ?, PaymentName = ?, ChequeNo = ?, SettlementId = ? WHERE IDInvoiceSettlement = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateSql);
                updateStatement.setInt(1, roomId);
                updateStatement.setInt(2, id);
                updateStatement.setInt(3, shopNo);
                updateStatement.setInt(4, terminalNo);
                updateStatement.setString(5, dateTransaction);
                updateStatement.setLong(6, codeModeDePaiement);
                updateStatement.setDouble(7, amount);
                updateStatement.setDouble(8, totalAmount);
                updateStatement.setLong(9, giftVoucherNo);
                updateStatement.setString(10, remark);
                updateStatement.setString(11, dateCreated);
                updateStatement.setString(12, paymentName);
                updateStatement.setLong(13, chequeNo);
                updateStatement.setInt(14, settlementId);
                updateStatement.setString(15, idInvoiceSettlement);

                updateStatement.executeUpdate();
                updateStatement.close();
            } else {


                // If the record doesn't exist, perform an insert
                String insertSql = "INSERT INTO InvoiceSettlement (SettlementId, room_id, table_id, ShopNo, TerminalNo, DateTransaction, CodeModeDePaiement, Amount, TotalAmount, GiftVoucherNo, IDInvoiceSettlement, Remark, DateCreated, PaymentName, ChequeNo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertSql);
                insertStatement.setInt(1, settlementId);
                insertStatement.setInt(2, roomId);
                insertStatement.setInt(3, id);
                insertStatement.setInt(4, shopNo);
                insertStatement.setInt(5, terminalNo);
                insertStatement.setString(6, dateTransaction);
                insertStatement.setLong(7, codeModeDePaiement);
                insertStatement.setDouble(8, amount);
                insertStatement.setDouble(9, totalAmount);
                insertStatement.setLong(10, giftVoucherNo);
                insertStatement.setString(11, idInvoiceSettlement);
                insertStatement.setString(12, remark);
                insertStatement.setString(13, dateCreated);
                insertStatement.setString(14, paymentName);
                insertStatement.setLong(15, chequeNo);

                insertStatement.executeUpdate();
                insertStatement.close();
            }

            resultSet.close();
            checkExistenceStatement.close();







        } catch (SQLException  e) {
            Log.e("INSERT_UPDATE_ERROR_InvoiceSettlement", e.getMessage());
        }
    }


    // Method to synchronize all data from SQLite to MSSQL
    public  void syncAllDataFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context);
        if (conn != null) {
            try {
                // Fetch all data from the local SQLite database
                mDatabaseHelper = new DatabaseHelper(context);
                Cursor localCursor = mDatabaseHelper.getAllItems();

                // Iterate through the cursor and insert each record into MSSQL
                if (localCursor != null && localCursor.moveToFirst()) {
                    do {
                        // Extract data from the cursor
                        // Extract data from the cursor
                        String name = localCursor.getString(localCursor.getColumnIndex(Name));
                        String desc = localCursor.getString(localCursor.getColumnIndex(DESC));
                        String category = localCursor.getString(localCursor.getColumnIndex(Category));
                        int quantity = localCursor.getInt(localCursor.getColumnIndex(Quantity));
                        String department = localCursor.getString(localCursor.getColumnIndex(Department));
                        String longDescription = localCursor.getString(localCursor.getColumnIndex(LongDescription));
                        String subDepartment = localCursor.getString(localCursor.getColumnIndex(SubDepartment));
                        BigDecimal price = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(Price)));
                        BigDecimal priceAfterDiscount = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(PriceAfterDiscount)));
                        String vat = localCursor.getString(localCursor.getColumnIndex(VAT));
                        boolean availableForSale = localCursor.getInt(localCursor.getColumnIndex(AvailableForSale)) == 1;
                        String soldBy = localCursor.getString(localCursor.getColumnIndex(SoldBy));
                        String image = localCursor.getString(localCursor.getColumnIndex(Image));
                        String sku = localCursor.getString(localCursor.getColumnIndex(SKU));
                        String variant = localCursor.getString(localCursor.getColumnIndex(Variant));
                        BigDecimal cost = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(Cost)));
                        BigDecimal weight = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(Weight)));
                        int userId = localCursor.getInt(localCursor.getColumnIndex(UserId));
                        String nature = localCursor.getString(localCursor.getColumnIndex(Nature));
                        String taxCode = localCursor.getString(localCursor.getColumnIndex(TaxCode));
                        String currency = localCursor.getString(localCursor.getColumnIndex(Currency));
                        String itemCode = localCursor.getString(localCursor.getColumnIndex(ItemCode));
                        String syncStatus = localCursor.getString(localCursor.getColumnIndex(SyncStatus));
                        String rateDiscount = localCursor.getString(localCursor.getColumnIndex(RateDiscount));
                        String amountDiscount = localCursor.getString(localCursor.getColumnIndex(AmountDiscount));
                        BigDecimal totalDiscount = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(TotalDiscount)));
                        BigDecimal totalDiscount2 = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(TotalDiscount2)));
                        BigDecimal totalDiscount3 = BigDecimal.valueOf(localCursor.getDouble(localCursor.getColumnIndex(TotalDiscount3)));
                        String dateCreated = localCursor.getString(localCursor.getColumnIndex(DateCreated));
                        String lastModified = localCursor.getString(localCursor.getColumnIndex(LastModified));
                        boolean hasOptions = localCursor.getInt(localCursor.getColumnIndex(hasoptions)) == 1;
                        String comments = localCursor.getString(localCursor.getColumnIndex(comment));
                        String relatedItem = localCursor.getString(localCursor.getColumnIndex(related_item));
                        String relatedItem2 = localCursor.getString(localCursor.getColumnIndex(related_item2));
                        String relatedItem3 = localCursor.getString(localCursor.getColumnIndex(related_item3));
                        String relatedItem4 = localCursor.getString(localCursor.getColumnIndex(related_item4));
                        String relatedItem5 = localCursor.getString(localCursor.getColumnIndex(related_item5));
                        boolean HasSupplements = localCursor.getInt(localCursor.getColumnIndex(hasSupplements)) == 1;
                        boolean RelatedSupplements = localCursor.getInt(localCursor.getColumnIndex(relatedSupplements)) == 1;


                        // Extract other fields similarly...

                        // Insert the data into MSSQL
                        // Insert the data into MSSQL
                        insertItemIntoMssql(conn, name, desc, category, quantity, department, longDescription, subDepartment,
                                price, priceAfterDiscount, vat, availableForSale, soldBy, image, sku, variant, cost, weight,
                                userId, nature, taxCode, currency, itemCode, syncStatus, rateDiscount, amountDiscount,
                                totalDiscount, totalDiscount2, totalDiscount3, dateCreated, lastModified, hasOptions,
                                comments, relatedItem, relatedItem2, relatedItem3, relatedItem4, relatedItem5, HasSupplements,
                                RelatedSupplements);
                    } while (localCursor.moveToNext());
                }

                // Close the cursor after use
                if (localCursor != null && !localCursor.isClosed()) {
                    localCursor.close();
                }
            } catch (Exception e) {
                Log.e("SYNC_ERROR1", e.getMessage());
            } finally {
                try {
                    // Close the database connection
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    Log.e("SYNC_ERROR2", e.getMessage());
                }
            }
        }
    }


    // Method to insert an item into MSSQL database
    private void insertItemIntoMssql(Connection conn, String name, String desc, String category, int quantity,
                                     String department, String longDescription, String subDepartment, BigDecimal price,
                                     BigDecimal priceAfterDiscount, String vat, boolean availableForSale, String soldBy,
                                     String image, String sku, String variant, BigDecimal cost, BigDecimal weight,
                                     int userId, String nature, String taxCode, String currency, String itemCode,
                                     String syncStatus, String rateDiscount, String amountDiscount, BigDecimal totalDiscount,
                                     BigDecimal totalDiscount2, BigDecimal totalDiscount3, String dateCreated,
                                     String lastModified, boolean hasOptions, String comment, String relatedItem,
                                     String relatedItem2, String relatedItem3, String relatedItem4, String relatedItem5,
                                     boolean hasSupplements, boolean relatedSupplements) {
        try {
            // Perform the insertion into MSSQL database
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO POSITEMS (name, description, category, Quantity, Department, LongDescription, SubDepartment, " +
                            "Price, PriceAfterDiscount, VAT, AvailableForSale, SoldBy, Image, SKU, Variant, Cost, Weight, " +
                            "UserId, Nature, TaxCode, Currency, ItemCode, SyncStatus, RateDiscount, AmountDiscount, TotalDiscount, " +
                            "TotalDiscount2, TotalDiscount3, DateCreated, LastModified, hasoptions, comment, related_item, " +
                            "related_item2, related_item3, related_item4, related_item5, HasSupplements, RelatedSupplements) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                            "?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");

            // Set parameters for the prepared statement
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, desc);
            preparedStatement.setString(3, category);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setString(5, department);
            preparedStatement.setString(6, longDescription);
            preparedStatement.setString(7, subDepartment);
            preparedStatement.setBigDecimal(8, price);
            preparedStatement.setBigDecimal(9, priceAfterDiscount);
            preparedStatement.setString(10, vat);
            preparedStatement.setBoolean(11, availableForSale);
            preparedStatement.setString(12, soldBy);
            preparedStatement.setString(13, image);
            preparedStatement.setString(14, sku);
            preparedStatement.setString(15, variant);
            preparedStatement.setBigDecimal(16, cost);
            preparedStatement.setBigDecimal(17, weight);
            preparedStatement.setInt(18, userId);
            preparedStatement.setString(19, nature);
            preparedStatement.setString(20, taxCode);
            preparedStatement.setString(21, currency);
            preparedStatement.setString(22, itemCode);
            preparedStatement.setString(23, syncStatus);
            preparedStatement.setString(24, rateDiscount);
            preparedStatement.setString(25, amountDiscount);
            preparedStatement.setBigDecimal(26, totalDiscount);
            preparedStatement.setBigDecimal(27, totalDiscount2);
            preparedStatement.setBigDecimal(28, totalDiscount3);
            preparedStatement.setString(29, dateCreated);
            preparedStatement.setString(30, lastModified);
            preparedStatement.setBoolean(31, hasOptions);
            preparedStatement.setString(32, comment);
            preparedStatement.setString(33, relatedItem);
            preparedStatement.setString(34, relatedItem2);
            preparedStatement.setString(35, relatedItem3);
            preparedStatement.setString(36, relatedItem4);
            preparedStatement.setString(37, relatedItem5);
            preparedStatement.setBoolean(38, hasSupplements);
           preparedStatement.setBoolean(39, relatedSupplements);

        } catch (Exception e) {
            Log.e("INSERT_ERROR_AllItems", e.getMessage());
        }
    }

    public void syncCountingReportDataFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        if (conn != null) {
            try {
                // Open SQLite database
                mDatabaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                // Query data from SQLite CountingReport table
                Cursor cursor = db.rawQuery("SELECT * FROM " + COUNTING_REPORT_TABLE_NAME, null);

                // Iterate through the cursor
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    int shiftNumber = cursor.getInt(cursor.getColumnIndex(TRANSACTION_SHIFT_NUMBER));
                    double totalizerTotal = cursor.getDouble(cursor.getColumnIndex(COUNTING_REPORT_TOTALIZER_TOTAL));
                    double totalValue = cursor.getDouble(cursor.getColumnIndex(COUNTING_REPORT_TOTAL_VALUE));
                    double difference = cursor.getDouble(cursor.getColumnIndex(COUNTING_REPORT_DIFFERENCE));
                    int cashierId = cursor.getInt(cursor.getColumnIndex(COUNTING_REPORT_CASHIER_ID));
                    String datetime = cursor.getString(cursor.getColumnIndex(COUNTING_REPORT_DATETIME));

                    // Insert the retrieved data into MSSQL CountReport table
                    insertCountingReportDataIntoMSSQL(conn, shiftNumber, totalizerTotal, totalValue, difference, cashierId, datetime);
                }

                // Close the cursor
                cursor.close();
            } catch (Exception e) {
                Log.e("SYNC_ERROR", e.getMessage());
            }
        }
    }

    public void syncCostDataFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        if (conn != null) {
            try {
                // Open SQLite database
                mDatabaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                // Query data from SQLite Cost table
                Cursor cursor = db.rawQuery("SELECT * FROM " + COST_TABLE_NAME, null);

                // Iterate through the cursor
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    String barcode = cursor.getString(cursor.getColumnIndex(Barcode));
                    String sku = cursor.getString(cursor.getColumnIndex(SKUCost));
                    double cost = cursor.getDouble(cursor.getColumnIndex(Cost));
                    String lastModified = cursor.getString(cursor.getColumnIndex(LastModified));
                    int userId = cursor.getInt(cursor.getColumnIndex(UserId));
                    String codeFournisseur = cursor.getString(cursor.getColumnIndex(CodeFournisseur));

                    // Insert the retrieved data into MSSQL Cost table
                    // Implement the insertion logic similar to what we discussed earlier
                    insertCostDataIntoMSSQL(conn, barcode, sku, cost, lastModified, userId, codeFournisseur);
                }

                // Close the cursor
                cursor.close();
            } catch (Exception e) {
                Log.e("SYNC_ERROR", e.getMessage());
            }
        }
    }

    public void syncSupplementsOptionsFromSQLiteToMSSQL( Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        try {
            // Start database connection
            mDatabaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            // Query SQLite database to retrieve all data
            Cursor cursor = db.rawQuery("SELECT * FROM " + SUPPLEMENTS_OPTIONS_TABLE_NAME, null);

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    // Extract data from the cursor
                    int supplementOptionId = cursor.getInt(cursor.getColumnIndex(SUPPLEMENT_OPTION_ID));
                    String supplementOptionName = cursor.getString(cursor.getColumnIndex(SUPPLEMENT_OPTION_NAME));

                    // Insert data into MSSQL
                    insertSupplementOptionIntoMSSQL(conn,supplementOptionId, supplementOptionName);
                } while (cursor.moveToNext());
            }

            // Close the cursor and database connection after use
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("SYNC_ERROR_SupplementName", e.getMessage());
        }
    }

    public void syncCashReportDataFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        if (conn != null) {
            try {
                // Open SQLite database
                mDatabaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                // Query data from SQLite CashReports table
                Cursor cursor = db.rawQuery("SELECT * FROM " + CASH_REPORT_TABLE_NAME, null);

                // Iterate through the cursor
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    int shiftNumber = cursor.getInt(cursor.getColumnIndex("ShiftNumber"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    int cashierId = cursor.getInt(cursor.getColumnIndex("cashiorid"));
                    int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                    double total = cursor.getDouble(cursor.getColumnIndex("total"));
                    int tillNum = cursor.getInt(cursor.getColumnIndex("TillNum"));
                    String transId = cursor.getString(cursor.getColumnIndex("Transid"));
                    String cashReturn = cursor.getString(cursor.getColumnIndex("CashReturn"));

                    // Insert the retrieved data into MSSQL CashReports table
                    insertCashReportDataIntoMSSQL(conn, shiftNumber, date, cashierId, quantity, total, tillNum, transId, cashReturn);
                }

                // Close the cursor
                cursor.close();
            } catch (Exception e) {
                Log.e("SYNC_ERROR", e.getMessage());
            }
        }
    }
    public void syncFinancialReportDataFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        if (conn != null) {
            try {
                // Open SQLite database
                mDatabaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                // Query data from SQLite Financial table
                Cursor cursor = db.rawQuery("SELECT * FROM " + FINANCIAL_TABLE_NAME, null);

                // Iterate through the cursor
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    int shiftNumber = cursor.getInt(cursor.getColumnIndex("ShiftNumber"));
                    String date = cursor.getString(cursor.getColumnIndex("date")); // Should match your SQLite schema
                    String time = cursor.getString(cursor.getColumnIndex("time")); // Check this
                    int cashierId = cursor.getInt(cursor.getColumnIndex("cashiorid"));
                    String transactionType = cursor.getString(cursor.getColumnIndex("TransactionType")); // New field
                    int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                    double total = cursor.getDouble(cursor.getColumnIndex("total"));
                    double totalizer = cursor.getDouble(cursor.getColumnIndex("Totalizer")); // New field
                    int tillNum = cursor.getInt(cursor.getColumnIndex("TillNum"));
                    int shopNumber = cursor.getInt(cursor.getColumnIndex("ShopNumber")); // New field
                    String payment = cursor.getString(cursor.getColumnIndex("Payment")); // New field


                    // Insert the retrieved data into MSSQL FinancialReportTable
                    insertFinancialReportDataIntoMSSQL(conn, shiftNumber, date, time, cashierId, transactionType, quantity, total, totalizer, tillNum, shopNumber, payment);
                }

                // Close the cursor
                cursor.close();
            } catch (Exception e) {
                Log.e("SYNC_ERROR_FinancialReport", e.getMessage());
            }
        }
    }

    public void syncTablesFromSQLiteToMSSQL( Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        try {
            // Start database connection
            mDatabaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            // Query SQLite database to retrieve all data
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLES, null);

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    int _id = cursor.getInt(cursor.getColumnIndex(TABLE_ID));
                    int roomId = cursor.getInt(cursor.getColumnIndex(ROOM_ID));
                    int tableNumber = cursor.getInt(cursor.getColumnIndex(TABLE_NUMBER));
                    int seatCount = cursor.getInt(cursor.getColumnIndex(SEAT_COUNT));
                    String waiterName = cursor.getString(cursor.getColumnIndex(WAITER_NAME));
                    String status = cursor.getString(cursor.getColumnIndex("STATUS"));
                    int merged = cursor.getInt(cursor.getColumnIndex(MERGED));
                    int mergedSetId = cursor.getInt(cursor.getColumnIndex(MERGED_SET_ID));

                    // Insert data into MSSQL
                    insertTableIntoMSSQL(conn,_id, roomId, tableNumber, seatCount, waiterName, status, merged, mergedSetId);
                } while (cursor.moveToNext());
            }

            // Close the cursor and database connection after use
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("SYNC_ERROR", e.getMessage());
        }
    }
    public void insertCashReportDataIntoMSSQL(Connection conn, int shiftNumber, String date, int cashierId, int quantity, double total, int tillNum, String transId, String cashReturn) {
        try {
            // Check if the input date includes time; if not, use just the date format
            SimpleDateFormat sdfSource;
            if (date.contains(" ")) {
                sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                sdfSource = new SimpleDateFormat("yyyy-MM-dd"); // Only date format
            }

            // Parse the date from SQLite format to a Date object
            Date parsedDate = sdfSource.parse(date);

            // Format the date into MSSQL format (if needed, usually just storing as is works)
            SimpleDateFormat sdfTarget = new SimpleDateFormat("yyyy-MM-dd"); // Format to match MSSQL's date type
            String formattedDate = sdfTarget.format(parsedDate);

            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO CashReports (ShiftNumber, date, cashiorid, quantity, total, TillNum, Transid, CashReturn) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setInt(1, shiftNumber);
            preparedStatement.setString(2, formattedDate);
            preparedStatement.setInt(3, cashierId);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setBigDecimal(5, BigDecimal.valueOf(total)); // Use BigDecimal for precision with numeric
            preparedStatement.setInt(6, tillNum);
            preparedStatement.setString(7, transId);
            preparedStatement.setString(8, cashReturn);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_CashReport", e.getMessage());
        } catch (ParseException e) {
            Log.e("PARSE_ERROR", e.getMessage());
        }
    }


    public void insertFinancialReportDataIntoMSSQL(Connection conn, int shiftNumber, String date, String time, int cashierId, String transactionType, int quantity, double total, double totalizer, int tillNum, int shopNumber, String payment) {
        try {
            // Parse the date and time from SQLite format to a Date object
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdfSource.parse(date);

            SimpleDateFormat sdfTimeSource = new SimpleDateFormat("HH:mm:ss");
            Date parsedTime = sdfTimeSource.parse(time);

            // Format the date and time into MSSQL format
            SimpleDateFormat sdfTarget = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = sdfTarget.format(parsedDate) + " " + sdfTimeSource.format(parsedTime);

            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO FinancialReportTable (ShiftNumber, date, time, cashiorid, TransactionType, quantity, total, Totalizer, TillNum, ShopNumber, Payment) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setInt(1, shiftNumber);
            // Set date and time, ensuring they are correctly formatted
            preparedStatement.setDate(2, java.sql.Date.valueOf(date)); // Convert to SQL Date
            preparedStatement.setTime(3, java.sql.Time.valueOf(time)); // Convert to SQL Time
            preparedStatement.setInt(4, cashierId);
            preparedStatement.setString(5, transactionType);
            preparedStatement.setBigDecimal(6, BigDecimal.valueOf(quantity)); // Use BigDecimal for precision with numeric
            preparedStatement.setBigDecimal(7, BigDecimal.valueOf(total)); // Use BigDecimal for precision with numeric
            preparedStatement.setBigDecimal(8, BigDecimal.valueOf(totalizer)); // Use BigDecimal for precision with numeric
            preparedStatement.setInt(9, tillNum);
            preparedStatement.setInt(10, shopNumber);
            preparedStatement.setString(11, payment);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_FinancialReport", e.getMessage());
        } catch (ParseException e) {
            Log.e("PARSE_ERROR_FinancialReport", e.getMessage());
        }
    }

    private void insertTableIntoMSSQL(Connection conn,int tableid, int roomId, int tableNumber, int seatCount, String waiterName, String status, int merged, int mergedSetId) {
        try {
            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO tables (id,room_id, table_number, seat_count, waiter_name, STATUS, Merged, Mergeg_Set_ID) VALUES (?,?, ?, ?, ?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setInt(1, tableid);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, tableNumber);
            preparedStatement.setInt(4, seatCount);
            preparedStatement.setString(5, waiterName);
            preparedStatement.setString(6, status);
            preparedStatement.setInt(7, merged);
            preparedStatement.setInt(8, mergedSetId);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_Table", e.getMessage());
        }
    }
    public void syncRoomsFromSQLiteToMSSQL( Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        try {
            // Start database connection
            mDatabaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            // Query SQLite database to retrieve all data
            Cursor cursor = db.rawQuery("SELECT * FROM " + ROOMS, null);

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    int roomId = cursor.getInt(cursor.getColumnIndex("id"));
                    String roomName = cursor.getString(cursor.getColumnIndex(ROOM_NAME));
                    int tableCount = cursor.getInt(cursor.getColumnIndex(TABLE_COUNT));

                    // Insert data into MSSQL
                    insertRoomIntoMSSQL(conn, roomId, roomName, tableCount);
                } while (cursor.moveToNext());
            }

            // Close the cursor and database connection after use
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Log.e("SYNC_ERROR_Room", e.getMessage());
        }
    }

    private void insertRoomIntoMSSQL(Connection conn, int roomId, String roomName, int tableCount) {
        try {
            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO rooms (id, room_name, table_count) VALUES (?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setInt(1, roomId);
            preparedStatement.setString(2, roomName);
            preparedStatement.setInt(3, tableCount);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR", e.getMessage());
        }
    }

    private void insertSupplementOptionIntoMSSQL(Connection conn,int supplementOptionId, String supplementOptionName) {
        try {
            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO " + SUPPLEMENTS_OPTIONS_TABLE_NAME + " (" + SUPPLEMENT_OPTION_ID + ", " + SUPPLEMENT_OPTION_NAME + ") VALUES (?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setInt(1, supplementOptionId);
            preparedStatement.setString(2, supplementOptionName);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR", e.getMessage());
        }
    }


    public void syncDataOptionsFromSQLiteToMSSQL(Context context) {
        Connection conn = start(context); // Start the MSSQL connection
        if (conn != null) {
            try {
                // Open SQLite database
                mDatabaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                // Select all data from the SQLite table
                Cursor cursor = db.rawQuery("SELECT * FROM " + VARIANTS_TABLE_NAME, null);

                // Loop through the cursor and insert each record into MSSQL
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        // Extract data from the cursor
                        long id = cursor.getLong(cursor.getColumnIndex(VARIANTS_OPTIONS_ID));
                        long variantId = cursor.getLong(cursor.getColumnIndex(VARIANT_ID));
                        long variantItemId = cursor.getLong(cursor.getColumnIndex(VARIANT_ITEM_ID));
                        String barcode = cursor.getString(cursor.getColumnIndex(VARIANT_BARCODE));
                        String desc = cursor.getString(cursor.getColumnIndex(VARIANT_DESC));
                        double price = cursor.getDouble(cursor.getColumnIndex(VARIANT_PRICE));

                        // Insert the data into MSSQL
                        insertDataIntoMSSQL(conn, id, variantId, variantItemId, barcode, desc, price);
                    } while (cursor.moveToNext());
                }

                // Close the cursor and SQLite database
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
            } catch (Exception e) {
                Log.e("SYNC_ERROR", e.getMessage());
            } finally {
                try {
                    // Close the MSSQL connection
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    Log.e("SYNC_ERROR", e.getMessage());
                }
            }
        }
    }

    public void insertCountingReportDataIntoMSSQL(Connection conn, int shiftNumber, double totalizerTotal, double totalValue, double difference, int cashierId, String datetime) {
        try {
            // Parse the date from SQLite format to a Date object
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdfSource.parse(datetime);

            // Format the date into MSSQL format
            SimpleDateFormat sdfTarget = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDatetime = sdfTarget.format(date);

            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO CountReport (ShiftNumber, totalizer_total, total_value, difference, cashier_id, datetime) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setInt(1, shiftNumber);
            preparedStatement.setDouble(2, totalizerTotal);
            preparedStatement.setDouble(3, totalValue);
            preparedStatement.setDouble(4, difference);
            preparedStatement.setInt(5, cashierId);
            preparedStatement.setString(6, formattedDatetime);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_CountReport", e.getMessage());
        } catch (ParseException e) {
            Log.e("PARSE_ERROR", e.getMessage());
        }
    }

    public void insertCostDataIntoMSSQL(Connection conn, String barcode, String sku, double cost, String lastModified, int userId, String codeFournisseur) {
        try {
            // Parse the date from SQLite format to a Date object
            SimpleDateFormat sdfSource = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            Date date = sdfSource.parse(lastModified);
            // Format the date into MSSQL format
            SimpleDateFormat sdfTarget = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedLastModified = sdfTarget.format(date);

            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO Cost (Barcode, SKU, Cost, LastModified, UserId, CodeFournisseur) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setString(1, barcode);
            preparedStatement.setString(2, sku);
            preparedStatement.setDouble(3, cost);
            preparedStatement.setString(4, formattedLastModified);
            preparedStatement.setInt(5, userId);
            preparedStatement.setString(6, codeFournisseur);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_Cost", e.getMessage());
        } catch (ParseException e) {
            Log.e("PARSE_ERROR", e.getMessage());
        }
    }
    public void insertSupplementDataIntoMSSQL(Connection conn, String supplementOptionName) {
        try {
            // Prepare the SQL statement for insertion
            String sql = "INSERT INTO SupplementTableName (SupplementOptionName) VALUES (?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            preparedStatement.setString(1, supplementOptionName);

            // Execute the insertion query
            preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR_Supplement", e.getMessage());
        }
    }


    private void insertDataIntoMSSQL(Connection conn, long id, long variantId, long variantItemId, String barcode, String desc, double price) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO Options (VariantOptionId, id, variantItemId, barcode, Descriptions, Price) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");

            // Set parameters for the prepared statement
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, variantId);
            preparedStatement.setLong(3, variantItemId);
            preparedStatement.setString(4, barcode);
            preparedStatement.setString(5, desc);
            preparedStatement.setDouble(6, price);

            // Execute the query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Log.e("INSERT_ERROR", e.getMessage());
        }
    }


}
