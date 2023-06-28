package com.accessa.ibora.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothPrinterActivity extends AppCompatActivity {

    private static final String MAC_ADDRESS = "0C:25:76:59:08:B2";
    private static final UUID UUID_SERIAL_PORT_PROFILE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_printer);

        Button printButton = findViewById(R.id.btnPrint);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printText("Hello, World!");
            }
        });
    }

    private void printText(String text) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice(MAC_ADDRESS);
        BluetoothSocket socket = null;

        try {
            socket = printerDevice.createRfcommSocketToServiceRecord(UUID_SERIAL_PORT_PROFILE);
            socket.connect();

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(text.getBytes());

            Toast.makeText(this, "Printing successful", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PrinterActivity", "Printing failed: " + e.getMessage());
            Toast.makeText(this, "Printing failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
