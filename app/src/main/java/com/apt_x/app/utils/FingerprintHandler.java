package com.apt_x.app.utils;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.CancellationSignal;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


@RequiresApi(api = 23)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    login login;
    CancellationSignal cancellationSignal;
    private boolean mSelfCancelled;

    public interface login {
        void login();
    }

    public FingerprintHandler(Context mContext, login lg) {
        this.context = mContext;
        this.login = lg;
    }

    public void startAuth(FingerprintManager manager, CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        if (ActivityCompat.checkSelfPermission(this.context, "android.permission.USE_FINGERPRINT") == 0) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }


    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fingerprint Authentication error\n");
            sb.append(errString);
            update(sb.toString());
        }
    }

    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        StringBuilder sb = new StringBuilder();
        sb.append("Fingerprint Authentication help\n");
        sb.append(helpString);
//        update(sb.toString());
    }

    public void onAuthenticationFailed() {
        update("Fingerprint Authentication failed.");
    }

    public void onAuthenticationSucceeded(AuthenticationResult result) {
        this.login.login();
    }

    private void update(String e) {
//        Utils.showToast(context,e);
//        ((ImageView) ((Activity) this.context).findViewById(R.id.ivFingerPrint)).setVisibility(View.GONE);
    }


    public void stopListening() {
        if (cancellationSignal != null) {
            mSelfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

}
