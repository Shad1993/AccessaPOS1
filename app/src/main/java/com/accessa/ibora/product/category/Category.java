package com.accessa.ibora.product.category;

import java.util.List;

public class Category {
    private int id;
    private String CatName;
    private String Color;
    private List<String> subcategories; // List to hold subcategories
    private boolean isExpanded;         // Track if the category is expanded


    public Category(int id, String CatName, String Color, List<String> subcategories) {
        this.id = id;
        this.CatName = CatName;
        this.Color = Color;
        this.subcategories = subcategories;
        this.isExpanded = false; // Initially collapsed
    }
    // Constructor without subcategories
    public Category(int id, String CatName, String Color) {
        this.id = id;
        this.CatName = CatName;
        this.Color = Color;
        this.isExpanded = false; // Initially collapsed
    }
    public int getId() {
        return id;
    }

    public String getCatName() {
        return CatName;
    }

    public String getColor() {
        return Color;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setCatName(String name) {
        this.CatName = name;
    }

    public void setColor(String color) {
        this.Color = color;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

}

