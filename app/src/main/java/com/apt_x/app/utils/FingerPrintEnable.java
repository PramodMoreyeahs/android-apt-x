package com.apt_x.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.biometric.BiometricManager;
import androidx.
        biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.apt_x.app.R;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;

import java.util.concurrent.Executor;

public class FingerPrintEnable{
    Activity context;
    Boolean status;
    onResult onResult;
    public FingerPrintEnable(Activity context, Boolean status, onResult onResult)
    {

        this.context=context;
        this.status=status;
        this.onResult=onResult;

    }
    public void enableFingerPrint()
   {
       BiometricManager biometricManager = androidx.biometric.BiometricManager.from(context);
       switch (biometricManager.canAuthenticate()) {

           // this means we can use biometric sensor
           case BiometricManager.BIOMETRIC_SUCCESS:
             //  msgtex.setText("You can use the fingerprint sensor to login");
              // msgtex.setTextColor(Color.parseColor("#fafafa"));
               break;

           // this means that the device doesn't have fingerprint sensor
           case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
               Utils.showToast(context,context.getString(R.string.devices_not_fingerprint_sensor));
               break;

           // this means that biometric sensor is not available
           case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
               Utils.showToast(context,context.getString(R.string.biometric_unavailable));
               break;

           // this means that the device doesn't contain your fingerprint
           case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
               Utils.showToast(context,context.getString(R.string.your_device_dont_have_saved_security));

               break;


       }

       Executor executor = ContextCompat.getMainExecutor(context);
       // this will give us result of AUTHENTICATION
       final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
           @SuppressLint("RestrictedApi")
           @Override
           public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
               onResult.onclick(false);
               if(status)
               {
                   if (errorCode == BiometricPrompt.ERROR_LOCKOUT) {
                       Utils.showToast(context,context.getString(R.string.authenticate_failedwait));
                      context.finishAffinity();
                   }
                   if (errorCode == BiometricPrompt.ERROR_LOCKOUT_PERMANENT) {
                        Utils.showToast(context,context.getString(R.string.authenticate_failedblocked));
                        context.finishAffinity();
                   }
                   if (errorCode == BiometricPrompt.ERROR_USER_CANCELED || errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                     //  enableFingerPrint();
                       Utils.showToast(context,context.getString(R.string.user_cancel));
                       System.out.println("canceled bio");
                       // User canceled the operation// you can either show the dialog again here// or use alternate authentication (e.g. a password) - recommended way
                       context.finishAffinity();
                   }

               }


           }

           // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
           @Override
           public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {

               super.onAuthenticationSucceeded(result);
               System.out.println("onAuthenticationSucceeded" + result.getAuthenticationType());
               Toast.makeText(context, " Success", Toast.LENGTH_SHORT).show();
               MyPref.getInstance(context).writeBooleanPrefs(MyPref.USE_FINGER_PRINT,true);
               onResult.onclick(true);

           }
           @Override
           public void onAuthenticationFailed() {
               super.onAuthenticationFailed();
               enableFingerPrint();
               System.out.println("onAuthenticationfailed" );
              // context.finishAffinity();

           }



       });



       final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle(context.getString(R.string.biometric_login))
               .setDescription(context.getString(R.string.please_identify_your_finger))
               .setNegativeButtonText(context.getString(R.string.cancel)).setConfirmationRequired(true).build();
       biometricPrompt.authenticate(promptInfo);







   }
   public interface onResult
   {
       void onclick(Boolean status);
   }
}
