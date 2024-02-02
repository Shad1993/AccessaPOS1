package com.accessa.ibora.ItemsReport;

// YourDataModel.java
public class DataModel {

    private String longDescription;
    private double totalPrice;
    private int quantity;

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
}

