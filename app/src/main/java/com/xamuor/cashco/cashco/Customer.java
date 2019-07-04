package com.xamuor.cashco.cashco;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "customers", indices = @Index(value = {"customerId"}, unique = true))
public class Customer {
    private int customerId;
    private int companyId;
    private String customerName;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int pCustId;
    @NonNull
    public int getpCustId() {
        return pCustId;
    }

    public void setpCustId(@NonNull int pCustId) {
        this.pCustId = pCustId;
    }
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
