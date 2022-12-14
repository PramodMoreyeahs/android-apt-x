package com.apt_x.app.utils;

import android.Manifest;

import com.apt_x.app.BuildConfig;

public interface AppUtils {

    String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] STORAGE_CAMERA_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    String os="android";
    String versionName= BuildConfig.VERSION_NAME;
    String REQUEST="request";
    String RESPONSE="response";
    /**
     * login keys
     */
    int empty_id = 101;

    int invalid_mail = 102;
    int empty_password = 103;
    int empty_old_password = 104;
    int empty_confirm_password = 105;
    int match_confirm_password = 106;
    int password_length = 107;
    int empty_number= 108;
    int empty_name= 109;
    int invalid_number= 110;
    int invalid_password = 111;
    int  invalid_otp =112 ;
    int empty_otp = 113;
    int empty_last_name = 200;

    int empty_street = 114;
    int empty_house = 115;
    int empty_city = 116;
    int empty_zip = 117;
    int empty_state = 118;
    int empty_passport = 119;
    int empty_issue_date = 120;
    int empty_expiration_date = 121;
    int empty_country = 122;
    int confirm_email = 123;
    int first_name = 124;
    int last_name = 125;
    int empty_verify_email = 126;



    /**
     * Intent request code
     */
    int REQUEST_CODE_CAMERA = 202;
    int SERVER_ERROR = 203;
    int NO_INTERNET=204;
    int STATUS_SCAN_CODE = 205;
    int ENTER_AMOUNT=206;
    int ENTER_DEBUTNUMBER=207;
    int INVALID_DEBUTNUMBER=209;
    int ENTER_ACCOUNT_NUMBER=208;

    /**
     * dialog keys
     */
    int dialog_ok_click = 301;
    int dialog_request_succes = 301;
    int dialog_ok_to_finish = 302;
    int USERREGISTERED = 303;
    int CONFIRMPASSWORD = 304;

    /**
     * api status code
     */
    String STATUS_FAIL = "0";
    String STATUS_SUCCESS = "1";



}
