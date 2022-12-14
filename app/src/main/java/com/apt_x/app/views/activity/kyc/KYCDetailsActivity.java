package com.apt_x.app.views.activity.kyc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.apt_x.app.BuildConfig;
import com.apt_x.app.model.KYCResponse;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityPassportDetailsBinding;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class KYCDetailsActivity extends BaseActivity {

    ActivityPassportDetailsBinding binding;
    String detail;
    ApiCalls apiCalls;
    KYCViewModel kycViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    File file = null;
    Bitmap bitmap = null;
    String bitmapString = null;
    String KYCURL = "";
    String LiveApiKey = "";
    String ApiKey = "";
    String sanboxApiKey = "";
    String identificationType = "";
    Context context = KYCDetailsActivity.this;
    Activity activity = KYCDetailsActivity.this;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_passport_details);
        kycViewModel = ViewModelProviders.of(this).get(KYCViewModel.class);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        if (getIntent() != null) {
            detail = getIntent().getStringExtra(Keys.detail);
            KYCURL = getIntent().getStringExtra(Keys.KYCURL);
            LiveApiKey = getIntent().getStringExtra(Keys.LiveApiKey);
            ApiKey = getIntent().getStringExtra(Keys.ApiKey);
            sanboxApiKey = getIntent().getStringExtra(Keys.sanboxApiKey);
//            byte[] byteArray = getIntent().getByteArrayExtra(Keys.bitMapToSend);
//            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        bitmapString =MyPref.getInstance(this).readPrefs(MyPref.CaptureIdBitmap);
        bitmap=StringToBitMap(bitmapString);
        apiCalls = ApiCalls.getInstance(KYCDetailsActivity.this);
        binding.etDateOfIssue.setOnClickListener(this);
        binding.header.ivBack.setOnClickListener(this);
        binding.etDateOfExpiration.setOnClickListener(this);
        kycViewModel.validator.observe(this, observer);
        kycViewModel.response_validator.observe(this, responseObserver);

//        switch (detail) {
//            case Keys.passport:
//                identificationType=Keys.PASSPORT;
//                binding.header.tvTitle.setText(getString(R.string.passport));
//                binding.tvTitle.setText(getString(R.string.passport_details));
//                binding.tvPlaceHolder.setText(getString(R.string.passport_number));
//                binding.etPassportNumber.setHint(getString(R.string.passport_number));
//                break;
//            case Keys.driving:
//                identificationType=Keys.DRIVING_LICENSE;
//                binding.header.tvTitle.setText(getString(R.string.driving_license));
//                binding.tvTitle.setText(getString(R.string.driving_license_details));
//                binding.tvPlaceHolder.setText(getString(R.string.driving_license_number));
//                binding.etPassportNumber.setHint(getString(R.string.driving_license_number));
//                break;
//            case Keys.permanentR:
//                identificationType=Keys.PERMANENT_RESIDENT_CARD;
//                binding.header.tvTitle.setText(getString(R.string.permanent_resident_card));
//                binding.tvTitle.setText(getString(R.string.permanent_resident_card_details));
//                binding.tvPlaceHolder.setText(getString(R.string.card_number));
//                binding.etPassportNumber.setHint(getString(R.string.card_number));
//                break;
//            case Keys.identity:
//                identificationType=Keys.IDENTITY_CARD;
//                binding.header.tvTitle.setText(getString(R.string.identity_card));
//                binding.tvTitle.setText(getString(R.string.identity_card_details));
//                binding.tvPlaceHolder.setText(getString(R.string.card_number));
//                binding.etPassportNumber.setHint(getString(R.string.card_number));
//                break;
//            case Keys.citizen:
//                identificationType=Keys.CITIZENSHIP_CARD;
//                binding.header.tvTitle.setText(getString(R.string.citizenship_card));
//                binding.tvTitle.setText(getString(R.string.citizenship_card_details));
//                binding.tvPlaceHolder.setText(getString(R.string.card_number));
//                binding.etPassportNumber.setHint(getString(R.string.card_number));
//                break;
//            case Keys.secure:
//                identificationType=Keys.SECURE_CERTIFICATE_OF_INDIAN_STATUS;
//                binding.header.tvTitle.setText(getString(R.string.secure_certificate_of_indian_status));
//                binding.tvTitle.setText(getString(R.string.secure_certificate_of_indian_status_details));
//                binding.tvPlaceHolder.setText(getString(R.string.card_number));
//                binding.etPassportNumber.setHint(getString(R.string.card_number));
//                break;
//        }
        binding.tvContinue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String etPassportNumber = binding.etPassportNumber.getText().toString().trim();
                String etDateOfIssue = binding.etDateOfIssue.getText().toString().trim();
                String etDateOfExpiration = binding.etDateOfExpiration.getText().toString().trim();
                String etState = Objects.requireNonNull(binding.etState.getText()).toString().trim();
                String etCountry = Objects.requireNonNull(binding.etCountry.getText()).toString().trim();

                if(!etPassportNumber.isEmpty()&&!etDateOfIssue.isEmpty()
                &&!etDateOfExpiration.isEmpty()&&!etState.isEmpty()
                        &&!etCountry.isEmpty()){
                    binding.tvContinue.setEnabled(true);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(KYCDetailsActivity.this, R.drawable.tv_btn_bg));
                }
                else {
                    binding.tvContinue.setEnabled(false);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(KYCDetailsActivity.this, R.drawable.disable_bg));
                }
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.etDateOfIssue.setText(sdf.format(myCalendar.getTime()));
        binding.etDateOfExpiration.setText("");
    }

    DatePickerDialog.OnDateSetListener date1 = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel1();
    };

    private void updateLabel1() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.etDateOfExpiration.setText(sdf.format(myCalendar.getTime()));
    }

    Observer<Integer> observer = integer -> {
        switch (integer) {
            case AppUtils.empty_number:
                Utils.showToast(KYCDetailsActivity.this, getString(R.string.empty_passport));
                break;
            case AppUtils.empty_issue_date:
                Utils.showToast(KYCDetailsActivity.this, getString(R.string.empty_issue_date));
                break;
            case AppUtils.empty_expiration_date:
                Utils.showToast(KYCDetailsActivity.this, getString(R.string.empty_expiration_date));
                break;
            case AppUtils.empty_state:
                Utils.showToast(KYCDetailsActivity.this, getString(R.string.state_empty));
                break;
            case AppUtils.empty_country:
                Utils.showToast(KYCDetailsActivity.this, getString(R.string.empty_country));
                break;

        }
    };


    Observer<KYCResponse> responseObserver = kycResponse ->
    {
        Utils.hideProgressDialog();
        if (kycResponse.getStatus()) {
            Pref.setBoolean(KYCDetailsActivity.this, true, Pref.IS_KYC_FILLED);
            startActivity(new Intent(KYCDetailsActivity.this, HomeActivity.class));
            finish();
        } else {
            Utils.showToast(KYCDetailsActivity.this, kycResponse.getMessage());
        }
    };


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContinue:
                if (kycViewModel.validatePassportForm(binding)) {
                    try {
                        callApiGetEvents();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.etDateOfIssue:
                DatePickerDialog dialog1 = new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog1.getDatePicker().setMaxDate(new Date().getTime());
                dialog1.show();
                break;
            case R.id.etDateOfExpiration:
                if (binding.etDateOfIssue.getText().toString().isEmpty()) {
                    Utils.showToast(this, getString(R.string.please_sele));
                } else {
                    DatePickerDialog dialog = new DatePickerDialog(this, date1, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 1));
                    dialog.show();
                }
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private void callApiGetEvents() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String tempphotoName =
                Utils.randomString(7) + formattedDate + c.toString();
        String photoName = tempphotoName.replaceAll("[^a-zA-Z0-9]", "");
        file = saveBitmap(this, bitmap, photoName);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("identificationNumber", binding.etPassportNumber.getText().toString().trim())
                .addFormDataPart("identificationDateOfExpiration", binding.etDateOfExpiration.getText().toString().trim())
                .addFormDataPart("identificationLocation", binding.etCountry.getText().toString())
                .addFormDataPart("identificationDate", binding.etDateOfIssue.getText().toString().trim())
                .addFormDataPart("identificationType", identificationType)
                .addFormDataPart("virtual", "true")
                .addFormDataPart("identificationFile", photoName,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .build();
        String aptCardId = MyPref.getInstance(this).readPrefs(MyPref.APPID);
        String apiUrl = "";
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            apiUrl = sanboxApiKey + KYCURL.split(":")[0] + aptCardId + KYCURL.split("payee_id")[1];
        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {
            apiUrl = LiveApiKey + KYCURL.split(":")[0] + aptCardId + KYCURL.split("payee_id")[1];
        }
        System.out.println("apiUrl::"+apiUrl);
        Request request = new Request.Builder()
                .url(apiUrl)
                .method("POST", body)
                .addHeader("AptPayApiKey", "ohl9KWxW2rdtx9f3EEmhzQaoAdtQ8d")
                .addHeader("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>")
                .build();
        Utils.showDialog(this, getString(R.string.please_wait));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("On Failure" + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 201) {
                    System.out.println("onSuccess");
                    kycViewModel.submitKYC(binding,apiCalls,identificationType);
                } else {
                    Utils.hideProgressDialog();
                    System.out.println("OnSuccess::code" + response.code());
                }
            }
        });

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private File saveBitmap(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".png");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
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