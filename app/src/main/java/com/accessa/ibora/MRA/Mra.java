package com.accessa.ibora.MRA;


import static android.app.PendingIntent.getActivity;

import static com.accessa.ibora.product.items.DatabaseHelper.ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_CURRENCY;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DISCOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_INVOICE_REF;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ITEM_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_MRA_Invoice_Counter;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_NATURE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TAX_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_DISCOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_HT_A;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_TTC;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_UNIT_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT_Type;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.accessa.ibora.sales.ticket.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Mra extends AppCompatActivity {
    private DatabaseHelper mDatabaseHelper;
    private String cashorlevel,ShopName,LogoPath,CompanyName;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount,TotalHT;
    private String aesKey;
    String transactionType;
    private String   irn;
    String selectedBuyerName;
    String Transaction_Id;
    String totalAmountinserted;
    String cashReturn;
    private  ArrayList<SettlementItem> settlementItems = new ArrayList<>();


    String selectedBuyerTAN ,SelectedBuyerProfile,roomid,tableid;
    String selectedBuyerCompanyName ;
    String selectedBuyerType ;
    String selectedBuyerBRN ;
    String selectedBuyerNIC ;
    String selectedBuyerAddresse ;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private static final String TAG = "EncryptionActivity";


    private class SendPayloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String encryptedPayload = params[0];

            try {


                // Get the current date and time
                Date currentDate = new Date();

                // Define the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault());

                // Format the date and time
                String formattedDateTime = dateFormat.format(currentDate);

                String requestidmethod = getRequestid();
                // Construct the JSON request body
                JSONObject requestBody = new JSONObject();
                requestBody.put("requestId", requestidmethod); // Replace with your request ID
                requestBody.put("payload", encryptedPayload);

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, requestBody.toString());

                Request request = new Request.Builder()
                        .url("https://vfisc.mra.mu/einvoice-token-service/token-api/generate-token")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("username", "LBatour")
                        .addHeader("ebsMraId", "16887137012292S4IDFGH10H")
                        .post(body)
                        .build();

                SharedPreferences shardPreference = getApplicationContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                String Till_id = shardPreference.getString("posNumber", null);

               if(Till_id== null) {
                   Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                   if (cursorCompany != null && cursorCompany.moveToFirst()) {
                       int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_POS_Num);
                       Till_id = cursorCompany.getString(columnCompanyNameIndex);

                   }
               }
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper
                    System.out.println("roomid: " + roomid);
                    System.out.println("tableid: " + tableid);
                    // Retrieve the total amount and total tax amount from the transactionheader table
                    Cursor cursor = mDatabaseHelper.getTransactionHeader(roomid,tableid);
                    int currentCounter = 1; // Default value if no data is present in the table

                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndexTotalAmount = cursor.getColumnIndex(TRANSACTION_TOTAL_TTC);
                        int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
                        int columnIndexTotalHT = cursor.getColumnIndex(TRANSACTION_TOTAL_HT_A);
                        int columnIndexInvoiceRef = cursor.getColumnIndex(TRANSACTION_INVOICE_REF);
                        int columnIndexCounter = cursor.getColumnIndex(TRANSACTION_MRA_Invoice_Counter);
                        int transid=cursor.getColumnIndex(TRANSACTION_TICKET_NO);
                        currentCounter = cursor.getInt(columnIndexCounter);
                        double totaldiscount = mDatabaseHelper.getTotalDiscountSumForInProgressTransaction(roomid,tableid);
                        System.out.println("totaldiscount: " + totaldiscount);
                        // Assuming you have retrieved the double values as you mentioned
                        double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                        double TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                        double TotalHT = cursor.getDouble(columnIndexTotalTaxAmount);
                        String transactionid= cursor.getString(transid);
                        double Totalinvoice= totalAmount + totaldiscount;
                        String InvoiceRefIdentifyer = cursor.getString(columnIndexInvoiceRef);

                        // Step 2: Increment the counter value
                        int newCounter = currentCounter + 1;

                        // Step 3: Update the counter value in the transactionheader table
                        mDatabaseHelper.updateCounter(newCounter); // Implement the updateCounter method in your DatabaseHelper


                        // Convert the doubles to formatted strings with 2 decimal places
                        String formattedTotalInvoice = String.format("%.2f", Totalinvoice);
                        String formattedTotalAmount = String.format("%.2f", totalAmount);
                        String formattedTaxtotalAmount = String.format("%.2f", TaxtotalAmount);
                        String formattedTotalHT = String.format("%.2f", TotalHT);
                        transactionIdInProgress=mDatabaseHelper.getInProgressTransactionIdnew(roomid,tableid);
                        // Construct the JSON request body
                        JSONObject jsondetailedtransacs = new JSONObject();
                        Log.d("cashier", cashorlevel);
