package com.accessa.ibora;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.accessa.ibora.printer.printerSetup;

public class PrintService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Retrieve the parameters from the intent extras
        String Pop = intent.getStringExtra("POP");
        // Your code to launch the printersetup activity goes here
        Intent printerIntent = new Intent(this, printerSetup.class);
// Pass the parameters as extras to the printerSetup activity
        printerIntent.putExtra("POP", Pop);

        printerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(printerIntent);

        // Return START_STICKY to indicate that the service should be restarted if it's terminated by the system.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}