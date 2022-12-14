package com.apt_x.app.views.activity.newTransactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.AddNewRecipientREsposne;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.BankDetailOfUserRequest;
import com.apt_x.app.model.BankDetailOfUserResponse;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.model.CreateTransactionResponse;
import com.apt_x.app.model.GetPurpose;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.model.TransactionEmailBody;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class SenderViewModel extends ViewModel {
   public final MutableLiveData<AddCountryResponse> response_validator=new MutableLiveData<>();

   public final MutableLiveData<GetUserByEmail> response_user_validator=new MutableLiveData<>();
    public final MutableLiveData<CountriesResponse> response_countries = new MutableLiveData<>();
    public final MutableLiveData<GetPurpose> response_purpose = new MutableLiveData<>();
    public final MutableLiveData<CreateTransactionResponse> response_create_transation = new MutableLiveData<>();
    public final MutableLiveData<BankDetailOfUserResponse> response_bank_detail = new MutableLiveData<>();
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AddNewRecipientREsposne> response_validator_recipient = new MutableLiveData<>();
    public final MutableLiveData<JsonObject> response_validator_delete_user = new MutableLiveData<>();
    public final MutableLiveData<JsonObject> response_validator_send_email = new MutableLiveData<>();
    public final MutableLiveData<JsonObject> response_validator_add_wallet = new MutableLiveData<>();
    public final MutableLiveData<JsonObject> response_validator_iban = new MutableLiveData<>();
    public final MutableLiveData<JsonObject> response_validator_load_fund = new MutableLiveData<>();

    public void getuser(ApiCalls apiCalls,String input)
    {
        apiCalls.getUserById(user_response,input);
    }
    public void getActiveCountry(ApiCalls apiCalls) {
        apiCalls.getActiveCountries(getActiveCountryDes);
    }
    public void deleteUser(ApiCalls apiCalls,String id) {
        apiCalls.deleteUser(delet_user_Des,id);
    }

    public void sendEmail(ApiCalls apiCalls, TransactionEmailBody body) {
        apiCalls.sendEmail(send_email_Des,body);
    }
    public void validatedIban(ApiCalls apiCalls, String ibna) {
        apiCalls.validateIban(iban_Des,ibna);
    }



    public void createTransaction(ApiCalls apiCalls, PostCreateTransactionBody postCreateTransactionBody)
    {
        apiCalls.createTransaction(postCreateTransactionBody,transactionDes);
    }

    public void addbank_detail(ApiCalls apiCalls, BankDetailOfUserRequest bankDetailOfUserRequest)
    {
        apiCalls.addUserBankDetail(bankDetailOfUserRequest,bank_detail_Des);
    }

    public void addRecipientSignUps(HashMap<String, String> map, ApiCalls apiCalls) {
        apiCalls.add_recipients(map, calladd_recipientDes);
    }

    public void addRecipientSignUp(AddRecipientRequest addRecipientRequest, ApiCalls apiCalls) {
        apiCalls.add_recipient(addRecipientRequest, calladd_recipientDes);
    }

    public void addCountry(ApiCalls apiCalls, String code)
    {
        apiCalls.addCountries(addcoountry_response,code);
    }



    public void getPurpose(ApiCalls apiCalls,String code) {
        apiCalls.getPurpose(getPurposeDes,code);
    }
    public void addWalletBalance(ApiCalls apiCalls, String payeeId,String amount)
    {
        apiCalls.addWalletFund(add_fund_Des,payeeId,amount);
    }

    public void loadFundEmail(ApiCalls apiCalls, String subject,String content)
    {
        apiCalls.loadFundEmail(load_fund_email_Des,subject,content);
    }



    DisposableObserver<CountriesResponse> getActiveCountryDes = new DisposableObserver<CountriesResponse>() {

        @Override
        public void onNext(@NonNull CountriesResponse countriesResponse) {
            Utils.hideProgressDialog();
            response_countries.setValue(countriesResponse);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<JsonObject> delet_user_Des = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            Utils.hideProgressDialog();
            response_validator_delete_user.setValue(jsonObject);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<JsonObject> send_email_Des = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            Utils.hideProgressDialog();
            response_validator_send_email.setValue(jsonObject);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<JsonObject> iban_Des = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            Utils.hideProgressDialog();
            response_validator_iban.setValue(jsonObject);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<JsonObject> add_fund_Des = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            Utils.hideProgressDialog();
            response_validator_add_wallet.setValue(jsonObject);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<JsonObject> load_fund_email_Des = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            Utils.hideProgressDialog();
            response_validator_load_fund.setValue(jsonObject);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<GetPurpose> getPurposeDes = new DisposableObserver<GetPurpose>() {

        @Override
        public void onNext(@NonNull GetPurpose getPurpose) {

            response_purpose.setValue(getPurpose);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);

            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<CreateTransactionResponse> transactionDes = new DisposableObserver<CreateTransactionResponse>() {

        @Override
        public void onNext(@NonNull CreateTransactionResponse getPurpose) {
            Utils.hideProgressDialog();
            response_create_transation.setValue(getPurpose);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<BankDetailOfUserResponse> bank_detail_Des = new DisposableObserver<BankDetailOfUserResponse>() {

        @Override
        public void onNext(@NonNull BankDetailOfUserResponse bankDetailOfUserResponse) {
            Utils.hideProgressDialog();
            response_bank_detail.setValue(bankDetailOfUserResponse);
        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<AddCountryResponse> addcoountry_response = new DisposableObserver<AddCountryResponse>() {

        @Override
        public void onNext(AddCountryResponse verifyOtpResponse) {
            Utils.hideProgressDialog();
            response_validator.setValue(verifyOtpResponse);
        }

        @Override
        public void onError(Throwable e) {
            try {
                if (Objects.requireNonNull(e.getMessage()).contains("400")) {

                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<GetUserByEmail> user_response = new DisposableObserver<GetUserByEmail>() {

        @Override
        public void onNext(GetUserByEmail getUserByEmail) {
            Utils.hideProgressDialog();
            response_user_validator.setValue(getUserByEmail);
        }

        @Override
        public void onError(Throwable e) {
            try {
                if (Objects.requireNonNull(e.getMessage()).contains("400")) {

                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<AddNewRecipientREsposne> calladd_recipientDes = new DisposableObserver<AddNewRecipientREsposne>() {

        @Override
        public void onNext(AddNewRecipientREsposne signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_recipient.setValue(signUpResponseBean);
        }

        @Override
        public void onError(Throwable e) {
            try {
                if (Objects.requireNonNull(e.getMessage()).contains("400")) {

                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
}
