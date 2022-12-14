package com.apt_x.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    private static final String SHARED_PREFS_NAME = "key_keep_pref";
    private static final String USER_NAME = "user_name";
    private static final String ACCESS_TOKEN = "Access_token";
    private static final String User_DETAIL = "user_detail";
    private static final String EMPLOYEE_ID = "employee_id";
    private static final String LOGIN_PASSWORD = "login_password";
    private static final String LANGUAGE = "language";

    public static final String REMEMBER_ME = "remember_me";
    public static final String IS_LOGIN = "is_login";
    public static final String IS_ADDRESS_FILLED = "IS_ADDRESS_FILLED";
    public static final String IS_KYC_FILLED = "IS_KYC_FILLED";

    public static final String COUNTRY_DATA = "country_data";
    public static final String RECIPIENT_DATA = "recipient_data";
    public static final String COME_FROM = "come_from";
    public static final String IS_BOIMETRIC = "is_biometric";
    public static String IS_FIRST = "0";
    public static String SIGNUPAUTO = "false";
    public static  String IS_DOB = "";
    public static float CRRate = 0;
    public static Boolean IS_BIONEED = false;


    /**
     * This method returns instance of shared preferences.
     * @param context
     * @return
     */
    private static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }


    /**
     * This method returns instance of shared preferences editor.
     * @param context
     * @return
     */
    private static SharedPreferences.Editor getEditor(Context context){
        return getSharedPrefs(context).edit();
    }


    /**
     * access token
     * @param context
     * @param value
     */
    public static void setAccessToken(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(ACCESS_TOKEN, value);
        editor.apply();
    }
    public static String getAccessToken(Context context){
        return getSharedPrefs(context).getString(ACCESS_TOKEN, "");
    }


    /**
     * user name
     * @param context
     * @param value
     */
    public static void setUserName(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(USER_NAME, value);
        editor.apply();
    }
    public static String getUserName(Context context){
        return getSharedPrefs(context).getString(USER_NAME, null);
    }

    /**
     * user detail
     * @param context
     * @param user_detail
     */
    public static void setUserDetail(Context context, String user_detail) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(User_DETAIL, user_detail);
        editor.apply();
    }
    public static String getUserDetail(Context context){
        return getSharedPrefs(context).getString(User_DETAIL, null);
    }


    /**
     * employee id
     * @param context
     * @param value
     */
    public static void setEmployeeID(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(EMPLOYEE_ID, value);
        editor.apply();
    }
    public static String getEmployeeID(Context context){
        return getSharedPrefs(context).getString(EMPLOYEE_ID, "");
    }

    /**
     * password
     * @param context
     * @param value
     */
    public static void setPassword(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(LOGIN_PASSWORD, value);
        editor.apply();
    }

    public static String getPassword(Context context){
        return getSharedPrefs(context).getString(LOGIN_PASSWORD, "");
    }


    public static void setBoolean(Context context, boolean value, String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key){
        return getSharedPrefs(context).getBoolean(key, false);
    }


}
