package com.apt_x.app.privacy.netcom.retrofit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.apt_x.app.application.MyApp;

import com.apt_x.app.authsdk.verifyAucant.Constants;
import com.apt_x.app.authsdk.verifyAucant.ProcessedData;
import com.apt_x.app.model.AddBankDisburesmentRequest;
import com.apt_x.app.model.AddCardDisbursmentRequest;
import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.AddFundResponse;
import com.apt_x.app.model.AddNewRecipientREsposne;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.BankDetailOfUserRequest;
import com.apt_x.app.model.BankDetailOfUserResponse;
import com.apt_x.app.model.ChangePasswordResponse;
import com.apt_x.app.model.CreateTransactionResponse;
import com.apt_x.app.model.CreateWalletResponse;
import com.apt_x.app.model.CrossBorderHistoryResponse;
import com.apt_x.app.model.ExistingP2PResponse;
import com.apt_x.app.model.GetBankBranch;
import com.apt_x.app.model.GetBankBranchesResponse;
import com.apt_x.app.model.GetBankDetailResponse;
import com.apt_x.app.model.GetBankModel;
import com.apt_x.app.model.GetCompanyConstantResponse;
import com.apt_x.app.model.GetCountryServiceResponse;
import com.apt_x.app.model.GetExchangeRateModel;
import com.apt_x.app.model.GetExistingUserResponse;
import com.apt_x.app.model.GetIdentityResponse;
import com.apt_x.app.model.GetOccupationResponse;
import com.apt_x.app.model.GetPurpose;
import com.apt_x.app.model.GetTransactionHistoryResponse;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.model.P2PRequest;
import com.apt_x.app.model.P2PResponse;
import com.apt_x.app.model.PorfilePictureUrlResponse;

