package com.accessa.ibora.POP;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import com.accessa.ibora.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.UUID;
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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class POP extends Activity {

    private TextView resultTextView;
private  String requestUUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);

        resultTextView = findViewById(R.id.resultTextView);

        // Replace "public_key" with your XML file name in res/raw folder
        String publicKeyPath = "public_key";
        String apiRequestBody  = "{\"header\": {\"apiversion\": \"string\", \"clientID\": \"string\", \"timeStamp\": \"string\", \"hCheckValue\": \"string\", \"requestUUID \": \"string\" }, \"request\": \"string\"}";

        Context context = POP.this;

        String publicKeyContent = loadPublicKey(context, publicKeyPath);
        String clientID = loadClientID(context);
        String reqRefId = generateReqRefId(publicKeyContent);
        String encryptedRequest = createEncryptedRequest(publicKeyContent, clientID, apiRequestBody);
        String hCheckValue = generateHCheckValue(apiRequestBody);

        // Read API link, username, and password from raw files
        String apiLink = readTextFile(context, R.raw.api_addresss);
        String username = readTextFile(context, R.raw.api_user);
        String password = readTextFile(context, R.raw.password);

        // Construct the API request with the obtained values
        String apiRequest = "API Link: " + apiLink + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n" +
                "ReqRefId: " + reqRefId + "\n" +
                "Encrypted Request: " + encryptedRequest + "\n" +
                "hCheckValue: " + hCheckValue;

        // resultTextView.setText(apiRequest);

        // Send the API request using OkHttp
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, apiRequest);

        Request request = new Request.Builder()
                .url(apiLink)
                .post(requestBody)
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
