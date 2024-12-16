package com.accessa.ibora.Settings.PaymentFragment;

public class payment {
    private int id;
    private String PaymentMethodName;
    private String PaymentMethodIcon;
    private int visibility;
    private int displayqr;
    private int dispplayphone;
    private String PaymentMethodqr;
    private String PaymentMethodPhone;
    private boolean OpenDrawer;


    public payment(int id,String PaymentMethodName,String PaymentMethodIcon, boolean OpenDrawer) {
        this.id = id;
        this.PaymentMethodName = PaymentMethodName;
        this.PaymentMethodIcon = PaymentMethodIcon;
        this.OpenDrawer = OpenDrawer;

    }
    public payment(String PaymentMethodName, String id, String PaymentMethodIcon, boolean OpenDrawer) {
    }

    public payment() {

    }

    public int getId() {
        return id;
    }
    public  boolean getOpenDrawer() {
        return OpenDrawer;
    }
    public void setDrawerOpen(boolean OpenDrawer) {
        this.OpenDrawer = OpenDrawer;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getDisplayqr() {
        return displayqr;
    }
    public int getDispplayphone() {
        return dispplayphone;
    }

    public int getVisibility() {
        return visibility;
    }
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setDispplayphone(int dispplayphone) {
        this.dispplayphone = dispplayphone;
    }
    public void setDisplayqr(int displayqr) {
        this.displayqr = displayqr;
    }
    public String getPaymentMethodqr() {
        return PaymentMethodqr;
    }
    public String getPaymentMethodPhone() {
        return PaymentMethodPhone;
    }
    public String getPaymentName() {
        return PaymentMethodName;
    }

    public void setPaymentMethodName(String PaymentMethodName) {
        this.PaymentMethodName = PaymentMethodName;
    }
    public void setPaymentMethodqr(String PaymentMethodqr) {
        this.PaymentMethodqr = PaymentMethodqr;
    }
    public void setPaymentMethodPhone(String PaymentMethodPhone) {
        this.PaymentMethodPhone = PaymentMethodPhone;
    }
    public String getPaymentMethodIcon() {
        return PaymentMethodIcon;
    }

    public void setDPaymentMethodIcon(String PaymentMethodIcon) {
        this.PaymentMethodIcon = PaymentMethodIcon;
    }


}
