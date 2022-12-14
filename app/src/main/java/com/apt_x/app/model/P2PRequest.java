package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class P2PRequest {


    @Expose
    @SerializedName("first_name")
    private String firstName;
    @Expose
    @SerializedName("emailId")
    private String emailid;
    @Expose
    @SerializedName("walletId")
    private String walletid;

    @Expose
    @SerializedName("payee_id")
    private String payee_id;
    public P2PRequest( String firstName, String emailid, String walletid,String payee_id) {
        this.firstName = firstName;
        this.emailid = emailid;
        this.walletid = walletid;
        this.payee_id = payee_id;
    }

    public String getPayee_id() {
        return payee_id;
    }

    public void setPayee_id(String payee_id) {
        this.payee_id = payee_id;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }
}
