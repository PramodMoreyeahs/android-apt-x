package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class P2PResponse {

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
        @SerializedName("__v")
        private int V;
        @Expose
        @SerializedName("updatedAt")
        private String updatedat;
        @Expose
        @SerializedName("createdAt")
        private String createdat;
        @Expose
        @SerializedName("last_name")
        private String lastName;
        @Expose
        @SerializedName("first_name")
        private String firstName;
        @Expose
        @SerializedName("emailId")
        private String emailid;
        @Expose
        @SerializedName("walletId")
        private String walletid;
        @Expose
        @SerializedName("_id")
        private String Id;

        public int getV() {
            return V;
        }

        public void setV(int V) {
            this.V = V;
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

        public String getEmailid() {
            return emailid;
        }

        public void setEmailid(String emailid) {
            this.emailid = emailid;
        }

        public String getWalletid() {
            return walletid;
        }

        public void setWalletid(String walletid) {
            this.walletid = walletid;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }
    }
}
