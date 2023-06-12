package com.accessa.ibora.Admin;

public class cashier {
    private int cashorid;
    private String CompanyName;
    private String pin;
    private String cashorlevel;
    private String cashorname;
    private String cashorDepartment;

    public cashier() {
        this.cashorid = cashorid;
        this.CompanyName = CompanyName;
        this.pin = pin;
        this.cashorlevel = cashorlevel;
        this.cashorname = cashorname;
        this.cashorDepartment = cashorDepartment;
    }

    public int getcashorid() {
        return cashorid;
    }

    public void setcashorid(int cashorid) {
        this.cashorid = cashorid;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setDiscountName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getpin() {
        return pin;
    }

    public void setpin(String pin) {
        this.pin = pin;
    }

    public String getcashorlevel() {
        return cashorlevel;
    }

    public void setcashorlevel(String cashorlevel) {
        this.cashorlevel = cashorlevel;
    }

    public String getcashorname() {
        return cashorname;
    }

    public void setcashorname(String  cashorname) {
        this.cashorname = cashorname;
    }


    public String getcashorDepartment() {
        return cashorDepartment;
    }

    public void setcashorDepartment(String cashorDepartment) {
        this.cashorDepartment = cashorDepartment;
    }
}

