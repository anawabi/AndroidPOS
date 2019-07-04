package com.xamuor.cashco.cashco;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "invoices")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int invoiceId;

    @ColumnInfo(name = "product_id")
    public int productId;
    @ColumnInfo(name = "comp_id")
    public int companyId;
    @ColumnInfo(name = "product_qty")
    public int productQty;
    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "product_price")
    public double productPrice;

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    } public String getItem() {
        return productName;
    }

    public void setItem(String productName) {
        this.productName = productName;
    }


    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }



}
