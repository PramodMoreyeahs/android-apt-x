package com.apt_x.app.model;

import java.io.Serializable;

 public class GetExistingUserResponse implements Serializable {
    private Data data;

    private String message;

    private Boolean status;

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class Data implements Serializable {

            private String role;

            private String city;

            private String street_line_2;

            private Boolean isActive;

            private String branch;

            private String userid;

            private String countryId;

            private String createdAt;

            private String password;

            private Boolean isDeleted;

            private String countryCode;

            private String street;

            private String accountNo;

            private Integer __v;

            private String state;

            private Long addedBy_aptCard_Id;

            private String first_name;

            private String email;
              private String email_id;


            private Boolean kycDone;

            private String updatedAt;

            private String zip;

            private String salt;

            private String paymentMode;

            private String accountType;

            private String mobile;

            private String last_name;

            private String Receivecurrency;

            private String aptCard_Id;

            private String _id;

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getRole() {
                return this.role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getCity() {
                return this.city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getStreet_line_2() {
                return this.street_line_2;
            }

            public void setStreet_line_2(String street_line_2) {
                this.street_line_2 = street_line_2;
            }

            public Boolean getIsActive() {
                return this.isActive;
            }

            public void setIsActive(Boolean isActive) {
                this.isActive = isActive;
            }

            public String getBranch() {
                return this.branch;
            }

            public void setBranch(String branch) {
                this.branch = branch;
            }

            public String getUserid() {
                return this.userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getCountryId() {
                return this.countryId;
            }

            public void setCountryId(String countryId) {
                this.countryId = countryId;
            }

            public String getCreatedAt() {
                return this.createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getPassword() {
                return this.password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public Boolean getIsDeleted() {
                return this.isDeleted;
            }

            public void setIsDeleted(Boolean isDeleted) {
                this.isDeleted = isDeleted;
            }

            public String getCountryCode() {
                return this.countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public String getStreet() {
                return this.street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getAccountNo() {
                return this.accountNo;
            }

            public void setAccountNo(String accountNo) {
                this.accountNo = accountNo;
            }

            public Integer get__v() {
                return this.__v;
            }

            public void set__v(Integer __v) {
                this.__v = __v;
            }

            public String getState() {
                return this.state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public Long getAddedBy_aptCard_Id() {
                return this.addedBy_aptCard_Id;
            }

            public void setAddedBy_aptCard_Id(Long addedBy_aptCard_Id) {
                this.addedBy_aptCard_Id = addedBy_aptCard_Id;
            }

            public String getFirst_name() {
                return this.first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getEmail() {
                return this.email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public Boolean getKycDone() {
                return this.kycDone;
            }

            public void setKycDone(Boolean kycDone) {
                this.kycDone = kycDone;
            }

            public String getUpdatedAt() {
                return this.updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getZip() {
                return this.zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getSalt() {
                return this.salt;
            }

            public void setSalt(String salt) {
                this.salt = salt;
            }

            public String getPaymentMode() {
                return this.paymentMode;
            }

            public void setPaymentMode(String paymentMode) {
                this.paymentMode = paymentMode;
            }

            public String getAccountType() {
                return this.accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public String getMobile() {
                return this.mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getLast_name() {
                return this.last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public String getReceivecurrency() {
                return this.Receivecurrency;
            }

            public void setReceivecurrency(String Receivecurrency) {
                this.Receivecurrency = Receivecurrency;
            }

            public String getAptCard_Id() {
                return this.aptCard_Id;
            }

            public void setAptCard_Id(String aptCard_Id) {
                this.aptCard_Id = aptCard_Id;
            }

            public String get_id() {
                return this._id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }
    }

