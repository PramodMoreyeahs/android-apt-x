package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public class KYCResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private KYCResponse.Data data;
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KYCResponse.Data getData() {
        return data;
    }

    public void setData(KYCResponse.Data data) {
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
        @SerializedName("identificationLocation")
        private String identificationLocation;
        @SerializedName("identificationDateOfExpiration")
        private String identificationDateOfExpiration;
        @SerializedName("identificationDate")
        private String identificationDate;
        @SerializedName("identificationNumber")
        private String identificationNumber;
        @SerializedName("identificationType")
        private String identificationType;
        @SerializedName("userId")
        private String userId;
        @SerializedName("_id")
        private String _id;
        @SerializedName("virtual")
        private boolean virtual;

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

        public String getIdentificationLocation() {
            return identificationLocation;
        }

        public void setIdentificationLocation(String identificationLocation) {
            this.identificationLocation = identificationLocation;
        }

        public String getIdentificationDateOfExpiration() {
            return identificationDateOfExpiration;
        }

        public void setIdentificationDateOfExpiration(String identificationDateOfExpiration) {
            this.identificationDateOfExpiration = identificationDateOfExpiration;
        }

        public String getIdentificationDate() {
            return identificationDate;
        }

        public void setIdentificationDate(String identificationDate) {
            this.identificationDate = identificationDate;
        }

        public String getIdentificationNumber() {
            return identificationNumber;
        }

        public void setIdentificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
        }

        public String getIdentificationType() {
            return identificationType;
        }

        public void setIdentificationType(String identificationType) {
            this.identificationType = identificationType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public boolean getVirtual() {
            return virtual;
        }

        public void setVirtual(boolean virtual) {
            this.virtual = virtual;
        }
    }
}
