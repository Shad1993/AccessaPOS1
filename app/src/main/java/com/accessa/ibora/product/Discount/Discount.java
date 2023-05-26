package com.accessa.ibora.product.Discount;

public class Discount {
    private int id;
    private String discountName;
    private String discountCode;
    private String discountTimestamp;
    private int discountValue;
    private int userId;

    public Discount() {
        this.id = id;
        this.discountName = discountName;
        this.discountCode = discountCode;
        this.discountTimestamp = discountTimestamp;
        this.discountValue = discountValue;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountTimestamp() {
        return discountTimestamp;
    }

    public void setDiscountTimestamp(String discountTimestamp) {
        this.discountTimestamp = discountTimestamp;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

