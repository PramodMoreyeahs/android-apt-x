package com.apt_x.app.model;     //Home Frag line 308, 313   Tran Frag 247

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CrossBorderHistoryResponse implements Serializable {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private Data data;

    @Expose
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Data implements Serializable {
        @Expose
        @SerializedName("transaction")
        private List<DataEntity> transaction;

        public List<DataEntity> getTransaction() {
            return transaction;
        }

        public void setTransaction(List<DataEntity> transaction) {
            this.transaction = transaction;
        }

    }

    public static class DataEntity implements Serializable {
        @Expose
        @SerializedName("transaction_id")
        private String transactionId;
        @Expose
        @SerializedName("date")
        private String date;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("amount")
        private String amount;
        @Expose
        @SerializedName("country_flag")
        private String country_flag;

        @Expose
        @SerializedName("bought")
        private String bought;
        @Expose
        @SerializedName("sold")
        private String sold;
        @Expose
        @SerializedName("rate")
        private String rate;
        @Expose
        @SerializedName("transferFee")
        private String transferFee;
        @Expose
        @SerializedName("countryName")
        private String countryName;
        @Expose
        @SerializedName("bankName")
        private String bankName;
        @Expose
        @SerializedName("transactionStatus")
        private String transactionStatus;


        public String getTransactionStatus() {
            return transactionStatus;
        }

        public void setCountry_flag(String country_flag) {
            this.country_flag = country_flag;
        }

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

        public String getTransferFee() {
            return transferFee;
        }

        public void setTransferFee(String transferFee) {
            this.transferFee = transferFee;
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

        public String getCountry_flag() {
            return country_flag;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
