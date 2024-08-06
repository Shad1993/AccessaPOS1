package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.items.DatabaseHelper.hasSupplements;

public class Item {
    private int id;
    private String name;
    private String ItemCode;
    private String TaxCode;
    private String description;
    private String longDescription;
    private float quantity;
    private String department;
    private String subDepartment;
    private String category;
    private String Nature;

    private String Discount;
    private String RateDiscount;

    private float TotalDiscount;
    private float price,price2,price3, priceAfterDiscount;
    private String barcode;
    private float weight;
    private String expiryDate;
    private String hascomment;
    private boolean hasoption;

    private String hasSupplements;
    private String relatedSupplement;
    private String relateditem;
    private String relateditem2;
    private String relateditem3;
    private String relateditem4;
    private String relateditem5;
    private String VAT;
    private String Currency;
    private String soldBy;
    private String image;
    private String SKU;
    private String variant;
    private String UserId;
    private String DateCreated;
    private String LastModified;
    private float cost;
    private boolean AvailableForSale;

    public Item(int id, String name, String description, float price, String longDescription, float quantity, String category, String department, String subDepartment, String barcode, float weight, String expiryDate, String VAT, String soldBy, Boolean AvailableForSale,String UserId, String DateCreated,String LastModified) {
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
        this.UserId = UserId;
        this.DateCreated = DateCreated;
        this.LastModified = LastModified;

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
        this.RateDiscount = RateDiscount;
        this.price = price;
        this.price2 = price2;
        this.price3 = price3;
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
        this.UserId = UserId;
        this.DateCreated = DateCreated;
        this.LastModified = LastModified;
        this.priceAfterDiscount = priceAfterDiscount;
        this.TotalDiscount=TotalDiscount;
        this.hascomment=hascomment;
        this.hasoption=hasoption;
        this.relateditem=relateditem;
        this.relateditem2=relateditem2;
        this.relateditem3=relateditem3;
        this.relateditem4=relateditem4;
        this.relateditem4=relateditem5;
        this.hasSupplements=hasSupplements;
        this.relatedSupplement=relatedSupplement;
    }

    public Item(String name, String id, String price, String description, String productImageName) {
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
    public String gethascomment() {
        return hascomment;
    }
    public void setHascomment(String hascomment) {
        this.hascomment = hascomment;
    }

    public boolean  gethasoptions() {
        return hasoption;
    }
    public void setHasoption(Boolean hasoption) {
        this.hasoption = hasoption;
    }

    public String  gethassupplements() {
        return hasSupplements;
    }
    public void sethassupplements(String hasSupplements) {
        this.hasSupplements = hasSupplements;}




    public String getrelatedSupplement() {
        return relatedSupplement;
    }

    public void setrelatedSupplement(String relatedSupplement) {
        this.relatedSupplement = relatedSupplement;
    }
    public String getRelateditem() {
        return relateditem;
    }

    public void setRelateditem(String relateditem) {
        this.relateditem = relateditem;
    }
    public String getRelateditem2() {
        return relateditem2;
    }

    public void setRelateditem2(String relateditem2) {
        this.relateditem2 = relateditem2;
    }

    public String getRelateditem3() {
        return relateditem3;
    }

    public void setRelateditem3(String relateditem3) {
        this.relateditem3 = relateditem3;
    }

    public String getRelateditem4() {
        return relateditem4;
    }

    public void setRelateditem4(String relateditem4) {
        this.relateditem4 = relateditem4;
    }

    public String getRelateditem5() {
        return relateditem5;
    }

    public void setRelateditem5(String relateditem5) {
        this.relateditem5 = relateditem5;
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
    public String getRateDiscount() {
        return RateDiscount;
    }

    public void setRateDiscount(String RateDiscount) {
        this.RateDiscount = RateDiscount;
    }


    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(String TaxCode) {
        this.TaxCode = TaxCode;
    }


    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String ItemCode) {
        this.ItemCode = ItemCode;
    }
    public void setNature(String Nature) {
        this.Nature = Nature;
    }
    public String getNature() {
        return Nature;
    }
    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }
    public String getCurrency() {
        return Currency;
    }
    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }
    public String getDiscount() {
        return Discount;
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

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public float getPrice3() {
        return price3;
    }

    public void setPrice3(float price3) {
        this.price3 = price3;
    }

    public float getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(float priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }
    public float getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(float TotalDiscount) {
        this.TotalDiscount = TotalDiscount;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
    public float getCost() {
        return cost;
    }

    public void setDateCreated(String DateCreated) {
        this.DateCreated = DateCreated;
    }
    public String getDateCreated() {
        return DateCreated;
    }

    public void setLastModified(String LastModified) {
        this.LastModified = LastModified;
    }
    public String getLastModified() {
        return LastModified;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }


}
