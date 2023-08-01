package com.accessa.ibora;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.accessa.ibora.POP.CancelPaymentPOPDialogFragment;
import com.accessa.ibora.printer.printerSetup;

public class CancelService extends Service {
    // In the CancelService


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the popreqid from the intent's extras
        String popreqid = intent.getStringExtra("popreqid");
        String key = intent.getStringExtra("key");
        String IV = intent.getStringExtra("IV");
        // Send a broadcast with the popreqid
        Intent broadcastIntent = new Intent("com.accessa.ibora.CANCEL_ACTION");
        broadcastIntent.putExtra("popreqid", popreqid);
        broadcastIntent.putExtra("key", key);
        broadcastIntent.putExtra("IV", IV);
        sendBroadcast(broadcastIntent);

        // Send a broadcast to notify the fragment to dismiss the dialog
        Intent broadcastIntents = new Intent("com.accessa.ibora.DISMISS_DIALOG");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntents);


        // Stop the service after the work is done
        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
