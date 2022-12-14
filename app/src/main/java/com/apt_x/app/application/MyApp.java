package com.apt_x.app.application;

import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.apt_x.app.BuildConfig;
import com.apt_x.app.model.bean.BaseRequestEntity;
import com.apt_x.app.model.bean.DaoMaster;
import com.apt_x.app.model.bean.DaoSession;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.RetrofitHolder;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.home.LogOutTimerUtil;

import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Method;

public class MyApp  extends MultiDexApplication implements LogOutTimerUtil.LogOutListener  {
    static MyApp instance;
    private static boolean activityVisible;
    private DaoSession daoSession;

    public static MyApp getInstance() {
        if (instance == null)
            instance = new MyApp();
        return instance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStart() {
        LogOutTimerUtil.startLogoutTimer(instance, instance);
    }

    public static void activityInteraction() {
        LogOutTimerUtil.startLogoutTimer(instance, instance);
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    /*
     *
     *
     * Method to set App body for api call..
     *
     * */
    public static BaseRequestEntity getBaseEntity(boolean includeToken) {
        BaseRequestEntity baseRequestEntity = new BaseRequestEntity();
        baseRequestEntity.setApi_key(Keys.API_KEY);
        baseRequestEntity.setDevice_id(Utils.getDeviceID());
        baseRequestEntity.setDevice_token("dfsfsdfsdfsdf"); //put firebase app token here from preferences
        baseRequestEntity.setDevice_type(Keys.TYPE_ANDROID);
        if (includeToken) {
            baseRequestEntity.setToken_type(Keys.TOKEN_TYPE);
            baseRequestEntity.setAccess_token(Pref.getAccessToken(instance));
        }
        return baseRequestEntity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = MyApp.this;

        try {

            enableStricMode();
            instantiateFabric();


            /**
             * init retrofit client to call network services
             */
            RetrofitHolder retrofitHolder = new RetrofitHolder(instance);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /*
         *
         * Initilizing Dao for the app
         *
         * */
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "myApp-db");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();


    }

    /*
     *
     *  Get Dao Session by creating application class context for dataabse query.
     *
     * */
    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void instantiateFabric() {
        try {
            if (!BuildConfig.DEBUG) {
//                Fabric.with(this, new Crashlytics());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void enableStricMode() {
        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void doLogout() {

    }
}
