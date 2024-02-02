package com.accessa.ibora.product.options;

public class Options1 {
    private int id;
    private String CatName;
    private String Color;


    public Options1(int id, String CatName, String Color) {
        this.id = id;
        this.CatName = CatName;
        this.Color = Color;

    }

    public Options1(String title, String description, int quantity) {
    }

    public Options1(String id, String CatName, String Color) {
    }

    public Options1() {

    }

    public  int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getOptionsName() {
        return CatName;
    }

    public void setNames(String CatName) {
        this.CatName = CatName;
    }


    public void setCatName(String name) {
        this.CatName = CatName;
    }

    public void setColor(String description) {
        this.Color = Color;
    }

}

