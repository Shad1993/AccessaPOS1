package com.accessa.ibora.ItemsReport;

// YourDataModel.java
public class CatDataModel {

    private String Categorycode;
    private double totalPrice;
    private int quantity;
    private String famille;
    private String Transactionid;

    public CatDataModel(String Categorycode, double totalPrice, int quantity,String Transactionid) {
        this.Categorycode = Categorycode;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.Transactionid = Transactionid;
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
    public String getTransactionid() {
        return Transactionid;
    }

    public void setTransactionid(String Transactionid) {
        this.Transactionid = Transactionid;
    }
}

