package com.accessa.ibora.printer.externalprinterlibrary2;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.sunmi.cloudprinter.bean.Router;
import com.sunmi.externalprinterlibrary2.BuildConfig;
import com.sunmi.externalprinterlibrary2.ConnectCallback;
import com.sunmi.externalprinterlibrary2.SetWifiCallback;
import com.sunmi.externalprinterlibrary2.SunmiPrinterManager;
import com.sunmi.externalprinterlibrary2.WifiResult;
import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;

import java.util.List;

public class WifiSelectActivity extends AppCompatActivity implements WifiResult, RouterListAdapter.OnItemClickListener, SetWifiCallback {

    private String name;
    private RouterListAdapter adapter;
    private Dialog passwordDialog;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_select);
        name = getIntent().getStringExtra("name");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = findViewById(R.id.wifi_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RouterListAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        initDialog();
        if(BuildConfig.DEBUG) {
            initSn();
        } else {
            initSearch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
        SunmiPrinterManager.getInstance().exitPrinterWifi(this, MyApplication.getInstance().getCloudPrinter(name));
    }

    /**
     * 先通过打印机获取SN，实际使用中应该由用户输入指定SN
     * 使用SN开始进入打印机配网代替开关配网
     */
    private void initSn() {
        CloudPrinter cloudPrinter = MyApplication.getInstance().getCloudPrinter(name);
        cloudPrinter.connect(this, new ConnectCallback() {
            @Override
            public void onConnect() {
                cloudPrinter.getDeviceSN(s -> {
                    cloudPrinter.release(WifiSelectActivity.this);
                    SunmiPrinterManager.getInstance().startPrinterWifi(WifiSelectActivity.this, cloudPrinter, s);
                    initSearch();
                });
            }

            @Override
            public void onFailed(String s) {

            }

            @Override
            public void onDisConnect() {

            }
        });
    }

    private void initSearch() {
        SunmiPrinterManager.getInstance().searchPrinterWifiList(this, MyApplication.getInstance().getCloudPrinter(name), this);
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
    public void onRouterFound(Router router) {
        System.out.println("onRouterFound " + router.getName());
        adapter.addData(router);
    }

    @Override
    public void onFinish() {
        System.out.println("onFinish ");
    }

    @Override
    public void onFailed() {
        System.out.println("onFailed ");
    }

    @Override
    public void onItemClick(int position, List<Router> data) {
        showMessageDialog(data.get(position));
    }

    private void initDialog() {
        if (loadingDialog == null) {
            synchronized (this) {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(this);
                    loadingDialog.setCanceledOnTouchOutside(false);
                }
            }
        }
    }

    public void showLoadingDialog() {
        runOnUiThread(() -> {
            if (loadingDialog == null || loadingDialog.isShowing()) {
                return;
            }
            loadingDialog.setLoadingContent(null);
            loadingDialog.show();
        });
    }

    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog == null || !loadingDialog.isShowing()) {
                    return;
                }
                loadingDialog.setCancelable(true);
                loadingDialog.dismiss();
            }
        });
    }

    private void showMessageDialog(final Router router) {
        passwordDialog = new Dialog(this, R.style.Son_dialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_router_message, null);
        TextView title = view.findViewById(R.id.tv_title);
        final ClearableEditText etPassword = view.findViewById(R.id.et_password);
        if (!router.isHasPwd()) {
            title.setText(getString(R.string.set_wifi_use, router.getName()));
            view.findViewById(R.id.et_password).setVisibility(View.GONE);
        } else {
            title.setText(R.string.set_wifi_input);
            ((TextView) view.findViewById(R.id.tv_msg))
                    .setText(getString(R.string.set_wifi_hint, router.getName()));
        }
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            passwordDialog.dismiss();
            passwordDialog = null;
        });
        view.findViewById(R.id.btnSure).setOnClickListener(v -> {
            passwordDialog.dismiss();
            passwordDialog = null;
            String psw = etPassword.getText().toString().trim();
            if (router.isHasPwd() && TextUtils.isEmpty(psw)) {
                Toast.makeText(WifiSelectActivity.this, R.string.set_wifi_password, Toast.LENGTH_LONG).show();
                return;
            }
            SunmiPrinterManager.getInstance().setPrinterWifi(WifiSelectActivity.this,
                    MyApplication.getInstance().getCloudPrinter(name), router.getEssid(), psw, this);
            showLoadingDialog();
        });
        passwordDialog.setContentView(view);
        passwordDialog.setCancelable(false);
        passwordDialog.show();
    }

    @Override
    public void onSetWifiSuccess() {

    }

    @Override
    public void onConnectWifiSuccess() {
        Toast.makeText(this, R.string.toast_wifi_ok, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onConnectWifiFailed() {
        hideLoadingDialog();
        Toast.makeText(this, R.string.toast_password_error, Toast.LENGTH_LONG).show();
    }
}