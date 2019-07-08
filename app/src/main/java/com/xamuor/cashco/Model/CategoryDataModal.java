package com.xamuor.cashco.Model;

public class CategoryDataModal {
    private int ctgId;
    private String ctgName;
    private String ctgDesc;

    public CategoryDataModal(int ctgId, String ctgName, String ctgDesc) {
        this.ctgId = ctgId;
        this.ctgName = ctgName;
        this.ctgDesc = ctgDesc;
    }

    public int getCtgId() {
        return ctgId;
    }

    public String getCtgName() {
        return ctgName;
    }

    public String getCtgDesc() {
        return ctgDesc;
    }

}
