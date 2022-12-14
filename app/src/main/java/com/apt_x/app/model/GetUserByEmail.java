package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetUserByEmail implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data implements Serializable {
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("accountNo")
        @Expose
        private String accountNo;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("branch")
        @Expose
        private String branch;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("countryCode")
        @Expose
        private String countryCode;
        @SerializedName("countryId")
        @Expose
        private String countryId;
        @SerializedName("email")
        @Expose
        private String email;

          @SerializedName("email_id")
        @Expose
        private String email_id;



        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("paymentMode")
        @Expose
        private String paymentMode;
        @SerializedName("Receivecurrency")
        @Expose
        private String receivecurrency;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("street_line_2")
        @Expose
        private String streetLine2;
        @SerializedName("zip")
        @Expose
        private String zip;
        @SerializedName("salt")
        @Expose
        private String salt;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;
        @SerializedName("kycDone")
        @Expose
        private Boolean kycDone;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
         @SerializedName("country_flag")
        @Expose
        private String country_flag;


        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("aptCard_Id")
        @Expose
        private Long aptCardId;
        @SerializedName("addedBy_aptCard_Id")
        @Expose
        private Long addedByAptCardId;

         @SerializedName("bankName")
        @Expose
        private String bankName;

        @SerializedName("bankId")
        private String bankid;

        public String getBankid() {
            return bankid;
        }

        public void setBankid(String bankid) {
            this.bankid = bankid;
        }

        private boolean ischeck=false;

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public boolean isIscheck() {
            return ischeck;
        }

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        public String getCountry_flag() {
            return country_flag;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public void setCountry_flag(String country_flag) {
            this.country_flag = country_flag;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getReceivecurrency() {
            return receivecurrency;
        }

        public void setReceivecurrency(String receivecurrency) {
            this.receivecurrency = receivecurrency;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreetLine2() {
            return streetLine2;
        }

        public void setStreetLine2(String streetLine2) {
            this.streetLine2 = streetLine2;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Boolean getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public Boolean getKycDone() {
            return kycDone;
        }

        public void setKycDone(Boolean kycDone) {
            this.kycDone = kycDone;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public Long getAptCardId() {
            return aptCardId;
        }

        public void setAptCardId(Long aptCardId) {
            this.aptCardId = aptCardId;
        }

        public Long getAddedByAptCardId() {
            return addedByAptCardId;
        }

        public void setAddedByAptCardId(Long addedByAptCardId) {
            this.addedByAptCardId = addedByAptCardId;
        }

    }
}

