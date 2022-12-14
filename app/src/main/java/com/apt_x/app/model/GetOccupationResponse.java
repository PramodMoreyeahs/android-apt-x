package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class GetOccupationResponse {

    @Expose
    @SerializedName("message")
    private String message;
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
        @SerializedName("occupation2")
        private List<String> occupation2;
        @Expose
        @SerializedName("occupation1")
        private List<String> occupation1;

        public List<String> getOccupation2() {
            return occupation2;
        }

        public void setOccupation2(List<String> occupation2) {
            this.occupation2 = occupation2;
        }

        public List<String> getOccupation1() {
            return occupation1;
        }

        public void setOccupation1(List<String> occupation1) {
            this.occupation1 = occupation1;
        }
    }
}
