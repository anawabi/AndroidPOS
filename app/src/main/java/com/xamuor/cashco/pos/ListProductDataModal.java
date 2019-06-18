package com.xamuor.cashco.pos;

public class ListProductDataModal {
    private String productImage, productName;
    private int productId;

    public ListProductDataModal(int productId, String productImage, String productName) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }
}