if(SelectedBuyerProfile==null) {
    if(cashorlevel.equals("1")) {
        transactionType = "TRN";
    }else{
        transactionType = "STD";
    }

        String TransactionType = "B2C";
        jsondetailedtransacs.put("invoiceCounter", String.valueOf(newCounter)); // Convert to String for JSON
        jsondetailedtransacs.put("transactionType", TransactionType);
        jsondetailedtransacs.put("personType", "NVTR");
        jsondetailedtransacs.put("invoiceTypeDesc", transactionType);
        jsondetailedtransacs.put("currency", "MUR");
        jsondetailedtransacs.put("invoiceIdentifier", transactionid);
        jsondetailedtransacs.put("invoiceRefIdentifier", InvoiceRefIdentifyer);
        jsondetailedtransacs.put("previousNoteHash", "prevNote");
        jsondetailedtransacs.put("reasonStated", "test");
    jsondetailedtransacs.put("invoiceTotal", formattedTotalInvoice);
    jsondetailedtransacs.put("discountTotalAmount", totaldiscount);
        jsondetailedtransacs.put("totalVatAmount", formattedTaxtotalAmount);
        jsondetailedtransacs.put("totalAmtWoVatCur", formattedTotalHT);
        jsondetailedtransacs.put("totalAmtPaid", formattedTotalAmount);
        jsondetailedtransacs.put("dateTimeInvoiceIssued", formattedDateTime);

}  else {
    if(cashorlevel.equals("1")) {
        transactionType = "TRN";
    }else{
        transactionType = "STD";
    }
    jsondetailedtransacs.put("invoiceCounter", String.valueOf(newCounter)); // Convert to String for JSON
    jsondetailedtransacs.put("transactionType", SelectedBuyerProfile);
    jsondetailedtransacs.put("personType", selectedBuyerType);
    jsondetailedtransacs.put("invoiceTypeDesc", transactionType);
    jsondetailedtransacs.put("currency", "MUR");
    jsondetailedtransacs.put("invoiceIdentifier", transactionid);
    jsondetailedtransacs.put("invoiceRefIdentifier", InvoiceRefIdentifyer);
    jsondetailedtransacs.put("previousNoteHash", "prevNote");
    jsondetailedtransacs.put("reasonStated", "test");
    jsondetailedtransacs.put("invoiceTotal", formattedTotalInvoice);
    jsondetailedtransacs.put("discountTotalAmount", totaldiscount);
    jsondetailedtransacs.put("totalVatAmount", formattedTaxtotalAmount);
    jsondetailedtransacs.put("totalAmtWoVatCur", formattedTotalHT);
    jsondetailedtransacs.put("totalAmtPaid", formattedTotalAmount);
    jsondetailedtransacs.put("dateTimeInvoiceIssued", formattedDateTime);

}



    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
    if (cursorCompany != null && cursorCompany.moveToFirst()) {
        int columnCompanyAddressIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_1);
        int columnCompanyAddress2Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_2);
        int columnCompanyAddress3Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_3);
        int columnCompanyVATIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_VAT_NO);
        int columnCompanyBRNIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_BRN_NO);
        int columnTelNumIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_TEL_NO);
        int columncompnameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_COMPANY_NAME);

        String CompanyName = cursorCompany.getString(columncompnameIndex);
        String CompanyAdress1 = cursorCompany.getString(columnCompanyAddressIndex);
        String CompanyAdress2 = cursorCompany.getString(columnCompanyAddress2Index);
        String CompanyAdress3 = cursorCompany.getString(columnCompanyAddress3Index);
        String Addresse = CompanyAdress1 + "," + CompanyAdress2 + "," + CompanyAdress3;
        String CompanyVatNo = cursorCompany.getString(columnCompanyVATIndex);
        String CompanyBRNNo = cursorCompany.getString(columnCompanyBRNIndex);
        String TelNum = cursorCompany.getString(columnTelNumIndex);
        // Construct the "seller" JSON object
        JSONObject seller = new JSONObject();
        seller.put("name", CompanyName);
        seller.put("tan", CompanyVatNo);
        seller.put("brn", CompanyBRNNo);
        seller.put("businessAddr", Addresse);
        seller.put("businessPhoneNum", TelNum);
        seller.put("ebsCounterNo", Till_id);
        jsondetailedtransacs.put("seller", seller);
    }


                        // Construct the "buyer" JSON object
                        JSONObject buyer = new JSONObject();
                        buyer.put("name", selectedBuyerName);
                        buyer.put("tan", selectedBuyerTAN);
                        buyer.put("brn", selectedBuyerBRN);
                        buyer.put("businessAdd", selectedBuyerAddresse);
                        buyer.put("buyerType", selectedBuyerType);
                        buyer.put("nic", selectedBuyerNIC);
                        jsondetailedtransacs.put("buyer", buyer);

                        // Assuming you have retrieved the list of items from your database
                        List<Transaction> itemListFromDatabase = getItemsFromDatabase(); // Replace with your method to fetch items

                        // Construct the "itemList" JSON array
                        JSONArray itemList = new JSONArray();

                        for (Transaction itemFromDatabase : itemListFromDatabase) {
                            JSONObject transaction = new JSONObject();
                            transaction.put("itemNo", itemFromDatabase.getId());
                            transaction.put("taxCode", itemFromDatabase.getTaxCode());
                            transaction.put("nature", itemFromDatabase.getNature());
                            transaction.put("currency", itemFromDatabase.getCurrency());
                            transaction.put("itemCode", itemFromDatabase.getItemcode());
                            transaction.put("itemDesc", itemFromDatabase.getLongDescription());
                            transaction.put("quantity", itemFromDatabase.getItemQuantity());
                            transaction.put("unitPrice", itemFromDatabase.getUnitPrice());
                            transaction.put("discount", itemFromDatabase.getDiscount());
                            transaction.put("amtWoVatCur", itemFromDatabase.getTotalAmountWitoutVAT());
                            transaction.put("amtWoVat", itemFromDatabase.getTotalAmountWitoutVAT());
                            transaction.put("tds", itemFromDatabase.getTotalDiscount());
                            transaction.put("vatAmt", itemFromDatabase.getTotalVatAmount());
                            transaction.put("totalPrice", itemFromDatabase.getTotalPrice());
                            itemList.put(transaction);
                        }

