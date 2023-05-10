package com.accessa.ibora.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.accessa.ibora.MainActivity;

public class ScannerReceiver extends BroadcastReceiver {
    private static final String ACTION_SCANNER_RECEIVED = "com.sunmi.scanner.ACTION_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SCANNER_RECEIVED.equals(action)) {
            String data = intent.getStringExtra("data");
            // Do something with the scanned data
            //Toast.makeText(context.getApplicationContext(), data, Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context.getApplicationContext(), MainActivity.class);
            intent1.putExtra("EXTRA_MESSAGE", data);
            startActivity(intent1);
        }
    }

    private void startActivity(Intent context) {
    }
}
