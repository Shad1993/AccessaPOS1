package com.accessa.ibora.POP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.accessa.ibora.R;

import com.accessa.ibora.product.items.DatabaseHelper;
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

public class PopMobileDialogFragment extends DialogFragment {
    private DatabaseHelper mDatabaseHelper;
    private TextView resultTextView;

    private TextView requestDataTextView;
    private Button Btncancel;
    private String key,IV;
    private String ReqRefId;
    private  String mobileNumber;
    private  String jsonRequestBody;
    private static final String EXTRA_MOBILE_NUMBER = "extra_mobile_number";
    private   String USERNAME,PASSWORD, status;

    // Add a method to retrieve the mobile number from arguments
    public static PopMobileDialogFragment newInstance(String mobileNumber) {
        PopMobileDialogFragment fragment = new PopMobileDialogFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_MOBILE_NUMBER, mobileNumber);
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

        // Get the mobile number from the arguments
         mobileNumber = getArguments().getString("EXTRA_MOBILE_NUMBER");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         view = LayoutInflater.from(getActivity()).inflate(R.layout.pop, null);
        mDatabaseHelper = new DatabaseHelper(getContext());
        // Get a reference to the GifImageView
        GifImageView loadingGifImageView = view.findViewById(R.id.loadapiresponse);


        // Show the loading GIF animation
        loadingGifImageView.setVisibility(View.VISIBLE);


        // Retrieve the mobile number from the arguments
         mobileNumber = getArguments().getString(EXTRA_MOBILE_NUMBER);

        resultTextView = view.findViewById(R.id.resultTextView);
        requestDataTextView = view.findViewById(R.id.requestDataTextView);


        resultTextView.setVisibility(View.GONE);
        requestDataTextView.setVisibility(View.GONE);
// Generate random KEY and IV
        key = generateRandomString(32);
        IV = generateRandomString(16);
        // Replace "public_key" with your XML file name in res/raw folder
        jsonRequestBody = "{\"outletId\":\"956\",\"tillId\":\"Till-356\",\"tranId\":\"808\",\"amount\":6.0,\"requestType\":\"Mobile\",\"requestValue\":\"23057031248\",\"currency\":\"MUR\",\"txnChannel\":\"POS\",\"remarks\":\"IntermartPOP\",\"expiry\":2}";

