package com.accessa.ibora.Settings.PaymentFragment;

public class payment {
    private int id;
    private String PaymentMethodName;
    private String PaymentMethodIcon;
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

    public String getPaymentName() {
        return PaymentMethodName;
    }

    public void setPaymentMethodName(String PaymentMethodName) {
        this.PaymentMethodName = PaymentMethodName;
    }

    public String getPaymentMethodIcon() {
        return PaymentMethodIcon;
    }

    public void setDPaymentMethodIcon(String PaymentMethodIcon) {
        this.PaymentMethodIcon = PaymentMethodIcon;
    }


}
