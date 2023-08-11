package com.accessa.ibora;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.accessa.ibora.POP.ValidatePOPDialogFragment;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;

public class proceedPOPService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get data from the intent
        String transactionIdInProgress = intent.getStringExtra("transaction_id");
        String amount = intent.getStringExtra("amount");
        String popFraction = intent.getStringExtra("popFraction");

        // ... perform necessary tasks ...

        // Perform your actions

        // Send a broadcast with the popreqid
        Intent broadcastIntent = new Intent("com.accessa.ibora.validate_ticket");
        broadcastIntent.putExtra("transactionIdInProgress", transactionIdInProgress);
        broadcastIntent.putExtra("amount", amount);
        broadcastIntent.putExtra("popFraction", popFraction);
        sendBroadcast(broadcastIntent);

        // Notify activity/fragment about completion
        Intent completionIntent = new Intent("proceedPOPServiceCompleted");
        LocalBroadcastManager.getInstance(this).sendBroadcast(completionIntent);

        stopSelf(); // Call this when your work is done
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
