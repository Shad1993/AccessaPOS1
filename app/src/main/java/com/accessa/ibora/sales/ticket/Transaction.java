package com.accessa.ibora.sales.ticket;

public class Transaction {
    private int unique_id;
    private int id;
    private int item_id;
    private String itemName;
    private String Longdescription;
    private String category;
    private String department;
    private String subDepartment;
    private String barcode;
    private String itemCode;
    private String itemNumber;
    private String weight;
    private String expiryDate;
    private String VAT;
    private String Nature;
    private String Currency;
    private String famille;

    private String TaxCode;
    private String comment;
    private String Discount;
    private double AmountWOVAT;

    private double ItemAmountWOVAT;
    private String TotalDiscount;
    private String relatedoptionid;

    private double itemPrice;
    private String unitPrice;

    private double TotalVatAmount;
    private double TotalPrice;

    private int itemQuantity;
    public Transaction(int uniqueId, String itemName, double itemPrice, int itemQuantity, String unitPrice, int itemId, String relatedoptionid) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        this.item_id=itemId;
        this.unique_id = uniqueId;
        this.relatedoptionid = relatedoptionid;
    }

    public Transaction() {
        this.unique_id = unique_id;
        this.id = id;
        this.itemName = itemName;
        this.itemNumber = itemNumber;
        this.Longdescription = Longdescription;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.category = category;
        this.department = department;
        this.subDepartment = subDepartment;
        this.barcode = barcode;
        this.weight = weight;
        this.expiryDate=expiryDate;
        this.VAT = VAT;
        this.Nature = Nature;
        this.Currency = Currency;
        this.comment=comment;
        this.famille = famille;
        this.itemCode = itemCode;
        this.Discount = Discount;
        this.AmountWOVAT = AmountWOVAT;
        this.ItemAmountWOVAT=ItemAmountWOVAT;
        this.TotalDiscount = TotalDiscount;
        this.TotalVatAmount = TotalVatAmount;
        this.TotalPrice = TotalPrice;
        this.TaxCode = TaxCode;
    }



    public int getitem_id() {
        return item_id;
    }
    public void setitem_id(int item_id) {
        this.item_id = item_id;
    }
    public int getunique_id() {
        return unique_id;
    }
    public void setunique_id(int unique_id) {
        this.unique_id = unique_id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTaxCode() {
        return TaxCode;
    }
    public void setTaxCode(String TaxCode) {
        this.TaxCode = TaxCode;
    }
    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
    public void setNature(String Nature) {
        this.Nature = Nature;
    }
    public String getNature() {
        return Nature;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemcode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getItemPrice() {
        return itemPrice;
    }
    public double getTotalAmountWitoutVAT() {
        return AmountWOVAT;
    }
    public void setAmountWOVAT(double AmountWOVAT) {
        this.AmountWOVAT = AmountWOVAT;
    }
    public double getItemAmountWitoutVAT() {
        return ItemAmountWOVAT;
    }
    public void setItemAmountWOVAT(double ItemAmountWOVAT) {
        this.ItemAmountWOVAT = ItemAmountWOVAT;
    }
    public double getTotalVatAmount() {
        return TotalVatAmount;
    }

    public void setTotalVatAmount(double TotalVatAmount) {
        this.TotalVatAmount = TotalVatAmount;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double TotalPrice) {
        this.TotalPrice = TotalPrice;
    }
    public String getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(String TotalDiscount) {
        this.TotalDiscount = TotalDiscount;
    }

    public double getAmountWOVAT() {
        return AmountWOVAT;
    }

    public String getLongDescription() {
        return Longdescription;
    }

    public void setLongDescription(String Longdescription) {
        this.Longdescription = Longdescription;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }
    public String getCurrency() {
        return Currency;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }
    public String getFamille() {
        return famille;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }


}

