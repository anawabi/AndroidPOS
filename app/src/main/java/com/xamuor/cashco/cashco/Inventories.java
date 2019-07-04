package com.xamuor.cashco.cashco;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "inventories", indices = @Index(value = {"productId"}, unique = true))
public class Inventories {
    private int compId;
    private int productId;
    private String productImage;
    private String productName;
    private double sellPrice;
    @ColumnInfo(name = "qty")
    private int qty;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(@NonNull String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(@NonNull String productName) {
        this.productName = productName;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
