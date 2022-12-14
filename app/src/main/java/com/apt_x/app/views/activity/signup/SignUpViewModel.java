package com.apt_x.app.views.activity.signup;

import androidx.lifecycle.MutableLiveData;
import android.text.TextUtils;


import com.apt_x.app.databinding.ActivitySentMoneyRecipientBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.AddNewRecipientREsposne;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.model.CreateWalletResponse;
import com.apt_x.app.model.GetExistingUserResponse;
import com.apt_x.app.model.GetOccupationResponse;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.SignUpResponseBean;
import com.apt_x.app.model.SocialSignupResponse;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Connectivity;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;
import com.apt_x.app.databinding.ActivitySignUpBinding;
import com.apt_x.app.databinding.ActivityVerifyPhoneBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by shivanivani on 22/4/21.
 */

public class SignUpViewModel extends BaseViewModel implements DialogClickListener {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<CreateWalletResponse> response_validator_create_wallet = new MutableLiveData<>();
    public final MutableLiveData<AddCountryResponse> response_validator_add_country=new MutableLiveData<>();
    public final MutableLiveData<GetUserByEmail> response_user_validator=new MutableLiveData<>();
    public final MutableLiveData<SignUpResponseBean> response_validator = new MutableLiveData<>();
    public final MutableLiveData<AddNewRecipientREsposne> response_validator_recipient = new MutableLiveData<>();
    public final MutableLiveData<AddAddressResponse> response_validator_address = new MutableLiveData<>();
    public final MutableLiveData<VerifyOtpResponse> response_validator_otp = new MutableLiveData<>();
    public final MutableLiveData<ResendOtpResponses> response_validator_resend = new MutableLiveData<>();
    public final MutableLiveData<SocialSignupResponse> response_validator_social = new MutableLiveData<>();
    public final MutableLiveData<CountriesResponse> response_countries = new MutableLiveData<>();
    public final MutableLiveData<GetOccupationResponse> response_occupation = new MutableLiveData<>();
    public final MutableLiveData<GetExistingUserResponse> response_existing_user = new MutableLiveData<>();

    public SignUpResponseBean signUpResponseBean;
    public AddAddressResponse addAddressResponse;

    public boolean checkEmail(String text) {
        if (Utils.isStringsEmpty(text)) {
            validator.setValue(AppUtils.empty_id);
            return false;
        } else if (!Utils.isValideEmail(text)) {
            validator.setValue(AppUtils.invalid_mail);
            return false;
        }
        return true;
    }
    public void addCountry(ApiCalls apiCalls, String code)
    {
        apiCalls.addCountries(addcoountry_response,code);
    }


    public boolean checkPassword(String text) {
        if (Utils.isStringsEmpty(text)) {
            validator.setValue(AppUtils.empty_password);
            return false;
        }else if (text.length()<8) {
            validator.setValue(AppUtils.invalid_password);
            return false;
        }
        return true;
    }


