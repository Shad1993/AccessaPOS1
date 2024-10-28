package com.accessa.ibora.Settings.PrinterSetup;

public class PrinterSetUpMethod {
    private String id;
    private String name;
    private String icon;
    private boolean drawerOpens;

    public PrinterSetUpMethod(String id, String name, String icon, boolean drawerOpens) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.drawerOpens = drawerOpens;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isDrawerOpens() {
        return drawerOpens;
    }
}
