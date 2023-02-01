package com.apt_x.app.privacy.netcom.retrofit;


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
import com.apt_x.app.model.LinkVerifyModel;
import com.apt_x.app.model.NewImageResponseModel;
import com.apt_x.app.model.P2PRequest;
import com.apt_x.app.model.P2PResponse;
import com.apt_x.app.model.PorfilePictureUrlResponse;
import com.apt_x.app.model.SendMoneyWalletRequest;
import com.apt_x.app.model.TransactionEmailBody;
import com.apt_x.app.model.UserListP2p;
import com.apt_x.app.model.bean.AddEftAccountResponse;
import com.apt_x.app.model.bean.GetBankListResponse;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.google.gson.JsonObject;
import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.KYCResponse;
import com.apt_x.app.model.ResendOtpResponses;
import com.apt_x.app.model.SignUpResponseBean;
import com.apt_x.app.model.SocialSignupResponse;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.model.WaitListResponse;
import com.apt_x.app.model.bean.BaseRequestEntity;
import com.apt_x.app.model.bean.ChangePasswordBean;
import com.apt_x.app.model.bean.ForgotPasswordResponseBean;
import com.apt_x.app.model.bean.LoginResponseBean;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.model.AddDisburesementResponse;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface AppStructureAPI {


    /*
     *
     *
     * App Login api call with @Params, Email,Password.
     *
     * */
// Login
    @POST(Config.LOGIN_URL)
    io.reactivex.Observable<LoginResponseBean> doLogin(@Body JsonObject jsonObject);

    @GET(Config.WALLET_BALANCE)
    io.reactivex.Observable<GetWalletBalanceResponse> getWalletBalance(@Header(Keys.authorization) String token);

    @GET(Config.TRANSACTION_HISTORY)
    io.reactivex.Observable<GetTransactionHistoryResponse> getTransaction(@Header(Keys.authorization) String token);

    @GET(Config.ACTIVE_COUNTRY_SERVICE)
    io.reactivex.Observable<GetCountryServiceResponse> getActiveCountryService(@Header(Keys.authorization) String token);

    @POST("crossborder/exchangeRate")
    io.reactivex.Observable<GetExchangeRateModel> getExchangeRate(@Header(Keys.authorization) String token, @Body JsonObject jsonObject);


    @GET(Config.GET_ACCOUNT_LIST + "{payeeId}")
    io.reactivex.Observable<GetBankListResponse> getAccountList(@Header(Keys.authorization) String token,
                                                                @Path(Keys.PAYEE_ID) String payeeId);

    @GET("crossborder/banks/{country}")
    io.reactivex.Observable<GetBankModel> getBank(@Header(Keys.authorization) String token,
                                                  @Path("country") String bank);

    @GET("crossborder/bank/{bankId}/branches")
    io.reactivex.Observable<GetBankBranch> getBankBranch(@Header(Keys.authorization) String token,
                                                         @Path("bankId") String banId);


    @POST(Config.CREATE_TRANSACTION)
    io.reactivex.Observable<CreateTransactionResponse> createTransaction(@Header(Keys.authorization) String token,
                                                                         @Body PostCreateTransactionBody postCreateTransactionBody);

    @GET("crossborder/bank/{bankId}/branches")
    io.reactivex.Observable<GetBankBranchesResponse> getBankBranches(@Header(Keys.authorization) String token,
                                                                     @Path("bankId") String bankId);

    @GET(Config.GET_KYC)
    io.reactivex.Observable<KYCResponse> getKyc(@Header(Keys.authorization) String token);

    @POST(Config.CHANGE_PASSWORD_URL)
    io.reactivex.Observable<ChangePasswordResponse> changePassword(
            @Body JsonObject jsonObject);

    @POST(Config.RESETPASSWORD)
    io.reactivex.Observable<ChangePasswordResponse> resetPAssword(
            @Body JsonObject jsonObject);


    @POST(Config.COVERT_MONEY)
    io.reactivex.Observable<VerifyOtpResponse> convertMoney(@Body JsonObject jsonObject);

    @POST(Config.VERIFY_OTP)
    io.reactivex.Observable<VerifyOtpResponse> doVerifyOtp(@Body JsonObject jsonObject);

    @POST(Config.VERIFY_LINK)
    io.reactivex.Observable<LinkVerifyModel> doVerifyLink(@Body JsonObject jsonObject);

    @POST(Config.RESEND_LINK)
    io.reactivex.Observable<LinkVerifyModel> doResendLink(@Body JsonObject jsonObject);


    @GET(Config.GET_PROFILE)
    io.reactivex.Observable<GetProfileResponse> getProfile(@Header(Keys.authorization) String token);

    @PUT(Config.ADD_ADDRESS_URL)
    io.reactivex.Observable<AddAddressResponse> addAddress(@Header(Keys.authorization) String Token, /*@Path(value = "payeeId", encoded = true) String payeeId,*/ @Body JsonObject jsonObject);

    //Login google

    @POST(Config.LOGIN_URL_GOOGLE)
    io.reactivex.Observable<LoginResponseBean> doLoginGoogle(@Body JsonObject jsonObject);

    // sign up
    @POST(Config.SIGN_UP)
    io.reactivex.Observable<SignUpResponseBean> doSignUp(@Body JsonObject jsonObject);

    // update profile
    @PUT(Config.UPDATE_PROFILE)
    io.reactivex.Observable<GetProfileResponse> doUpdateProfile(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);


    @POST(Config.ADD_NEW_EFT_ACCOUNT)
    Observable<AddEftAccountResponse> addNewEftAccount(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);

    @POST(Config.ADD_NEW_EFT_ACCOUNT)
    io.reactivex.Observable<AddEftAccountResponse> addCard(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);


    @Multipart
  /*  @POST("https://uatcaradmin.moreyeahs.in/api/upload/UploadFileAll")*/
    @POST("https://api-staging.apt-xb.com/api/auth/uploadAwsFile")
   /* @POST("https://uatbuilddream.moreyeahs.in/api/Upload/UploadFileAll")*/
    io.reactivex.Observable<NewImageResponseModel> uploadProfile2(
            @Header(Keys.authorization) String Token,
            @Part MultipartBody.Part rProfilePicture);
/*
            @Query("Type") String type);
*/



    @Multipart
    /*  @POST("https://uatcaradmin.moreyeahs.in/api/upload/UploadFileAll")*/
    @POST("https://api-staging.apt-xb.com/api/auth/uploadAwsFile")
        /* @POST("https://uatbuilddream.moreyeahs.in/api/Upload/UploadFileAll")*/
    io.reactivex.Observable<PorfilePictureUrlResponse> uploadProfile(
            @Header(Keys.authorization) String Token,
            @Part MultipartBody.Part rProfilePicture);
/*
            @Query("Type") String type);
*/


    //createWallet
    @POST(Config.CREATE_WALLET)
    io.reactivex.Observable<CreateWalletResponse> createWallet(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);


    @POST(Config.RESEND_OTP)
    io.reactivex.Observable<ResendOtpResponses> doResendOTP(@Body JsonObject jsonObject);

    @POST(Config.WAIT_LIST)
    io.reactivex.Observable<WaitListResponse> postWaitList(@Body JsonObject jsonObject);

    //SignUpGoogle
    @POST(Config.SIGN_UP_GOOGLE)
    io.reactivex.Observable<SocialSignupResponse> doSignUpGoogle(@Body JsonObject jsonObject);

    //addKyc
    @POST(Config.ADD_KYC)
    io.reactivex.Observable<KYCResponse> doKYC(@Header(Keys.authorization) String Token,
                                               @Body JsonObject jsonObject);

    @GET(Config.GET_ACTIVE_COUNTRIES)
    io.reactivex.Observable<CountriesResponse> getActiveCountries();


    /*
     *
     *
     * Forgot password api call with @Param,Email.
     *
     * */
    @POST(Config.FORGOT_PASSWORD_URL)
    Call<ForgotPasswordResponseBean> forgotPassword(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMAIL) String email);

    /*
     *
     *
     * Change password api call with @Param,OldPassword,NewPassword,ConfirmNewPassword,EmployeeId
     *
     * */

    @POST(Config.CHANGE_PASSWORD_URL)
    Call<ChangePasswordBean> doChangePassword(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.OLD_PASSWORD) String oldPassword,
            @Query(Keys.NEW_PASSWORD) String newPassword,
            @Query(Keys.CONFIRM_NEW_PASSWORD) String confirmPassword,
            @Query(Keys.EMPLOYEE_ID) String employeeId
    );


    @GET(Config.GET_COMPANY_CONSTANT)
    Observable<GetCompanyConstantResponse> getCompanyConstant(@Query(Keys.keyApi) String keyApi);


    @POST(Config.ADD_COUNTRY)
    Observable<AddCountryResponse> addCountry(@Query(Keys.keyApi) String keyApi, @Body JsonObject jsonObject);


    @POST(Config.ADD_DISBURSEMENT)
    Observable<AddDisburesementResponse> addDisbursement(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);

    @POST(Config.ADD_FUND)
    Observable<AddFundResponse> addFund(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);

    @POST(Config.ADD_DISBURSEMENT2)
    Observable<AddDisburesementResponse> addDisbursement2(@Header(Keys.authorization) String Token, @Body AddBankDisburesmentRequest addBankDisburesmentRequest);

    @POST(Config.ADD_DISBURSEMENT2)
    Observable<AddDisburesementResponse> addCardDisbursement2(@Header(Keys.authorization) String Token, @Body AddCardDisbursmentRequest addCardDisbursmentRequest);

    @POST(Config.GET_USER_BY_EMAIL)
    Observable<GetUserByEmail> getUserById(@Header(Keys.authorization) String Token, @Body JsonObject jsonObject);

    @GET(Config.GET_PURPOSE + "{code}")
    Observable<GetPurpose> getPurpose(@Header(Keys.authorization) String Token, @Path("code") String code);

    @POST(Config.ADD_NEW_RECIPIENT)
    Observable<AddNewRecipientREsposne> add_recipient(@Header(Keys.authorization) String token, @Body AddRecipientRequest addRecipientRequest);

    @POST(Config.ADD_NEW_RECIPIENT)
    Observable<AddNewRecipientREsposne> add_recipients(@Header(Keys.authorization) String token, @Body HashMap<String, String> map);

    @GET(Config.GET_OCCUPATION)
    Observable<GetOccupationResponse> getOccupation();

    @POST(Config.ADD_BANK_DETAIL)
    Observable<BankDetailOfUserResponse> add_bank_detail(@Body BankDetailOfUserRequest bankDetailOfUserRequest);

    @POST(Config.GET_BANK_DETAIL)
    Observable<GetBankDetailResponse> getBankDetail(@Body JsonObject jsonObject);

    @POST(Config.GET_IDENTITY)
    Observable<GetIdentityResponse> getIdentity(@Body JsonObject jsonObject);

    @GET(Config.GET_EXITING_RECIPIENT)
    Observable<GetExistingUserResponse> getexitingUser(@Header(Keys.authorization) String token, @Query("email_id") String email_id);

    @POST(Config.SEND_WALLET_BALANCE)
    Observable<JsonObject> sendmoney(@Body SendMoneyWalletRequest sendMoneyWalletRequest);

    @POST(Config.CREATE_USER_P2P)
    Observable<P2PResponse> userAdd(@Header(Keys.authorization) String token, @Body P2PRequest p2PRequest);

    @GET(Config.CROSS_BORDER_HISTORY)
    Observable<CrossBorderHistoryResponse> getcrossborderHistory(@Header(Keys.authorization) String token, @Query("payeeId") String payID);

    @GET(Config.USER_LIST_P2P)
    Observable<UserListP2p> getUserP2p(@Header(Keys.authorization) String token, @Query("walletId") String id);

    @POST(Config.EXISTING_USER_P2P)
    Observable<ExistingP2PResponse> getExistingUserP2p(@Header(Keys.authorization) String token, @Body JsonObject jsonObject);

    @DELETE(Config.DELETE_USER)
    Observable<JsonObject> deleteUser(@Header(Keys.authorization) String token, @Query("_id") String id);

    @POST(Config.SEND_TRANSACTION_EMAIL)
    Observable<JsonObject> sendTransactionEmail(@Header(Keys.authorization) String token, @Body TransactionEmailBody transactionEmailBody);

    @POST(Config.ADD_Wallet_FUND)
    Observable<JsonObject> addWalletFund(@Body JsonObject jsonObject);
       @POST(Config.LOAD_FUND_EMAIL)
    Observable<JsonObject> loadFundEmail(@Header(Keys.authorization) String token,@Body JsonObject jsonObject);


    @GET("/api/crossborder/validateIban/{iban}")
    Observable<JsonObject> validetIban(@Path("iban") String iban);

  @POST("/api/auth/sendSupportEmail")
    Observable<JsonObject> sendIssue(@Header(Keys.authorization) String token,@Body JsonObject jsonObject);


}
