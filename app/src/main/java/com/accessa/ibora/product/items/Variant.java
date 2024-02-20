package com.accessa.ibora.product.items;

public class Variant {
    private long variantId;
    private long itemId;
    private String barcode;
    private String description;

    private String Variantitemid;
    private double price;

    public Variant(long variantId, long itemId, String barcode, String description, double price) {
        this.variantId = variantId;
        this.itemId = itemId;
        this.barcode = barcode;
        this.description = description;
        this.price = price;
    }

    public Variant() {

    }

    public long getVariantId() {
        return variantId;
    }
    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }
    public String getVariantitemid() {
        return Variantitemid;
    }
    public void setVariantitemid(String Variantitemid) {
        this.Variantitemid = Variantitemid;
    }
    public long getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}

