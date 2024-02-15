package com.accessa.ibora.product.items;

public class Options {
    private long OptionsId;
    private long OptionsId2;
    private long OptionsId3;
    private long OptionsId4;
    private long OptionsId5;

    private String Optionsname;
    private String Optionsname2;
    private String Optionsname3;
    private String Optionsname4;
    private String Optionsname5;


    public Options(long OptionsId, String Optionsname) {
        this.OptionsId = OptionsId;
        this.Optionsname = Optionsname;

    }

    public Options() {

    }
    public void setOptionId(long OptionsId) {
        this.OptionsId = OptionsId;
    }
    public long getOptionId() {
        return OptionsId;
    }
    public void setOptionName(String Optionsname) {
        this.Optionsname = Optionsname;
    }
    public String getOptionname() {
        return Optionsname;
    }

    public long getOptionId2() {
        return OptionsId2;
    }

    public String getOptionname2() {
        return Optionsname2;
    }

    public long getOptionId3() {
        return OptionsId3;
    }

    public String getOptionname3() {
        return Optionsname3;
    }
    public long getOptionId4() {
        return OptionsId4;
    }

    public String getOptionname4() {
        return Optionsname4;
    }
    public long getOptionId5() {
        return OptionsId5;
    }

    public String getOptionname5() {
        return Optionsname5;
    }


}

