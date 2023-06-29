package com.accessa.ibora.printer.externalprinterlibrary2;

import android.Manifest;
import android.app.Activity;
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

public class SearchActivity extends AppCompatActivity implements PrinterListAdapter.OnItemClickListener, SearchCallback {

    private PrinterListAdapter adapter;

    private int method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.search_print);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = findViewById(R.id.search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrinterListAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        initData();
        initSearch();
    }

    private void initData() {
        method = getIntent().getIntExtra("method", SearchMethod.USB);
        System.out.println("method =" + method);
    }

    private void initSearch() {
        if(method == SearchMethod.BT) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityResultLauncher<String[]> launcher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    boolean isGranted;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        isGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION)
                                && result.get(Manifest.permission.BLUETOOTH_SCAN)
                                && result.get(Manifest.permission.BLUETOOTH_CONNECT);
                    } else {
                        isGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    if(isGranted) {
                        BluetoothManager bm = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
                        if(bm.getAdapter().isEnabled()) {
                            search();
                        } else {
                            Toast.makeText(SearchActivity.this, R.string.toast_bt_switch, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, R.string.toast_bt_off, Toast.LENGTH_LONG).show();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    launcher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT});
                } else {
                    launcher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                }

                return;
            }
        }
        search();
    }

    private void search() {
        try {
            SunmiPrinterManager.getInstance().searchCloudPrinter(this, method, this);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SunmiPrinterManager.getInstance().stopSearch(this, method);
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
        Intent intent = new Intent();
        intent.putExtra("name", cloudPrinter.getCloudPrinterInfo().name);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFound(CloudPrinter cloudPrinter) {
        System.out.println("onFound " + cloudPrinter.getCloudPrinterInfo().toString());
        runOnUiThread(() -> adapter.addData(cloudPrinter));
    }
}