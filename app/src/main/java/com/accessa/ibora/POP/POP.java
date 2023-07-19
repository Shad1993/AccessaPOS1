package com.accessa.ibora.POP;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.accessa.ibora.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class POP extends Activity {

    private TextView resultTextView;
    private TextView requestDataTextView;

    private static final String USERNAME = "im_api_usr";
    private static final String PASSWORD = "6ef9f8fa32eddba5d33a0e4e81b60";
    private static final String AUTHORIZATION_HEADER = "Basic " + Base64.encodeToString((USERNAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);

        resultTextView = findViewById(R.id.resultTextView);
        requestDataTextView = findViewById(R.id.requestDataTextView);

        // Replace "public_key" with your XML file name in res/raw folder
        String publicKeyPath = "public_key";
        String jsonRequestBody = "{ \"outletId\": \"001\", \"tillId\": \"Till-144\", \"tranId\": \"12345\", \"amount\": 10, \"requestType\": \"Mobile\", \"requestValue\": \"23051234567\", \"currency\": \"MUR\", \"txnChannel\": \"eComm\", \"remarks\": \"KioskAPI_DevEnv\", \"expiry\": 5, \"cartDetails\": [ { \"productname\": \"DairyMilk\", \"sku\": \"SKU\", \"numberofitem\": 5, \"typeofgoods\": \"TYPE\", \"category\": \"CAT\", \"unitamount\": 10.00, \"totalamount\": 50.00, \"description\": \"Cadbury\", \"weight\": \"5g\", \"dimension\": \"200X200X200\", \"extData1\": \"\", \"extData2\": \"\", \"extData3\": \"\", \"extData4\": \"\", \"extData5\": \"\" }, { \"productname\": \"Snickers\", \"sku\": \"SKU\", \"numberofitem\": 10, \"typeofgoods\": \"TYPE\", \"category\": \"CAT\", \"unitamount\": 50.00, \"totalamount\": 500.00, \"description\": \"Cadbury\", \"weight\": \"10g\", \"dimension\": \"200X200X200\", \"extData1\": \"\", \"extData2\": \"\", \"extData3\": \"\",\"extData4\": \"\", \"extData5\": \"\" } ] }";

       String apiRequestBody = "{\"header\": {\"apiversion\": \"string\", \"clientID\": \"string\", \"timeStamp\": \"string\", \"hCheckValue\": \"string\", \"requestUUID\": \"string\" }, \"request\": \"string\"}";
       // String apiRequestBody = "{ \"header\": { \"apiversion\": \"v1\", \"clientID\": \"ead65173-250b-4453-a0d0-5fdf8d770eb5\", \"timeStamp\": \"20230719072015205\", \"hCheckValue\": \"205BF6246206DC744906111D9B8A40B4E7AC0F8DFC1A60165F10CAB417C7E506\", \"requestUUID\": \"L7tpJOeh7ezi5pOtRcKB0DKs\" }, \"request\": \"DqEtT3xTvamKtPHO3hCXzt3ZBpDXMmTpzjlD7tQvF9E2tWvQpbjlVifmuFpmggbMDC4X2QnFn4+5W07RTRn9XEdm\\/rOhMgNV2XrDmUuVD2xzVvRyp\\/dV088oDl5soMHk\\/IMhMyiAPUBPfal+U40nC3qWZJUpxgaa2a5iRXLJK4Wr6+251V1QXUrf5\\/Nr9ODosQD8LsXVgDOPOIu9XfiZO0mXw5yz7yVEBtF6UxgDKC5KZUGO7Q0Jv6v+Cj1ifTsSjHVPK0FNjNV5f2zo8jVdgDpE7Z9qwryMakwdxsivcQ7FNGf2+fjQtgopadztNvgjlnyG8sJJJY2Kamm\\/CGc7klqj43SMIdAAY8i8qOw1MjLdsBEcgUpbM1BcRf0abvX+7a7pSwGrpi03LOB5X0o3vJnOy18nnYDRVwGoaGRshEh7CleplTueq\\/SUziGTsaVPy+DqN2tOTkPgCTFNXCsKCWLnrLpLZwohK1+GkSGNPd1z\\/uxaja7QXX\\/8GA+Hw6EJbG1o0yFXxozAmUnvjNvZYajAE7+6QmiTCGz\\/leFLrwldDtD6QckLOka7RBboTad2pACMgPnd31SwKOpioGiz\\/YoQx++noxS4tov50CVUfvt8Ctscl9n7AURL5TSUt9BBsxgO\\/wl9bOfEDVT4E+w6KrwCJaz5kq2xLv6JKIGPoospNgy2dtbFi2IQPne0KEbwlKqJs6X6d66Th8FxG2WJ7d78jCJMyBrmWypXQY2wLMZP0RUvG1Qt19dyBrY21Orqku+POHNXBPzLyrnLz9RlxEGZqCB3GMHvj2g8KoysLd6WehDKrkKb2uscrCY+YRysOHBbSmBliPNfBgT+YB578kq2f6PGcbTz+v4tlnQfeauW6LBzRTyVHVTNdHXKCk3pHivHUaTdaB1g+k5OoRyAJcX7dFo7M1dVZ3WFuHfM95h14FI3CpEiTt8gU1jLxgAia5vWb+wXryAYrf4mNgNA43BRBRB4iQGQURSduB9AKJ3v0AVwmE\\/sCV4ug8dKKwt20QAAVNjmdclNwMPnwybI9IxyBr3Z9EpopTJujxQLJ9B7J0LFaNUpkPdA6Y41EBtp\\/1DQpe82EMWotnlVg3Y09O81OTtdbl0gmVyrX+fe+yXKTwTHapxIZ9uHlv\\/14fLdGRNzlmHC2Idbl7xhYuwkMk7i75MG0ojPafgU+ecQ0880bdz8oI2heUAB71rczeGH2W4FR7XI+GCzzWG8cBqMgaeFJ94jRktdhKv8SYf8yNWe0jgQlmW5y6hyR\\/OV2541\\/Y6IKtW9VEE82kwhUhvrBHV0cxrJsWo366bBfnCDV2uEcjk1uR6blHppmTnyvIz7HzpzSrKrPecUeBZe\\/48XqOfnQGtfkn7asQ041D+4L0eDGXcS\\/eZbGWJi4NkhFd3HOTAGARqrY6TI1e0Js5t4ZH77kSg01\\/NGz07UMpzC\\/urwoVZW4jaegOMwGr9d6UqwZPiDxDZX50skIWpm1dR54vVhEVLz7eaCdzqY0hzWsYV9pNRr59Ty8E00Il6s3Ny+RjWqtewNmDu99RbGMUWEXQK7cJyVRn8294TyTcAaXTaxaVjzNcp9YvtjAIjqDEfrGypqrdh5XmMnC3BskkqIca5iKzvGNsV0TuClK8o0TRs1g5KXWCzz+Sj01TWDqhTPdKdBgiRsRdUolQdI11soXtkaACvymxRf7e\\/m2YFwZNXEYRv2ytZDGFjGON8VjCcLinHtmMHbsfYIRzFwtq3DA4hTufxCnUOY8mIXVPAtdeO+UZmfIyHTiB8zef97c8K50T1rxTRPHK\\/1OapbWDMn6wCdruRX4ZfoZ3tlKSHTQXg=\"}";


        Context context = POP.this;

        String publicKeyContent = loadPublicKey(context, publicKeyPath);
        String clientID = loadClientID(context);
        String reqRefId = generateReqRefId(publicKeyContent);
        String encryptedRequest = createEncryptedRequest(publicKeyContent, clientID, jsonRequestBody);
        String hCheckValue = generateHCheckValue(jsonRequestBody);

        // Read API link from raw file
        String apiLink = readTextFile(context, R.raw.api_addresss);

        // Construct the API request with the obtained values
        String apiRequest = "API Link: " + apiLink + "\n" +
                "Username: " + USERNAME + "\n" +
                "Password: " + PASSWORD + "\n" +
                "ReqRefId: " + reqRefId + "\n" +
                "Encrypted Request: " + encryptedRequest + "\n" +
                "hCheckValue: " + hCheckValue;

        requestDataTextView.setText(apiRequest); // Display the sent data

        // Send the API request using OkHttp
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8"); // Specify the content type and charset
        RequestBody requestBody = RequestBody.create(mediaType, encryptedRequest);

        Request request = new Request.Builder()
                .url(apiLink)
                .post(requestBody)
                .addHeader("Authorization", AUTHORIZATION_HEADER) // Include the authorization header
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle request failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    // Handle successful response
                    Log.d("API Response", responseData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setText(responseData);
                        }
                    });
                } else {
                    // Handle response error
                    final String errorResponse = response.body().string(); // Retrieve the error response body

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("API Response Error", "Response was not successful. Code: " + response.code() + ", Error: " + errorResponse);
                            resultTextView.setText("Error: " + errorResponse); // Display the error message on the TextView
                        }
                    });
                }
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

    public static String loadPublicKey(Context context, String publicKeyPath) {
        try {
            int publicKeyResourceId = context.getResources().getIdentifier(publicKeyPath, "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(publicKeyResourceId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder publicKeyBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                publicKeyBuilder.append(line);
            }

            bufferedReader.close();

            return publicKeyBuilder.toString();
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

    public static String generateReqRefId(String publicKeyContent) {
        try {
            // Generate random KEY and IV
            String key = generateRandomString(32);
            String iv = generateRandomString(16);

            // Concatenate KEY and IV with a pipe separator
            String concatenatedKeyIv = key + "|" + iv;

            // Encrypt the concatenated key and IV using RSA
            String encryptedKeyIv = encryptWithRSA(publicKeyContent, concatenatedKeyIv);


            return encryptedKeyIv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createEncryptedRequest(String publicKeyContent, String clientID, String requestBody) {
        try {
            // Generate random KEY and IV
            String key = generateRandomString(32);
            String iv = generateRandomString(16);
            String requestUUID = UUID.randomUUID().toString();

            // Encrypt the request body using AES 256 Algorithm (CBC Mode)
            String encryptedRequest = encryptWithAES(key, iv, requestBody);

            // Create the JSON object containing the header and encrypted request
            String jsonRequest = "{\"header\":{\"apiversion\":\"v1\",\"clientID\":\"" + clientID + "\",\"timeStamp\":\"" + getCurrentTimestamp() + "\",\"hCheckValue\":\"" + generateHCheckValue(requestBody) + "\",\"requestUUID\":\"" + requestUUID + "\"},\"request\":\"" + encryptedRequest + "\"}";

            // Remove line breaks and whitespace characters
            jsonRequest = jsonRequest.replace("\n", "").replace("\r", "").replace("\t", "").replaceAll("\\s+", " ");

            return jsonRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String generateHCheckValue(String requestBody) {
        try {
            // Create SHA256 hash of the plain request body
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(requestBody.getBytes(StandardCharsets.UTF_8));

            // Convert hash bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
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

    public static String encryptWithRSA(String publicKeyContent, String plaintext) {
        try {
            // Parse XML public key content
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(publicKeyContent.getBytes(StandardCharsets.UTF_16));
            Document document = documentBuilder.parse(inputStream);
            Element root = document.getDocumentElement();
            String modulusString = root.getElementsByTagName("Modulus").item(0).getTextContent();
            String exponentString = root.getElementsByTagName("Exponent").item(0).getTextContent();

            // Convert modulus and exponent from Base64 to byte arrays
            byte[] modulusBytes = Base64.decode(modulusString, Base64.DEFAULT);
            byte[] exponentBytes = Base64.decode(exponentString, Base64.DEFAULT);

            // Create RSA public key spec
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1, modulusBytes), new BigInteger(1, exponentBytes));

            // Generate RSA public key
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Encrypt the plaintext using RSA
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }
}
