package com.apt_x.app.privacy.netcom.retrofit;


import com.apt_x.app.BuildConfig;

public interface Config {

    /*
    *
    * BASE URL FOR SERVER COMMUNICATION
    *
    * */
    public static final String BASE_URL = BuildConfig.BASE_URL;

    /**
     * API NAMES
     */

    String LOGIN_URL = "auth/signin";
    String WALLET_BALANCE = "wallet/getWalletBalance";
    String TRANSACTION_HISTORY = "wallet/GetWalletTransactionHistory";
    String ACTIVE_COUNTRY_SERVICE = "crossborder/getCountryServices";
    String GET_ACCOUNT_LIST = "disbursement/disbursement-instrument/list/";
    String GET_BANK = "crossborder/purposes/";
    String GET_KYC = "auth/kycDone";
    String CREATE_TRANSACTION = "crossborder/createTransaction";
    String GET_PROFILE = "users/me";
    String LOGIN_URL_GOOGLE = "auth/socialSignin";
    String SIGN_UP_GOOGLE = "auth/socialSignup";
    String SIGN_UP = "auth/signup";
    String GoogleGeocoderAPI = "https://maps.googleapis.com/maps/api/";
    String bseurlPlace = "https://maps.googleapis.com/maps/api/";
    String UPDATE_PROFILE = "auth/updateUserProfiles";
    String UPLOAD_PROFILE = "https://uatcaradmin.moreyeahs.in/api/upload/UploadFileAll";
    String CREATE_WALLET = "wallet/createWallet";
    String ADD_NEW_EFT_ACCOUNT = "disbursement/addDisbursementInstrument";
    String RESEND_OTP = "auth/reSendOtp";
    String VERIFY_OTP = "auth/verifyOtp";
    String VERIFY_LINK = "auth/identities/sendverificationlink";
    String RESEND_LINK = "auth/reSendVerificationLink";
    String COVERT_MONEY = "crossborder/calculate";
    String GET_ACTIVE_COUNTRIES = "auth/getActiveCountries";
    String FORGOT_PASSWORD_URL = "forgotpassword";
    String CHANGE_PASSWORD_URL = "auth/changePassword";
    String RESETPASSWORD = "auth/resetPassword";
    //todo tem comment for check url is right or not?
    //String ADD_ADDRESS_URL = "auth/updateUser/{payeeId}";
    String ADD_ADDRESS_URL = "auth/updateUserDetails";
    String WAIT_LIST = "auth//wList";
    String ADD_KYC = "auth/addKyc";
    String GET_COMPANY_CONSTANT = "auth/getCompanyConstants";
    String GET_CITY_RESULTS = "/maps/api/place/autocomplete/json";
    String POST_KYC_DATA = "/payees/119/kyc";
    String ADD_DISBURSEMENT = "/api/disbursement/addDisbursementV1";
    String ADD_FUND = "/api/auth/wallet/add";
    String ADD_DISBURSEMENT2 = "/api/disbursement/addDisbursementV1";
    String ADD_COUNTRY = "/api/auth/AddActiveCountries";
    String GET_USER_BY_EMAIL = "/api/auth/getUserByEmailSearch";
    String GET_PURPOSE = "/api/crossborder/purposes/";
    String ADD_NEW_RECIPIENT = "/api/crossborder/newRecipient";
    String GET_OCCUPATION = "/api/auth/getOccupationList";
    String ADD_BANK_DETAIL = "/api/crossborder/bankDetailsOfUser";
    String GET_IDENTITY = "/api/wallet/getWalletIdByEmail";
    String GET_BANK_DETAIL = "/api/crossborder/getBankDetailsOfUserByPayeeId";
    String GET_EXITING_RECIPIENT = "/api/crossborder/getUserByEmail";
    String SEND_WALLET_BALANCE = "/api/wallet/sendWalletBalance";
    String CROSS_BORDER_HISTORY = "/api/crossborder/getCrossborderTransactionHistory";
    String CREATE_USER_P2P = "/api/wallet/createTransactionOfP2P";
    String USER_LIST_P2P = "/api/wallet/getAllusers";
    String EXISTING_USER_P2P = "/api/wallet/getUserByEmail";
    String DELETE_USER = "/api/crossborder/deleteNewRecipient";
    String SEND_TRANSACTION_EMAIL = "/api/auth/sendtransectionemail";
    String ADD_Wallet_FUND = "/api/wallet/addWalletBalance";
    String VALIDATE_IBAN = "/api/crossborder/validateIban";
    String LOAD_FUND_EMAIL = "/api/wallet/loadFundEmail";





}

