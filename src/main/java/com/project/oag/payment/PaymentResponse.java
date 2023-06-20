package com.project.oag.payment;

public class PaymentResponse {
    private String checkOutUrl;
    private String txRef;

    public String getCheckOutUrl() {
        return checkOutUrl;
    }

    public void setCheckOutUrl(String checkOutUrl) {
        this.checkOutUrl = checkOutUrl;
    }

    public String getTxRef() {
        return txRef;
    }

    public void setTxRef(String txRef) {
        this.txRef = txRef;
    }
}
