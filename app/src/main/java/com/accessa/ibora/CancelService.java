package com.accessa.ibora;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.accessa.ibora.POP.CancelPaymentPOPDialogFragment;
import com.accessa.ibora.printer.printerSetup;

public class CancelService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the popreqid from the intent's extras
        String popreqid = intent.getStringExtra("popreqid");

        // Do whatever you need to do with the popreqid in your CancelService
        // For example, you can perform a cancellation operation here.

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
