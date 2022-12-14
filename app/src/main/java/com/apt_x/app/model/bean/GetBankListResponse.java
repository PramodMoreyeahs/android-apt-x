package com.apt_x.app.model.bean;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class GetBankListResponse implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class DataEntity implements Serializable {
        @SerializedName("type")
        private int type;
        @SerializedName("bankNumber")
        private String banknumber;
        @SerializedName("expirationDate")
        private String expirationdate;
        @SerializedName("disbursementNumber")
        private String disbursementnumber;
        @SerializedName("id")
        private String id;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getBanknumber() {
            return banknumber;
        }

        public void setBanknumber(String banknumber) {
            this.banknumber = banknumber;
        }

        public String getExpirationdate() {
            return expirationdate;
        }

        public void setExpirationdate(String expirationdate) {
            this.expirationdate = expirationdate;
        }

        public String getDisbursementnumber() {
            return disbursementnumber;
        }

        public void setDisbursementnumber(String disbursementnumber) {
            this.disbursementnumber = disbursementnumber;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}


