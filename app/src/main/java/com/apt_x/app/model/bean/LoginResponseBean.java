package com.apt_x.app.model.bean;

import com.google.gson.annotations.SerializedName;


public class LoginResponseBean {

    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        @SerializedName("__v")
        private int __v;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("countryId")
        private String countryId;

        public Boolean getKycDone() {
            return kycDone;
        }

        public void setKycDone(Boolean kycDone) {
            this.kycDone = kycDone;
        }

        @SerializedName("kycDone")
        private Boolean kycDone;
        @SerializedName("email")
        private String email;
        @SerializedName("name")
        private String name;
        @SerializedName("_id")
        private String _id;
        @SerializedName("role")
        private String role;
        private String aptCard_Id;

        @SerializedName("profilePicture")
        private String profilePicture;
        @SerializedName("walletCreateId")
        private String walletCreateId;

        @SerializedName("first_name")
        private String first_name;

        @SerializedName("last_name")
        private String lastname;

        public String getFirst_name() {
            return first_name;
        }

        public String getLastname() {
            return lastname;
        }

        public String getWalletCreateId() {
            return walletCreateId;
        }

        public void setWalletCreateId(String walletCreateId) {
            this.walletCreateId = walletCreateId;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getAptCard_Id() {
            return aptCard_Id;
        }

        public void setAptCard_Id(String aptCard_Id) {
            this.aptCard_Id = aptCard_Id;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

}
