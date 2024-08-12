package com.accessa.ibora.ItemsReport;

public class ItemSummary {
    private String itemName;
    private double totalPrice;
    private int totalQuantity;

    public ItemSummary(String itemName, double totalPrice, int totalQuantity) {
        this.itemName = itemName;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
