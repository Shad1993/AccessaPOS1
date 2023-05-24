package com.accessa.ibora.product.Vendor;

public class Vendor {
    private int id;
    private String CodeFournisseur;
    private String NomFournisseur;

    private String LastModified;
    private String CashierID;



    public Vendor() {
        this.id = id;
        this.CodeFournisseur = CodeFournisseur;
        this.NomFournisseur = NomFournisseur;
        this.LastModified = LastModified;
        this.CashierID = CashierID;

    }

    public int getId() {
        return id;
    }
    public String getCodeFournisseur() {
        return CodeFournisseur;
    }
    public void setCodeFournisseur(String CodeFournisseur) {
        this.CodeFournisseur = CodeFournisseur;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNomFournisseur() {
        return NomFournisseur;
    }

    public void setNomFournisseur(String SubDepartmentName) {
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

}
