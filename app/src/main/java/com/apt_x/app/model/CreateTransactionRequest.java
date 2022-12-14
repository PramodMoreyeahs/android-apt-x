package com.apt_x.app.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTransactionRequest {
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    public class Receiver {

        @SerializedName("payeeId")
        @Expose
        private Long payeeId;

        public Long getPayeeId() {
            return payeeId;
        }

        public void setPayeeId(Long payeeId) {
            this.payeeId = payeeId;
        }

    }
    public class Transaction {

        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("paymentMode")
        @Expose
        private String paymentMode;
        @SerializedName("sourceCurrency")
        @Expose
        private String sourceCurrency;
        @SerializedName("receiveCurrency")
        @Expose
        private String receiveCurrency;
        @SerializedName("account")
        @Expose
        private String account;
        @SerializedName("branch")
        @Expose
        private String branch;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("purpose")
        @Expose
        private String purpose;
        @SerializedName("sourceOfFunds")
        @Expose
        private String sourceOfFunds;

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getSourceCurrency() {
            return sourceCurrency;
        }

        public void setSourceCurrency(String sourceCurrency) {
            this.sourceCurrency = sourceCurrency;
        }

        public String getReceiveCurrency() {
            return receiveCurrency;
        }

        public void setReceiveCurrency(String receiveCurrency) {
            this.receiveCurrency = receiveCurrency;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getSourceOfFunds() {
            return sourceOfFunds;
        }

        public void setSourceOfFunds(String sourceOfFunds) {
            this.sourceOfFunds = sourceOfFunds;
        }

    }

}
