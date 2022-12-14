package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class CountriesResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Data> data;
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Data {
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("flagUrl")
        private String flagUrl;
        @SerializedName("countryCode")
        private String countryCode;
        @SerializedName("isDelete")
        private boolean isDelete;
        @SerializedName("isActive")
        private boolean isActive;
        @SerializedName("name")
        private String name;
        @SerializedName("_id")
        private String _id;

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

        public String getFlagUrl() {
            return flagUrl;
        }

        public void setFlagUrl(String flagUrl) {
            this.flagUrl = flagUrl;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public boolean getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(boolean isDelete) {
            this.isDelete = isDelete;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
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
    }
}
