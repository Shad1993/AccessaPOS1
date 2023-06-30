package com.accessa.ibora.printer.cloudPrinter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sunmi.externalprinterlibrary2.ResultCallback;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.externalprinterlibrary2.CloudPrinterActivity;
import com.accessa.ibora.printer.externalprinterlibrary2.MyApplication;
import com.accessa.ibora.printer.externalprinterlibrary2.SearchActivity;
import com.sunmi.externalprinterlibrary2.ConnectCallback;
import com.sunmi.externalprinterlibrary2.ResultCallback;
import com.sunmi.externalprinterlibrary2.SearchMethod;
import com.sunmi.externalprinterlibrary2.SunmiPrinterManager;
import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;
import com.sunmi.externalprinterlibrary2.style.AlignStyle;
import com.sunmi.externalprinterlibrary2.style.BarcodeType;
import com.sunmi.externalprinterlibrary2.style.CloudPrinterStatus;
import com.sunmi.externalprinterlibrary2.style.ErrorLevel;
import com.sunmi.externalprinterlibrary2.style.HriStyle;

public class bluetoothPrinter extends AppCompatActivity implements ResultCallback {

    private ActivityResultLauncher<Integer> activityResultLauncher;

    private TextView tv, encoding;
    private EditText et;

    private CloudPrinter cloudPrinter;
    private Dialog mDialog;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_printer);
        tv = findViewById(R.id.showPrinter);
        et = findViewById(R.id.printContents);
        et.setText(SunmiPrinterManager.getInstance().getVersionInfo());
        encoding = findViewById(R.id.showEncoding);

        activityResultLauncher = registerForActivityResult(new ActivityResultContract<Integer, CloudPrinter>() {

            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Integer method) {
                Intent intent = new Intent(bluetoothPrinter.this, SearchActivity.class);
                intent.putExtra("method", method);
                return intent;
            }
            @Override
            public CloudPrinter parseResult(int i, @Nullable Intent intent) {
                if(intent == null) {
                    return cloudPrinter;
                }
                String name = intent.getStringExtra("name");
                if(i == Activity.RESULT_OK && !TextUtils.isEmpty(name)) {
                    return MyApplication.getInstance().getCloudPrinter(name);
                }
                return null;
            }


        }, result -> {
            if(result != null) {
                tv.setText(result.getCloudPrinterInfo().toString());
                cloudPrinter = result;
                connect(view);
            }
        });

        findViewById(R.id.addBt).setOnClickListener(view -> activityResultLauncher.launch(SearchMethod.BT));


    }
    public void connect(View view) {
        if(cloudPrinter != null) {
            cloudPrinter.printText("before Success ****");
            cloudPrinter.connect(this, new ConnectCallback() {

                @Override
                public void onConnect() {

                        runOnUiThread(() -> {
                            Toast.makeText(bluetoothPrinter.this, R.string.toast_connect, Toast.LENGTH_LONG).show();
                            cloudPrinter.printText("Success ****");
                            printSample(view); // Call printSample() method here
                        });

                }


                @Override
                public void onFailed(String s) {

                    runOnUiThread(() -> {

                        Toast.makeText(bluetoothPrinter.this, s, Toast.LENGTH_LONG).show();

                    });

                }

                @Override
                public void onDisConnect() {
                    cloudPrinter.printText("On disconnect ****");
                    printSample(view); // Call printSample() method here
                }
            });

        }else {
            // Call printSample() method directly if cloudPrinter is already set
            printSample(view);
        }
    }
    public void disconnect(View view) {
        if(cloudPrinter != null) {
            cloudPrinter.release(this);
        }
    }
    public void printSample(View view) {
        if(checkConnect()) {
            cloudPrinter.setAlignment(AlignStyle.CENTER);
            cloudPrinter.appendText("****");
            cloudPrinter.setBoldMode(true);
            cloudPrinter.setCharacterSize(2, 2);
            cloudPrinter.appendText("#1");
            cloudPrinter.setBoldMode(false);
            cloudPrinter.setCharacterSize(1, 1);
            cloudPrinter.printText("Take out list****");
            cloudPrinter.setAlignment(AlignStyle.CENTER);
            cloudPrinter.printText("批萨Pizza");
            cloudPrinter.dotsFeed(20);
            cloudPrinter.setCharacterSize(1, 2);
            cloudPrinter.printText("--已支付--");
            cloudPrinter.printText("预计19：00送达");
            cloudPrinter.initStyle();
            cloudPrinter.dotsFeed(20);
            cloudPrinter.printText("【下单时间】2021-8-1 12:00");
            cloudPrinter.printText("--------------------------------");
            cloudPrinter.printColumnsText(new String[]{"菜名", "数量", "小计"}, new int[]{6,10,8},
                    new AlignStyle[]{AlignStyle.LEFT, AlignStyle.LEFT, AlignStyle.RIGHT});
            cloudPrinter.printColumnsText(new String[]{"测试1", "11", "111"}, new int[]{6,10,8},
                    new AlignStyle[]{AlignStyle.LEFT, AlignStyle.LEFT, AlignStyle.RIGHT});
            cloudPrinter.printColumnsText(new String[]{"测试2", "22", "222"}, new int[]{6,10,8},
                    new AlignStyle[]{AlignStyle.LEFT, AlignStyle.LEFT, AlignStyle.RIGHT});
            cloudPrinter.printColumnsText(new String[]{"测试3", "33", "333"}, new int[]{6,10,8},
                    new AlignStyle[]{AlignStyle.LEFT, AlignStyle.LEFT, AlignStyle.RIGHT});
            cloudPrinter.printText("--------------------------------");
            cloudPrinter.printQrcode("1234567890", 9, ErrorLevel.L);
            cloudPrinter.printText("--------------------------------");
            cloudPrinter.printBarcode("1234567890", BarcodeType.CODE128, 200, 3, HriStyle.BELOW);
            cloudPrinter.lineFeed(3);
            cloudPrinter.postCutPaper(true, 0);
            cloudPrinter.commitTransBuffer(this);

        } else {
        connect(view);}
    }
    private boolean checkConnect() {
        if (cloudPrinter == null) {
            runOnUiThread(() -> {
                Toast.makeText(this, R.string.toast_start_add, Toast.LENGTH_SHORT).show();

            });
            return false;
        }
        if (!cloudPrinter.isConnected()) {
            runOnUiThread(() -> {
                Toast.makeText(this, R.string.toast_start_connect, Toast.LENGTH_SHORT).show();

            });
            return false;
        }
        return true;
    }


    @Override
    public void onComplete() {
        runOnUiThread(() -> {
            Toast.makeText(bluetoothPrinter.this, R.string.toast_print_ok, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onFailed(CloudPrinterStatus cloudPrinterStatus) {
        runOnUiThread(() -> {
            Toast.makeText(bluetoothPrinter.this, String.format(getString(R.string.toast_print_failed), cloudPrinterStatus.name()), Toast.LENGTH_LONG).show();
            cloudPrinter.printText("--------------------------------");
        });
    }

}