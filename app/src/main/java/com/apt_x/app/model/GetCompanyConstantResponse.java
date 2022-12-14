package com.apt_x.app.model;

import com.google.gson.annotations.SerializedName;

public class GetCompanyConstantResponse {
    @SerializedName("add_kyc")
    private String add_kyc;
    @SerializedName("LIVE_API_Endpoint")
    private String LIVE_API_Endpoint;
    @SerializedName("APTPAY_Sandbox_API")
    private String APTPAY_Sandbox_API;
    @SerializedName("API_Key")
    private String API_Key;
    private String aptPayEmailofrPayment;

    public String getAptPayEmailofrPayment() {
        return aptPayEmailofrPayment;
    }

    public void setAptPayEmailofrPayment(String aptPayEmailofrPayment) {
        this.aptPayEmailofrPayment = aptPayEmailofrPayment;
    }

    @SerializedName("status")
    private boolean status;

    public String getAdd_kyc() {
        return add_kyc;
    }

    public void setAdd_kyc(String add_kyc) {
        this.add_kyc = add_kyc;
    }

    public String getLIVE_API_Endpoint() {
        return LIVE_API_Endpoint;
    }

    public void setLIVE_API_Endpoint(String LIVE_API_Endpoint) {
        this.LIVE_API_Endpoint = LIVE_API_Endpoint;
    }

    public String getAPTPAY_Sandbox_API() {
        return APTPAY_Sandbox_API;
    }

    public void setAPTPAY_Sandbox_API(String APTPAY_Sandbox_API) {
        this.APTPAY_Sandbox_API = APTPAY_Sandbox_API;
    }

    public String getAPI_Key() {
        return API_Key;
    }

    public void setAPI_Key(String API_Key) {
        this.API_Key = API_Key;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
