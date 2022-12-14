package com.apt_x.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTransactionHistoryResponse {


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
        @SerializedName("walletCreateId")
        private String walletcreateid;
        @Expose
        @SerializedName("_id")
        private String Id;
        @Expose
        @SerializedName("WalletTransactions")
        private List<WalletTransactionsEntity> wallettransactions;

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

        public String getWalletcreateid() {
            return walletcreateid;
        }

        public void setWalletcreateid(String walletcreateid) {
            this.walletcreateid = walletcreateid;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public List<WalletTransactionsEntity> getWallettransactions() {
            return wallettransactions;
        }

        public void setWallettransactions(List<WalletTransactionsEntity> wallettransactions) {
            this.wallettransactions = wallettransactions;
        }
    }

    public class WalletTransactionsEntity {
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("payeeId")
        private String payeeId;
        @Expose
        @SerializedName("payeeName")
        private String payeeName;
        @Expose
        @SerializedName("cardNumber")
        private String cardNumber;
        @Expose
        @SerializedName("cardID")
        private String cardID;

        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("merchant")
        private String merchant;


        @Expose
        @SerializedName("amount")
        private String amount;
        @Expose
        @SerializedName("date")
        private String date;

        public String getPayeeId() {
            return payeeId;
        }

        public void setPayeeId(String payeeId) {
            this.payeeId = payeeId;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardID() {
            return cardID;
        }

        public void setCardID(String cardID) {
            this.cardID = cardID;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMerchant() {
            return merchant;
        }

        public void setMerchant(String merchant) {
            this.merchant = merchant;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
