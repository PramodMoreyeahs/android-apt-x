package com.apt_x.app.model;

public class GetIdentityResponse {
    private String status;
    private String message;
    private GetData data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetData getData() {
        return data;
    }

    public void setData(GetData data) {
        this.data = data;
    }

    public class GetData{

        private String id;
        private String walletId;
        private String first_name;
        private String last_name;

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWalletId() {
            return walletId;
        }

        public void setWalletId(String walletId) {
            this.walletId = walletId;
        }
    }

}
