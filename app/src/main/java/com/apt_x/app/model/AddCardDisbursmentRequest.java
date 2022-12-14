package com.apt_x.app.model;

import java.io.Serializable;

public class AddCardDisbursmentRequest implements Serializable {

    private double amount;
    private String transactionType;
    private String disbursementNumber;
    private String payeeId;
    private String currency;
    private String expirationDate;
    private String referenceId;
    private String instrumentId;

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDisbursementNumber() {
        return disbursementNumber;
    }


    public void setDisbursementNumber(String disbursementNumber) {
        this.disbursementNumber = disbursementNumber;
    }



    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
