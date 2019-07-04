package com.xamuor.cashco.cashco;

public class Users {
//    Values are set for these variable while login
    public static int companyId;
    private static int custId;
    private static int userId;
    private static String fname;
    private static String lname;
    private static String phone;
    private static String role;
    private static int status;
    private static String photo;

    public static int getCustId() {
        return custId;
    }

    public static void setCustId(int custId) {
        Users.custId = custId;
    }

    public static int getCompanyId() {
        return companyId;
    }

    public static void setCompanyId(int companyId) {
        Users.companyId = companyId;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Users.userId = userId;
    }

    public static String getFname() {
        return fname;
    }

    public static void setFname(String fname) {
        Users.fname = fname;
    }

    public static String getLname() {
        return lname;
    }

    public static void setLname(String lname) {
        Users.lname = lname;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Users.phone = phone;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        Users.role = role;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        Users.status = status;
    }

    public static String getPhoto() {
        return photo;
    }

    public static void setPhoto(String photo) {
        Users.photo = photo;
    }
}
