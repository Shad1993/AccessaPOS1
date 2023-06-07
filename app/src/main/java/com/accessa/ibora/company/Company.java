package com.accessa.ibora.company;

public class Company {
    private String companyName;
    private String address;
    private String phoneNumber;
    private String faxNumber;
    private String email;
    private String VATNo;
    private String BRNNo;

    public Company(String companyName, String address, String phoneNumber, String faxNumber, String email, String VATNo, String BRNNo) {
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.email = email;
        this.VATNo = VATNo;
        this.BRNNo = BRNNo;
    }

    // Getter and Setter methods for all fields

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

