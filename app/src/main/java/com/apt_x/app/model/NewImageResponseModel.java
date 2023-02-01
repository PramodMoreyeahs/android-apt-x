package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public class NewImageResponseModel {


    @SerializedName("data")
    private DataEntity data;
    @SerializedName("message")
    private String errormessage;

    @SerializedName("status")
    private Boolean status;


    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class DataEntity {

        @SerializedName("Location")
        private String message;



        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class DataEntity1 {
        @SerializedName("filepath")
        private String filepath;

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }
    }
}
