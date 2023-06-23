package com.accessa.ibora.sales.ticket.Checkout;

public class Payment {
    private String id;
    private String name;
    private String icon;



    public Payment(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;

    }
    public Payment(String id, String name) {
        this.id = id;
        this.name = name;

    }
    public Payment() {
        this.id = id;
        this.name = name;
        this.icon = icon;

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geticon() {
        return icon;
    }

    public void seticon(String icon) {
        this.icon = icon;
    }

}
