package com.xamuor.cashco.Model;

public class CustomerDataModal {
    private int custId, custStatus;
    private String custFname, custLname, custPhone, custEmail, cust_state, cust_addr;


    public CustomerDataModal(int custId, int custStatus, String custFname, String custLname, String custPhone, String custEmail, String cust_state, String cust_addr) {
        this.custId = custId;
        this.custStatus = custStatus;
        this.custFname = custFname;
        this.custLname = custLname;
        this.custPhone = custPhone;
        this.custEmail = custEmail;
        this.cust_state = cust_state;
        this.cust_addr = cust_addr;

    }

    public int getCustId() {
        return custId;
    }

    public int getCustStatus() {
        return custStatus;
    }

    public String getCustFname() {
        return custFname;
    }

    public String getCustLname() {
        return custLname;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public String getCust_state() {
        return cust_state;
    }

    public String getCust_addr() {
        return cust_addr;
    }
}
