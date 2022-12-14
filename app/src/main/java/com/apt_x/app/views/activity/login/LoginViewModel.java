package com.apt_x.app.views.activity.login;

import androidx.lifecycle.MutableLiveData;

import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.bean.LoginResponseBean;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Connectivity;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;
import com.apt_x.app.databinding.ActivityLoginBinding;

import io.reactivex.observers.DisposableObserver;

public class LoginViewModel extends BaseViewModel implements DialogClickListener {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<LoginResponseBean> response_validator = new MutableLiveData<>();
    public LoginResponseBean loginResponseBean;

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


    public boolean checkPassword(String text) {

        if (Utils.isStringsEmpty(text)) {
            validator.setValue(AppUtils.empty_password);
            return false;
        }
       else if (text.length()<6) {
            validator.setValue(AppUtils.invalid_password);
            return false;
        }
        return true;
    }


    @Override
    public void onDialogClick(int which, int requestCode) {

    }


    public boolean validateLogin(ActivityLoginBinding binding) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (!checkEmail(binding.etEmail.getText().toString())) {
            return false;
        } else if (!checkPassword(binding.etUserPassword.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    public void doLogin(ActivityLoginBinding binding, ApiCalls apiCalls) {
        String email = binding.etEmail.getText().toString();
        String password = binding.etUserPassword.getText().toString();
        loginResponseBean = new LoginResponseBean();
        apiCalls.login(email,password,callLoginDes);


    }
    public void doLoginGoogle(String userId,String email, ApiCalls apiCalls) {
        loginResponseBean = new LoginResponseBean();
        apiCalls.loginGoogle(userId,email,callLoginDes);
    }

    DisposableObserver<LoginResponseBean> callLoginDes = new DisposableObserver<LoginResponseBean>() {

        @Override
        public void onNext(LoginResponseBean loginResponse) {
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


}