// Add the itemList to the main JSON object
                        jsondetailedtransacs.put("itemList", itemList);


                        jsondetailedtransacs.put("salesTransactions", "CASH");
                        jsondetailedtransacs.put("paymentMethods", "CASH");

                        // Convert JSON object to string
                        String jsondetails = jsondetailedtransacs.toString();
                        jsondetails = "[" + jsondetails + "]";

                        System.out.println("newjson: " + jsondetails);
                        System.out.println("selectedBuyerName: " + selectedBuyerName);



                        // Parse the response JSON to extract the key
                        JSONObject responseJson = new JSONObject(responseBody);
                        String encryptedKeyBase64 = responseJson.getString("key");
                        String encryptedtokenBase64 = responseJson.getString("token");
                        System.out.println("token: " + encryptedtokenBase64);
                        // Decrypt the encrypted key using the key from your payload
                        String decryptedKey = decryptKey(encryptedKeyBase64, aesKey);

                        // Now you have the decrypted key to use for encryption
                        String encryptedInvoice = encryptedInvoice(jsondetails, decryptedKey);


                        // Construct the JSON request body
                        JSONObject requestBodyMRAQR = new JSONObject();
                        requestBodyMRAQR.put("requestId", requestidmethod); // Replace with your request ID
                        requestBodyMRAQR.put("requestDateTime", formattedDateTime);
                        requestBodyMRAQR.put("signedHash", ""); // Replace with your request ID
                        requestBodyMRAQR.put("encryptedInvoice", encryptedInvoice);

                        OkHttpClient clients = new OkHttpClient();

                        MediaType mediaTypes = MediaType.parse("application/json");
                        RequestBody body1 = RequestBody.create(mediaTypes, requestBodyMRAQR.toString());
                        System.out.println("requestbody: " + requestBodyMRAQR);

                        Request requests = new Request.Builder()
                                .url("https://vfisc.mra.mu/realtime/invoice/transmit")
                                .addHeader("Content-Type", "application/json")

                                .addHeader("token", encryptedtokenBase64)
                                .addHeader("ebsMraId", "16887137012292S4IDFGH10H")
                                .addHeader("username", "LBatour")
                                .addHeader("areaCode", "734")
                                .post(body1)
                                .build();

                        Response responsesQRMRA = clients.newCall(requests).execute();
                        if (responsesQRMRA.isSuccessful()) {
                            String responseBody1 = responsesQRMRA.body().string();
                            System.out.println("response: " + responseBody1);
                            String qrCode = extractQrCode(responseBody1);
                             irn= extractIRNCode(responseBody1);
                            if (qrCode != null) {
                                System.out.println("QR Code: " + qrCode);
                                System.out.println("IRN: " + irn);
                            } else {
                                System.out.println("QR Code not found or error occurred.");
                            }

// If QR code not found, return appropriate message
                            return qrCode;

                        } else {
                            return "Error response code: " + responsesQRMRA;
                        }
                    }else{
                    return "Error from sqlite: " ;
                    }

                } else {
                        return "Error response code 1: " + response.code();
                    }

                } catch(Exception e){
                    e.printStackTrace();
                    return "Request Failed: " + e.getMessage();
                }
            }

        @Override
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result != null && !result.startsWith("Request Failed")) {
                        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(roomid,tableid);
                        Log.d("qrcode", result); // Log the QR code string
                        Log.d("result1", result); // Log the QR code string
                        unmergeTable(tableid);
                        Intent intent = new Intent(getApplication(), printerSetup.class);
                        intent.putExtra("amount_received", totalAmountinserted);
                        intent.putExtra("cash_return", cashReturn);
                        intent.putExtra("settlement_items", settlementItems);
                        intent.putExtra("mraQR", result);
                        intent.putExtra("transactionid", transactionIdInProgress);
                        intent.putExtra("MRAIRN", irn);
                        intent.putExtra("roomid", roomid);
                        intent.putExtra("tableid", tableid);
                        System.out.println("tr: " + transactionIdInProgress);
                        System.out.println("selectedBuyerTAN: " + selectedBuyerTAN);

                        String MRAMETHOD="Single";
                        insertCashReturn(cashReturn, totalAmountinserted,result,irn,MRAMETHOD,transactionType, selectedBuyerName, selectedBuyerTAN, selectedBuyerBRN, selectedBuyerNIC);
                        startActivity(intent);
                    } else {
                        String result="Request Failed";
                        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(roomid,tableid);
                        // Show "MRA Request Failed" message
                        Log.d("qrcode", result); // Log the QR code string
                        Log.d("result2", result);
                        unmergeTable(tableid);
                        Intent intent = new Intent(getApplication(), printerSetup.class);
                        intent.putExtra("amount_received", totalAmountinserted);
                        intent.putExtra("cash_return", cashReturn);
                        intent.putExtra("settlement_items", settlementItems);
                        intent.putExtra("mraQR", result);
                        intent.putExtra("MRAIRN", irn);
                        intent.putExtra("roomid", roomid);
                        intent.putExtra("tableid", tableid);

                        System.out.println("tr: " + transactionIdInProgress);


                        String MRAMETHOD="Single";
                        insertCashReturn(cashReturn, totalAmountinserted,result,irn,MRAMETHOD,transactionType, selectedBuyerName, selectedBuyerTAN, selectedBuyerBRN, selectedBuyerNIC);
                        startActivity(intent);
                    }
                }
            });
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashflash);
        mDatabaseHelper = new DatabaseHelper(this);

        // Retrieve the passed buyer information from the intent
        Intent intent = getIntent();
        if (intent != null) {
            Transaction_Id = intent.getStringExtra("id");
            totalAmountinserted= intent.getStringExtra("amount_received");
            cashReturn= intent.getStringExtra("cash_return");
            settlementItems = getIntent().getParcelableArrayListExtra("settlement_items");
             selectedBuyerName = intent.getStringExtra("selectedBuyerName");
             selectedBuyerTAN = intent.getStringExtra("selectedBuyerTAN");
             selectedBuyerCompanyName = intent.getStringExtra("selectedBuyerCompanyName");
             selectedBuyerType = intent.getStringExtra("selectedBuyerType");
             selectedBuyerBRN = intent.getStringExtra("selectedBuyerBRN");
             selectedBuyerNIC = intent.getStringExtra("selectedBuyerNIC");
             selectedBuyerAddresse = intent.getStringExtra("selectedBuyerAddresse");
             SelectedBuyerProfile= intent.getStringExtra("selectedBuyerprofile");
            roomid= getIntent().getStringExtra("roomid");
            tableid  = getIntent().getStringExtra("tableid");



            // Retrieve other buyer information as needed
        }




        SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierName = sharedPreference.getString("cashorName", null);
        cashorlevel = sharedPreference.getString("cashorlevel", null);
        CompanyName=sharedPreference.getString("CompanyName",null);
        ShopName = sharedPreference.getString("ShopName", null);


        try {

            aesKey = generateRandomAESKey();


            String payload = "{\n" +
                    " \"username\": \"LBatour\",\n" +
                    " \"password\": \"Logi159753@\",\n" +
                    " \"encryptKey\": \""+ aesKey +"\",\n" +
                    " \"refreshToken\": \"false\"\n" +
                    "}";


            Resources res = getResources();
            InputStream certificateInputStream = res.openRawResource(R.raw.ibora_pos_mra_pub_key);  // Replace with your cert filename

            // Load the recipient's certificate
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(certificateInputStream);


            // Extract the recipient's public key from the certificate
            PublicKey publicKey = certificate.getPublicKey();
            byte[] encoded = publicKey.getEncoded();

            byte[] b64key = android.util.Base64.encode(encoded, android.util.Base64.DEFAULT);

            String b64keyString = new String(b64key, StandardCharsets.UTF_8).replace("\n", "");


            String encryptedPayload = encryptData(payload,b64keyString);

            //textViewResult.setText("Encrypted Payload:\n" + encryptedPayload);
            // Execute the background task to send the payload
            new SendPayloadTask().execute(encryptedPayload);

        } catch (Exception e) {
            e.printStackTrace();
           // textViewResult.setText("Encryption Failed");
            Log.e(TAG, "Encryption Failed", e);
        }
    }


    public void  insertCashReturn(String cashReturn, String totalAmountinserted, String qrMra, String mrairn, String MRAMETHOD,String Transactiontype,String selectedBuyerName,String selectedBuyerTAN,String selectedBuyerBRN,String selectedBuyerNIC){
        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(roomid,tableid);
        Log.d("tr2", String.valueOf(transactionIdInProgress));
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        double sumBeforeDisc = mDatabaseHelper.getSumOfTransactionVATBeforeDiscByTransactionId(db,transactionIdInProgress);
        double sumAfterDisc = mDatabaseHelper.getSumOfTransactionVATAfterDiscByTransactionId(db,transactionIdInProgress);

        db.close();
        mDatabaseHelper.insertcashReturn(cashReturn,totalAmountinserted, transactionIdInProgress,qrMra,mrairn,MRAMETHOD);
        mDatabaseHelper.getTotalVATAndUpdateTransactionHeader(transactionIdInProgress,sumBeforeDisc,sumAfterDisc,selectedBuyerName,selectedBuyerTAN,selectedBuyerBRN,selectedBuyerNIC);

    }
    public static String extractQrCode(String apiResponse) {
        try {
            JSONObject jsonResponse = new JSONObject(apiResponse);
            if (jsonResponse.has("fiscalisedInvoices")) {
                JSONObject firstInvoice = jsonResponse.getJSONArray("fiscalisedInvoices").getJSONObject(0);
                if (firstInvoice.has("qrCode")) {
                    return firstInvoice.getString("qrCode");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // Return null if qrCode is not found or an error occurs
    }
    public static String extractIRNCode(String apiResponse) {
        try {
            JSONObject jsonResponse = new JSONObject(apiResponse);
            if (jsonResponse.has("fiscalisedInvoices")) {
                JSONObject firstInvoice = jsonResponse.getJSONArray("fiscalisedInvoices").getJSONObject(0);
                if (firstInvoice.has("irn")) {
                    return firstInvoice.getString("irn");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // Return null if qrCode is not found or an error occurs
    }

    public static String getRequestid() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static String generateRandomAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // 256 bits = 32 bytes
            SecretKey secretKey = keyGenerator.generateKey();

            byte[] encodedKey = secretKey.getEncoded();
            String base64Key = Base64.encodeToString(encodedKey, Base64.DEFAULT);

            return base64Key.trim(); // Remove any newline characters from the Base64 string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String encryptData(String plainText, String b64PublicKey) throws Exception {
        // Convert the base64-encoded public key back to bytes
        byte[] publicKeyBytes = android.util.Base64.decode(b64PublicKey, android.util.Base64.DEFAULT);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // Initialize the Cipher with the recipient's public key for encryption using RSA/ECB/PKCS1Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Encrypt the data
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.NO_WRAP);
    }


    private String decryptKey(String encryptedKeyBase64, String encryptionKeyFromPayload) throws Exception {
        byte[] encryptedKeyBytes = Base64.decode(encryptedKeyBase64, Base64.DEFAULT);
        byte[] encryptionKeyBytes = Base64.decode(encryptionKeyFromPayload, Base64.DEFAULT);

        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKeyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedKeyBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    private String encryptedInvoice(String plainText, String encryptionKey) throws Exception {
        byte[] keyBytes = Base64.decode(encryptionKey, Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
    }

    private  String readTextFromFile(String fileName) {
        try {
            FileInputStream fileInputStream = this.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private List<Transaction> getItemsFromDatabase() {
        List<Transaction> itemList = new ArrayList<>();

        // Replace the following with your actual database query logic
        Cursor cursor = mDatabaseHelper.getTransactionsByRoomAndTable(mDatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS,roomid,tableid);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract column indices
                int columnIndexItemNo = cursor.getColumnIndex(ITEM_ID); // Replace with the actual column name
                int columnIndexTaxCode = cursor.getColumnIndex(TRANSACTION_TAX_CODE); // Replace with the actual column name
                int columnIndexNature = cursor.getColumnIndex(TRANSACTION_NATURE);
                int columnIndexCurrency = cursor.getColumnIndex(TRANSACTION_CURRENCY);
                int columnIndexItemCode = cursor.getColumnIndex(TRANSACTION_ITEM_CODE);
                int columnIndexitemDesc = cursor.getColumnIndex(LongDescription);
                int columnIndexquantity = cursor.getColumnIndex(QUANTITY);
                int columnIndexunitPrice = cursor.getColumnIndex(TRANSACTION_UNIT_PRICE);
                int columnIndexdiscount = cursor.getColumnIndex(TRANSACTION_DISCOUNT);
                int columnIndexamtWoVatCur = cursor.getColumnIndex(TRANSACTION_TOTAL_HT_A);
                int columnIndexamtWoVat = cursor.getColumnIndex(TRANSACTION_TOTAL_HT_A);
                int columnIndextds = cursor.getColumnIndex(TRANSACTION_TOTAL_DISCOUNT);
                int columnIndexvatAmt = cursor.getColumnIndex(VAT);
                int columnIndextotalPrice = cursor.getColumnIndex(TRANSACTION_TOTAL_TTC);

                // Retrieve data from the cursor
                String itemNo = cursor.getString(columnIndexItemNo);
                String taxCode = cursor.getString(columnIndexTaxCode);
                String nature = cursor.getString(columnIndexNature);
                String currency = cursor.getString(columnIndexCurrency);
                String itemcode = cursor.getString(columnIndexItemCode);
                String descryption = cursor.getString(columnIndexitemDesc);
                String quantity = cursor.getString(columnIndexquantity);
                String UnitPrice = cursor.getString(columnIndexunitPrice);
                String discount = cursor.getString(columnIndexdiscount);
                String amountWOVATCur = cursor.getString(columnIndexamtWoVatCur);
                String amountWOVAT = cursor.getString(columnIndexamtWoVat);
                String tds = cursor.getString(columnIndextds);
                String VatAmount = cursor.getString(columnIndexvatAmt);
                String totalPrice = cursor.getString(columnIndextotalPrice);

                // Create an Item object and add it to the list
                Transaction transaction = new Transaction();
                transaction.setId(Integer.parseInt(itemNo));
                transaction.setTaxCode(taxCode);
                transaction.setNature(nature);
                transaction.setCurrency(currency);
                transaction.setItemCode(itemcode);
                transaction.setLongDescription(descryption);
                transaction.setItemQuantity(Integer.parseInt(quantity));
                transaction.setUnitPrice(UnitPrice);
                transaction.setDiscount(discount);
                transaction.setAmountWOVAT(Double.parseDouble(amountWOVATCur));
                transaction.setAmountWOVAT(Double.parseDouble(amountWOVAT));
                transaction.setTotalDiscount(tds);
                transaction.setTotalVatAmount(Double.parseDouble(VatAmount));
                transaction.setTotalPrice(Double.parseDouble(totalPrice));

                // ... Set other properties

                itemList.add(transaction);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return itemList;
    }
    private void unmergeTable(String selectedTableNum) {


        // Extract the numeric part from selectedTableToMerge
        String numericPart = extractNumericPart(selectedTableNum);

        // Update your database to unmerge the table
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues valuesTable2 = new ContentValues();
        // Update MERGED and MERGED_SET_ID columns
        values.put(DatabaseHelper.MERGED, 0);
        values.put(DatabaseHelper.MERGED_SET_ID,"0");

        int rowsUpdated = db.update(DatabaseHelper.TABLES, values, DatabaseHelper.MERGED_SET_ID + " = ?",
                new String[]{selectedTableNum});
        valuesTable2.put(DatabaseHelper.MERGED, 0);

        db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.TABLE_NUMBER + " = ?",
                new String[]{numericPart});
        db.close();

        if (rowsUpdated > 0) {
            // Table unmerged successfully
            Log.d("UnmergeTable", "Table " + selectedTableNum + " has been unmerged.");
        } else if (rowsUpdated == 0) {
            // No rows were updated
            Log.d("UnmergeTable", "Failed to unmerge table " + selectedTableNum + ". No rows were updated.");
        } else {
            // An error occurred
            Log.d("UnmergeTable", "Failed to unmerge table " + selectedTableNum + ". Error occurred during update.");
        }

        // Refresh the UI to reflect the changes
        // You may need to update your RecyclerView adapter or rerun the database query to fetch updated data


        SharedPreferences    sharedPreferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", "0").apply();
        sharedPreferences.edit().putInt("roomnum", 0).apply();


    }

    private String extractNumericPart(String tableString) {
        // Split the string based on space and return the last part
        String[] parts = tableString.split(" ");
        return parts[parts.length - 1];
    }
}