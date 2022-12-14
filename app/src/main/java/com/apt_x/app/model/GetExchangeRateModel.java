package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class GetExchangeRateModel {

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
        @SerializedName("receiveCurrency")
        private String receivecurrency;
        @Expose
        @SerializedName("rateExpiryDate")
        private String rateexpirydate;
        @Expose
        @SerializedName("rateStartDate")
        private String ratestartdate;
        @Expose
        @SerializedName("receiveCountryName")
        private String receivecountryname;
        @Expose
        @SerializedName("receiveCountry")
        private String receivecountry;
        @Expose
        @SerializedName("modeOfPaymentId")
        private String modeofpaymentid;
        @Expose
        @SerializedName("payerId")
        private String payerid;
        @Expose
        @SerializedName("endRate")
        private double endrate;
        @Expose
        @SerializedName("startRate")
        private double startrate;
        @Expose
        @SerializedName("modeOfPayment")
        private String modeofpayment;
        @Expose
        @SerializedName("payerName")
        private String payername;

         @Expose
        @SerializedName("serviceFee")
        private String serviceFee;

        public String getServiceFee() {
            return serviceFee;
        }

        public String getReceivecurrency() {
            return receivecurrency;
        }

        public void setReceivecurrency(String receivecurrency) {
            this.receivecurrency = receivecurrency;
        }

        public String getRateexpirydate() {
            return rateexpirydate;
        }

        public void setRateexpirydate(String rateexpirydate) {
            this.rateexpirydate = rateexpirydate;
        }

        public String getRatestartdate() {
            return ratestartdate;
        }

        public void setRatestartdate(String ratestartdate) {
            this.ratestartdate = ratestartdate;
        }

        public String getReceivecountryname() {
            return receivecountryname;
        }

        public void setReceivecountryname(String receivecountryname) {
            this.receivecountryname = receivecountryname;
        }

        public String getReceivecountry() {
            return receivecountry;
        }

        public void setReceivecountry(String receivecountry) {
            this.receivecountry = receivecountry;
        }

        public String getModeofpaymentid() {
            return modeofpaymentid;
        }

        public void setModeofpaymentid(String modeofpaymentid) {
            this.modeofpaymentid = modeofpaymentid;
        }

        public String getPayerid() {
            return payerid;
        }

        public void setPayerid(String payerid) {
            this.payerid = payerid;
        }

        public double getEndrate() {
            return endrate;
        }

        public void setEndrate(double endrate) {
            this.endrate = endrate;
        }

        public double getStartrate() {
            return startrate;
        }

        public void setStartrate(double startrate) {
            this.startrate = startrate;
        }

        public String getModeofpayment() {
            return modeofpayment;
        }

        public void setModeofpayment(String modeofpayment) {
            this.modeofpayment = modeofpayment;
        }

        public String getPayername() {
            return payername;
        }

        public void setPayername(String payername) {
            this.payername = payername;
        }
    }
}
