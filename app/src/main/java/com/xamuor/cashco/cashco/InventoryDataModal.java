package com.xamuor.cashco.cashco;

public class InventoryDataModal {
    private int productId;
    private String productImage;
    private String productName;
    private double productPrice;
    private int productQty;
//    Constructor
    public InventoryDataModal(int productId, String productImage, String productName, double productPrice, int productQty) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductQty() {
        return productQty;
    }
}
