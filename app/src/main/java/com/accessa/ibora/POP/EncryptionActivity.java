package com.accessa.ibora.POP;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.accessa.ibora.R;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import android.util.Base64;
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
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class EncryptionActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        // Read RSA public key from raw resource and convert to PEM format
        String pemKey = convertXMLToPEM(R.raw.public_key);

        // Print the PEM format key to the log
        Log.d("PEM Key", pemKey);

        // The concatenated key and IV you want to encrypt
        String keyAndIVConcatenation = "6ZAinCb4V3pz64axMpT0nsQ3j1q6Ea6k|ecj1VnEWoH0ovZeG";

        // Encrypt the concatenated key and IV using the PEM public key

        PublicKey publicKey = getPublicKeyFromPEM(pemKey);
        byte[] encryptedData = encryptWithPublicKey(keyAndIVConcatenation, publicKey);


        // Display encrypted data in log
        if (encryptedData != null) {
            String encryptedBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT);
            Log.d("Encrypted Data", encryptedBase64);
        } else {
            Log.e("Encryption Error", "Failed to encrypt data.");
        }
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







}
