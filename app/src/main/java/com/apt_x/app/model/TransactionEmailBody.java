package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class TransactionEmailBody {

    @Expose
    @SerializedName("receivingCountry")
    private String receivingcountry;

    private String recipientBank;
    @Expose
    @SerializedName("transactionNumber")
    private String transactionnumber;
    @Expose
    @SerializedName("amount")
    private String amount;
    @Expose
    @SerializedName("currency")
    private String currency;
    @Expose
    @SerializedName("recipientName")
    private String recipientname;
    @Expose
    @SerializedName("costDeducted")
    private String costdeducted;
    @Expose
    @SerializedName("transferFees")
    private String transferfees;
    @Expose
    @SerializedName("sold")
    private String sold;
    @Expose
    @SerializedName("rate")
    private String rate;
    @Expose
    @SerializedName("bought")
    private String bought;
    @Expose
    @SerializedName("valueDate")
    private String valuedate;

    public String getRecipientBank() {
        return recipientBank;
    }

    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    public String getReceivingcountry() {
        return receivingcountry;
    }

    public void setReceivingcountry(String receivingcountry) {
        this.receivingcountry = receivingcountry;
    }



    public String getTransactionnumber() {
        return transactionnumber;
    }

    public void setTransactionnumber(String transactionnumber) {
        this.transactionnumber = transactionnumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRecipientname() {
        return recipientname;
    }

    public void setRecipientname(String recipientname) {
        this.recipientname = recipientname;
    }

    public String getCostdeducted() {
        return costdeducted;
    }

    public void setCostdeducted(String costdeducted) {
        this.costdeducted = costdeducted;
    }

    public String getTransferfees() {
        return transferfees;
    }

    public void setTransferfees(String transferfees) {
        this.transferfees = transferfees;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBought() {
        return bought;
    }

    public void setBought(String bought) {
        this.bought = bought;
    }

    public String getValuedate() {
        return valuedate;
    }

    public void setValuedate(String valuedate) {
        this.valuedate = valuedate;
    }
}
