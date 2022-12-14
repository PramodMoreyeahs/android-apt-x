package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class AddActiveCounteries {

    @Expose
    @SerializedName("flagUrl")
    private String flagurl;
    @Expose
    @SerializedName("countryCode")
    private String countrycode;
    @Expose
    @SerializedName("isDelete")
    private boolean isdelete;
    @Expose
    @SerializedName("isActive")
    private boolean isactive;
    @Expose
    @SerializedName("name")
    private String name;

    public String getFlagurl() {
        return flagurl;
    }

    public void setFlagurl(String flagurl) {
        this.flagurl = flagurl;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public boolean getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    public boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
