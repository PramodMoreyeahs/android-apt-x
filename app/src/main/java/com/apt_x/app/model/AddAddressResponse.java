package com.apt_x.app.model;


import com.google.gson.annotations.SerializedName;

public class AddAddressResponse {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("data")
    private Data data;
    @SerializedName("message")
    private String message;


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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class Data {

        @SerializedName("role")
            private String role;
        @SerializedName("_id")
            private String id;
        @SerializedName("name")
            private String name;
        @SerializedName("email")
            private String email;
        @SerializedName("password")
            private String password;
        @SerializedName("mobile")
            private String mobile;
        @SerializedName("countryId")
            private String countryId;
        @SerializedName("salt")
            private String salt;
        @SerializedName("createdAt")
            private String createdAt;
        @SerializedName("updatedAt")
            private String updatedAt;
        @SerializedName("__v")
            private Integer v;
        @SerializedName("city")
            private String city;
        @SerializedName("state")
            private String state;
        @SerializedName("street")
            private String street;
        @SerializedName("street_line_2")
            private String streetLine2;
        @SerializedName("zip")
            private String zip;


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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

    }
}


