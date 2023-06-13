package com.accessa.ibora.scanner;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;

public class BarcodeReader extends AppCompatActivity {

    private static final int START_SCAN = 0x0001;
    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);

        textureView = findViewById(R.id.textureView);

        startScanning();
    }

    private void startScanning() {
        Intent intent = new Intent("com.summi.scan");
        intent.setClassName("com.sunmi.sunmiqrcodescanner",
                "com.sunmi.sunmiqrcodescanner.activity.ScanActivity");

        intent.putExtra("PLAY_SOUND", true);
        intent.putExtra("PLAY_VIBRATE", false);
        intent.putExtra("IDENTIFY_MORE_CODE", false);
        intent.putExtra("IS_SHOW_SETTING", true);
        intent.putExtra("IS_SHOW_ALBUM", true);
        intent.putExtra("IDENTIFY_INVERSE", true);
        intent.putExtra("IS_OPEN_LIGHT", true);
        intent.putExtra("IS_QR_CODE_ENABLE", true);
        intent.putExtra("IS_PDF417_ENABLE", true);
        intent.putExtra("IS_DATA_MATRIX_ENABLE", true);
        intent.putExtra("IS_AZTEC_ENABLE", true);

        startActivityForResult(intent, START_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_SCAN && resultCode == RESULT_OK) {
            String scannedCode = data.getStringExtra("SCAN_RESULT");
            Toast.makeText(this, "Scanned Code: " + scannedCode, Toast.LENGTH_SHORT).show();
        }
    }
}
