package com.accessa.ibora.company;


public class Company {
    private String Abv;
    private String StockNo;
    private String PriceNo;
    private String DefSupplierCode;
    private String VATNo;
    private String BRNNo;
    private String ADR1;
    private String ADR2;
    private String ADR3;
    private String TelNo;
    private String FaxNo;
    private String CompanyName;
    private String cashiorid;
    private String lastmodified;
    private String imagelink;

    public Company() {
        this.Abv = Abv;
        this.StockNo = StockNo;
        this.PriceNo = PriceNo;
        this.DefSupplierCode = DefSupplierCode;
        this.VATNo = VATNo;
        this.BRNNo = BRNNo;
        this.ADR1 = ADR1;
        this.ADR2 = ADR2;
        this.ADR3 = ADR3;
        this.TelNo = TelNo;
        this.FaxNo = FaxNo;
        this.CompanyName = CompanyName;
        this.cashiorid = cashiorid;
        this.lastmodified = lastmodified;
        this.imagelink=imagelink;

    }

    // Getter and Setter methods for all fields

    public String getAbv() {
        return Abv;
    }

    public void setAbv(String Abv) {
        this.Abv = Abv;
    }

    public String getStockNo() {
        return StockNo;
    }

    public void setStockNo(String StockNo) {
        this.StockNo = StockNo;
    }

    public String getPriceNo() {
        return PriceNo;
    }

    public void setPriceNo(String PriceNo) {
        this.PriceNo = PriceNo;
    }

    public String getDefSupplierCode() {
        return DefSupplierCode;
    }

    public void setDefSupplierCode(String DefSupplierCode) {
        this.DefSupplierCode = DefSupplierCode;
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
}

