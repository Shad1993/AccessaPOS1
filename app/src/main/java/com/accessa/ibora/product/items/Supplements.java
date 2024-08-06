package com.accessa.ibora.product.items;

public class Supplements {
    private long supplementId;
    private long itemId;
    private String barcode;
    private String description;
    private long OptionsId;
    private String Variantitemid;
    private double price;

    public Supplements(long supplementId, long itemId, String barcode, String description, double price) {
        this.supplementId = supplementId;
        this.itemId = itemId;
        this.barcode = barcode;
        this.description = description;
        this.price = price;
    }

    public Supplements() {

    }

    public  long getsupplementId() {
        return supplementId;
    }
    public void setsupplementId(int supplementId) {
        this.supplementId = supplementId;
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

