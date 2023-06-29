package com.accessa.ibora.printer.externalprinterlibrary2;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sunmi.externalprinterlibrary2.ResultCallback;
import com.accessa.ibora.R;
import com.sunmi.externalprinterlibrary2.BuildConfig;
import com.sunmi.externalprinterlibrary2.ConnectCallback;
import com.sunmi.externalprinterlibrary2.SearchMethod;
import com.sunmi.externalprinterlibrary2.SunmiPrinterManager;
import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;
import com.sunmi.externalprinterlibrary2.style.AlignStyle;
import com.sunmi.externalprinterlibrary2.style.BarcodeType;
import com.sunmi.externalprinterlibrary2.style.CloudPrinterStatus;
import com.sunmi.externalprinterlibrary2.style.EncodeType;
import com.sunmi.externalprinterlibrary2.style.ErrorLevel;
import com.sunmi.externalprinterlibrary2.style.HriStyle;
import com.sunmi.externalprinterlibrary2.style.ImageAlgorithm;

import java.util.ArrayList;
import java.util.List;


public class CloudPrinterActivity extends AppCompatActivity implements ResultCallback {

    private ActivityResultLauncher<Integer> activityResultLauncher;

    private TextView tv, encoding;
    private EditText et;

    private CloudPrinter cloudPrinter;
    private Dialog mDialog;


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
                Intent intent = new Intent(CloudPrinterActivity.this, SearchActivity.class);
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
            }
        });
        findViewById(R.id.addBt).setOnClickListener(view -> activityResultLauncher.launch(SearchMethod.BT));
        findViewById(R.id.addIp).setOnClickListener(view -> activityResultLauncher.launch(SearchMethod.LAN));
        findViewById(R.id.addUsb).setOnClickListener(view -> activityResultLauncher.launch(SearchMethod.USB));
        findViewById(R.id.setWifi).setOnClickListener(view -> {
            Intent intent;
            if(BuildConfig.DEBUG) {
                //调试模式使用命令配网
                intent = new Intent(CloudPrinterActivity.this, PrinterSelectActivity.class);
            } else {
                intent = new Intent(CloudPrinterActivity.this, WifiConfigActivity.class);
            }
            startActivity(intent);
        });
        findViewById(R.id.setEncoding).setOnClickListener(view -> showSelectDialog());
    }



    public void connect(View view) {
        if(cloudPrinter != null) {
            cloudPrinter.connect(this, new ConnectCallback() {
                @Override
                public void onConnect() {
                    runOnUiThread(() -> {
                        Toast.makeText(CloudPrinterActivity.this, R.string.toast_connect, Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onFailed(String s) {
                    runOnUiThread(() -> {
                        Toast.makeText(CloudPrinterActivity.this, s, Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onDisConnect() {

                }
            });
        }
    }

    public void disconnect(View view) {
        if(cloudPrinter != null) {
            cloudPrinter.release(this);
        }
    }

    public void print(View view) {
        if(checkConnect()) {
            String text = et.getText().toString();
            if(TextUtils.isEmpty(text)) {
                cloudPrinter.printText("test printing contents....");
            } else {
                cloudPrinter.printText(text);
            }
            cloudPrinter.commitTransBuffer(this);
        }
    }

    public void printImage(View view) {
        if(checkConnect()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_item_printer);
            cloudPrinter.printImage(bitmap, ImageAlgorithm.DITHERING);
            cloudPrinter.commitTransBuffer(this);
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
        }
    }

    public void openCash(View view) {
        if(checkConnect()) {
            cloudPrinter.openCashBox();
        }
    }

    public void getSn(View view) {
        if(checkConnect()) {
            cloudPrinter.getDeviceSN(s -> runOnUiThread(() -> Toast.makeText(CloudPrinterActivity.this, s, Toast.LENGTH_LONG).show()));
        }
    }


    public void getState(View view) {
        if(checkConnect()) {
            cloudPrinter.getDeviceState(s -> runOnUiThread(() -> Toast.makeText(CloudPrinterActivity.this, s.name(), Toast.LENGTH_LONG).show()));
        }
    }

    @Override
    public void onComplete() {
        runOnUiThread(() -> {
            Toast.makeText(CloudPrinterActivity.this, R.string.toast_print_ok, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onFailed(CloudPrinterStatus cloudPrinterStatus) {
        runOnUiThread(() -> {
            Toast.makeText(CloudPrinterActivity.this, String.format(getString(R.string.toast_print_failed), cloudPrinterStatus.name()), Toast.LENGTH_LONG).show();
        });
    }

    private void showSelectDialog() {
        if(mDialog == null) {
            mDialog = new Dialog(this, R.style.Son_dialog);
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_select, null);
            ListView listView = view.findViewById(R.id.select_list);
            List<String> list = new ArrayList<>();
            list.add(EncodeType.ASCII.name());
            list.add(EncodeType.GB18030.name());
            list.add(EncodeType.BIG5.name());
            list.add(EncodeType.SHIFT_JIS.name());
            list.add(EncodeType.JIS_0208.name());
            list.add(EncodeType.KSC_5601.name());
            list.add(EncodeType.UTF_8.name());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                if(checkConnect()) {
                    cloudPrinter.setEncodeMode(EncodeType.valueOf(list.get(i)));
                    encoding.setText(list.get(i));
                }
                mDialog.dismiss();
                mDialog = null;
            });
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    private boolean checkConnect() {
        if(cloudPrinter == null) {
            Toast.makeText(this, R.string.toast_start_add, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!cloudPrinter.isConnected()) {
            Toast.makeText(this, R.string.toast_start_connect, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}