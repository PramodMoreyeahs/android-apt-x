package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public  class GetProfileResponse {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {

        @SerializedName("__v")
        private int V;
        @SerializedName("updatedAt")
        private String updatedat;
        @SerializedName("createdAt")
        private String createdat;
        @SerializedName("userid")
        private String userid;
        @SerializedName("kycDone")
        private boolean kycdone;
        @SerializedName("isDeleted")
        private boolean isdeleted;
        @SerializedName("isActive")
        private boolean isactive;
        @SerializedName("countryId")
        private String countryid;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("email")
        private String email;
        @SerializedName("_id")
        private String Id;
        @SerializedName("role")
        private String role;
        @SerializedName("aptCard_Id")
        private String aptCard_Id;
        @SerializedName("walletCreateId")
        private String walletCreateId;
        @SerializedName("city")
        private String city;
        @SerializedName("country")
        private String country;
        @SerializedName("state")
        private String state;
        @SerializedName("street")
        private String street;
        @SerializedName("street_line_2")
        private String street_line_2;
        @SerializedName("zip")
        private String zip;
        @SerializedName("profilePicture")
        private String profilePicture;
  @SerializedName("dateOfBirth")
        private String dateOfBirth;

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
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

        public String getWalletCreateId() {
            return walletCreateId;
        }

        public void setWalletCreateId(String walletCreateId) {
            this.walletCreateId = walletCreateId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
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

        public String getStreet_line_2() {
            return street_line_2;
        }

        public void setStreet_line_2(String street_line_2) {
            this.street_line_2 = street_line_2;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
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

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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
