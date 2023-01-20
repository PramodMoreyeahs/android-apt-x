package com.apt_x.app.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.apt_x.app.application.MyApp;


/*
*
*
* Class id used to store the app data in share perfrence.
*
*
*
* */
@SuppressWarnings("ALL")
public class MyPref {

    public static final String HOME_SCREEN_BACKGROUND_IMG_ID= "image_id";
    public static final String USER_ID = "userid";
    public static final String VERIFICATON_CODE = "verificationCode";
    public static final String USER_PROFILE_IMAGE = "userProfileImage";
    public static final String USER_NAME = "userName";
    public static final String USER_MOBILE = "userMobile";
    public static final String USER_GENDER = "user_gender";
    public static final String USER_COUNTRY_DIAL_CODE = "dialCode";
    public static final String USER_DOB = "userDob";
    public static final String ALMOST_EXPIRED_COUNT = "almostExpiredCount";
    public static final String CATEGORY_LIST_STATUS = "callApi";
    public static final String EMAILINTERAC = "EMAILINTERAC";
    public static final String APPID = "APPID";
    public static final String FULL_NAME = "full_name";
    public static final String WALLET_ID = "WALLETID";
    public static final String WALLET_BALANCE = "wallet_balance";

    @SuppressLint("StaticFieldLeak")
    static  Context mContext;
    private static final String PERFERENCE_NAME = "Kalamansee";
    private static SharedPreferences _sPrefs = null;
    private static SharedPreferences.Editor _editor = null;
    @SuppressLint("StaticFieldLeak")
    private static MyPref _instance = null;
    public static final String LANG_TYPE_PREF = "language_pref";
    public static final String DOBSHARED = "";
    public static final String CHINEASE_LANG= "2";
    public static final String ENGLISH_LANG= "1";
    public static final String VERSION_CODE= "versionCode";
    public static final String EMAIL_ID_USER= "email_id_usr";
    public static final String LANG_ENG= "en";
    public static final String LANG_CHINESE_CH= "ch";
    public static final String LANG_CHI= "zh";
    public static final String IsRedeemSuccuss="isRedeem";
    public static final String IsWalkThroughSeened="isWalkThroughSeened";
    public static final String BONUS="bonus";
    public static final String VENDOR_NAME="vendorName";
    public static final String USE_FINGER_PRINT="USE_FINGER_PRINT";
    public static final String ACCESS_TOKEN="ACCESS_TOKEN";
    public static final String CaptureIdBitmap="CaptureIdBitmap";
    public static final String USER_SELFI="USER_SELFI";
    public static final String USER_SELFI1="USER_SELFI1";
    public static final String USER_AMOUNT="user_amount";
    public static final String EXCHANGE_RATE="exchange_rate";
    public static final String CAD_AMOUNT="cad_amount";
    public static final String SERVICE_FEE="service_fee";
    public static final String USER_EMAIL="user_email";




    public MyPref() {
    }
    public MyPref(Context context) {
        _sPrefs = context.getSharedPreferences(PERFERENCE_NAME,
                Context.MODE_PRIVATE);
    }
    @SuppressLint("CommitPrefEdits")
    public static MyPref getInstance(Context context) {
        mContext = context;
        if (_instance == null) {
            _instance = new MyPref();
        }
        if(context==null){
            context= MyApp.getInstance().getApplicationContext();
        }
        _sPrefs = context.getSharedPreferences(PERFERENCE_NAME, Context.MODE_PRIVATE);
        _editor = _sPrefs.edit();
        return _instance;
    }
    public static void setInstance(MyPref instance) {
        // TODO Auto-generated method stub
        _instance = instance;
    }
    public String readPrefs(String pref_name) {
        return _sPrefs.getString(pref_name, "");
    }
    public String readPrefs(String pref_name, String defaultVaule) {
        return _sPrefs.getString(pref_name, defaultVaule);
    }
    public void writePrefs(String pref_name, String pref_val) {
        _editor.putString(pref_name, pref_val);
        _editor.commit();
    }
    public void clearPrefs() {
        _editor.clear();
        _editor.commit();
    }
    public boolean readBooleanPrefs(String pref_name) {
        return _sPrefs.getBoolean(pref_name, false);
    }
    public boolean readBooleanPrefs(String pref_name, boolean value) {
        return _sPrefs.getBoolean(pref_name, value);
    }
    public int readIntegerPrefs(String pref_name) {
        return _sPrefs.getInt(pref_name, -1);
    }
    public int readIntegerPrefsGeo(String pref_name) {
        return _sPrefs.getInt(pref_name, 0);
    }
    public int readIntegerPrefs(String pref_name, int defaultValue) {
        return _sPrefs.getInt(pref_name, defaultValue);
    }
    public long readLongPrefs(String pref_name, long defaultValue) {
        return _sPrefs.getLong(pref_name, defaultValue);
    }
    public void writeBooleanPrefs(String pref_name, boolean pref_val) {
        _editor.putBoolean(pref_name, pref_val);
        _editor.commit();
    }
    public void writeIntegerPref(String pref_name, int pref_val) {
        _editor.putInt(pref_name, pref_val);
        _editor.commit();
    }
    public void writeLongPref(String pref_name, long pref_val) {
        _editor.putLong(pref_name, pref_val);
        _editor.commit();
    }
    public void removePref(String pref_name) {
        _editor.remove(pref_name);
        _editor.commit();
    }


}
