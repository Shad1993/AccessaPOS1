package com.accessa.ibora.sales.Sales;

import com.accessa.ibora.product.items.Item;

import java.util.List;

public class Section {
    private String subCategoryName;
    private List<Item> items;

    public Section(String subCategoryName, List<Item> items) {
        this.subCategoryName = subCategoryName;
        this.items = items;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public List<Item> getItems() {
        return items;
    }
}

