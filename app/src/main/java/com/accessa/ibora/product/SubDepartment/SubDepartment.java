package com.accessa.ibora.product.SubDepartment;

public class SubDepartment {
    private int id;
    private String SubDepartmentName;
    private String DepartmentCode;
    private String LastModified;
    private String CashierID;




    public SubDepartment() {
        this.id = id;
        this.SubDepartmentName = SubDepartmentName;
        this.DepartmentCode = DepartmentCode;
        this.LastModified = LastModified;
        this.CashierID = CashierID;

    }

    public int getId() {
        return id;
    }
    public String getSubDepartmentCode() {
        return DepartmentCode;
    }
    public void setSubDepartmentCode(String DepartmentCode) {
        this.DepartmentCode = DepartmentCode;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getSubName() {
        return SubDepartmentName;
    }

    public void setSubName(String SubDepartmentName) {
        this.SubDepartmentName = SubDepartmentName;
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
