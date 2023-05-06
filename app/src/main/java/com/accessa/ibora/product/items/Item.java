package com.accessa.ibora.product.items;

public class Item {
    private int id;
    private String name;
    private String description;
    private String LongDescription;

    private float quantity;

    private String Department;
    private String SubDepartment;
    private String Category;
    private float price;

    private String Barcode;
    private float Weight;
    private String ExpiryDate;
    private int VAT;

    public Item(int id, String name, String description, float price, String LongDescription, float quantity, String Category,  String Department, String SubDepartment,String Barcode,float Weight,String ExpiryDate,int VAT) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.LongDescription = LongDescription;
        this.price = price;
        this.quantity = quantity;
        this.Category = Category;
        this.Department = Department;
        this.SubDepartment = SubDepartment;
        this.Barcode = Barcode;
        this.Weight = Weight;
        this.VAT = VAT;
    }

    public Item(String title, String description, int quantity) {
    }

    public Item(String id, String name, String description) {
    }

    public  int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getLongDescription() {
        return LongDescription;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setLongDescription(String LongDescription) {
        this.LongDescription = LongDescription;
    }
    public float getPrice() {
        return price;
    }
    public float getquantity() {
        return quantity;
    }
    public void setCategory(String Category) {
        this.Category = Category;
    }
    public void setDepartment(String Department) {
        this.Department = Department;
    }
    public void setSubDepartment(String SubDepartment) {
        this.SubDepartment = SubDepartment;
    }
    public void setSBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public float getWeight() {
        return Weight;
    }
    public int getVAT() {
        return VAT;
    }
}

