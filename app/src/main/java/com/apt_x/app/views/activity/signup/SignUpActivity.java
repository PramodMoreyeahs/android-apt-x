package com.apt_x.app.views.activity.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.apt_x.app.model.CreateWalletResponse;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.privacy.PrivacyPolicy;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.DataUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.apt_x.app.R;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.model.CountryCodeObject;
import com.apt_x.app.model.SignUpResponseBean;
import com.apt_x.app.model.SocialSignupResponse;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.login.LoginActivity;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.apt_x.app.views.adapter.DialCodeAdapter;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.views.customview.Link;
import com.apt_x.app.databinding.ActivitySignUpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        DialCodeAdapter.OnItemClickListener, DialogClickListener {
    SignUpViewModel viewModel;
    private ActivitySignUpBinding binding;
    public ApiCalls apiCalls;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String TAG = "MainActivity";
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    ArrayList<CountryCodeObject> countryCodeObjects;
    ArrayList<CountriesResponse.Data> countryCodeList = new ArrayList<>();
    DialCodeAdapter dialCodeAdapter;
    AppCompatEditText search;
    ObservableBoolean terms1 = new ObservableBoolean();
    ObservableBoolean terms2 = new ObservableBoolean();
    ObservableBoolean radioCheck = new ObservableBoolean();
    ObservableBoolean radiocheck2 = new ObservableBoolean();
    Dialog dialog;
    Context context = SignUpActivity.this;
    Activity activity = SignUpActivity.this;
    //   String countryId= "62b400656f070d306420787e"; //UAT
    String countryId = "62df73699427b30af2ff246a";//Staging
   //String countryId = "633eb5fe762f842b609cfc0e";//Staging
    // String countryId= "63314d126518a30f28b51930";//ngrok
   //   String countryId= "63280e12b7a27008e9898297";//Production

    ObservableBoolean emailcheck = new ObservableBoolean();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initializeViews();
    }

    //load the country codes
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("country_code.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json.replace("\n", "").replace("\r", "");
    }

    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {
                case AppUtils.empty_id:
                    Utils.showToast(context, getString(R.string.enter_employeeid));
                    break;
                case AppUtils.empty_password:
                    Utils.showToast(context, getString(R.string.enter_password));
                    break;
                case AppUtils.invalid_mail:
                    Utils.showToast(context, getString(R.string.enter_valid_employeeid));
                    break;
                case AppUtils.empty_name:
                    Utils.showToast(context, getString(R.string.enter_first_name));
                    break;
                case AppUtils.empty_last_name:
                    Utils.showToast(context, getString(R.string.enter_last_name));
                    break;
                case AppUtils.empty_number:
                    Utils.showToast(context, getString(R.string.enter_phone));
                    break;
                case AppUtils.invalid_number:
                    Utils.showToast(context, getString(R.string.enter_valid_phone));
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
            }
        }
    };

    Observer<SignUpResponseBean> response_observer = new Observer<SignUpResponseBean>() {

        @Override
        public void onChanged(@Nullable SignUpResponseBean loginBean) {
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (loginBean.getStatus()) {

                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.ACCESS_TOKEN, loginBean.getToken());
                Pref.setAccessToken(SignUpActivity.this, "Bearer " + loginBean.getToken());
                Pref.setBoolean(SignUpActivity.this, true, Pref.REMEMBER_ME);
                Pref.setBoolean(SignUpActivity.this, true, Pref.IS_LOGIN);
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.APPID, String.valueOf(loginBean.getData().getAptCard_Id()));
                viewModel.createWallet(MyPref.getInstance(SignUpActivity.this).readPrefs(MyPref.APPID), apiCalls);

               /* startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                finish();*/
            } else if (loginBean.getMessage() != null) {
                logoutGoogle();
                if (loginBean.getMessage().equals("User already registered")) {
                    Utils.showToast(context, getString(R.string.user_already_register));
                } else {
                    Utils.showToast(context, loginBean.getMessage());
                }

            } else {
                logoutGoogle();
                Utils.showToast(context, getString(R.string.something_went_wrong));
            }

        }
    };

    Observer<CreateWalletResponse> response_observer_create_wallet = new Observer<CreateWalletResponse>() {

        @Override
        public void onChanged(@Nullable CreateWalletResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (loginBean.getStatus()) {

                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.WALLET_ID, String.valueOf(loginBean.getData().getWalletCreateId()));
                // startActivity(new Intent(getApplicationContext(), KYCActivity.class));
                startActivity(new Intent(getApplicationContext(), VerifyPhoneActivity.class)
                        .putExtra(Keys.EMAIL, binding.etEmail.getText().toString()));
                //  finish();

                //todo change address screen
                /*startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                finish();*/
            } else if (loginBean.getMessage() != null) {
                logoutGoogle();
                Utils.showToast(context, loginBean.getMessage());
            } else {
                logoutGoogle();
                Utils.showToast(context, getString(R.string.something_went_wrong));
            }

        }
    };

    Observer<SocialSignupResponse> response_observer_social = new Observer<SocialSignupResponse>() {

        @Override
        public void onChanged(@Nullable SocialSignupResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (loginBean.getStatus()) {
                if (loginBean.getToken() != null && !loginBean.getToken().isEmpty()) {
                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.ACCESS_TOKEN, loginBean.getToken());
                    Pref.setAccessToken(context, "Bearer " + loginBean.getToken());
                    Pref.setBoolean(context, true, Pref.REMEMBER_ME);
                    Pref.setBoolean(context, true, Pref.IS_LOGIN);
                    MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.APPID, String.valueOf(loginBean.getData().getAptCard_Id()));

                    startActivity(new Intent(SignUpActivity.this, AddAddressActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            } else if (loginBean.getMessage() != null) {
                logoutGoogle();
                Utils.showToast(context, loginBean.getMessage());
            } else {
                logoutGoogle();
                Utils.showToast(context, getString(R.string.something_went_wrong));
            }
        }
    };

    Observer<CountriesResponse> response_observer_countries = new Observer<CountriesResponse>() {

        @Override
        public void onChanged(@Nullable CountriesResponse countriesResponse) {
            if (countriesResponse == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            countryCodeList.clear();
            if (countriesResponse.getData() != null && !countriesResponse.getData().isEmpty()) {
                countryCodeList.addAll(countriesResponse.getData());
            }
            if (!countryCodeList.isEmpty()) {
                binding.editCountryCode.setText(countryCodeList.get(0).getCountryCode());
                //countryId = countryCodeList.get(0).get_id();
                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(countryCodeList.get(0).getFlagUrl())
                        .into(binding.ivFlag);
            }
        }
    };

    @Override
    public void initializeViews() {
        context = this;
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.setEmailCheck(emailcheck);
        binding.setTerms1(terms1);
        binding.setTerms2(terms2);
        binding.setRadioCheck(radioCheck);
        binding.setRadioCheck2(radiocheck2);
        binding.tvContinue.setOnClickListener(this);
        binding.editCountryCode.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        viewModel.response_user_validator.observe(this, userObserver);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator_create_wallet.observe(this, response_observer_create_wallet);

        viewModel.response_validator.observe(this, response_observer);
        viewModel.response_validator_social.observe(this, response_observer_social);
        viewModel.response_countries.observe(this, response_observer_countries);
        apiCalls = ApiCalls.getInstance(SignUpActivity.this);
        // viewModel.getActiveCountry(apiCalls);
        binding.etEmail.addTextChangedListener(emailTextWatcher);
       customTextView(binding.tvPrivacy);
        //  makeTextViewResizable(binding.tvTerms, 2, "see more", true);


        try {
            countryCodeObjects = new ArrayList<>();
            Gson gson = new Gson();
            String jsonOutput = loadJSONFromAsset();
            Type listType = new TypeToken<List<CountryCodeObject>>() {
            }.getType();
            countryCodeObjects = gson.fromJson(jsonOutput, listType);
            Log.e("size", "" + countryCodeObjects.size());

        } catch (Exception e) {
            e.printStackTrace();
        }


        callbackManager = CallbackManager.Factory.create();
        faceBookLogin();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        binding.etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Utils.hideSoftKeyboard(SignUpActivity.this, binding.etPassword);
                return true; // Focus will do whatever you put in the logic.
            }
            return false;
        });

        Utils.addLink(binding.tvTermsConditionPrivacyPolicy,
                getString(R.string.userAgreement), true,
                getResources().getColor(R.color.colorAccent)).setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                startActivity(new Intent(SignUpActivity.this, PrivacyPolicy.class).putExtra("type", "terms"));

            }
        });
        Utils.addLink(binding.tvTermsConditionPrivacyPolicy,
                getString(R.string.privacyPolicy), true,
                getResources().getColor(R.color.colorAccent)).setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                startActivity(new Intent(SignUpActivity.this, PrivacyPolicy.class).putExtra("type", "privacy"));
            }
        });
    }

    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {
                viewModel.getuserValidate(apiCalls, charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    };

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    void faceBookLogin() {
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess: " + loginResult.getAccessToken().getUserId());
                Log.e(TAG, "UserId::" + loginResult.getAccessToken().getUserId());
                Log.e(TAG, "Token::" + loginResult.getAccessToken().getToken());
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "Cancelled: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: " + error.toString());
            }
        });

        binding.loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

    }

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
                                    viewModel.doSignUpGoogle("", id, first_name, last_name, email, countryId, apiCalls);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Log.e(TAG, "Google Email::" + account.getEmail());
            Log.e(TAG, "Google Name::" + account.getDisplayName());
            Log.e(TAG, "Google ID::" + account.getId());
            Log.e(TAG, "Google Token ID::" + account.getIdToken());
