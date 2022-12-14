package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public  class PostChangePasswordBody {


    @SerializedName("cNewPassword")
    private String cNewPassword;
    @SerializedName("newPassword")
    private String newPassword;
    @SerializedName("email")
    private String email;

    public PostChangePasswordBody(String cNewPassword, String newPassword, String email) {
        this.cNewPassword = cNewPassword;
        this.newPassword = newPassword;
        this.email = email;
    }

    public String getCNewPassword() {
        return cNewPassword;
    }

    public void setCNewPassword(String cNewPassword) {
        this.cNewPassword = cNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
