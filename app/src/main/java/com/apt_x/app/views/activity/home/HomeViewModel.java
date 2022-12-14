package com.apt_x.app.views.activity.home;


import androidx.lifecycle.MutableLiveData;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;

import io.reactivex.observers.DisposableObserver;

public class HomeViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<GetProfileResponse> response_validator = new MutableLiveData<>();


    public void getProfile( ApiCalls apiCalls) {
        apiCalls.getProfile(getProfileDes);
    }



    DisposableObserver<GetProfileResponse> getProfileDes = new DisposableObserver<GetProfileResponse>() {

        @Override
        public void onNext(GetProfileResponse getProfileResponse) {
            Utils.hideProgressDialog();
            response_validator.setValue(getProfileResponse);

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