    public boolean validateSignUp(ActivitySignUpBinding binding) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (TextUtils.isEmpty(binding.etFistName.getText().toString())) {
            validator.setValue(AppUtils.empty_name);
            return false;
        }
        else if (TextUtils.isEmpty(binding.etLastName.getText().toString())) {
            validator.setValue(AppUtils.empty_last_name);
            return false;
        }else if (TextUtils.isEmpty(binding.etNumber.getText().toString())) {
            validator.setValue(AppUtils.empty_number);
            return false;
        }/* else if (binding.etNumber.getText().toString().length() < 10) {
            validator.setValue(AppUtils.invalid_number);
            return false;
        }*/ else if (!checkEmail(binding.etEmail.getText().toString())) {
            return false;
        } else if (!checkPassword(binding.etPassword.getText().toString())) {
            return false;
        } else {
            return true;
        }

    }


    public void getuserValidate(ApiCalls apiCalls,String input)
    {
        apiCalls.getUserById(user_response,input);
    }

    public void getExistingUser(ApiCalls apiCalls,String input)
    {
        apiCalls.getExistingUser(existing_user_response,input);
    }
    public boolean validateOtp(ActivityVerifyPhoneBinding binding) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (TextUtils.isEmpty(binding.etOtp.getText().toString())) {
            validator.setValue(AppUtils.empty_otp);
            return false;
        }
        else if (binding.etOtp.getText().toString().length()<4) {
            validator.setValue(AppUtils.invalid_otp);
            return false;
        }else {
            return true;
        }

    }
    public void doSignUp(
          /*  String email, String password, String phone,  String fname, String lastname,String countryId,String occupation,*/
            ActivitySignUpBinding binding,String countryId,
            ApiCalls apiCalls) {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String phone = binding.etNumber.getText().toString();
        String name = binding.etFistName.getText().toString();
        String lastname = binding.etLastName.getText().toString();
        String countryCode = binding.editCountryCode.getText().toString();
        signUpResponseBean = new SignUpResponseBean();
        apiCalls.signUp(email, password, name,lastname, phone, countryId, callSignUpDes);
    }
    public void addRecipientSignUp(ActivitySentMoneyRecipientBinding binding, String countryId, ApiCalls apiCalls) {
        String email = binding.etEmail.getText().toString();
        String password = "123456";
        String phone = binding.etNumber.getText().toString();
        String name = binding.etFirstName.getText().toString();
        String lastname = binding.etLastName.getText().toString();
        String countryCode = binding.editCountryCode.getText().toString();
        signUpResponseBean = new SignUpResponseBean();
      /*  AddRecipientRequest addRecipientRequest= new AddRecipientRequest(
                name,lastname,email,"123456",phone,countryId,binding.etAddress.getText().toString(),binding.etCity.getText().toString(),
                binding.etHouse.getText().toString(),binding.etZipCode.getText().toString(),binding.etSelectPro.getText().toString()
        );*/



    }

    public void address(ActivitySentMoneyRecipientBinding binding, ApiCalls apiCalls,String dob) {
        String address = binding.etAddress.getText().toString();
        String houseNo = binding.etHouse.getText().toString();
        String city = binding.etCity.getText().toString();
        String zip = binding.etZipCode.getText().toString();
        String state = binding.etSelectPro.getText().toString();
        addAddressResponse = new AddAddressResponse();
        apiCalls.addAddress(address, houseNo, city, zip, "CA",state,Utils.convertDate(dob), addAddressDes);
    }

    //Create wallet
    public void createWallet(String countryId, ApiCalls apiCalls) {

        apiCalls.createWallet(countryId, createWalletDes);
    }


    public void doSignUpGoogle(String mobile,String userId,String userName,String lastname, String email,String countryId,ApiCalls apiCalls) {
        signUpResponseBean = new SignUpResponseBean();
        apiCalls.signUpGoogle(mobile,userId,email, userName,lastname,countryId, callSignUpSocialDes);
    }

    public void verifyOtp(String otp, String email, ApiCalls apiCalls) {
        apiCalls.verifyOtp(email, otp, callVerifyOtpDes);
    }
    public void resendOtp(String email, ApiCalls apiCalls) {
        apiCalls.resendOTP(email, resendOtpDes);
    }
    public void getActiveCountry(ApiCalls apiCalls) {
        apiCalls.getActiveCountries(getActiveCountryDes);
    }
    public void getOccupation(ApiCalls apiCalls) {
        apiCalls.getOccupation(getOccupationdes);
    }


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

    DisposableObserver<GetExistingUserResponse> existing_user_response = new DisposableObserver<GetExistingUserResponse>() {

        @Override
        public void onNext(GetExistingUserResponse getExistingUserResponse) {
            Utils.hideProgressDialog();
            response_existing_user.setValue(getExistingUserResponse);
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
    DisposableObserver<AddAddressResponse> addAddressDes = new DisposableObserver<AddAddressResponse>() {
        @Override
        public void onNext(@NonNull AddAddressResponse addAddressResponse) {
            Utils.hideProgressDialog();
            response_validator_address.setValue(addAddressResponse);
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
    DisposableObserver<CreateWalletResponse> createWalletDes = new DisposableObserver<CreateWalletResponse>() {

        @Override
        public void onNext(CreateWalletResponse signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_create_wallet.setValue(signUpResponseBean);
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



    DisposableObserver<SignUpResponseBean> callSignUpDes = new DisposableObserver<SignUpResponseBean>() {

        @Override
        public void onNext(SignUpResponseBean signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator.setValue(signUpResponseBean);
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

    DisposableObserver<ResendOtpResponses> resendOtpDes = new DisposableObserver<ResendOtpResponses>() {

        @Override
        public void onNext(ResendOtpResponses resendOtpResponses) {
            Utils.hideProgressDialog();
            response_validator_resend.setValue(resendOtpResponses);
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

    DisposableObserver<VerifyOtpResponse> callVerifyOtpDes = new DisposableObserver<VerifyOtpResponse>() {

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

    DisposableObserver<SocialSignupResponse> callSignUpSocialDes = new DisposableObserver<SocialSignupResponse>() {

        @Override
        public void onNext(SocialSignupResponse signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_social.setValue(signUpResponseBean);
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
    DisposableObserver<GetOccupationResponse> getOccupationdes = new DisposableObserver<GetOccupationResponse>() {

        @Override
        public void onNext(@NonNull GetOccupationResponse getOccupationResponse) {
            Utils.hideProgressDialog();
            response_occupation.setValue(getOccupationResponse);
        }

        @Override
        public void onError(Throwable e) {

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
