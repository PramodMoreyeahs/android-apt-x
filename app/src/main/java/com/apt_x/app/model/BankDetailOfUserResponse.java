package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class BankDetailOfUserResponse {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("token")
    private String token;
    @Expose
    @SerializedName("data")
    private DataEntity data;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
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
        @SerializedName("__v")
        private int V;
        @Expose
        @SerializedName("updatedAt")
        private String updatedat;
        @Expose
        @SerializedName("createdAt")
        private String createdat;
        @Expose
        @SerializedName("userid")
        private String userid;
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
        @Expose
        @SerializedName("email")
        private String email;
        @Expose
        @SerializedName("_id")
        private String Id;
        @Expose
        @SerializedName("role")
        private String role;

        public int getV() {
            return V;
        }

        public void setV(int V) {
            this.V = V;
        }

        public String getUpdatedat() {
            return updatedat;
        }

        public void setUpdatedat(String updatedat) {
            this.updatedat = updatedat;
        }

        public String getCreatedat() {
            return createdat;
        }

        public void setCreatedat(String createdat) {
            this.createdat = createdat;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
