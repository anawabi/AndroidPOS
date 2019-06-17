package com.xamuor.cashco.pos;

public class CategoryDataModal {
    private String ctgName;
    private String ctgDesc;

    public CategoryDataModal(String ctgName, String ctgDesc) {
        this.ctgName = ctgName;
        this.ctgDesc = ctgDesc;
    }

    public String getCtgName() {
        return ctgName;
    }

    public String getCtgDesc() {
        return ctgDesc;
    }

}
