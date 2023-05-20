package com.accessa.ibora.product.Department;

public class Department {
    private int id;
    private String DepartmentName;
    private String DepartmentCode;
    private String LastModified;
    private String CashierID;



    public Department() {
        this.id = id;
        this.DepartmentName = DepartmentName;
        this.DepartmentCode = DepartmentCode;
        this.LastModified = LastModified;
        this.CashierID = CashierID;

    }

    public int getId() {
        return id;
    }
    public String getDepartmentCode() {
        return DepartmentCode;
    }
    public void setDepartmentCode(String DepartmentCode) {
        this.DepartmentCode = DepartmentCode;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return DepartmentName;
    }

    public void setName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
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
