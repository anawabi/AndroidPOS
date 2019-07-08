package com.xamuor.cashco.Model;

public class InvoiceDataModal {
    private String productName;
    private int productQty;
    private double productPrice;
    private double productSubtotal;
public InvoiceDataModal(){

}
    public InvoiceDataModal(int productQty, String productName, double productPrice,  double productSubtotal) {
        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.productSubtotal = productSubtotal;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQty() {
        return productQty;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getProductSubtotal() {
        return productSubtotal;
    }
}
