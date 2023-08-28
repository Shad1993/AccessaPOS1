package com.accessa.ibora.company;


public class Company {
    private String Shopname;
    private String ShopNumber;
    private String VATNo;
    private String BRNNo;
    private String ADR1;
    private String ADR2;
    private String ADR3;
    private String CompADR;
    private String CompADR2;
    private String CompADR3;
    private String TelNo;
    private String FaxNo;
    private String CompanyName;
    private String cashiorid;
    private String lastmodified;
    private String imagelink;
    private String Comptel;
    private String CompFax;


    private String Openinghours;

    public Company() {
        this.Shopname = Shopname;
        this.ShopNumber = ShopNumber;
        this.VATNo = VATNo;
        this.BRNNo = BRNNo;
        this.ADR1 = ADR1;
        this.ADR2 = ADR2;
        this.ADR3 = ADR3;
        this.CompADR = CompADR;
        this.CompADR2 = CompADR2;
        this.CompADR3 = CompADR3;
        this.TelNo = TelNo;
        this.FaxNo = FaxNo;
        this.CompanyName = CompanyName;
        this.cashiorid = cashiorid;
        this.lastmodified = lastmodified;
        this.imagelink=imagelink;
        this.Openinghours=Openinghours;
        this.Comptel=Comptel;
        this.CompFax=CompFax;

    }

    // Getter and Setter methods for all fields

    public String getShopName() {
        return Shopname;
    }

    public void setShopName(String Shopname) {
        this.Shopname = Shopname;
    }
    public String getShopNumber() {
        return ShopNumber;
    }

    public void setShopNumber(String ShopNumber) {
        this.ShopNumber = ShopNumber;
    }


    public String getVATNo() {
        return VATNo;
    }

    public void setVATNo(String VATNo) {
        this.VATNo = VATNo;
    }

    public String getBRNNo() {
        return BRNNo;
    }

    public void setBRNNo(String BRNNo) {
        this.BRNNo = BRNNo;
    }

    public String getADR1() {
        return ADR1;
    }

    public void setADR1(String ADR1) {
        this.ADR1 = ADR1;
    }

    public String getADR2() {
        return ADR2;
    }

    public void setADR2(String ADR2) {
        this.ADR2 = ADR2;
    }

    public String getADR3() {
        return ADR3;
    }

    public void setADR3(String ADR3) {
        this.ADR3 = ADR3;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String TelNo) {
        this.TelNo = TelNo;
    }

    public String getFaxNo() {
        return FaxNo;
    }

    public void setFaxNo(String FaxNo) {
        this.FaxNo = FaxNo;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public void setCashorId(String cashiorid) { this.cashiorid= cashiorid;
    }

    public void setLastModified(String lastmodified) {this.lastmodified=lastmodified;
    }

    public String getImage() {return imagelink;
    }
    public void setImage(String imagelink) {
        this.imagelink = imagelink;
    }
    public void setCompADR(String CompADR) {
        this.CompADR = CompADR;
    }
    public void setCompADR2(String CompADR2) {
        this.CompADR2 = CompADR2;
    }
    public void setCompADR3(String CompADR3) {
        this.CompADR3 = CompADR3;
    }
    public void setOpeninghours(String Openinghours) {
        this.Openinghours = Openinghours;
    }
    public void setComptel(String Comptel) {
        this.Comptel = Comptel;
    }
    public void setCompFax(String CompFax) {
        this.CompFax = CompFax;
    }

    public String getCompADR() {
        return CompADR;
    }
    public String getCompADR2() {
        return CompADR2;
    }
    public String getCompADR3() {
        return CompADR3;
    }
    public String getOpeninghours() {
        return Openinghours;
    }
    public String getComptel() {
        return Comptel;
    }
    public String getCompFax() {
        return CompFax;
    }
}

