package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class BankDetailOfUserRequest {

    @Expose
    @SerializedName("countryCode")
    private String countrycode;
    @Expose
    @SerializedName("branch")
    private String branch;
    @Expose
    @SerializedName("Receivecurrency")
    private String receivecurrency;
    @Expose
    @SerializedName("paymentMode")
    private String paymentmode;
    @Expose
    @SerializedName("accountType")
    private String accounttype;
    @Expose
    @SerializedName("accountNo")
    private String accountno;
    @Expose
    @SerializedName("payeeId")
    private String payeeid;




    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getReceivecurrency() {
        return receivecurrency;
    }

    public void setReceivecurrency(String receivecurrency) {
        this.receivecurrency = receivecurrency;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getPayeeid() {
        return payeeid;
    }

    public void setPayeeid(String payeeid) {
        this.payeeid = payeeid;
    }


}
