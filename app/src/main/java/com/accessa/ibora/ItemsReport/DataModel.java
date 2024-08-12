package com.accessa.ibora.ItemsReport;

// YourDataModel.java
public class DataModel {

    private String longDescription;
    private double totalPrice;
    private int quantity;
    private String itemName;

    private int totalQuantity;
    public DataModel(String longDescription, double totalPrice, int quantity) {
        this.longDescription = longDescription;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    public void setTotalQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalQuantity() {
        return quantity;
    }
}

