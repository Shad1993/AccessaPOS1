package com.accessa.ibora.product.items;

public class ItemData {
    private int currentQuantity;
    private double unitPrice;
    private int itemId;
    private String vatType;

    public ItemData(int currentQuantity, double unitPrice, int itemId, String vatType) {
        this.currentQuantity = currentQuantity;
        this.unitPrice = unitPrice;
        this.itemId = itemId;
        this.vatType = vatType;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public String getVatType() {
        return vatType;
    }
}

