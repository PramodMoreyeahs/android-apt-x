package com.apt_x.app.views.activity.changepassword;

import androidx.lifecycle.MutableLiveData;

import com.apt_x.app.databinding.ChangePasswordActivityBinding;
import com.apt_x.app.databinding.ForgetPasswordChangeActivityBinding;
import com.apt_x.app.model.ChangePasswordResponse;
import com.apt_x.app.model.bean.ChangePasswordBean;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;
import com.google.gson.JsonObject;

import io.reactivex.observers.DisposableObserver;

public class ChangePasswordViewModel extends BaseViewModel {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<ChangePasswordResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<JsonObject> response_validator_issue = new MutableLiveData<>();
    public ChangePasswordBean loginResponseBean;

    public void changePassword(ChangePasswordActivityBinding binding, ApiCalls apiCalls,String email) {

        String password = binding.etOld.getText().toString();
        String newpassword = binding.etNew.getText().toString();
        String confirmpassword = binding.etConfNew.getText().toString();
        loginResponseBean = new ChangePasswordBean();
        if(!confirmpassword.equals(newpassword))
        {
            validator.setValue(AppUtils.CONFIRMPASSWORD);
        }
        else {
            apiCalls.changePassword(email,password,newpassword,callLoginDes);
        }



    }

    public void sendIssue(  ApiCalls apiCalls,String email,String issue,String detail) {

            apiCalls.sendIssue(callSendIssue,email,issue,detail);




    }



    public void changeForgetPassword(ForgetPasswordChangeActivityBinding binding, ApiCalls apiCalls, String email) {


        String newpassword = binding.etNew.getText().toString();
        String confirmpassword = binding.etConfNew.getText().toString();
        loginResponseBean = new ChangePasswordBean();
        if(!confirmpassword.equals(newpassword))
        {
            validator.setValue(AppUtils.CONFIRMPASSWORD);
        }
        else {
            apiCalls.forgetchangePassword(email,newpassword,confirmpassword,callLoginDes);
        }



    }
    DisposableObserver<ChangePasswordResponse> callLoginDes = new DisposableObserver<ChangePasswordResponse>() {

        @Override
        public void onNext(ChangePasswordResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator.setValue(loginResponse);

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
    DisposableObserver<JsonObject> callSendIssue = new DisposableObserver<JsonObject>() {

        @Override
        public void onNext(JsonObject loginResponse) {
            Utils.hideProgressDialog();
            response_validator_issue.setValue(loginResponse);

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


}
