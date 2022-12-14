package com.apt_x.app.views.activity.verification;

import androidx.lifecycle.MutableLiveData;

import com.apt_x.app.authsdk.verifyAucant.ProcessedData;
import com.apt_x.app.databinding.ActivityUpdateAddressBinding;
import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Connectivity;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;
import com.apt_x.app.databinding.ActivityAddAddressBinding;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class AddressViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AddAddressResponse> response_validator = new MutableLiveData<>();
    public AddAddressResponse addAddressResponse;

    public final MutableLiveData<GetProfileResponse> response_validator_get = new MutableLiveData<>();


    public void getProfile( ApiCalls apiCalls) {
        apiCalls.getProfile(getProfileDes);
    }


    DisposableObserver<GetProfileResponse> getProfileDes = new DisposableObserver<GetProfileResponse>() {
        @Override
        public void onNext(@NonNull GetProfileResponse getProfileResponse) {
            Utils.hideProgressDialog();
            response_validator_get.setValue(getProfileResponse);
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

    public boolean checkAddress(String street) {

        if (Utils.isStringsEmpty(street)) {
            validator.setValue(AppUtils.empty_street);
            return false;
        }
        return true;
    }

 /*   public boolean checkHouse(String houseNo) {
        if (Utils.isStringsEmpty(houseNo)) {
            validator.setValue(AppUtils.empty_house);
            return false;
        }
        return true;
    }*/

    public boolean checkCity(String city) {
        if (Utils.isStringsEmpty(city)) {
            validator.setValue(AppUtils.empty_city);
            return false;
        }
        return true;
    }

    public boolean checkZip(String zip) {
        if (Utils.isStringsEmpty(zip)) {
            validator.setValue(AppUtils.empty_zip);
            return false;
        }
        return true;
    }

    public boolean checkState(String state) {
        if (Utils.isStringsEmpty(state)) {
            validator.setValue(AppUtils.empty_state);
            return false;
        }
        return true;
    }

    public boolean validateAddress(ActivityAddAddressBinding binding) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (!checkAddress(binding.etAddress.getText().toString())) {
            return false;
        }
     /*   else if (!checkHouse(binding.etHouse.getText().toString())) {
            return false;
        }*/

        else if (!checkCity(binding.etCity.getText().toString())) {
            return false;
        } else if (!checkZip(binding.etZipCode.getText().toString())) {
            return false;
        } else return checkState(binding.acTvState.getText().toString());

    }
    public boolean validateAddress(ActivityUpdateAddressBinding binding) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (!checkAddress(binding.etAddress.getText().toString())) {
            return false;
        }
        /*else if (!checkHouse(binding.etHouse.getText().toString())) {
            return false;
        }*/
        else if (!checkCity(binding.etCity.getText().toString())) {
            return false;
        } else if (!checkZip(binding.etZipCode.getText().toString())) {
            return false;
        } else return checkState(binding.acTvState.getText().toString());

    }

    public void address(ActivityAddAddressBinding binding, ApiCalls apiCalls,String dob) {
        String address = binding.etAddress.getText().toString();
        String houseNo = binding.etHouse.getText().toString();
        String city = binding.etCity.getText().toString();
        String zip = binding.etZipCode.getText().toString();
        String state = binding.acTvState.getText().toString();


        addAddressResponse = new AddAddressResponse();
        apiCalls.addAddress(address, houseNo, city, zip, "CA",state, ProcessedData.dateOfBirth, addAddressDes);
    }
    public void address1(ActivityUpdateAddressBinding binding, ApiCalls apiCalls, String dob) {
        String address = binding.etAddress.getText().toString();
        String houseNo = binding.etHouse.getText().toString();
        String city = binding.etCity.getText().toString();
        String zip = binding.etZipCode.getText().toString();
        String state = binding.acTvState.getText().toString();
        addAddressResponse = new AddAddressResponse();
        apiCalls.addAddress(address, houseNo, city, zip, "CA",state,dob, addAddressDes);
    }

    DisposableObserver<AddAddressResponse> addAddressDes = new DisposableObserver<AddAddressResponse>() {
        @Override
        public void onNext(@NonNull AddAddressResponse addAddressResponse) {
            Utils.hideProgressDialog();
            response_validator.setValue(addAddressResponse);
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
}
