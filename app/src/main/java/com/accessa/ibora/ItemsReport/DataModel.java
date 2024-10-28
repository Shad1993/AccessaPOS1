package com.accessa.ibora.ItemsReport;

// YourDataModel.java
public class DataModel {

    private String longDescription;
    private double totalPrice;
    private int quantity;
    private String itemName;
    private String comment;
    private String Transactionid;

    private int totalQuantity;
    public DataModel(String longDescription, double totalPrice, int quantity,String Transactionid,String comment) {
        this.longDescription = longDescription;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.Transactionid = Transactionid;
        this.comment = comment;
    }

    public String getLongDescription() {
        return longDescription;
    }
    public String getTransactionid() {
        return Transactionid;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
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


    public void setTransactionid(String Transactionid) {
        this.Transactionid = Transactionid;
    }
    public void setTotalQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalQuantity() {
        return quantity;
    }
}

