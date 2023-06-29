package com.accessa.ibora.printer.cloudPrinter;

import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;

import java.util.HashMap;

public class MyApplication extends android.app.Application {


    private static MyApplication instance = null;

    private final HashMap<String, CloudPrinter> cloudPrinters = new HashMap<>();

    public synchronized static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void addCloudPrinter(CloudPrinter cloudPrinter) {
        cloudPrinters.put(cloudPrinter.getCloudPrinterInfo().name, cloudPrinter);
    }

    public CloudPrinter getCloudPrinter(String name) {
        return cloudPrinters.get(name);
    }
}
