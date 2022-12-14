package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public  class CreateWalletResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;
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

    public static class Data {
        @SerializedName("walletCreateId")
        private String walletCreateId;
        @SerializedName("aptCard_Id")
        private String aptCard_Id;
        @SerializedName("__v")
        private int V;
        @SerializedName("updatedAt")
        private String updatedat;
        @SerializedName("createdAt")
        private String createdat;
        @SerializedName("kycDone")
        private boolean kycdone;
        @SerializedName("isDeleted")
        private boolean isdeleted;
        @SerializedName("isActive")
        private boolean isactive;
        @SerializedName("salt")
        private String salt;
        @SerializedName("countryId")
        private String countryid;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("password")
        private String password;
        @SerializedName("email")
        private String email;
        @SerializedName("_id")
        private String Id;
        @SerializedName("role")
        private String role;

        public String getWalletCreateId() {
            return walletCreateId;
        }

        public void setWalletCreateId(String walletCreateId) {
            this.walletCreateId = walletCreateId;
        }

        public String getAptCard_Id() {
            return aptCard_Id;
        }

        public void setAptCard_Id(String aptCard_Id) {
            this.aptCard_Id = aptCard_Id;
        }

        public int getV() {
            return V;
        }

        public void setV(int v) {
            V = v;
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

        public boolean isKycdone() {
            return kycdone;
        }

        public void setKycdone(boolean kycdone) {
            this.kycdone = kycdone;
        }

        public boolean isIsdeleted() {
            return isdeleted;
        }

        public void setIsdeleted(boolean isdeleted) {
            this.isdeleted = isdeleted;
        }

        public boolean isIsactive() {
            return isactive;
        }

        public void setIsactive(boolean isactive) {
            this.isactive = isactive;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getCountryid() {
            return countryid;
        }

        public void setCountryid(String countryid) {
            this.countryid = countryid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
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

        public void setId(String id) {
            Id = id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
