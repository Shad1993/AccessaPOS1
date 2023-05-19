package com.accessa.ibora.product.items;

public class Item {
    private int id;
    private String name;
    private String description;
    private String longDescription;
    private float quantity;
    private String department;
    private String subDepartment;
    private String category;
    private float price;
    private String barcode;
    private float weight;
    private String expiryDate;
    private String VAT;
    private String soldBy;
    private String image;
    private String SKU;
    private String variant;
    private float cost;
    private boolean AvailableForSale;

    public Item(int id, String name, String description, float price, String longDescription, float quantity, String category, String department, String subDepartment, String barcode, float weight, String expiryDate, String VAT, String soldBy, Boolean AvailableForSale) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.department = department;
        this.subDepartment = subDepartment;
        this.barcode = barcode;
        this.weight = weight;
        this.expiryDate = expiryDate;
        this.VAT = VAT;
        this.soldBy = soldBy;
        this.AvailableForSale = AvailableForSale;
    }

    public Item(String title, String description, int quantity) {
    }

    public Item(String id, String name, String description) {
    }

    public Item() {
        this.id = id;
        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.department = department;
        this.subDepartment = subDepartment;
        this.barcode = barcode;
        this.weight = weight;
        this.expiryDate=expiryDate;
        this.VAT = VAT;
        this.soldBy = soldBy;
        this.AvailableForSale = AvailableForSale;
    }



    // Getter and Setter methods for all fields

    public int getId() {
        return id;
    }
    public boolean getAvailableForSale() {
        return AvailableForSale;
    }
    public void setAvailableForSale(boolean AvailableForSale) {
        this.AvailableForSale = AvailableForSale;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
