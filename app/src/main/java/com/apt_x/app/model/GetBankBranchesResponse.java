package com.apt_x.app.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class GetBankBranchesResponse {


    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<DataEntity> data;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class DataEntity {
        @Expose
        @SerializedName("TownId")
        private String townid;
        @Expose
        @SerializedName("TownName")
        private String townname;
        @Expose
        @SerializedName("CityId")
        private String cityid;
        @Expose
        @SerializedName("CityName")
        private String cityname;
        @Expose
        @SerializedName("StateId")
        private String stateid;
        @Expose
        @SerializedName("StateName")
        private String statename;
        @Expose
        @SerializedName("CountryId")
        private String countryid;
        @Expose
        @SerializedName("CountryName")
        private String countryname;
        @Expose
        @SerializedName("BankBranchID")
        private String bankbranchid;
        @Expose
        @SerializedName("BankBranchName")
        private String bankbranchname;
        @Expose
        @SerializedName("BankID")
        private String bankid;

        public String getTownid() {
            return townid;
        }

        public void setTownid(String townid) {
            this.townid = townid;
        }

        public String getTownname() {
            return townname;
        }

        public void setTownname(String townname) {
            this.townname = townname;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getStateid() {
            return stateid;
        }

        public void setStateid(String stateid) {
            this.stateid = stateid;
        }

        public String getStatename() {
            return statename;
        }

        public void setStatename(String statename) {
            this.statename = statename;
        }

        public String getCountryid() {
            return countryid;
        }

        public void setCountryid(String countryid) {
            this.countryid = countryid;
        }

        public String getCountryname() {
            return countryname;
        }

        public void setCountryname(String countryname) {
            this.countryname = countryname;
        }

        public String getBankbranchid() {
            return bankbranchid;
        }

        public void setBankbranchid(String bankbranchid) {
            this.bankbranchid = bankbranchid;
        }

        public String getBankbranchname() {
            return bankbranchname;
        }

        public void setBankbranchname(String bankbranchname) {
            this.bankbranchname = bankbranchname;
        }

        public String getBankid() {
            return bankid;
        }

        public void setBankid(String bankid) {
            this.bankid = bankid;
        }
    }
}


