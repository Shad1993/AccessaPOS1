package com.accessa.ibora.ItemsReport;

// YourDataModel.java
public class CatDataModel {

    private String Categorycode;
    private double totalPrice;
    private int quantity;
    private String famille;

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
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    public void setTotalQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getTotalQuantity() {
        return quantity;
    }
    public String getFamilleName(){return famille;}
}

