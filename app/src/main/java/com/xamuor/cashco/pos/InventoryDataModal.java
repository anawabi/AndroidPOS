package com.xamuor.cashco.pos;

public class InventoryDataModal {
    private int productId;
    private String productImage;
    private String productName;
    private double productPrice;
//    Constructor
    public InventoryDataModal(int productId, String productImage, String productName, double productPrice) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
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


}
