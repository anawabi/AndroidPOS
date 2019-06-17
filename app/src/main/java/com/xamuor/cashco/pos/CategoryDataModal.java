package com.xamuor.cashco.pos;

public class CategoryDataModal {
    private String ctgName;
    private String ctgDesc;
    private String ctgRegDate;

    public CategoryDataModal(String ctgName, String ctgDesc, String ctgRegDate) {
        this.ctgName = ctgName;
        this.ctgDesc = ctgDesc;
        this.ctgRegDate = ctgRegDate;
    }

    public String getCtgName() {
        return ctgName;
    }

    public String getCtgDesc() {
        return ctgDesc;
    }

    public String getCtgRegDate() {
        return ctgRegDate;
    }
}
