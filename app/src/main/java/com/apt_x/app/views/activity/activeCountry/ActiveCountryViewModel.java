package com.apt_x.app.views.activity.activeCountry;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.GetBankDetailResponse;
import com.apt_x.app.model.GetCountryServiceResponse;
import com.apt_x.app.model.GetExchangeRateModel;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.google.gson.JsonObject;

import java.util.Objects;

import io.reactivex.observers.DisposableObserver;


public class ActiveCountryViewModel extends ViewModel implements DialogClickListener {
    public final MutableLiveData<GetWalletBalanceResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<GetCountryServiceResponse> response_validator_transaction = new MutableLiveData<>();
    public final MutableLiveData<GetBankDetailResponse> response_validator_bank_detail = new MutableLiveData<>();
    public final MutableLiveData<GetExchangeRateModel> response_validator_exchange = new MutableLiveData<>();
    public final MutableLiveData<VerifyOtpResponse> response_validator_otp = new MutableLiveData<>();
    public final MutableLiveData<AddCountryResponse> response_validator_add_country=new MutableLiveData<>();

    public void convertMoney(String paymentMode, String receiveCurrency,String sourceCurrency, String receiveCountry,String sentAmount, ApiCalls apiCalls) {
        apiCalls.convertMoney(paymentMode,receiveCurrency,sourceCurrency,receiveCountry,sentAmount ,convertMoneydes);
    }
    public void getActiveCountryService( ApiCalls apiCalls) {
        apiCalls.getActiveCountryService(transactionDes);

    }
    public void getBankDetail( ApiCalls apiCalls,String id) {
        apiCalls.getBankDetail(bankDetailDes,id);

    }


 public void getExchangeRate( ApiCalls apiCalls,String code) {
     JsonObject jsonObject= new JsonObject();
     jsonObject.addProperty("sourceCurrency","CAD");
     jsonObject.addProperty("receiveCountry",code);


        apiCalls.getExchangeRate(exchangeDes,jsonObject);

    }
    public void addCountry(ApiCalls apiCalls, String code)
    {
        apiCalls.addCountries(addcoountry_response,code);
    }
    public void getBankDea
            (ApiCalls apiCalls, String code)
    {
        apiCalls.addCountries(addcoountry_response,code);
    }



    DisposableObserver<VerifyOtpResponse> convertMoneydes = new DisposableObserver<VerifyOtpResponse>() {

        @Override
        public void onNext(VerifyOtpResponse verifyOtpResponse) {
            Utils.hideProgressDialog();
            response_validator_otp.setValue(verifyOtpResponse);
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

    DisposableObserver<GetCountryServiceResponse> transactionDes = new DisposableObserver<GetCountryServiceResponse>() {

        @Override
        public void onNext(GetCountryServiceResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_transaction.setValue(loginResponse);

        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.USERREGISTERED);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<GetBankDetailResponse> bankDetailDes = new DisposableObserver<GetBankDetailResponse>() {

        @Override
        public void onNext(GetBankDetailResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_bank_detail.setValue(loginResponse);

        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.USERREGISTERED);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    DisposableObserver<AddCountryResponse> addcoountry_response = new DisposableObserver<AddCountryResponse>() {

        @Override
        public void onNext(AddCountryResponse addCountryResponse) {
            Utils.hideProgressDialog();
            response_validator_add_country.setValue(addCountryResponse);
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
    DisposableObserver<GetExchangeRateModel> exchangeDes = new DisposableObserver<GetExchangeRateModel>() {

        @Override
        public void onNext(GetExchangeRateModel getExchangeRateModel) {
            Utils.hideProgressDialog();
            response_validator_exchange.setValue(getExchangeRateModel);

        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.USERREGISTERED);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
