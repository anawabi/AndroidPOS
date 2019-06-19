package com.xamuor.cashco.pos;

public class ReportDataModal {
    private String pImage, pName, customer, payment, date;
    private int invoice, qtySold;

    public ReportDataModal(String pImage, String pName, String customer, String payment, String date, int invoice, int qtySold) {
        this.pImage = pImage;
        this.pName = pName;
        this.customer = customer;
        this.payment = payment;
        this.date = date;
        this.invoice = invoice;
        this.qtySold = qtySold;
    }

    public String getpImage() {
        return pImage;
    }

    public String getpName() {
        return pName;
    }

    public String getCustomer() {
        return customer;
    }

    public String getPayment() {
        return payment;
    }

    public String getDate() {
        return date;
    }

    public int getInvoice() {
        return invoice;
    }

    public int getQtySold() {
        return qtySold;
    }
}
