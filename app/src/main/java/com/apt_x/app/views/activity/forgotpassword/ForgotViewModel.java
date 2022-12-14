package com.apt_x.app.views.activity.forgotpassword;

import androidx.lifecycle.MutableLiveData;


import com.apt_x.app.application.MyApp;
import com.apt_x.app.databinding.ForgotPasswordBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.model.bean.ForgotPasswordResponseBean;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.privacy.netcom.retrofit.RetrofitHolder;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;

import java.util.Objects;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivanivani on 22/4/21
 */
public class ForgotViewModel extends BaseViewModel implements DialogClickListener {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<ForgotPasswordResponseBean> response_validator = new MutableLiveData<>();
    public final MutableLiveData<VerifyOtpResponse> response_validator_otp = new MutableLiveData<>();
    public final MutableLiveData<ResendOtpResponses> response_validator_resend = new MutableLiveData<>();



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

    public void forgotPassword(ForgotPasswordBinding binding) {

        String email = binding.etOtp.getText().toString();

        Call<ForgotPasswordResponseBean> call = RetrofitHolder.getService().forgotPassword(MyApp.getInstance().getBaseEntity(false), email);

        call.enqueue(new Callback<ForgotPasswordResponseBean>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponseBean> call, Response<ForgotPasswordResponseBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponseBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }
    public void resendOtp(String email, ApiCalls apiCalls) {
        apiCalls.resendOTP(email, resendOtpDes);
    }

    public void verifyOtp(String otp, String email, ApiCalls apiCalls) {
        apiCalls.verifyOtp(email, otp, callVerifyOtpDes);
    }

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

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

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
}
