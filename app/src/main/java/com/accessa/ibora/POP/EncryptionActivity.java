package com.accessa.ibora.POP;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import com.accessa.ibora.R;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionActivity extends Activity {

    private TextView encryptedRequestTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        encryptedRequestTextView = findViewById(R.id.encryptedRequestTextView);

        // Request body JSON
        String requestBody = "{ \"outletId\": \"001\", \"tillId\": \"Till-178\", \"tranId\": \"12345\", \"amount\": 10, \"requestType\": \"Mobile\", \"requestValue\": \"23051234567\", \"currency\": \"MUR\", \"txnChannel\": \"eComm\", \"remarks\": \"KioskAPI_DevEnv\", \"expiry\": 5, \"cartDetails\": [ { \"productname\": \"DairyMilk\", \"sku\": \"SKU\", \"numberofitem\": 5, \"typeofgoods\": \"TYPE\", \"category\": \"CAT\", \"unitamount\": 10.00, \"totalamount\": 50.00, \"description\": \"Cadbury\",\"weight\": \"5g\", \"dimension\": \"200X200X200\", \"extData1\": \"\", \"extData2\": \"\", \"extData3\": \"\", \"extData4\": \"\", \"extData5\": \"\" }, { \"productname\": \"Snickers\", \"sku\": \"SKU\", \"numberofitem\": 10, \"typeofgoods\": \"TYPE\", \"category\": \"CAT\", \"unitamount\": 50.00, \"totalamount\": 500.00, \"description\": \"Cadbury\", \"weight\": \"10g\", \"dimension\": \"200X200X200\", \"extData1\": \"\", \"extData2\": \"\", \"extData3\": \"\", \"extData4\": \"\", \"extData5\": \"\" } ] }";

        // AES 256 Algorithm parameters
        String key = "U1BfQduprlNKx9BKSIVhTUroqMsj39VU";
        String iv = "xrMpHU2ZfjQB6lp5";

        // Encrypt the request using AES 256 Algorithm (CBC Mode)
        String encryptedRequest = encryptWithAES(key, iv, requestBody);

        // Display the encrypted request
        encryptedRequestTextView.setText(encryptedRequest);
    }

    private String encryptWithAES(String key, String iv, String plaintext) {
        try {
            // Convert key and IV to byte arrays
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] ivBytes = iv.getBytes("UTF-8");

            // Create AES cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encrypt the plaintext using AES
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));

            // Convert encrypted bytes to Base64 string
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
