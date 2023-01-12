package com.apt_x.app.views.activity.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;

import android.content.res.Configuration;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.apt_x.app.authsdk.verifyAucant.MainActivity;
import com.apt_x.app.utils.ConnectivityReceiver;
import com.apt_x.app.utils.FingerPrintEnable;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.kyc.KYCfailedActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityLoginBinding;
import com.apt_x.app.model.bean.LoginResponseBean;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.FingerprintHandler;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.forgotpassword.ForgotPasswordActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.signup.SignUpActivity;
import com.apt_x.app.views.base.BaseActivity;


import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener ,
        FingerprintHandler.login, DialogInterface.OnClickListener, FingerPrintEnable.onResult, ConnectivityReceiver.ConnectivityReceiverListener {

    LoginViewModel viewModel;
    private ActivityLoginBinding binding;
    public ApiCalls apiCalls;
    CallbackManager callbackManager;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient googleApiClient;
    private static final String KEY_NAME = "APTPAY";
    private static final int RC_SIGN_IN = 1;
    FingerprintManager.CryptoObject cryptoObject;
    FingerprintManager fingerprintManager;
    KeyStore keyStore;
    Cipher cipher;
    Utils utils;
    FingerprintHandler helper;
    FingerPrintEnable fingerPrintEnable;
    Context context = LoginActivity.this;
    Activity activity = LoginActivity.this;
    private int REQUEST_CODE_AUTH=101;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        fingerprintManager=(FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        if(fingerprintManager!=null&& fingerprintManager.isHardwareDetected() &&  Pref.IS_BIONEED)
        {
            binding.ivFingerPrint.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.ivFingerPrint.setVisibility(View.GONE);
        }
        initializeViews();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }
    Observer observer = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showToast(context, getString(R.string.enter_emailid));
                    break;

                case AppUtils.empty_password:
                    Utils.showToast(context, getString(R.string.enter_password));
                    break;

                case AppUtils.invalid_mail:
                    Utils.showToast(context, getString(R.string.enter_valid_employeeid));
                    break;
                case AppUtils.invalid_password:
                    Utils.showToast(context, getString(R.string.enter_valid_password));
                    break;
                case AppUtils.NO_INTERNET:
                    Utils.showToast(context, getString(R.string.internet_connection));
                    break;
                case AppUtils.SERVER_ERROR:
                    Utils.showToast(context, getString(R.string.server_error));
                    break;
                case AppUtils.USERREGISTERED:
                    Utils.showToast(context, getString(R.string.user_not_registered));
                    break;
            }
        }
    };
    Observer<LoginResponseBean> response_observer = new Observer<LoginResponseBean>() {

        @Override
        public void onChanged(@Nullable LoginResponseBean loginBean) {
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if(loginBean.getStatus()){
                if (loginBean.getToken() != null && !loginBean.getToken().isEmpty()) {
                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.ACCESS_TOKEN, loginBean.getToken());
                    Pref.setAccessToken(context, "Bearer "+loginBean.getToken());
                    Pref.setBoolean(context, true, Pref.REMEMBER_ME);
                    MyPref.getInstance(getApplicationContext()).writeBooleanPrefs( Pref.IS_LOGIN,true);
                    MyPref.getInstance(getApplicationContext()).writeBooleanPrefs( Pref.IS_ADDRESS_FILLED,true);

                   // MyPref.getInstance(getApplicationContext()).writeBooleanPrefs( Pref.IS_KYC_FILLED,true);

                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.APPID, String.valueOf(loginBean.getData().getAptCard_Id()));
                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.FULL_NAME, loginBean.getData().getFirst_name()+" "+loginBean.getData().getLastname());
                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.USER_SELFI,loginBean.getData().getProfilePicture());
                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.WALLET_ID,loginBean.getData().getWalletCreateId());

                    if (loginBean.getData().getKycDone()){
                        Log.e(TAG, "Kyc Done : "+loginBean.getData().getKycDone() );
                        MyPref.getInstance(getApplicationContext()).writeBooleanPrefs( Pref.IS_KYC_FILLED,true);
                        Pref.IS_FIRST = "1";



                        startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }


                }
            }
            else if(loginBean.getMessage()!=null){
                Utils.showToast(context,loginBean.getMessage());
            }
            else {
                Utils.showToast(context,getString(R.string.something_went_wrong));
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initializeViews() {
        Log.e("Date of birth ****",""+Utils.convertDate("12-17-1978"));


        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.tvLogin.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        apiCalls = ApiCalls.getInstance(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();
        fingerPrintEnable=new FingerPrintEnable(this,false,this);


        faceBookLogin();
       // enableFingerPrint();
        // Google login
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .build();


        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        binding.etUserPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Utils.hideSoftKeyboard(LoginActivity.this, binding.etUserPassword);
                return true; // Focus will do whatever you put in the logic.
            }
            return false;
        });
        binding.ivFingerPrint.setOnClickListener(v -> {
         /*   FingerPrintBottomFragment bottomSheet = new FingerPrintBottomFragment();
            bottomSheet.show(getSupportFragmentManager(), "bottom_Sheet");*/
            fingerPrintEnable.enableFingerPrint();
        });

  /*      binding.testkycpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, KYCfailedActivity.class));
            }
        });*/

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
  /*  void enableFingerPrint()
    {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        helper = new FingerprintHandler(this, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cryptoObject = new FingerprintManager.CryptoObject(cipher);
        }
        if (fingerprintManager != null && fingerprintManager.isHardwareDetected())
        {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            assert fingerprintManager != null;
            if (!fingerprintManager.isHardwareDetected()) {
                *//**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 *//*
                binding.ivFingerPrint.setVisibility(View.GONE);
                Utils.showToast(this,"Your Device does not have a Fingerprint Sensor");
            } else {
                binding.ivFingerPrint.setVisibility(View.VISIBLE);
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                    Utils.showToast(this,"Fingerprint authentication permission not enabled");
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {

                        Utils.showToast(this,"Register at least one fingerprint in Settings");
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            Utils.showToast(this,"Lock screen security not enabled in Settings");
                        } else {
                            generateKey();
                            if (cipherInit()) {
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }

    }*/

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }
        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }
    void faceBookLogin()
    {
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess: " + loginResult.getAccessToken().getUserId());
                Log.e(TAG,"UserId::"+loginResult.getAccessToken().getUserId());
                Log.e(TAG,"Token::"+loginResult.getAccessToken().getToken());
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "Cancelled: " );
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: " + error.toString());
            }
        });

        binding.loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

         if(resultCode==REQUEST_CODE_AUTH)
         {
           //  AuthorizationResponse authResponse = AuthorizationResponse.fromIntent(data);
           //  AuthorizationException authException = AuthorizationException.fromIntent(data);

           //  mAuthState = new AuthState(authResponse, authException);

             // Handle authorization response error here

             //retrieveTokens(authResponse);
         }


    }

   /* private void retrieveTokens(AuthorizationResponse authResponse) {
        TokenRequest tokenRequest = authResponse.createTokenExchangeRequest();

        AuthorizationService service = new AuthorizationService(this);

       *//* service.performTokenRequest(request, mClientAuthentication,
                new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(TokenResponse tokenResponse,
                                                        AuthorizationException tokenException) {
                     //   mAuthState.update(tokenResponse, tokenException);

                        // Handle token response error here

                       // persistAuthState(mAuthState);
                    }

                });*//*
    }*/

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            Log.e(TAG,"Google Email::"+account.getEmail());
            Log.e(TAG,"Google Name::"+account.getDisplayName());
            Log.e(TAG,"Google ID::"+account.getId());
            Log.e(TAG,"Google token id ::"+account.getIdToken());
            Utils.showDialog(this, getString(R.string.please_wait));
            if(account.getEmail()!=null)
            viewModel.doLoginGoogle(account.getId(),account.getEmail(),apiCalls);
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    //Method for logging out with google
    void  logoutGoogle()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()){
                            // do something
                        }else{
                            Toast.makeText(getApplicationContext(),"Session not close",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }




    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                if (viewModel.validateLogin(binding)) {
                    Utils.showDialog(this, getString(R.string.please_wait));
                    viewModel.doLogin(binding,apiCalls);



                }
                break;
            case R.id.tvSignUp:
                  startActivity(new Intent(context, SignUpActivity.class));
                break;
            case R.id.ivFb:
               // binding.loginButton.performClick();



            //    authorize();
                break;
            case R.id.ivGoogle:
                signIn();
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

  /*  private void authorize() {
        AuthorizationRequest authRequestBuilder = new AuthorizationRequest.Builder(
                mServiceConfiguration,
                "900330274579-hilidcl60l27qsl5naepdkpocrh7ln5u.apps.googleusercontent.com", // Client ID
                ResponseTypeValues.CODE,
                Uri.parse("com.example://oauth-callback") // Redirect URI
        ).build();





        AuthorizationService service = new AuthorizationService(this);

        Intent intent = service.getAuthorizationRequestIntent(authRequestBuilder);
        startActivityForResult(intent, REQUEST_CODE_AUTH);
    }*/

    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {

                            Log.d(TAG, "fb json object: " + object);
                            Log.d(TAG, "fb graph response: " + response);

                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            Log.e(TAG, "onCompleted: first_name" + first_name);
                            String last_name = object.getString("last_name");
                            Log.e(TAG, "onCompleted: last_name" + last_name);
//                            String gender = object.getString("gender");
//                            String birthday = object.getString("birthday");
//                            String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
//                            Log.e(TAG, "onCompleted: image_url" + image_url);
                            String email;
                            if (object.has("email")) {
                                email = object.getString("email");
                                Log.e(TAG, "onCompleted: email" + email);
                                if (email != null && first_name != null) {
                                    Utils.showDialog(getApplicationContext(), getString(R.string.please_wait));
                                    viewModel.doLoginGoogle(id,email, apiCalls);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void login() {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onclick(Boolean status) {
        System.out.println("Biometrics in Login activity call back" + status);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected)
         {
            Utils.showToast(getApplicationContext(),"No internet");

        }
    }

  @Override
    public void doLogout() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Timeout");
                builder.setMessage("Sorry this Session Timeout");
                builder.setCancelable(false);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                AlertDialog dialog = builder.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.blue));
            }
        });
    }
}
