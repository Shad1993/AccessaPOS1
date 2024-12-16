package com.accessa.ibora.sales.Sales;

import com.accessa.ibora.product.items.Item;

public class GroupedItem {
    public static final int TYPE_SUBCATEGORY = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_HEADER = 2;

    private int type;
    private String subcategoryName;
    private Item item; // Replace `Item` with your actual item class.

    public GroupedItem(int type, String subcategoryName) {
        this.type = type;
        this.subcategoryName = subcategoryName;
    }

    public GroupedItem(int type, Item item) {
        this.type = type;
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public Item getItem() {
        return item;
    }
}
