package com.xamuor.cashco.pos;

public class Inventory2DataModal {
    private int pId;
    private String pImage;
    private String pName;
    private int pQty;
    private double pPurchasePrice;
    private double pSellPrice;
    private String pRegDate;

    public Inventory2DataModal(int pId, String pImage, String pName, int pQty, double pPurchasePrice, double pSellPrice, String pRegDate) {
        this.pId = pId;
        this.pImage = pImage;
        this.pName = pName;
        this.pQty = pQty;
        this.pPurchasePrice = pPurchasePrice;
        this.pSellPrice = pSellPrice;
        this.pRegDate = pRegDate;
    }

    public int getpId() {
        return pId;
    }

    public String getpImage() {
        return pImage;
    }

    public String getpName() {
        return pName;
    }

    public int getpQty() {
        return pQty;
    }

    public double getpPurchasePrice() {
        return pPurchasePrice;
    }

    public double getpSellPrice() {
        return pSellPrice;
    }

    public String getpRegDate() {
        return pRegDate;
    }
}