import com.apt_x.app.model.SendMoneyWalletRequest;
import com.apt_x.app.model.TransactionEmailBody;
import com.apt_x.app.model.UserListP2p;
import com.apt_x.app.model.bean.AddEftAccountResponse;
import com.apt_x.app.model.bean.GetBankListResponse;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.KYCResponse;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.SignUpResponseBean;
import com.apt_x.app.model.SocialSignupResponse;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.model.WaitListResponse;
import com.apt_x.app.model.bean.LoginResponseBean;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.model.AddDisburesementResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCalls {
    private static Activity mActivity;
    private static ApiCalls apiCalls;
    private CompositeDisposable disposable;



    public static ApiCalls getInstance(Activity activity) {
        if (apiCalls == null) {
            apiCalls = new ApiCalls();
        }
        mActivity = activity;
        return apiCalls;
    }

    public void login(String email, String password, DisposableObserver<LoginResponseBean> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.PASSWORD, password);
        service.doLogin(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<LoginResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginResponseBean loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    //wallet balance
    public void getWalletBalance( DisposableObserver<GetWalletBalanceResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getWalletBalance(Pref.getAccessToken(MyApp.getInstance()))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetWalletBalanceResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetWalletBalanceResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void getBankBranches( String bankId,DisposableObserver<GetBankBranchesResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getBankBranches(Pref.getAccessToken(MyApp.getInstance()),bankId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetBankBranchesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetBankBranchesResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void createTransaction(PostCreateTransactionBody postCreateTransactionBody, DisposableObserver<CreateTransactionResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.createTransaction(Pref.getAccessToken(MyApp.getInstance()),postCreateTransactionBody)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<CreateTransactionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CreateTransactionResponse createTransactionResponse) {
                        disposable.onNext(createTransactionResponse);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }



    public void getBank( String country,DisposableObserver<GetBankModel> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getBank(Pref.getAccessToken(MyApp.getInstance()),country)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetBankModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetBankModel getBankModel) {
                        disposable.onNext(getBankModel);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    public void getBankBranch( String bankId,DisposableObserver<GetBankBranch> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getBankBranch(Pref.getAccessToken(MyApp.getInstance()),bankId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetBankBranch>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetBankBranch getBankBranch) {
                        disposable.onNext(getBankBranch);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }
    //get Account list
    public void getAccountList(DisposableObserver<GetBankListResponse> disposable, String payeeId) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getAccountList(Pref.getAccessToken(MyApp.getInstance()),payeeId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetBankListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetBankListResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }




    // get country service
public void getActiveCountryService( DisposableObserver<GetCountryServiceResponse> disposable) {
    AppStructureAPI service = RetrofitHolder.getService();
    service.getActiveCountryService(Pref.getAccessToken(MyApp.getInstance()))
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new io.reactivex.Observer<GetCountryServiceResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(GetCountryServiceResponse loginResponseBeanServer) {
                    disposable.onNext(loginResponseBeanServer);
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    try {
                        HttpException error = (HttpException) t;
                        String errorBody = error.response().errorBody().string();
                        Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    disposable.onError(t);

                }

                @Override
                public void onComplete() {
                    disposable.onComplete();

                }
            });
}
    public void getExchangeRate(DisposableObserver<GetExchangeRateModel> disposable, JsonObject jsonObject) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getExchangeRate(Pref.getAccessToken(MyApp.getInstance()),jsonObject)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetExchangeRateModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetExchangeRateModel getExchangeRateModel) {
                        disposable.onNext(getExchangeRateModel);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }



    //transaction history
    public void getTransaction( DisposableObserver<GetTransactionHistoryResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getTransaction(Pref.getAccessToken(MyApp.getInstance()))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetTransactionHistoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetTransactionHistoryResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    //getKyc
    public void getKyc( DisposableObserver<KYCResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getKyc(Pref.getAccessToken(MyApp.getInstance()))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<KYCResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(KYCResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    public void changePassword(String email, String password,String newpassword, DisposableObserver<ChangePasswordResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.OLDPASSWORD, password);
        jsonObject1.addProperty("NewPassword", newpassword);

        service.changePassword(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ChangePasswordResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ChangePasswordResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void forgetchangePassword(String email, String password,String confirmpassword, DisposableObserver<ChangePasswordResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.addProperty("NewPassword", password);
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.C_PASSWORD, confirmpassword);

        service.resetPAssword(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ChangePasswordResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ChangePasswordResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    public void loginGoogle(String userId,String email, DisposableObserver<LoginResponseBean> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.USER_ID, userId);

        jsonObject1.addProperty(Keys.EMAIL, email);

        service.doLoginGoogle(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<LoginResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginResponseBean loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    //uploadProfile
    public void uploadProfile(File file, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "file", file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));

        Log.e("TAG", "uploadProfile: ******   "+file.getName() );
        service.uploadProfile(filePart,"profile")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<PorfilePictureUrlResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(PorfilePictureUrlResponse signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }
    //add new card
    public void addCard(String bankId, String cardNumber, String expiryDate, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        String  payeeId1= (MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));

        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.addProperty(Keys.DISBURSEMENT_NUMBER, cardNumber);
        jsonObject1.addProperty(Keys.CURRENCY, "CAD");
        jsonObject1.addProperty(Keys.TYPE, "1");
        jsonObject1.addProperty(Keys.EXPIRY_DATE, expiryDate);
        jsonObject1.addProperty(Keys.PAYEE_ID, payeeId1);




        service.addCard(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddEftAccountResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AddEftAccountResponse signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    //add new eft account
    public void addNewEftAccount(String bankId, String transitNumber, String accountNumber, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        String  payeeId1= (MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));

        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.addProperty(Keys.DISBURSEMENT_NUMBER, accountNumber);
        jsonObject1.addProperty(Keys.TRANSIT_NUMBER, transitNumber);
        jsonObject1.addProperty(Keys.BANK_NUMBER, bankId);
        jsonObject1.addProperty(Keys.CURRENCY, "CAD");
        //jsonObject1.addProperty(Keys.CURRENCY, "CAD");
        jsonObject1.addProperty(Keys.TYPE, "4");
        jsonObject1.addProperty(Keys.PAYEE_ID, payeeId1);




        service.addNewEftAccount(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddEftAccountResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AddEftAccountResponse signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }



    //updateProfile
    public void doUpdateProfile(String firstName, String lastName, String mobile, /*String street_line_2, String country, String street,String zip,String state,String city,*/String profile, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.FIRST_NAME, firstName);
        jsonObject1.addProperty(Keys.LAST_NAME, lastName);
        jsonObject1.addProperty(Keys.mobile, mobile);
       // jsonObject1.addProperty(Keys.country, country);
        jsonObject1.addProperty(Keys.profileImage, profile);
       /* jsonObject1.addProperty(Keys.STREET_LINE_2, street_line_2);
        jsonObject1.addProperty(Keys.city, city);
        jsonObject1.addProperty(Keys.STATE, state);
        jsonObject1.addProperty(Keys.STREET, street);
        jsonObject1.addProperty(Keys.ZIP, zip);*/

        service.doUpdateProfile(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetProfileResponse signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }



    public void signUp(String email, String password, String name, String lastName, String mobile, String countryId, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.PASSWORD, password);
        jsonObject1.addProperty(Keys.FIRST_NAME, name);
        jsonObject1.addProperty(Keys.LAST_NAME, lastName);
        jsonObject1.addProperty(Keys.mobile, mobile);
        jsonObject1.addProperty(Keys.countryId, countryId);
      //  jsonObject1.addProperty(Keys.occupation, occupation);
        service.doSignUp(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<SignUpResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SignUpResponseBean signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void add_recipient(AddRecipientRequest addRecipientRequest, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.add_recipient(Pref.getAccessToken(MyApp.getInstance()),addRecipientRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddNewRecipientREsposne>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AddNewRecipientREsposne addNewRecipientREsposne) {
                        disposable.onNext(addNewRecipientREsposne);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void add_recipients(HashMap<String, String> map, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.add_recipients(Pref.getAccessToken(MyApp.getInstance()),map)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddNewRecipientREsposne>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AddNewRecipientREsposne addNewRecipientREsposne) {
                        disposable.onNext(addNewRecipientREsposne);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    //createWallet
    public void createWallet( String payeeId, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.PAYEE_ID, payeeId);
        service.createWallet(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<CreateWalletResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CreateWalletResponse signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }



    public void verifyOtp(String email, String otp, DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.OTP, otp);

        service.doVerifyOtp(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<VerifyOtpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(VerifyOtpResponse verifyOtpResponse) {
                        disposable.onNext(verifyOtpResponse);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void convertMoney(String paymentMode, String receiveCurrency,String sourceCurrency, String receiveCountry,String sentAmount,DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.PAYMENT_MODE, paymentMode);
        jsonObject1.addProperty(Keys.RECEIVE_CURRENCY, receiveCurrency);
        jsonObject1.addProperty(Keys.SOURCECURRENCY, sourceCurrency);
        jsonObject1.addProperty(Keys.RECEIVECOUNTRY, receiveCountry);
        jsonObject1.addProperty(Keys.SET_AMOUNT, sentAmount);

        service.convertMoney(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<VerifyOtpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(VerifyOtpResponse verifyOtpResponse) {
                        disposable.onNext(verifyOtpResponse);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;

                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void signUpGoogle(String mobile, String userId,String email, String name, String lastName, String countryId, DisposableObserver<SocialSignupResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
/*
                ["first_name": firstName, "last_name": lastName, "email": email,
                "countryId": countryId, "userid": userid, "mobile": mobile]*/
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.countryId, countryId);
        jsonObject1.addProperty(Keys.FIRST_NAME, name);
        jsonObject1.addProperty(Keys.LAST_NAME, lastName);
        jsonObject1.addProperty(Keys.USER_ID, userId);
        jsonObject1.addProperty(Keys.MOBILE, mobile);
        service.doSignUpGoogle(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<SocialSignupResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SocialSignupResponse signUpResponseBean) {
                        disposable.onNext(signUpResponseBean);
                    }

                    @Override
                    public void onError(Throwable t) {

                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void resendOTP(String email, DisposableObserver<ResendOtpResponses> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        service.doResendOTP(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ResendOtpResponses>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResendOtpResponses resendOtpResponses) {
                        disposable.onNext(resendOtpResponses);
                    }

                    @Override
                    public void onError(Throwable t) {

                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }


    public void postWaitList(String email, String notificationId, DisposableObserver<WaitListResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.EMAIL, email);
        jsonObject1.addProperty(Keys.notificationId, notificationId);
        service.postWaitList(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<WaitListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WaitListResponse resendOtpResponses) {
                        disposable.onNext(resendOtpResponses);
                    }

                    @Override
                    public void onError(Throwable t) {

                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }
    public void getIdentity(String email, DisposableObserver<GetIdentityResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("payeeEmail", email);
        service.getIdentity(jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetIdentityResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetIdentityResponse resendOtpResponses) {
                        disposable.onNext(resendOtpResponses);
                    }

                    @Override
                    public void onError(Throwable t) {

                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    // get countries

    public void getActiveCountries(DisposableObserver<CountriesResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getActiveCountries()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<CountriesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CountriesResponse clinicProviderResponse) {


                        disposable.onNext(clinicProviderResponse);
                    }


                    @Override
                    public void onError(Throwable e) {
                        disposable.onError(e);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void getOccupation(DisposableObserver<GetOccupationResponse> disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getOccupation()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetOccupationResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetOccupationResponse clinicProviderResponse) {


                        disposable.onNext(clinicProviderResponse);
                    }


                    @Override
                    public void onError(Throwable e) {
                        disposable.onError(e);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void getProfile(DisposableObserver disposable) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getProfile(Pref.getAccessToken(MyApp.getInstance()))
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetProfileResponse loginResponseBeanServer) {
                        disposable.onNext(loginResponseBeanServer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        try {
                            HttpException error = (HttpException) t;
                            String errorBody = error.response().errorBody().string();
                            Log.e("errorBody", "" + errorBody);
//                            MyPref.getInstance(MyApp.getInstance()).writePrefs(MyPref.ERROR_BODY, errorBody);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        disposable.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();

                    }
                });
    }

    public void addAddress(String street, String street_line_2, String city, String zip, String country,String state,String dob, DisposableObserver disposableObserver) {
        //{"street":"hb","city":"ubub","zip":"vyby","country":"CA"}
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();

        if( Pref.IS_DOB == null ||   Pref.IS_DOB.equals("")){
            jsonObject1.addProperty(Keys.DATEOFBIRTH, MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.DOBSHARED));
            System.out.println("SHARED DOB" +   MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.DOBSHARED));

        }else{
            jsonObject1.addProperty(Keys.DATEOFBIRTH,  Pref.IS_DOB);
        }

       if(street_line_2 == null || street_line_2.equals("")) {


           jsonObject1.addProperty(Keys.STREET, street);
        /*   jsonObject1.addProperty(Keys.STREET_LINE_2, street_line_2);*/
           jsonObject1.addProperty(Keys.CITY, city);
           jsonObject1.addProperty(Keys.ZIP, zip);
           jsonObject1.addProperty(Keys.STATE, state);
           System.out.println("DATEOFBIRTH#########" +   Pref.IS_DOB);

       }else{

           jsonObject1.addProperty(Keys.STREET, street);
              jsonObject1.addProperty(Keys.STREET_LINE_2, street_line_2);
           jsonObject1.addProperty(Keys.CITY, city);
           jsonObject1.addProperty(Keys.ZIP, zip);
           jsonObject1.addProperty(Keys.STATE, state);
           System.out.println("DATEOFBIRTH#########" +   Pref.IS_DOB);

       }


       // jsonObject1.addProperty(Keys.COUNTRY, country);

        service.addAddress(Pref.getAccessToken(MyApp.getInstance()), /*MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID),*/jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddAddressResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AddAddressResponse resendOtpResponses) {
                        disposableObserver.onNext(resendOtpResponses);
                    }

                    @Override
                    public void onError(Throwable t) {

                        disposableObserver.onError(t);

                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();

                    }
                });
    }

    public void doKyc(String identificationType,
                      String identificationNumber,
                      String identificationDate,
                      String identificationDateOfExpiration,
                      String identificationLocation,
                      DisposableObserver disposableObserver) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.IDENTIFICATION_TYPE, identificationType);
        jsonObject1.addProperty(Keys.IDENTIFICATION_Number, identificationNumber);
        jsonObject1.addProperty(Keys.IDENTIFICATION_DATE, identificationDate);
        jsonObject1.addProperty(Keys.IDENTIFICATION_DATE_OF_EXPIRATION, identificationDateOfExpiration);
        jsonObject1.addProperty(Keys.IDENTIFICATION_LOCATION, identificationLocation);
        service.doKYC(Pref.getAccessToken(MyApp.getInstance()), jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<KYCResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull KYCResponse kycResponse) {
                        disposableObserver.onNext(kycResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();
                    }
                });
    }


    public void addDisburesement(String amount,
                                 String transactionType,
                                 String currency,
                                 String disbursementNumber,
                                 String expirationDate,
                                 DisposableObserver disposableObserver) {
        Random random = new Random(System.nanoTime());

        int randomInt = random.nextInt(1000000000);
        String  payeeId= (MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.AMOUNT, Double.parseDouble(amount));
        jsonObject1.addProperty(Keys.TRANSACTION_TYPE, transactionType);
        jsonObject1.addProperty(Keys.disbursementNumber, disbursementNumber);
        jsonObject1.addProperty(Keys.expirationDate, expirationDate);
        jsonObject1.addProperty(Keys.PAYEE_ID, payeeId);
        jsonObject1.addProperty(Keys.referenceId, randomInt);
        jsonObject1.addProperty(Keys.CURRENCY, currency);
        service.addDisbursement(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddDisburesementResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AddDisburesementResponse kycResponse) {
                        disposableObserver.onNext(kycResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();
                    }
                });
    }



    public void addDisburesement2(AddBankDisburesmentRequest addBankDisburesmentRequest,
                                  DisposableObserver disposableObserver) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.addDisbursement2(Pref.getAccessToken(MyApp.getInstance()), addBankDisburesmentRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddDisburesementResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AddDisburesementResponse kycResponse) {
                        disposableObserver.onNext(kycResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();
                    }
                });
    }
    public void addCardDisburesement(AddCardDisbursmentRequest addCardDisbursmentRequest,
                                     DisposableObserver disposableObserver) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.addCardDisbursement2(Pref.getAccessToken(MyApp.getInstance()), addCardDisbursmentRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddDisburesementResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AddDisburesementResponse kycResponse) {
                        disposableObserver.onNext(kycResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();
                    }
                });
    }







    //Add fund
    public void adFund(String amount,
                                 String payeeId,
                                 DisposableObserver disposableObserver) {

//        int payeeId= Integer.parseInt(MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(Keys.AMOUNT, Double.parseDouble(amount));
        jsonObject1.addProperty(Keys.PAYEE_ID, payeeId);

        service.addFund(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddFundResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AddFundResponse kycResponse) {
                        disposableObserver.onNext(kycResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();
                    }
                });
    }


    public void getCompanyConstant(DisposableObserver<GetCompanyConstantResponse> disposable, String keyApi) {
        AppStructureAPI service = RetrofitHolder.getService();
        service.getCompanyConstant(keyApi)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetCompanyConstantResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull GetCompanyConstantResponse clinicProviderResponse) {
                        disposable.onNext(clinicProviderResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }


    public void addCountries(DisposableObserver<AddCountryResponse> disposable, String code) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name", code);
        jsonObject1.addProperty("isActive", false);
        jsonObject1.addProperty("isDelete", true);
        jsonObject1.addProperty("countryCode", "+1");
        jsonObject1.addProperty("flagUrl", "string");

        service.addCountry(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddCountryResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AddCountryResponse clinicProviderResponse) {
                        disposable.onNext(clinicProviderResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getUserById(DisposableObserver<GetUserByEmail> disposable, String input) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("email_id", input);
        jsonObject1.addProperty("first_name", input);


        service.getUserById(Pref.getAccessToken(MyApp.getInstance()),jsonObject1)

                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetUserByEmail>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull GetUserByEmail getUserByEmail) {
                        disposable.onNext(getUserByEmail);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });

    }

    public void getPurpose(DisposableObserver<GetPurpose> disposable, String input) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.getPurpose(Pref.getAccessToken(MyApp.getInstance()),input)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetPurpose>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull GetPurpose getPurpose) {
                        disposable.onNext(getPurpose);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }

    public void addUserBankDetail(BankDetailOfUserRequest bankDetailOfUserRequest,
                                  DisposableObserver disposableObserver) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.add_bank_detail(bankDetailOfUserRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<BankDetailOfUserResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BankDetailOfUserResponse kycResponse) {
                        disposableObserver.onNext(kycResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposableObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposableObserver.onComplete();
                    }
                });
    }

    public void getBankDetail(DisposableObserver<GetBankDetailResponse> disposable, String input) {
        AppStructureAPI service = RetrofitHolder.getService();
          JsonObject jsonObject= new JsonObject();
          jsonObject.addProperty("payeeId",input);
        service.getBankDetail(jsonObject)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetBankDetailResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull GetBankDetailResponse getPurpose) {
                        disposable.onNext(getPurpose);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }

    public void getExistingUser(DisposableObserver<GetExistingUserResponse> disposable, String input) {
        AppStructureAPI service = RetrofitHolder.getService();
       /* JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("email",input);*/
        service.getexitingUser(Pref.getAccessToken(MyApp.getInstance()),input)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<GetExistingUserResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull GetExistingUserResponse getExistingUserResponse) {
                        disposable.onNext(getExistingUserResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void sendMoney(DisposableObserver<JsonObject> disposable, SendMoneyWalletRequest sendMoneyWalletRequest) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.sendmoney(sendMoneyWalletRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject sendMoneyWalletResponse) {
                        disposable.onNext(sendMoneyWalletResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                        Log.e("Error**********",""+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }

    public void userAdd(DisposableObserver<P2PResponse> disposable, P2PRequest p2PRequest) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.userAdd(Pref.getAccessToken(MyApp.getInstance()),p2PRequest)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<P2PResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull P2PResponse p2PResponse) {
                        disposable.onNext(p2PResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                        Log.e("Error**********",""+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void getCrossborderHistory(DisposableObserver<CrossBorderHistoryResponse> disposable, String id) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.getcrossborderHistory(Pref.getAccessToken(MyApp.getInstance()),id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<CrossBorderHistoryResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CrossBorderHistoryResponse crossBorderHistoryResponse) {
                        disposable.onNext(crossBorderHistoryResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void getP2PUserList(DisposableObserver<UserListP2p> disposable,String walletId) {
        AppStructureAPI service = RetrofitHolder.getService();

        service.getUserP2p(Pref.getAccessToken(MyApp.getInstance()),walletId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<UserListP2p>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull UserListP2p userListP2p) {
                        disposable.onNext(userListP2p);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void getP2PExistingUser(DisposableObserver<ExistingP2PResponse> disposable,String email) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("emailId",email);
        service.getExistingUserP2p(Pref.getAccessToken(MyApp.getInstance()),jsonObject)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ExistingP2PResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ExistingP2PResponse existingP2PResponse) {
                        disposable.onNext(existingP2PResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }

    public void deleteUser(DisposableObserver<JsonObject> disposable,String id) {
        AppStructureAPI service = RetrofitHolder.getService();
      /*  JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("emailId",email);*/
        service.deleteUser(Pref.getAccessToken(MyApp.getInstance()),id)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        disposable.onNext(jsonObject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void sendEmail(DisposableObserver<JsonObject> disposable, TransactionEmailBody body ) {
        AppStructureAPI service = RetrofitHolder.getService();
      /*  JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("emailId",email);*/
        service.sendTransactionEmail(Pref.getAccessToken(MyApp.getInstance()),body)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        disposable.onNext(jsonObject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void loadFundEmail(DisposableObserver<JsonObject> disposable, String subject,String content ) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("subject",subject);
        jsonObject.addProperty("content",content);
        service.loadFundEmail(Pref.getAccessToken(MyApp.getInstance()),jsonObject)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        disposable.onNext(jsonObject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }


    public void validateIban(DisposableObserver<JsonObject> disposable, String iban ) {
        AppStructureAPI service = RetrofitHolder.getService();
      /*  JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("emailId",email);*/
        service.validetIban(iban)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        disposable.onNext(jsonObject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
    public void addWalletFund(DisposableObserver<JsonObject> disposable, String payeeId ,String amount ) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("payeeId",payeeId);
        jsonObject.addProperty("amount",amount);
        service.addWalletFund(jsonObject)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        disposable.onNext(jsonObject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }

    public void sendIssue(DisposableObserver<JsonObject> disposable, String email ,String issue,String detail ) {
        AppStructureAPI service = RetrofitHolder.getService();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("issuedType",issue);
        jsonObject.addProperty("details",detail);
        service.sendIssue(Pref.getAccessToken(MyApp.getInstance()),jsonObject)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        disposable.onNext(jsonObject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable.onComplete();
                    }
                });
    }
}
