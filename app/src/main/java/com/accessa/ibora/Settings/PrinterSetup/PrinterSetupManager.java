package com.accessa.ibora.Settings.PrinterSetup;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrinterSetupManager {
    private static final String PREFS_NAME = "PrinterSetupPrefs";
    private static final String KEY_PRINTER_SETUP_METHODS = "PrinterSetupMethods";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PrinterSetupManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void savePrinterSetupMethods(List<PrinterSetUpMethod> methods) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(methods);
        editor.putString(KEY_PRINTER_SETUP_METHODS, json);
        editor.apply();
    }

    public List<PrinterSetUpMethod> loadPrinterSetupMethods() {
        String json = sharedPreferences.getString(KEY_PRINTER_SETUP_METHODS, null);
        if (json != null) {
            PrinterSetUpMethod[] methodsArray = gson.fromJson(json, PrinterSetUpMethod[].class);
            return new ArrayList<>(Arrays.asList(methodsArray));
        }
        return new ArrayList<>();
    }
}

