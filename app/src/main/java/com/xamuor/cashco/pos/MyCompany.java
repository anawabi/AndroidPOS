package com.xamuor.cashco.pos;

public class MyCompany {
//    Values are set for these variable while login
    public static int companyId;
    private static int custId;
    private static int userId;

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

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        MyCompany.userId = userId;
    }
}
