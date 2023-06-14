package com.accessa.ibora.scanner;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.TextureView;
        import android.widget.Toast;

        import androidx.activity.result.ActivityResult;
        import androidx.activity.result.ActivityResultCallback;
        import androidx.activity.result.ActivityResultLauncher;
        import androidx.activity.result.contract.ActivityResultContracts;
        import androidx.appcompat.app.AppCompatActivity;

        import com.accessa.ibora.R;

        import java.util.ArrayList;
        import java.util.HashMap;

public class inbuildScannerSunmiT2Mini extends AppCompatActivity {

    private ActivityResultLauncher<Intent> scanLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ActivityResultLauncher
        scanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            ArrayList<HashMap<String, String>> scanResult = (ArrayList<HashMap<String, String>>) bundle.getSerializable("data");
                            if (scanResult != null) {
                                for (HashMap<String, String> hashMap : scanResult) {

                                    String scanType = hashMap.get("TYPE");
                                    String scanValue = hashMap.get("VALUE");

                                    Log.i("sunmi", hashMap.get("TYPE")); // Scan type
                                    Log.i("sunmi", hashMap.get("VALUE")); // Scan result
                                    Toast.makeText(inbuildScannerSunmiT2Mini.this, "Scanned Barcode: " + scanValue, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }
                });

        // Start scanning
        startScanning();
    }

    private void startScanning() {
        Intent intent = new Intent("com.summi.scan");
        intent.setClassName("com.sunmi.sunmiqrcodescanner",
                "com.sunmi.sunmiqrcodescanner.activity.ScanActivity");
        intent.putExtra("CURRENT_PPI", 0X0003);// The current resolution
        //The best for M1 & V1 is 800*480.
        //PPI_1920_1080 = 0X0001;
        //PPI_1280_720 = 0X0002;
        //PPI_BEST = 0X0003;
        // Add optional configurations to the intent
        intent.putExtra("PLAY_SOUND", true);
        intent.putExtra("PLAY_VIBRATE", false);
        intent.putExtra("IDENTIFY_MORE_CODE", false);// Identify multiple QR codes in the screen, the default is false
        intent.putExtra("IS_SHOW_SETTING", true);// Whether to display the setting button in the upper right corner, the default is true
        intent.putExtra("IS_SHOW_ALBUM", true);// Whether to display the button to select a picture from the album, the default is true
        intent.putExtra("IDENTIFY_INVERSE", true);// Allow to read inverse QR code, default: true
        intent.putExtra("IS_EAN_8_ENABLE", true);//Allow to read EAN-8 code, default: true
        intent.putExtra("IS_UPC_E_ENABLE", true);//Allow to read UPC-E code, default: true
        intent.putExtra("IS_ISBN_10_ENABLE", true);//Allow to read ISBN-10 (from EAN-13) code, default: true
        intent.putExtra("IS_CODE_11_ENABLE", true);//Allow to read CODE-11 code, default: false
        intent.putExtra("IS_UPC_A_ENABLE", true);//Allow to read UPC-A code, default: true
        intent.putExtra("IS_EAN_13_ENABLE", true);//Allow to read AN-13 code, default: true
        intent.putExtra("IS_ISBN_13_ENABLE", true);//Allow to read ISBN-13 (from EAN-13) code, default: true
        intent.putExtra("IS_INTERLEAVED_2_OF_5_ENABLE", true);//Allow to read Interleaved 2 of 5 codes, default: false
        intent.putExtra("IS_CODE_128_ENABLE", true);//Allow to read ECode 128 code, default true
        intent.putExtra("IS_CODABAR_ENABLE", true);//Allow to read Codabar code, default: true
        intent.putExtra("IS_CODE_39_ENABLE", true);//Allow to read Code 39 code, default: true
        intent.putExtra("IS_CODE_93_ENABLE", true);//Allow to read Code 93 code, default: true
        intent.putExtra("IS_DATABAR_ENABLE", true);//Allow to read DataBar (RSS-14) code, default: true
        intent.putExtra("IS_DATABAR_EXP_ENABLE", true);//Allow to read DataBar Expanded code, default: true
        intent.putExtra("IS_Micro_PDF417_ENABLE", true);//Allow to read Micro PDF417 code, default: false
        intent.putExtra("IS_MicroQR_ENABLE", true);//Allow to read Micro QR Code code, default: true
        intent.putExtra("IS_OPEN_LIGHT", true);// Whether to display the flash, the default: true
        intent.putExtra("SCAN_MODE", false);// Whether it is a loop mode, the default: false
        intent.putExtra("IS_QR_CODE_ENABLE", true);// Allow to read QR code, default: true
        intent.putExtra("IS_PDF417_ENABLE", true);// Allow to read PDF417 code, default: true
        intent.putExtra("IS_DATA_MATRIX_ENABLE", true);// Allow to read DataMatrix code, default: true
        intent.putExtra("IS_AZTEC_ENABLE", true);// Allow to read AZTEC code, default: true
        scanLauncher.launch(intent);
    }
}