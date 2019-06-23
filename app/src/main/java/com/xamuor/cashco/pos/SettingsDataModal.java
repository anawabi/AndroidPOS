package com.xamuor.cashco.pos;

public class SettingsDataModal {
    private String photo, fname, lname, phone, role;
    private int status, userId;

    public SettingsDataModal(int userId, String photo, String fname, String lname, String phone, String role, int status) {
        this.userId = userId;
        this.photo = photo;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public int getStatus() {
        return status;
    }
}
