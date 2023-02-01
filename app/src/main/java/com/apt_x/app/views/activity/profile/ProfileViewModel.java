package com.apt_x.app.views.activity.profile;


import androidx.lifecycle.MutableLiveData;

import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.NewImageResponseModel;
import com.apt_x.app.model.PorfilePictureUrlResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseViewModel;

import java.io.File;
import java.util.Objects;

import io.reactivex.observers.DisposableObserver;

public class ProfileViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<GetProfileResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<GetProfileResponse> response_validator_update_profile = new MutableLiveData<>();
    public final MutableLiveData<PorfilePictureUrlResponse> response_validator_picture = new MutableLiveData<>();
    public final MutableLiveData<NewImageResponseModel> response_validator_picture2 = new MutableLiveData<>();


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

    public void uploadProfile( File file, ApiCalls apiCalls) {

        apiCalls.uploadProfile(file, callUpdateProfilePictureDes);
    }

    public void uploadProfile2( File file, ApiCalls apiCalls) {

        apiCalls.uploadProfile2(file, callUpdateProfilePictureDes2);
    }

    public void doUpdateProfile(String firstName,String lastName,String mobile,/*String state,String street,String street2,String country,String zip,String city,*/String file, ApiCalls apiCalls) {

        apiCalls.doUpdateProfile(firstName, lastName, mobile,file, callUpdateProfileDes);
    }

    DisposableObserver<GetProfileResponse> callUpdateProfileDes = new DisposableObserver<GetProfileResponse>() {

        @Override
        public void onNext(GetProfileResponse signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_update_profile.setValue(signUpResponseBean);
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
    DisposableObserver<PorfilePictureUrlResponse> callUpdateProfilePictureDes = new DisposableObserver<PorfilePictureUrlResponse>() {

        @Override
        public void onNext(PorfilePictureUrlResponse signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_picture.setValue(signUpResponseBean);
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
    DisposableObserver<NewImageResponseModel> callUpdateProfilePictureDes2 = new DisposableObserver<NewImageResponseModel>() {

        @Override
        public void onNext(NewImageResponseModel signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_picture2.setValue(signUpResponseBean);
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
