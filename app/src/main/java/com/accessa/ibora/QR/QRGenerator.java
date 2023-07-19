package com.accessa.ibora.QR;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class QRGenerator {

    private static final String MERCHANT_PHONE_NUMBER = "23057862807";
    private static final String MERCHANT_TILL_NUMBER = "143";
    private static final String MERCHANT_NAME = "IntermartOne";
    private static final String GREEN_TEXT = "North I622202112305786280707031436304BE4F";
    private static final String ADDITIONAL_INFO_TAG = "62";

    public static String generateQRString(String amount, String billNumber, String loyaltyNumber) {
        StringBuilder qrStringBuilder = new StringBuilder();

        // Append the fixed part of the QR string
        qrStringBuilder.append("00020101021126630009mu.");
        qrStringBuilder.append("maucas0112BKONMUM0XXXX02110301106595803150000000000000525204727853034805");
        qrStringBuilder.append("802MU5912");
        qrStringBuilder.append(MERCHANT_NAME);
        qrStringBuilder.append("6015Agalega");

        // Append the merchant phone number
        qrStringBuilder.append(MERCHANT_PHONE_NUMBER);

        // Append the merchant till number
        qrStringBuilder.append(MERCHANT_TILL_NUMBER);

        // Append the amount
        String amountTag = String.format(Locale.ENGLISH, "5310%s", amount);
        qrStringBuilder.append(amountTag);

        // Append the bill number if available
        if (billNumber != null && !billNumber.isEmpty()) {
            qrStringBuilder.append("01");
            qrStringBuilder.append(String.format(Locale.ENGLISH, "%02d%s", billNumber.length(), billNumber));
        }

        // Append the loyalty number if available
        if (loyaltyNumber != null && !loyaltyNumber.isEmpty()) {
            qrStringBuilder.append("04");
            qrStringBuilder.append(String.format(Locale.ENGLISH, "%02d%s", loyaltyNumber.length(), loyaltyNumber));
        }

        // Append the green text
        qrStringBuilder.append(GREEN_TEXT);

        // Generate and append the CRC
        String crc = calculateCRC(qrStringBuilder.toString());
        qrStringBuilder.append("6304");
        qrStringBuilder.append(crc);

        // Append additional info tag
        qrStringBuilder.append(ADDITIONAL_INFO_TAG);

        return qrStringBuilder.toString();
    }


    private static String calculateCRC(String data) {
        try {
            byte[] bytes = data.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] crcBytes = md.digest(bytes);
            return bytesToHex(crcBytes).substring(0, 4);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
