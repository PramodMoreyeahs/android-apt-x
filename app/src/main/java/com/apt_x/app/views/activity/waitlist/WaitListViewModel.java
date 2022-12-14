package com.apt_x.app.views.activity.waitlist;


import androidx.lifecycle.MutableLiveData;

import com.apt_x.app.model.WaitListResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;

import io.reactivex.observers.DisposableObserver;

public class WaitListViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<WaitListResponse> response_validator = new MutableLiveData<>();


    public void postWaitList(ApiCalls apiCalls,String fcmToken, String email) {
        apiCalls.postWaitList(email,fcmToken,postToken);
    }


    DisposableObserver<WaitListResponse> postToken = new DisposableObserver<WaitListResponse>() {

        @Override
        public void onNext(WaitListResponse getProfileResponse) {
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
