package com.accessa.ibora.ItemsReport;

// YourDataModel.java
public class CatDataModel {

    private String Categorycode;
    private double totalPrice;
    private int quantity;

    public CatDataModel(String Categorycode, double totalPrice, int quantity) {
        this.Categorycode = Categorycode;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    public String getCategorycode() {
        return Categorycode;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}

