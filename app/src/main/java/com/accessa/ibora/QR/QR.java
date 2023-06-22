package com.accessa.ibora.QR;

public class QR {
    private String id;
    private String name;
    private String code;


    public QR(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;

    }
    public QR(String id, String name) {
        this.id = id;
        this.name = name;

    }
    public QR() {
        this.id = id;
        this.name = name;
        this.code = code;

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQRcode() {
        return code;
    }

    public void setQRcode(String code) {
        this.code = code;
    }
}
