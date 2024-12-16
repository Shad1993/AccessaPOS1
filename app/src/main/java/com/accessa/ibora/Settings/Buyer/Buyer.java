package com.accessa.ibora.Settings.Buyer;

public class Buyer {
    private int id;
    private String name;
    private String Othername;
    private String tan;
    private String brn;
    private String businessAddr;
    private String buyerType;
    private String nic;
    private String companyName;
    private String profile;
    private String priceLevel;

    private String CashiorId;
    private String datecreated;
    private String lastmodified;

    public Buyer(String name,String Othername, String tan, String brn, String businessAddr, String buyerType, String profile, String nic, String companyName,String PriceLevel,String Cashiorid,String dateCreated,String LastModified) {
        this.name = name;
        this.Othername = Othername;
        this.tan = tan;
        this.brn = brn;
        this.businessAddr = businessAddr;
        this.buyerType = buyerType;
        this.nic = nic;
        this.companyName = companyName;
        this.profile = profile;
        this.CashiorId = Cashiorid;
        this.datecreated = dateCreated;
        this.lastmodified = LastModified;
        this.priceLevel = PriceLevel;
    }
    public Buyer() {
        this.id = id;
        this.name = name;
        this.tan = tan;
        this.brn = brn;
        this.businessAddr = businessAddr;
        this.buyerType = buyerType;
        this.nic = nic;
        this.companyName = companyName;
        this.profile = profile;
        this.priceLevel = priceLevel;
        this.CashiorId = CashiorId;
        this.datecreated = datecreated;
        this.lastmodified = lastmodified;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setNames(String name) {
        this.name = name;
    }
    public String getNames() {
        return name;
    }

    public void setCashiorId(String CashiorId) {
        this.CashiorId = CashiorId;
    }
    public String getCashiorId() {
        return CashiorId;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }
    public String getDatecreated() {
        return datecreated;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }
    public String getLastmodified() {
        return lastmodified;
    }
    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }
    public String getPriceLevel() {
        return priceLevel;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getProfile() {
        return profile;
    }
    public void setTan(String tan) {
        this.tan = tan;
    }
    public String getTan() {
        return tan;
    }
    public void setBrn(String brn) {
        this.brn = brn;
    }
    public String getBrn() {
        return brn;
    }
    public void setBusinessAddr(String businessAddr) {
        this.businessAddr = businessAddr;
    }
    public String getBusinessAddr() {
        return businessAddr;
    }
    public void setBuyerType(String buyerType) {
        this.buyerType = buyerType;
    }
    public String getBuyerType() {
        return buyerType;
    }
    public void setNic(String nic) {
        this.nic = nic;
    }
    public String getNic() {
        return nic;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setBuyerOtherName(String Othername) {
        this.Othername = Othername;
    }

    public String getBuyerOtherName() {
        return Othername;
    }
    @Override
    public String toString() {
        return companyName; // Return the company name for display
    }
}
