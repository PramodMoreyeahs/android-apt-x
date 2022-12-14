package com.apt_x.app.app;

import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDexApplication;

import com.apt_x.app.utils.ConnectivityReceiver;

import java.lang.reflect.Method;

public class MyApp extends MultiDexApplication {
    static MyApp instance;

    public static MyApp getInstance() {
        if (instance == null)
            instance = new MyApp();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = MyApp.this;
        enableStrictMode();
        instantiateFabric();

        /*boolean isLoggedIn = AuthUser.getInstance().injectContext(this).isLoggedIn();
        if (isLoggedIn) {
            String token = AuthUser.getInstance().injectContext(this).getCurrentUser().get_response().getAccess_token();
            ApiController.getInstance().updateInterceptor(new AuthTokenInterceptor(token));
        }*/
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

    private void enableStrictMode() {
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
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
