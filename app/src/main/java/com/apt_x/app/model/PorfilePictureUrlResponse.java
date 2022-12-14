package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public class PorfilePictureUrlResponse {


    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("Data")
    private DataEntity data;
    @SerializedName("ErrorMessage")
    private String errormessage;
    @SerializedName("Status")
    private String status;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataEntity {
        @SerializedName("data")
        private DataEntity1 data;
        @SerializedName("Flag")
        private boolean flag;
        @SerializedName("Message")
        private String message;

        public DataEntity1 getData() {
            return data;
        }

        public void setData(DataEntity1 data) {
            this.data = data;
        }

        public boolean getFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

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
