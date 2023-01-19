

package com.apt_x.app.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class LinkVerifyModel {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private String token;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public class User {

            @SerializedName("role")
            @Expose
            private String role;
            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("first_name")
            @Expose
            private String firstName;
            @SerializedName("last_name")
            @Expose
            private String lastName;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("countryId")
            @Expose
            private String countryId;
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
            @SerializedName("__v")
            @Expose
            private Integer v;
            @SerializedName("aptCard_Id")
            @Expose
            private Long aptCardId;
            @SerializedName("walletCreateId")
            @Expose
            private String walletCreateId;

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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
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

            public String getCountryId() {
                return countryId;
            }

            public void setCountryId(String countryId) {
                this.countryId = countryId;
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

            public String getWalletCreateId() {
                return walletCreateId;
            }

            public void setWalletCreateId(String walletCreateId) {
                this.walletCreateId = walletCreateId;
            }

        }

    }


}