        String apiRequestBody = "{\"header\": {\"apiversion\": \"string\", \"clientID\": \"string\", \"timeStamp\": \"string\", \"hCheckValue\": \"string\", \"requestUUID\": \"string\" }, \"request\": \"string\"}";
        //String apiRequestBody = "{ \"header\": { \"apiversion\": \"v1\", \"clientID\": \"ead65173-250b-4453-a0d0-5fdf8d770eb5\", \"timeStamp\": \"20230719072015205\", \"hCheckValue\": \"205BF6246206DC744906111D9B8A40B4E7AC0F8DFC1A60165F10CAB417C7E506\", \"requestUUID\": \"L7tpJOeh7ezi5pOtRcKB0DKs\" }, \"request\": \"DqEtT3xTvamKtPHO3hCXzt3ZBpDXMmTpzjlD7tQvF9E2tWvQpbjlVifmuFpmggbMDC4X2QnFn4+5W07RTRn9XEdm\\/rOhMgNV2XrDmUuVD2xzVvRyp\\/dV088oDl5soMHk\\/IMhMyiAPUBPfal+U40nC3qWZJUpxgaa2a5iRXLJK4Wr6+251V1QXUrf5\\/Nr9ODosQD8LsXVgDOPOIu9XfiZO0mXw5yz7yVEBtF6UxgDKC5KZUGO7Q0Jv6v+Cj1ifTsSjHVPK0FNjNV5f2zo8jVdgDpE7Z9qwryMakwdxsivcQ7FNGf2+fjQtgopadztNvgjlnyG8sJJJY2Kamm\\/CGc7klqj43SMIdAAY8i8qOw1MjLdsBEcgUpbM1BcRf0abvX+7a7pSwGrpi03LOB5X0o3vJnOy18nnYDRVwGoaGRshEh7CleplTueq\\/SUziGTsaVPy+DqN2tOTkPgCTFNXCsKCWLnrLpLZwohK1+GkSGNPd1z\\/uxaja7QXX\\/8GA+Hw6EJbG1o0yFXxozAmUnvjNvZYajAE7+6QmiTCGz\\/leFLrwldDtD6QckLOka7RBboTad2pACMgPnd31SwKOpioGiz\\/YoQx++noxS4tov50CVUfvt8Ctscl9n7AURL5TSUt9BBsxgO\\/wl9bOfEDVT4E+w6KrwCJaz5kq2xLv6JKIGPoospNgy2dtbFi2IQPne0KEbwlKqJs6X6d66Th8FxG2WJ7d78jCJMyBrmWypXQY2wLMZP0RUvG1Qt19dyBrY21Orqku+POHNXBPzLyrnLz9RlxEGZqCB3GMHvj2g8KoysLd6WehDKrkKb2uscrCY+YRysOHBbSmBliPNfBgT+YB578kq2f6PGcbTz+v4tlnQfeauW6LBzRTyVHVTNdHXKCk3pHivHUaTdaB1g+k5OoRyAJcX7dFo7M1dVZ3WFuHfM95h14FI3CpEiTt8gU1jLxgAia5vWb+wXryAYrf4mNgNA43BRBRB4iQGQURSduB9AKJ3v0AVwmE\\/sCV4ug8dKKwt20QAAVNjmdclNwMPnwybI9IxyBr3Z9EpopTJujxQLJ9B7J0LFaNUpkPdA6Y41EBtp\\/1DQpe82EMWotnlVg3Y09O81OTtdbl0gmVyrX+fe+yXKTwTHapxIZ9uHlv\\/14fLdGRNzlmHC2Idbl7xhYuwkMk7i75MG0ojPafgU+ecQ0880bdz8oI2heUAB71rczeGH2W4FR7XI+GCzzWG8cBqMgaeFJ94jRktdhKv8SYf8yNWe0jgQlmW5y6hyR\\/OV2541\\/Y6IKtW9VEE82kwhUhvrBHV0cxrJsWo366bBfnCDV2uEcjk1uR6blHppmTnyvIz7HzpzSrKrPecUeBZe\\/48XqOfnQGtfkn7asQ041D+4L0eDGXcS\\/eZbGWJi4NkhFd3HOTAGARqrY6TI1e0Js5t4ZH77kSg01\\/NGz07UMpzC\\/urwoVZW4jaegOMwGr9d6UqwZPiDxDZX50skIWpm1dR54vVhEVLz7eaCdzqY0hzWsYV9pNRr59Ty8E00Il6s3Ny+RjWqtewNmDu99RbGMUWEXQK7cJyVRn8294TyTcAaXTaxaVjzNcp9YvtjAIjqDEfrGypqrdh5XmMnC3BskkqIca5iKzvGNsV0TuClK8o0TRs1g5KXWCzz+Sj01TWDqhTPdKdBgiRsRdUolQdI11soXtkaACvymxRf7e\\/m2YFwZNXEYRv2ytZDGFjGON8VjCcLinHtmMHbsfYIRzFwtq3DA4hTufxCnUOY8mIXVPAtdeO+UZmfIyHTiB8zef97c8K50T1rxTRPHK\\/1OapbWDMn6wCdruRX4ZfoZ3tlKSHTQXg=\"}";

        encryptRequest(jsonRequestBody,key,IV);
        Context context = getContext();



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
            Log.d("Reqrefid", ReqRefId);
        } else {
            Log.e("Encryption Error", "Failed to encrypt data.");
        }


        String clientID = loadClientID(context);

        String encryptedRequest = createEncryptedRequest(mDatabaseHelper,mobileNumber, context,clientID, jsonRequestBody,key,IV);
        String hCheckValue = generateHCheckValue(jsonRequestBody);


        // Remove newline characters from the reqRefId value
        ReqRefId = ReqRefId.replaceAll("\\n|\\r", "");
        // Read API link from raw file
        String apiLink = readTextFile(context, R.raw.api_addresss);
        USERNAME =readTextFile(context, R.raw.api_user);
        PASSWORD = readTextFile(context, R.raw.password);
        String AUTHORIZATION_HEADER = "Basic " + Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);


        // Construct the API request with the obtained values
        String apiRequest =
                "mobilenumber: " + mobileNumber + "\n" +
                        "ReqRefId: " + ReqRefId + "\n" +
                        "Encrypted Request: " + encryptedRequest + "\n" +
                        "hCheckValue: " + hCheckValue;

        requestDataTextView.setText(apiRequest);

// Create a HashMap to store header parameters
        Map<String, String> headerParameters = new HashMap<>();
        headerParameters.put("clientID", clientID);
        headerParameters.put("ReqRefId", ReqRefId);

