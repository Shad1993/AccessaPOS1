package com.accessa.ibora.printer;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;


public class printerSetup extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(printerSetup.this, "Printer connected", Toast.LENGTH_SHORT).show();
                    try {
                        service.printText("Cashor name: Reshad\n", null);
                        service.printText("Cashor lv: 11\n", null);
                        service.printText("Product name: Desperado\n", null);
                        service.printText("Price : Rs 150\n", null);

                        // Cut the paper
                        service.cutPaper(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }


        @Override
        public void onDisconnected() {
            // Printer service is disconnected, you cannot print
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(printerSetup.this, "Printer disconnected", Toast.LENGTH_SHORT).show();


                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Initialize the printer service
        boolean result = false;
        try {
            result = InnerPrinterManager.getInstance().bindService(this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            throw new RuntimeException(e);
        }

        if (result) {
            Toast.makeText(this, "Printer service initialization successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Printer service initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the printer service
        try {
            InnerPrinterManager.getInstance().unBindService(this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            throw new RuntimeException(e);
        }
    }
}