package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public  class AddBankDisburesmentRequest {


    @SerializedName("program")
    private int program;
    @SerializedName("instrumentId")
    private String instrumentid;
    @SerializedName("accountNumber")
    private String accountnumber;
    @SerializedName("branchTransitNumber")
    private String branchtransitnumber;
    @SerializedName("bankNumber")
    private String banknumber;
    @SerializedName("referenceId")
    private String referenceid;
    @SerializedName("currency")
    private String currency;
    @SerializedName("payeeId")
    private String payeeid;
    @SerializedName("transactionType")
    private String transactiontype;
    @SerializedName("amount")
    private int amount;

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    public String getInstrumentid() {
        return instrumentid;
    }

    public void setInstrumentid(String instrumentid) {
        this.instrumentid = instrumentid;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getBranchtransitnumber() {
        return branchtransitnumber;
    }

    public void setBranchtransitnumber(String branchtransitnumber) {
        this.branchtransitnumber = branchtransitnumber;
    }

    public String getBanknumber() {
        return banknumber;
    }

    public void setBanknumber(String banknumber) {
        this.banknumber = banknumber;
    }

    public String getReferenceid() {
        return referenceid;
    }

    public void setReferenceid(String referenceid) {
        this.referenceid = referenceid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayeeid() {
        return payeeid;
    }

    public void setPayeeid(String payeeid) {
        this.payeeid = payeeid;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