// Send the API request using OkHttp with the header parameters
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase the connection timeout
                .readTimeout(60, TimeUnit.SECONDS) // Increase the read timeout
                .build();


        MediaType mediaType = MediaType.parse("application/json; charset=utf-8"); // Specify the content type and charset
        RequestBody requestBody = RequestBody.create(mediaType, encryptedRequest);

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


                                // Extract the hCheckValue field
                                String recievedhCheckValue = jsonObject.getJSONObject("header").getString("hCheckValue");

                                Log.d("hCheckValue", recievedhCheckValue);
                                // Extract the response field
                                String responseData = jsonObject.getString("response");
                                // Extract the hcheckValue field
                                // Extract the status object
                                JSONObject statusObject = jsonObject.getJSONObject("status");

                                // Extract the value of "m" (message)
                                String errormsg = statusObject.getString("m");


                            String decryptedresponse=  decryptResponse(responseData, key, IV);

                                // Validate hCheckValue
                                String responsehCheckValue = generateHCheckValue(decryptedresponse);
                                switch (errormsg) {
                                    case "Customer not found.":
                                        // Show Customer Not Found pop-up
                                        showCustomerNotFoundPopup();

                                        dismiss();
                                        break;
                                    case "TillCode not found.":
                                        // Show Customer Not Found pop-up
                                        showTillNotFoundPopup();
                                        dismiss();
                                        break;
                                    case "OutletId:The OutletId field is required.":
                                        // Show OutletId is Required pop-up
                                        showOutletIdRequiredPopup();
                                        dismiss();
                                        break;
                                    case "TillId:The TillId field is required.":
                                        // Show TillId is Required pop-up
                                        showTillIdRequiredPopup();
                                        dismiss();
                                        break;
                                    case "TranId:The TranId field is required.":
                                        // Show TranId is Required pop-up
                                        showTranIdRequiredPopup();
                                        dismiss();
                                        break;
                                    case "Amount:The Amount field is required.":
                                        // Show Amount is Required pop-up
                                        showAmountRequiredPopup();
                                        dismiss();
                                        break;
                                    case "RequestType:The RequestType field is required.":
                                        // Show RequestType is Required pop-up
                                        showRequestTypeRequiredPopup();
                                        dismiss();
                                        break;
                                    case "RequestValue:The RequestValue field is required.":
                                        // Show RequestValue is Required pop-up
                                        showRequestValueRequiredPopup();
                                        dismiss();
                                        break;
                                    case "Currency:The Currency field is required.":
                                        // Show Currency is Required pop-up
                                        showCurrencyRequiredPopup();
                                        dismiss();
                                        break;
                                    case "TxnChannel:The TxnChannel field is required.":
                                        // Show TxnChannel is Required pop-up
                                        showTxnChannelRequiredPopup();
                                        dismiss();
                                        break;
                                    case "Remarks:The Remarks field is required.":
                                        // Show Remarks is Required pop-up
                                        showRemarksRequiredPopup();
                                        dismiss();
                                        break;
                                    case "Expiry:The Expiry field is required.":
                                        // Show Expiry is Required pop-up
                                        showExpiryRequiredPopup();
                                        dismiss();
                                        break;
                                    case "TranId must be alphanumeric string. The Amount field is required. Enter valid positive amount up to 2 decimals.":
                                        // Show Invalid TranId and Amount pop-up
                                        showInvalidTranIdAmountPopup();
                                        dismiss();
                                        break;
                                    case "Failed to decrypt the response/An error occurred while processing your request. Please try again later.":
                                        // Show Decryption Error pop-up
                                        showDecryptionErrorPopup();
                                        dismiss();
                                        break;
                                    case "Unable to process the request. Please contact to system administrator.":
                                        // Show System Error pop-up
                                        showSystemErrorPopup();
                                        dismiss();
                                        break;
                                    default:

                                        dismiss();
                                        break;
                                }
                            if (responsehCheckValue.equals(recievedhCheckValue)) {
                                    // hCheckValue is valid, proceed with decrypting the response



                                    resultTextView.setVisibility(View.GONE);
                                    requestDataTextView.setVisibility(View.GONE);
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(decryptedresponse);
                                        String popReqId = jsonObject1.getString("popReqId");

                                        // Create the ValidatePOPDialogFragment instance and pass the mobile number, key, and IV as arguments
                                        ValidatePOPDialogFragment popMobileDialogFragment = ValidatePOPDialogFragment.newInstance(popReqId, key, IV);

                                        // Show the ValidatePOPDialogFragment
                                        popMobileDialogFragment.show(getChildFragmentManager(), "pop_mobile_dialog");

                                        // Dismiss the dialog
                                        dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }



                                } else {
                                    // hCheckValue is invalid, show the pop-up message
                                    showInvalidHCheckValuePopup();
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

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
    private void showCustomerNotFoundPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Customer Not Found");
        builder.setMessage("The customer is not found.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showTillNotFoundPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("TillCode Not Found");
        builder.setMessage("The TillCode is not found.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showOutletIdRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("OutletId is Required");
        builder.setMessage("The OutletId field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showTillIdRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("TillId is Required");
        builder.setMessage("The TillId field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showTranIdRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("TranId is Required");
        builder.setMessage("The TranId field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAmountRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Amount is Required");
        builder.setMessage("The Amount field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showRequestTypeRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("RequestType is Required");
        builder.setMessage("The RequestType field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showRequestValueRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("RequestValue is Required");
        builder.setMessage("The RequestValue field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showCurrencyRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Currency is Required");
        builder.setMessage("The Currency field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showTxnChannelRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("TxnChannel is Required");
        builder.setMessage("The TxnChannel field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showRemarksRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Remarks is Required");
        builder.setMessage("The Remarks field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showExpiryRequiredPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Expiry is Required");
        builder.setMessage("The Expiry field is required.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showInvalidTranIdAmountPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Invalid TranId and Amount");
        builder.setMessage("TranId must be an alphanumeric string. Enter a valid positive amount up to 2 decimals.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDecryptionErrorPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Decryption Error");
        builder.setMessage("Failed to decrypt the response or an error occurred while processing your request. Please try again later.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showSystemErrorPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("System Error");
        builder.setMessage("Unable to process the request. Please contact the system administrator.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void showInvalidHCheckValuePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Invalid hCheckValue")
                .setMessage("Please contact the POP Team.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, you can add any additional actions here if needed
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and show it
        AlertDialog dialog = builder.create();
        dialog.show();
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



    public static String loadClientID(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.client_id);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder clientIDBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                clientIDBuilder.append(line);
            }

            bufferedReader.close();

            return clientIDBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String createEncryptedRequest( DatabaseHelper mDatabaseHelper,String mobilenumber,Context  context, String clientID, String requestBody, String key, String IV) {
        try {

            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
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
                String Till_id = readTextFile(context, R.raw.till_id);
                String Outlet_id = readTextFile(context, R.raw.outlet);
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

// Remove hyphens from the transactionIdInProgress string
                transactionIdInProgress = transactionIdInProgress.replaceAll("-", "");


                String jsonRequestBodymobile = "{\"outletId\":\"" + Outlet_id + "\",\"tillId\":\"" + Till_id + "\",\"tranId\":\"" + transactionIdInProgress +"\",\"amount\":" + formattedTotalAmount + ",\"requestType\":\"Mobile\",\"requestValue\":\"" + mobilenumber +"\",\"currency\":\"MUR\",\"txnChannel\":\"POS\",\"remarks\":\"IntermartPOP\",\"expiry\":2}";
                String jsonRequestBodyqr = "{\"outletId\":\"" + Outlet_id + "\",\"tillId\":\"" + Till_id + "\",\"tranId\":\"" + transactionIdInProgress +"\",\"amount\":505.04,\"requestType\":\"QR\",\"requestValue\":\"00020101021126630009mu.maucas0112BKONMUM0XXXX021103011065958031500000000000005252047278530348054074235.285802MU5912IntermartOne6015Agalega North I622202112305786280707031436304BE4F \n\n\",\"currency\":\"MUR\",\"txnChannel\":\"POS\",\"remarks\":\"IntermartPOP\",\"expiry\":2}";

                // Create the JSON object containing the header and encrypted request
                String jsonRequest = "{\"header\":{\"apiversion\":\"v1\",\"clientID\":\"" + clientID + "\",\"timeStamp\":\"" + getUTCEpochTime() + "\",\"hCheckValue\":\"" + generateHCheckValue(jsonRequestBodymobile) + "\",\"requestUUID\":\"" + requestUUID + "\"},\"request\":\"" + encryptRequest(jsonRequestBodymobile, key, IV) + "\"}";

                String time = String.valueOf(getUTCEpochTime());

                Log.d("timestamp", time);
                // Remove line breaks and whitespace characters
                jsonRequest = jsonRequest.replace("\n", "").replace("\r", "").replace("\t", "").replaceAll("\\s+", " ");
                Log.d("plain request", jsonRequestBodymobile);
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

}
