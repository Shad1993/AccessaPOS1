package com.accessa.ibora.printer.externalprinterlibrary2;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.sunmi.externalprinterlibrary2.SearchCallback;
import com.sunmi.externalprinterlibrary2.SearchMethod;
import com.sunmi.externalprinterlibrary2.SunmiPrinterManager;
import com.sunmi.externalprinterlibrary2.exceptions.SearchException;
import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;

public class PrinterSelectActivity extends AppCompatActivity implements PrinterListAdapter.OnItemClickListener, SearchCallback {

    private PrinterListAdapter adapter;
    private ActivityResultLauncher<String[]> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_select);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.search_print_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = findViewById(R.id.set_printer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrinterListAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        launcher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean isGranted;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                isGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION)
                        && result.get(Manifest.permission.BLUETOOTH_SCAN)
                        && result.get(Manifest.permission.BLUETOOTH_CONNECT);
            } else {
                isGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if(isGranted) {
                search();
            } else {
                Toast.makeText(PrinterSelectActivity.this, R.string.toast_bt_off, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSearch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSearch();
    }

    private void initSearch() {
        System.out.println("init search");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                launcher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT});
            } else {
                launcher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            }
        } else {
            search();
        }
    }

    private void search() {
        System.out.println("search");
        BluetoothManager bm = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        if(bm.getAdapter().isEnabled()) {
            try {
                SunmiPrinterManager.getInstance().searchCloudPrinter(this, SearchMethod.BT, this);
            } catch (SearchException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(PrinterSelectActivity.this, R.string.toast_bt_switch, Toast.LENGTH_LONG).show();
        }
    }

    private void stopSearch() {
        try {
            SunmiPrinterManager.getInstance().stopSearch(this, SearchMethod.BT);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(CloudPrinter cloudPrinter) {
        MyApplication.getInstance().addCloudPrinter(cloudPrinter);
        Intent intent = new Intent(this, WifiSelectActivity.class);
        intent.putExtra("name", cloudPrinter.getCloudPrinterInfo().name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFound(CloudPrinter cloudPrinter) {
        runOnUiThread(() -> adapter.addData(cloudPrinter));
    }
}