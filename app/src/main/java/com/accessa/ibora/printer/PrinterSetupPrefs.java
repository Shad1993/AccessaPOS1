package com.accessa.ibora.printer;


public class PrinterSetupPrefs {
    private String name;
    private boolean drawerOpens;

    public PrinterSetupPrefs(String name, boolean drawerOpens) {
        this.name = name;
        this.drawerOpens = drawerOpens;
    }

    public String getName() {
        return name;
    }

    public boolean isDrawerOpens() {
        return drawerOpens;
    }
}
