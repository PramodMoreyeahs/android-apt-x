package com.apt_x.app.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class PostCreateTransactionBody implements Serializable {


    @SerializedName("transaction")
    private TransactionEntity transaction;
    @SerializedName("receiver")
    private ReceiverEntity receiver;

    @SerializedName("data")
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public ReceiverEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverEntity receiver) {
        this.receiver = receiver;
    }


    public static class TransactionEntity implements Serializable {
        @SerializedName("sourceOfFunds")
        private String sourceoffunds;
        @SerializedName("bankId")
        private String bankid;
        @SerializedName("purpose")
        private String purpose;
        @SerializedName("accountType")
        private String accounttype;
        @SerializedName("branch")
        private String branch;
        @SerializedName("account")
        private String account;
        @SerializedName("receiveCurrency")
        private String receivecurrency;
        @SerializedName("sourceCurrency")
        private String sourcecurrency;
        @SerializedName("paymentMode")
        private String paymentmode;
        @SerializedName("amount")
        private float amount;



       /*  @SerializedName("bought")
        private String bought;
         @SerializedName("sold")
        private String sold;
         @SerializedName("rate")
        private String rate;
         @SerializedName("transfer_fee")
        private String transfer_fee;
         @SerializedName("countryName")
        private String countryName;
         @SerializedName("bankName")
        private String bankName;*/

     /*   public void setSold(String sold) {
            this.sold = sold;
        }

        public String getSold() {
            return sold;
        }

        public String getBought() {
            return bought;
        }

        public void setBought(String bought) {
            this.bought = bought;
        }



        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getTransfer_fee() {
            return transfer_fee;
        }

        public void setTransfer_fee(String transfer_fee) {
            this.transfer_fee = transfer_fee;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }*/

        public String getBankid() {
            return bankid;
        }

        public void setBankid(String bankid) {
            this.bankid = bankid;
        }


        public String getSourceoffunds() {
            return sourceoffunds;
        }

        public void setSourceoffunds(String sourceoffunds) {
            this.sourceoffunds = sourceoffunds;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getAccounttype() {
            return accounttype;
        }

        public void setAccounttype(String accounttype) {
            this.accounttype = accounttype;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getReceivecurrency() {
            return receivecurrency;
        }

        public void setReceivecurrency(String receivecurrency) {
            this.receivecurrency = receivecurrency;
        }

        public String getSourcecurrency() {
            return sourcecurrency;
        }

        public void setSourcecurrency(String sourcecurrency) {
            this.sourcecurrency = sourcecurrency;
        }

        public String getPaymentmode() {
            return paymentmode;
        }

        public void setPaymentmode(String paymentmode) {
            this.paymentmode = paymentmode;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }


    }

    public static  class Data implements Serializable{
        @SerializedName("bought")
        private String bought;
        @SerializedName("sold")
        private String sold;
        @SerializedName("rate")
        private String rate;
        @SerializedName("transferFee")
        private String transfer_fee;
        @SerializedName("countryName")
        private String countryName;
        @SerializedName("bankName")
        private String bankName;


        public String getBought() {
            return bought;
        }

        public void setBought(String bought) {
            this.bought = bought;
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

        public String getTransfer_fee() {
            return transfer_fee;
        }

        public void setTransfer_fee(String transfer_fee) {
            this.transfer_fee = transfer_fee;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
    }

    public static class ReceiverEntity implements Serializable {
        @SerializedName("payeeId")
        private String payeeid;

        public String getPayeeid() {
            return payeeid;
        }

        public void setPayeeid(String payeeid) {
            this.payeeid = payeeid;
        }


    }
}
