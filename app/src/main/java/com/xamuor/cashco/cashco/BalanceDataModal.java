package com.xamuor.cashco.cashco;

public class BalanceDataModal {
    private int invoiceNO;
    private String paymentType, customer, recievable;
    private double recieved;
    private String purDate;

    public BalanceDataModal(int inVoiceNO, String paymentType, String customer, double recieved, String recievable, String purDate) {
        this.invoiceNO = inVoiceNO;
        this.paymentType = paymentType;
        this.customer = customer;
        this.recieved = recieved;
        this.recievable = recievable;
        this.purDate = purDate;
    }

    public int getInvoiceNO() {
        return invoiceNO;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getCustomer() {
        return customer;
    }

    public double getRecieved() {
        return recieved;
    }

    public String getRecievable() {
        return recievable;
    }

    public String getPurDate() {
        return purDate;
    }
}
