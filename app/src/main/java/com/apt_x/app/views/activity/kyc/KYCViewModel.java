package com.apt_x.app.views.activity.kyc;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Connectivity;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;
import com.apt_x.app.model.KYCResponse;

import com.apt_x.app.databinding.ActivityPassportDetailsBinding;

import io.reactivex.observers.DisposableObserver;

public class KYCViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<KYCResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<KYCResponse> response_validator_kyc = new MutableLiveData<>();
    public final MutableLiveData<AddAddressResponse> response_validator_addAddress = new MutableLiveData<>();


    public void getKyc( ApiCalls apiCalls) {
        apiCalls.getKyc(WalletBalanceDes);
    }
    public void addAddress(ApiCalls apiCalls, String street, String street_line_2, String city, String zip, String country,String state,String dob){
        apiCalls.addAddress(street,street_line_2,city,zip,country,state,dob,AddAddressDes);

    }

    DisposableObserver<KYCResponse> WalletBalanceDes = new DisposableObserver<KYCResponse>() {

        @Override
        public void onNext(KYCResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_kyc.setValue(loginResponse);

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

    DisposableObserver<AddAddressResponse> AddAddressDes = new DisposableObserver<AddAddressResponse>() {

        @Override
        public void onNext(AddAddressResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_addAddress.setValue(loginResponse);

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
    public boolean checkPassportNumber(String number) {

        if (Utils.isStringsEmpty(number)) {
            validator.setValue(AppUtils.empty_number);
            return false;
        }
        return true;
    }

    public boolean checkIssueDate(String date) {

        if (Utils.isStringsEmpty(date)) {
            validator.setValue(AppUtils.empty_issue_date);
            return false;
        }
        return true;
    }

    public boolean checkExpirationDate(String date) {

        if (Utils.isStringsEmpty(date)) {
            validator.setValue(AppUtils.empty_expiration_date);
            return false;
        }
        return true;
    }

    public void submitKYC(ActivityPassportDetailsBinding binding, ApiCalls apiCalls,String identificationType) {
        String passportNumber = binding.etPassportNumber.getText().toString();
        String issueDate = binding.etDateOfIssue.getText().toString();
        String expDate = binding.etDateOfExpiration.getText().toString();
        String state = binding.etState.getText().toString();
        String country = binding.etCountry.getText().toString();
        apiCalls.doKyc(identificationType, passportNumber, issueDate, expDate, state+","+country, disposableObserver);
    }

    DisposableObserver<KYCResponse> disposableObserver = new DisposableObserver<KYCResponse>() {
        @Override
        public void onNext(@NonNull KYCResponse kycResponse) {
            Utils.hideProgressDialog();
            response_validator.setValue(kycResponse);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };





    public boolean checkState(String state) {

        if (Utils.isStringsEmpty(state)) {
            validator.setValue(AppUtils.empty_state);
            return false;
        }
        return true;
    }

    public boolean checkCountry(String country) {

        if (Utils.isStringsEmpty(country)) {
            validator.setValue(AppUtils.empty_country);
            return false;
        }
        return true;
    }

    public boolean validatePassportForm(ActivityPassportDetailsBinding binding) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (!checkPassportNumber(binding.etPassportNumber.getText().toString())) {
            return false;
        } else if (!checkIssueDate(binding.etDateOfIssue.getText().toString())) {
            return false;
        } else if (!checkExpirationDate(binding.etDateOfExpiration.getText().toString())) {
            return false;
        } else if (!checkState(binding.etState.getText().toString())) {
            return false;
        } else if (!checkCountry(binding.etCountry.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }
}
