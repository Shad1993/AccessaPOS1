package com.accessa.ibora.Receipt;

public class Receipt {
    private String id;
    private String TransId;
    private String Total;
    private String lastModified;
    private String date;


    public Receipt() {
        this.id = id;
        this.TransId = TransId;
        this.Total = Total;
        this.lastModified = lastModified;
        this.date = date;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return TransId;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public String getTotal() {
        return Total;
    }

    public void setlastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getlastModified() {
        return lastModified;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getdate() {
        return date;
    }


}