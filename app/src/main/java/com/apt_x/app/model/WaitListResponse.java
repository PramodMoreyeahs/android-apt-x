package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public  class WaitListResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
