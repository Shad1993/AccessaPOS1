package com.accessa.ibora.product.Vendor;

public class Vendor {
    private int id;
    private String CodeFournisseur;
    private String NomFournisseur;
    private String LastModified;
    private String CashierID;
    private String PhoneNumber;
    private String Street;
    private String Town;
    private String PostalCode;
    private String Email;
    private String InternalCode;
    private String Salesmen;

    public Vendor() {
        this.id = id;
        this.CodeFournisseur = CodeFournisseur;
        this.NomFournisseur = NomFournisseur;
        this.LastModified = LastModified;
        this.CashierID = CashierID;
        this.PhoneNumber = PhoneNumber;
        this.Street = Street;
        this.Town = Town;
        this.PostalCode = PostalCode;
        this.Email = Email;
        this.InternalCode = InternalCode;
        this.Salesmen = Salesmen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeFournisseur() {
        return CodeFournisseur;
    }

    public void setCodeFournisseur(String CodeFournisseur) {
        this.CodeFournisseur = CodeFournisseur;
    }

    public String getNomFournisseur() {
        return NomFournisseur;
    }

    public void setNomFournisseur(String NomFournisseur) {
        this.NomFournisseur = NomFournisseur;
    }

    public String getLastModified() {
        return LastModified;
    }

    public void setLastModified(String LastModified) {
        this.LastModified = LastModified;
    }

    public String getCashierID() {
        return CashierID;
    }

    public void setCashierID(String CashierID) {
        this.CashierID = CashierID;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String Street) {
        this.Street = Street;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String Town) {
        this.Town = Town;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getInternalCode() {
        return InternalCode;
    }

    public void setInternalCode(String InternalCode) {
        this.InternalCode = InternalCode;
    }

    public String getSalesmen() {
        return Salesmen;
    }

    public void setSalesmen(String Salesmen) {
        this.Salesmen = Salesmen;
    }
}