//            Toast.makeText(getApplicationContext(), "Sign in success", Toast.LENGTH_LONG).show();
            if (account.getEmail() != null && account.getDisplayName() != null) {
                Utils.showDialog(this, getString(R.string.please_wait));
                if (account.getDisplayName().contains(" ")) {
                    String firstName = account.getDisplayName().split(" ")[0];
                    String lastName = account.getDisplayName().split(" ")[1];
                    viewModel.doSignUpGoogle("", account.getId(), firstName, lastName, account.getEmail(), countryId, apiCalls);
                } else {
                    viewModel.doSignUpGoogle("", account.getId(), account.getDisplayName(), "", account.getEmail(), countryId, apiCalls);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    //Method for logging out with google

    void logoutGoogle() {
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@io.reactivex.annotations.NonNull Status status) {
                        if (status.isSuccess()) {

                            // do something
                        } else {
                            Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContinue: {
                if (viewModel.validateSignUp(binding)) {
                    if (terms1.get() == false) {
                        Utils.showToast(getApplicationContext(), getString(R.string.please_check_privacy));
                    } /*else if (terms2.get() == false) {
                        Utils.showToast(getApplicationContext(), "Please check all privacy policy");
                    }*/
                    else if (radioCheck.get() == false) {
                        Utils.showToast(getApplicationContext(), getString(R.string.please_check_terms_condition));
                    }
                    else {
                        Utils.showDialog(this, getString(R.string.please_wait));
                        viewModel.doSignUp(binding, countryId, apiCalls);
                    }

                 /*   if(emailcheck.get()==true)
                    {
                        Utils.showToast(getApplicationContext(),"Try with another email Address");
                    }
                    else
                    {
                        Utils.showDialog(this, getString(R.string.please_wait));
                        startActivity(new Intent(SignUpActivity.this,AddOccupationActivity.class)
                                .putExtra("bundle",setBundle(binding.etFistName.getText().toString(),binding.etLastName.getText().toString(),binding.etNumber.getText().toString()
                                        ,binding.etEmail.getText().toString(),binding.etPassword.getText().toString(),countryId)));
                    }*/


                }
            }
            break;
            case R.id.ivFb:
                binding.loginButton.performClick();
                break;
            case R.id.ivGoogle:
                signIn();
                break;
            case R.id.tvSignIn:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            case R.id.edit_country_Code:
                /*if (countryCodeList.size() >= 1) {
                    showCountryCodes();
                }*/

                break;
            case R.id.ivBack:
                onBackPressed();

                break;

            case R.id.check1:
                if (terms1.get() == true) {
                    terms1.set(false);
                } else {
                    terms1.set(true);
                }
                break;
            case R.id.check2:
                if (terms2.get() == true) {
                    terms2.set(false);
                } else {
                    terms2.set(true);
                }
                break;

            case R.id.radio1:
                if (radioCheck.get()) {
                    radiocheck2.set(false);
                } else {
                    radioCheck.set(true);
                    radiocheck2.set(false);
                }
                break;
            case R.id.radio2:
                if (radiocheck2.get()) {
                    radioCheck.set(false);
                } else {
                    radiocheck2.set(true);
                    radioCheck.set(false);
                }


                break;
            case R.id.tv_terms:
                startActivity(new Intent(SignUpActivity.this, PrivacyPolicy.class).putExtra("type", "terms"));


                break;
        }
    }

    private Bundle setBundle(String firstName, String lastName, String number, String email, String password, String countryId) {
        Bundle bundle = new Bundle();
        bundle.putString("firstName", firstName);
        bundle.putString("lastName", lastName);
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("number", number);
        bundle.putString("countryId", countryId);
        return bundle;
    }

    Observer<GetUserByEmail> userObserver = new Observer<GetUserByEmail>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetUserByEmail getUserByEmail) {
         /*   if(getUserByEmail.getData().size()==0)
            {
                emailcheck.set(false);
            }
            else
            {

                emailcheck.set(true);
            }*/


        }
    };

    void showCountryCodes() {
        dialog = Utils.showCustomDialog(SignUpActivity.this, R.layout.dialog_country_dial_code);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv_dial_code);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SignUpActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dialCodeAdapter = new DialCodeAdapter(SignUpActivity.this, countryCodeList);
        recyclerView.setAdapter(dialCodeAdapter);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(SignUpActivity.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.decorater_selctor));
        recyclerView.addItemDecoration(itemDecorator);
        dialCodeAdapter.setOnItemClickListener(SignUpActivity.this);
        search = dialog.findViewById(R.id.sv_doctor);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dialCodeAdapter.getFilter().filter(editable);
            }
        });
        dialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "ConnectionFailed::" + connectionResult.getErrorMessage());
    }


    @Override
    public void onItemClick(int position, int id) {
        if (id == R.id.rootLayout) {
            Utils.hideSoftKeyboard(SignUpActivity.this, search);
            countryId = dialCodeAdapter.getList().get(position).get_id();
            binding.editCountryCode.setText(dialCodeAdapter.getList().get(position).getCountryCode());
            Glide
                    .with(this)
                    .asBitmap()
                    .load(dialCodeAdapter.getList().get(position).getFlagUrl())
                    .into(binding.ivFlag);

            dialog.dismiss();
        }
    }


    @Override
    public void onDialogClick(int which, int requestCode) {

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new AddOccupationActivity.MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#2FB1F8"));
        }

        @Override
        public void onClick(View widget) {


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
