package com.accessa.ibora.POP;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import com.accessa.ibora.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSAEncryptionActivity extends Activity {

    private TextView encryptedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_encryption);

        encryptedTextView = findViewById(R.id.encryptedTextView);

        // Load the public key from the XML file
        String publicKeyContent = loadPublicKeyFromXML(this, R.raw.public_key);

        // String to encrypt
        String plainText = "U1BfQduprlNKx9BKSIVhTUroqMsj39VU|xrMpHU2ZfjQB6lp5";

        // Encrypt the string with the public key
        String encryptedText = encryptWithRSA(publicKeyContent, plainText);

        // Display the encrypted text
        encryptedTextView.setText(encryptedText);
    }

    public static String loadPublicKeyFromXML(Context context, int publicKeyResourceId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(publicKeyResourceId);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element root = document.getDocumentElement();
            String modulusString = root.getElementsByTagName("Modulus").item(0).getTextContent();
            String exponentString = root.getElementsByTagName("Exponent").item(0).getTextContent();
            byte[] modulusBytes = Base64.decode(modulusString, Base64.DEFAULT);
            byte[] exponentBytes = Base64.decode(exponentString, Base64.DEFAULT);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1, modulusBytes), new BigInteger(1, exponentBytes));
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            return Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptWithRSA(String publicKeyContent, String plaintext) {
        try {
            byte[] publicKeyBytes = Base64.decode(publicKeyContent, Base64.DEFAULT);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

