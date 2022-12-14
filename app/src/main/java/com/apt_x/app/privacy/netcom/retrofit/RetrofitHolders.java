package com.apt_x.app.privacy.netcom.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.apt_x.app.BuildConfig;


import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Initializing retrofit
 */
public class RetrofitHolders {

    public static final int TIMEOUT = 60;
    private static AppStructureAPI service;
    private Context context;
    private Retrofit retrofit;


    public RetrofitHolders(Context context) {
        this.context = context;
//        initService();
        getClient();
    }

    public static AppStructureAPI getService() {
        return service;
    }

    private void initService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .baseUrl(Config.GoogleGeocoderAPI)
                    .build();
        }
        service = retrofit.create(AppStructureAPI.class);
    }

    public  void getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .baseUrl(Config.GoogleGeocoderAPI)
                    .build();
        }
        service = retrofit.create(AppStructureAPI.class);

    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(TIMEOUT, TimeUnit.SECONDS);

        okHttpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .header("Noencryption", "true ")
                    .build();
            return chain.proceed(request);
        });
//        okHttpClient.addInterceptor(chain -> chain.proceed(getRequestWithHeaders(chain)));
        // If you need an Interceptor to add some header
//        okHttpClient.addInterceptor();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(interceptor);
        }
        return okHttpClient.build();

    }

    // creating a TrustManager that trusts the CAs in our KeyStore
    private TrustManagerFactory createTrustManagerCAs(KeyStore keyStore) {
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory managerFactory = null;
        try {
            managerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert managerFactory != null;
            managerFactory.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return managerFactory;
    }

    /**
     * set header in need
     */
    private Request getRequestWithHeaders(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
//        if (chain.request().url().toString().contains(Config.BASE_URL)) {
//            builder.addHeader(HEADER_API_KEY, Utils.getApiKey());
//            builder.addHeader(HEADER_DEVICE_ID, Utils.getDeviceID(context));
//            builder.addHeader(HEADER_DEVICE_TOKEN, Utils.getToken());
//            builder.addHeader(HEADER_DEVICE_TYPE, Utils.getDeviceType());

//            try {
//                JSONObject settingJsonObject = new JSONObject();
//                settingJsonObject.put("CurrentVersion", BuildConfig.VERSION_NAME);
//                if (Config.USE_BAIDU) {
//                    settingJsonObject.put(REGION_HEADER, ASIA_CHINA_REGION);
//                } else {
//                    settingJsonObject.put(REGION_HEADER, ASIA_REGION);
//                }
//                byte[] settingsByteArray = settingJsonObject.toString().getBytes("UTF-8");
//                builder.addHeader(SETTINGS_HEADER, Base64.encodeToString(settingsByteArray, Base64.NO_WRAP));
//            } catch (Exception e) {
//                LogUtils.e("RetrofitHolder", "Add header settings json" + e, e);
//            }
//        }
        return builder.build();
    }

}
