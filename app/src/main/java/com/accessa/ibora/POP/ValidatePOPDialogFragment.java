package com.accessa.ibora.POP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.accessa.ibora.CancelService;
import com.accessa.ibora.PrintService;
import com.accessa.ibora.R;
import com.accessa.ibora.proceedPOPService;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.bumptech.glide.Glide;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;

public class ValidatePOPDialogFragment extends DialogFragment {
    private DatabaseHelper mDatabaseHelper;
    private TextView resultTextView;

    private TextView requestDataTextView;

    private GifImageView     loadingGifImageView;
    private String key,IV;
    private String ReqRefId;
    private SoundPool soundPool;
    private int soundId,roomid;
    private  String encryptedRequest;
    private  String popreqid,amount,tableid;
    // Add a handler as a class member
    private final Handler handler = new Handler();

    // Add a constant for the check interval in milliseconds
    private static final long CHECK_INTERVAL = 5000; // 10 seconds
    private  String jsonRequestBody;
    private String clientID;
    private String   apiLink;
    private Context context;
    private Button btn;

    private AlertDialog alertDialog; // Declare the member variable

    private   String USERNAME,PASSWORD, status;
   // Add a method to retrieve the mobile number from arguments
   @Override
   public void onAttach(@NonNull Context context) {
       super.onAttach(context);
       this.context = context;
   }
    public static ValidatePOPDialogFragment newInstance(String popReqId, String key, String IV, String amount) {
        ValidatePOPDialogFragment fragment = new ValidatePOPDialogFragment();
        Bundle args = new Bundle();
        args.putString("popReqId", popReqId);
        args.putString("key", key);
        args.putString("IV", IV);
        args.putString("amount", amount);
        fragment.setArguments(args);
        return fragment;
    }


