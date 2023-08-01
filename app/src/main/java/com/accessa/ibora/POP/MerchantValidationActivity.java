package com.accessa.ibora.POP;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MerchantValidationActivity extends AppCompatActivity {
    private Button btnValidatePayment;
    private Context context;
    private TextView tvResponse,tvSentData;
    private static final String TAG = MerchantValidationActivity.class.getSimpleName();
    private static final String VALIDATE_URL = "https://apimprod.bankone.mu/MW/api/MW/Teller/ValidateQR/v1.0";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String CLIENT_ID = "ead65173-250b-4453-a0d0-5fdf8d770eb5";
    private   String USERNAME,PASSWORD, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_validate);

        btnValidatePayment = findViewById(R.id.btnValidatePayment);
        tvResponse = findViewById(R.id.tvResponse);
        tvSentData= findViewById(R.id.tvsent);
        context = this;
        btnValidatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMerchantPayment();
            }
        });
    }

    private void validateMerchantPayment() {
        try {

            USERNAME =readTextFile(context, R.raw.api_user);
            PASSWORD = readTextFile(context, R.raw.password);
            String AUTHORIZATION_HEADER = "Basic " + Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);


            // Create JSON request body
            JSONObject requestBody = createValidationRequestBody();

            // Create OkHttpClient instance
            OkHttpClient client = new OkHttpClient();

            // Create POST request with JSON body
            RequestBody body = RequestBody.create(JSON, requestBody.toString());
            Request request = new Request.Builder()
                    .url(VALIDATE_URL)
                    .post(body)
                    .header("Content-Type", "application/json")
                    .header("Charset", "UTF-8")
                    .header("Authorization", AUTHORIZATION_HEADER)
                    .header("clientID", CLIENT_ID)
                    .build();

            // Execute the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBody = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    handleValidationResponse(jsonResponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    tvResponse.setText("Error: Invalid JSON response");
                                }
                            } else {
                                tvResponse.setText("Error: " + response.code() + " - " + responseBody);
                            }

                            // Display the sent data
                            String sentData = null;
                            try {
                                sentData = createValidationRequestBody().toString();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            tvSentData.setText(sentData);
                        }
                    });
                }


                @Override
                public void onFailure(Call call, IOException e) {
                    // Request failed
                    Log.e(TAG, "Error occurred during merchant validation: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error occurred during merchant validation: " + e.getMessage());
        }
    }

    private JSONObject createValidationRequestBody() throws JSONException {
        // Create the request body in JSON format
        JSONObject requestBody = new JSONObject();

        // Add your request parameters here
        requestBody.put("outletId", "001");
        requestBody.put("tillId", "Till-178");
        requestBody.put("tranId", "12345");
        requestBody.put("amount", 10);
        requestBody.put("requestType", "Mobile");
        requestBody.put("requestValue", "23051234567");
        requestBody.put("currency", "MUR");
        requestBody.put("txnChannel", "eComm");
        requestBody.put("remarks", "KioskAPI_DevEnv");
        requestBody.put("expiry", 5);

        // Add cart details (if needed)
        JSONArray cartDetailsArray = new JSONArray();
        JSONObject cartDetail1 = new JSONObject();
        cartDetail1.put("productname", "DairyMilk");
        // Add more properties to the cartDetail1 object if necessary
        cartDetailsArray.put(cartDetail1);
        // Add more cart detail objects to the array if needed
        requestBody.put("cartDetails", cartDetailsArray);

        return requestBody;
    }

    private void handleValidationResponse(JSONObject response) throws JSONException {
        // Handle the validation response here
        // Extract the necessary information from the response JSON object
        String popReqId = response.getString("popReqId");
        String customerId = response.getString("customerId");

        // Create the response string
        String responseText = "POP Request ID: " + popReqId + "\nCustomer ID: " + customerId;

        // Update the UI to display the response
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResponse.setText(responseText);
            }
        });
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
}
