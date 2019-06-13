package com.xamuor.cashco.pos;

public class MyCompany {
    public static int companyId;
    private static int custId;

    public static int getCustId() {
        return custId;
    }

    public static void setCustId(int custId) {
        MyCompany.custId = custId;
    }

    public static int getCompanyId() {
        return companyId;
    }

    public static void setCompanyId(int companyId) {
        MyCompany.companyId = companyId;
    }
}