    private byte[] encryptWithPublicKey(String plainText, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec(
                    "SHA-1", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT
            );
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParameterSpec);
            return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private View view; // Declare the view variable



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");
        // Get the mobile number from the arguments
        popreqid = getArguments().getString("popReqId");
        amount = getArguments().getString("amount");
        // Initialize the SoundPool and load the sound file
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        soundId = soundPool.load(getActivity(), R.raw.notif, 1);

// Play the sound effect when an item is added
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            }
        });

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Call showStatusPopup() when the fragment is properly attached to its activity.
        // Ensure that the view is not null and the fragment is attached to the activity.
        if (getView() != null && isAdded()) {
            showStatusPopup(status,context); // Pass the status you want to show
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         view = LayoutInflater.from(getActivity()).inflate(R.layout.pop, null);
        mDatabaseHelper = new DatabaseHelper(getContext());

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(dismissReceiver,
                new IntentFilter("com.accessa.ibora.DISMISS_DIALOG"));
        Log.d("ValidatePOPDialog", "onCreateDialog called");
        // Get a reference to the GifImageView
         loadingGifImageView = view.findViewById(R.id.loadapiresponse);


        // Show the loading GIF animation
        loadingGifImageView.setVisibility(View.VISIBLE);


        // Retrieve the mobile number from the arguments
        popreqid = getArguments().getString("popReqId");
        key = getArguments().getString("key");
        IV = getArguments().getString("IV");

        resultTextView = view.findViewById(R.id.resultTextView);
        requestDataTextView = view.findViewById(R.id.requestDataTextView);


        resultTextView.setVisibility(View.GONE);
        requestDataTextView.setVisibility(View.GONE);
        // Read the content from internal storage files
        String validate = readTextFromFile("api_addresss.txt");
        String status = readTextFromFile("payment_status.txt");


        String tillnum = readTextFromFile("till_id.txt");
        String outletnum = readTextFromFile("outlet.txt");
        String clientId = readTextFromFile("client_id.txt");

        // Replace "public_key" with your XML file name in res/raw folder
        jsonRequestBody = "{ \"tranId\": \"12345\", \"popReqId\": \"20210806000013\"}";

        String apiRequestBody = "{\"header\": {\"apiversion\": \"string\", \"clientID\": \"string\", \"timeStamp\": \"string\", \"hCheckValue\": \"string\", \"requestUUID\": \"string\" }, \"request\": \"string\"}";

        encryptRequest(jsonRequestBody,key,IV);
         context = getContext();



        // Read RSA public key from raw resource and convert to PEM format
        String pemKey = convertXMLToPEM(R.raw.public_key);

        // Print the PEM format key to the log
        Log.d("PEM Key", pemKey);

        // The concatenated key and IV you want to encrypt
        String keyAndIVConcatenation = key +"|" +IV;

        // Encrypt the concatenated key and IV using the PEM public key

        PublicKey publicKey = getPublicKeyFromPEM(pemKey);
        byte[] encryptedData = encryptWithPublicKey(keyAndIVConcatenation, publicKey);


        // Display encrypted data in log
        if (encryptedData != null) {
            ReqRefId= Base64.encodeToString(encryptedData, Base64.DEFAULT);

        } else {
            Log.e("Encryption Error", "Failed to encrypt data.");
        }


         clientID = clientId;

         encryptedRequest = createEncryptedRequest(mDatabaseHelper,tillnum,outletnum,popreqid, context,clientID, jsonRequestBody,key,IV,roomid,tableid);
        String hCheckValue = generateHCheckValue(jsonRequestBody);


        // Remove newline characters from the reqRefId value
        ReqRefId = ReqRefId.replaceAll("\\n|\\r", "");
        // Read API link from raw file
         apiLink = status;

        // Construct the API request with the obtained values
        String apiRequest =
                "popreqid: " + popreqid + "\n" +
                        "ReqRefId: " + ReqRefId + "\n" +
                        "Encrypted Request: " + encryptedRequest + "\n" +
                        "hCheckValue: " + hCheckValue;

        requestDataTextView.setText(apiRequest);

            resendApiRequest(clientID,context);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }



    // Schedule periodic checks
    private void schedulePeriodicCheck() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Resend the API request
                resendApiRequest(clientID,context);
            }
        }, CHECK_INTERVAL);
    }

    // Method to resend the API request
    private void resendApiRequest( String clientID,Context context) {

        // Create a HashMap to store header parameters
        Map<String, String> headerParameters = new HashMap<>();
        headerParameters.put("clientID", clientID);
        headerParameters.put("ReqRefId", ReqRefId);
        // Create and send the API request again
        // Send the API request using OkHttp with the header parameters
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase the connection timeout
                .readTimeout(60, TimeUnit.SECONDS) // Increase the read timeout
                .build();


        MediaType mediaType = MediaType.parse("application/json; charset=utf-8"); // Specify the content type and charset
        RequestBody requestBody = RequestBody.create(mediaType, encryptedRequest);
        String username = readTextFromFile("api_user.txt");
        String passwordValue = readTextFromFile("password.txt");
        USERNAME =username;
        PASSWORD = passwordValue;
        String AUTHORIZATION_HEADER = "Basic " + Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);


        Request.Builder requestBuilder = new Request.Builder()
                .url(apiLink)
                .post(requestBody)
                .addHeader("Authorization", AUTHORIZATION_HEADER); // Set the Authorization header for Basic Authentication

        // Add the header parameters to the request
        for (Map.Entry<String, String> entry : headerParameters.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle request failure
                // Hide the loading GIF animation
                loadingGifImageView.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    // Handle successful response
                    Log.d("API Response", responseData);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            // Hide the loading GIF animation
                            loadingGifImageView.setVisibility(View.GONE);
                            resultTextView.setVisibility(View.VISIBLE);
                            requestDataTextView.setVisibility(View.VISIBLE);

                            try {
                                // Parse the JSON data
                                JSONObject jsonObject = new JSONObject(responseData);



                                // Extract the response field
                                String responseData = jsonObject.getString("response");
                                // Extract the hcheckValue field

                                String decryptedresponse=  decryptResponse(responseData, key, IV);

                                resultTextView.setVisibility(View.VISIBLE);
                                resultTextView.setText(decryptedresponse);

                                try {
                                    // Parse the JSON data
                                    JSONObject jsonObject1 = new JSONObject(decryptedresponse);

                                    // Extract the status field from the response
                                    String status = jsonObject1.getString("status");
                                    if ("Payment request Pending".equals(status)) {


                                        // If the status is "Payment request Pending," schedule the next check
                                        schedulePeriodicCheck();


                                    }
                                    // Show the status in a pop-up dialog

                                    Log.e("status",status);



                                    showStatusPopup(status,context);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    });
                } else {
                    // Handle response error
                    final String errorResponse = response.body().string(); // Retrieve the error response body

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setVisibility(View.VISIBLE);
                            requestDataTextView.setVisibility(View.VISIBLE);
                            Log.e("API Response Error", "Response was not successful. Code: " + response.code() + ", Error: " + errorResponse);
                            resultTextView.setText("Error: " + errorResponse); // Display the error message on the TextView
                            // Hide the loading GIF animation
                            loadingGifImageView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void showStatusPopup(String status,Context context) {


        // Dismiss the previous AlertDialog if it exists and is showing
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Payment Status")
                .setMessage(status)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        // Set the custom view to the AlertDialog
        View view = LayoutInflater.from(context).inflate(R.layout.payment_status, null);
        builder.setView(view);
        builder.setCancelable(false);
        // Initialize the AlertDialog object
        alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        // Store the AlertDialog instance in the member variable
        alertDialog = builder.create();

        // Get a reference to the AppCompatImageView
        AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);
        Button cancelBtn= view.findViewById(R.id.btncancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serviceIntent = new Intent(context, CancelService.class);
                serviceIntent.putExtra("popreqid", popreqid);
                serviceIntent.putExtra("key", key);
                serviceIntent.putExtra("IV", IV);
                context.startService(serviceIntent);

            }
        });
        if("Payment Request not found.".equals(status)){
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.paynotfound)
                    .into(gifImageView);
            cancelBtn.setVisibility(View.GONE);
        }else if("Payment request Cancelled".equals(status)){
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.paycancelgif)
                    .into(gifImageView);
            cancelBtn.setVisibility(View.GONE);
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);
                if(formattedTotalAmount.equals(amount)) {
                    // Pass the amount received and settlement items as extras to the print activity
                    Intent serviceIntent = new Intent(context, PrintService.class);
                    serviceIntent.putExtra("POP", "POP");
                    context.startService(serviceIntent);

                }else{

                    Intent serviceIntent = new Intent(context, proceedPOPService.class);
                    String transactionIdInProgress;
                    String TRANSACTION_ID_KEY = "transaction_id";
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
                    serviceIntent.putExtra("transaction_id", TRANSACTION_ID_KEY);
                    serviceIntent.putExtra("amount", amount);
                    // Create and show the dialog fragment with the data
                    String popFraction= "popfraction";
                    serviceIntent.putExtra("popFraction", popFraction);
                    context.startService(serviceIntent);



                }
            }
        }else if("null".equals(status)){
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.paymentsucessgif)
                    .into(gifImageView);
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);
                if(formattedTotalAmount.equals(amount)) {
                    // Pass the amount received and settlement items as extras to the print activity
                    Intent serviceIntent = new Intent(context, PrintService.class);
                    serviceIntent.putExtra("POP", "POP");
                    context.startService(serviceIntent);
                    cancelBtn.setVisibility(View.GONE);
                }else{
                    String transactionIdInProgress;
                    String TRANSACTION_ID_KEY = "transaction_id";
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
                    // Get the activity associated with the fragment
                    AppCompatActivity activity = (AppCompatActivity) requireActivity();

                    // Create and show the dialog fragment with the data
                    String popFraction= "popfraction";
                    String result=null;
                    validateticketDialogFragment dialogFragment = validateticketDialogFragment.newInstance(transactionIdInProgress,amount,popFraction,null,null,null,null,null,null,null,null);
                    dialogFragment.setTargetFragment(ValidatePOPDialogFragment.this, 0);
                    dialogFragment.show(activity.getSupportFragmentManager(), "validate_transaction_dialog");
                }
            }

        }
        else {
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.pendinggif)
                    .into(gifImageView);


        }

        // Set the status text in the popup
        TextView statusTextView = view.findViewById(R.id.statusTextView);
        statusTextView.setText(status);


        alertDialog.show();
    }

    private void dismissStatusPopup() {
        // Dismiss the AlertDialog
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
    public static String encryptRequest(String jsonformat, String key, String iv) {
        try {
            byte[] plainTextBytes = jsonformat.getBytes(StandardCharsets.UTF_8);


            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(plainTextBytes);

            // Convert the byte[] array to a printable String format
            String encryptedString = Base64.encodeToString(encrypted, Base64.DEFAULT);

            // Display the encrypted request in the log
            Log.d("Encrypted request", encryptedString);

            return encryptedString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public  String decryptResponse(String response, String key, String iv) {
        try {
            // Base64 decode the response data to get the encrypted bytes
            byte[] encryptedBytes = Base64.decode(response, Base64.DEFAULT);

            // Convert key and IV to byte arrays
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
            // Create AES cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Decrypt the response data using AES
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Convert decrypted bytes to plaintext (request)
            String decryptedRequest = new String(decryptedBytes, StandardCharsets.UTF_8);
            resultTextView.setText(decryptedRequest);
            // Display the encrypted request in the log
            Log.d("Decrypted request", decryptedRequest);
            return decryptedRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String readTextFile(Context context, int resourceId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

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





    public static String createEncryptedRequest( DatabaseHelper mDatabaseHelper,String Till_id,String Outlet_id,String popreqid,Context  context, String clientID, String requestBody, String key, String IV,int roomid,String tableid) {
        try {

            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);
                String transactionIdInProgress;
                String TRANSACTION_ID_KEY = "transaction_id";

                // Generate random KEY and IV
                String requestUUID = generateRandomString(24);

                // Encrypt the request body using AES 256 Algorithm (CBC Mode)
                String encryptedRequest = encryptWithAES(key, IV, requestBody);
                Log.d("request", encryptedRequest);

                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

// Remove hyphens from the transactionIdInProgress string
                transactionIdInProgress = transactionIdInProgress.replaceAll("-", "");

               String jsonRequestBody = "{ \"tranId\": \""+ transactionIdInProgress +"\", \"popReqId\": \"" + popreqid + "\"}";

                // Create the JSON object containing the header and encrypted request
                String jsonRequest = "{\"header\":{\"apiversion\":\"v1\",\"clientID\":\"" + clientID + "\",\"timeStamp\":\"" + getUTCEpochTime() + "\",\"hCheckValue\":\"" + generateHCheckValue(jsonRequestBody) + "\",\"requestUUID\":\"" + requestUUID + "\"},\"request\":\"" + encryptRequest(jsonRequestBody, key, IV) + "\"}";

                String time = String.valueOf(getUTCEpochTime());

                Log.d("timestamp", time);
                // Remove line breaks and whitespace characters
                jsonRequest = jsonRequest.replace("\n", "").replace("\r", "").replace("\t", "").replaceAll("\\s+", " ");
                Log.d("plain request", jsonRequestBody);
                return jsonRequest;
            }else {
                return "";
            }
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String generateHCheckValue(String requestBody) {
        try {
            // Create SHA256 hash of the plain request body
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(requestBody.getBytes(StandardCharsets.UTF_8));

            // Convert hash bytes to hexadecimal string in uppercase
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = String.format("%02X", 0xFF & hashByte);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String encryptWithAES(String key, String iv, String plaintext) {
        try {
            // Convert key and IV to byte arrays
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);

            // Create AES cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encrypt the plaintext using AES
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Convert encrypted bytes to Base64 string
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readTextFromFile(String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
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
    public static String generateRandomString(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(allowedChars.length());
            stringBuilder.append(allowedChars.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }


    private PublicKey getPublicKeyFromPEM(String pemKey) {
        try {
            PemReader pemReader = new PemReader(new StringReader(pemKey));
            PemObject pemObject = pemReader.readPemObject();
            byte[] publicKeyBytes = pemObject.getContent();
            pemReader.close();

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String convertXMLToPEM(int publicKeyResourceId) {
        try {
            // Read the XML file from raw resource
            InputStream inputStream = getResources().openRawResource(publicKeyResourceId);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int ctr;
            while ((ctr = inputStream.read()) != -1) {
                byteArrayOutputStream.write(ctr);
            }
            inputStream.close();

            // Parse the XML and get the public key components
            String xmlContent = byteArrayOutputStream.toString(StandardCharsets.UTF_16.name());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(xmlContent)));
            Element rootElement = document.getDocumentElement();
            String exponent = rootElement.getElementsByTagName("Exponent").item(0).getTextContent();
            String modulus = rootElement.getElementsByTagName("Modulus").item(0).getTextContent();

            // Convert the public key to PEM format
            String pemKey = convertToPEM(exponent, modulus);

            return pemKey;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String convertToPEM(String exponent, String modulus) {
        try {
            byte[] pubExpBytes = Base64.decode(exponent, Base64.DEFAULT);
            byte[] modBytes = Base64.decode(modulus, Base64.DEFAULT);
            BigInteger pubExp = new BigInteger(1, pubExpBytes);
            BigInteger mod = new BigInteger(1, modBytes);
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(mod, pubExp);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            StringWriter stringWriter = new StringWriter();
            PemWriter pemWriter = new PemWriter(stringWriter);
            PemObject pemObject = new PemObject("RSA PUBLIC KEY", publicKey.getEncoded());
            pemWriter.writeObject(pemObject);
            pemWriter.close();

            return stringWriter.toString();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static long getUTCEpochTime() {
        // Get the current timestamp in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Convert the current timestamp to UTC timezone
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(utcTimeZone);
        calendar.setTimeInMillis(currentTimeMillis);

        // Get the UTC timestamp in seconds (epoch time)
        long utcEpochTime = calendar.getTimeInMillis() / 1000;

        return utcEpochTime;
    }
    // In the ValidatePOPDialogFragment

    // Local broadcast receiver to dismiss the dialog
    private BroadcastReceiver dismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Dismiss the dialog
            dismiss();
        }
    };

    @Override
    public void onDestroyView() {
        // Unregister the local broadcast receiver
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(dismissReceiver);
        super.onDestroyView();
    }
    private void playSoundEffect() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
