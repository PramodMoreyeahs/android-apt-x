package com.apt_x.app.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class GetBankResponse {


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

    public static class DataEntity {
        @SerializedName("Name")
        private String name;
        @SerializedName("Id")
        private String id;

       public DataEntity(){}
        public DataEntity(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}


