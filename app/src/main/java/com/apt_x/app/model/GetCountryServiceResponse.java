package com.apt_x.app.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class GetCountryServiceResponse implements Serializable {

    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("status")
    private boolean status;

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

        @SerializedName("mobileWallet")
        private boolean mobilewallet;
        @SerializedName("cashPickup")
        private boolean cashpickup;
        @SerializedName("bankDeposit")
        private boolean bankdeposit;
        @SerializedName("isoNumeric")
        private int isonumeric;
        @SerializedName("isoAlpha3")
        private String isoalpha3;
        @SerializedName("isoAlpha2")
        private String isoalpha2;
        @SerializedName("currency")
        private CurrencyEntity currency;
        @SerializedName("dial_code")
        private String dialCode;
        @SerializedName("code")
        private String code;
        @SerializedName("flag")
        private String flag;
        @SerializedName("name")
        private String name;

        public boolean getMobilewallet() {
            return mobilewallet;
        }

        public void setMobilewallet(boolean mobilewallet) {
            this.mobilewallet = mobilewallet;
        }

        public boolean getCashpickup() {
            return cashpickup;
        }

        public void setCashpickup(boolean cashpickup) {
            this.cashpickup = cashpickup;
        }

        public boolean getBankdeposit() {
            return bankdeposit;
        }

        public void setBankdeposit(boolean bankdeposit) {
            this.bankdeposit = bankdeposit;
        }

        public int getIsonumeric() {
            return isonumeric;
        }

        public void setIsonumeric(int isonumeric) {
            this.isonumeric = isonumeric;
        }

        public String getIsoalpha3() {
            return isoalpha3;
        }

        public void setIsoalpha3(String isoalpha3) {
            this.isoalpha3 = isoalpha3;
        }

        public String getIsoalpha2() {
            return isoalpha2;
        }

        public void setIsoalpha2(String isoalpha2) {
            this.isoalpha2 = isoalpha2;
        }

        public CurrencyEntity getCurrency() {
            return currency;
        }

        public void setCurrency(CurrencyEntity currency) {
            this.currency = currency;
        }

        public String getDialCode() {
            return dialCode;
        }

        public void setDialCode(String dialCode) {
            this.dialCode = dialCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class CurrencyEntity implements Serializable {
            @SerializedName("symbol")
            private String symbol;
            @SerializedName("name")
            private String name;
            @SerializedName("code")
            private String code;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }
}


