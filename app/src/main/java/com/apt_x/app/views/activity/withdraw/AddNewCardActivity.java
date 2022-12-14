package com.apt_x.app.views.activity.withdraw;

import static com.apt_x.app.utils.Utils.hideKeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddNewCardBinding;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.bean.AddEftAccountResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.profile.PaymentCardActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.bigkoo.pickerview.builder.TimePickerBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewCardActivity extends BaseActivity {
    private ActivityAddNewCardBinding binding;
    WithdrawViewModel viewModel;
    ApiCalls apicalls;
    Calendar  selectedDate;
    Context context = AddNewCardActivity.this;
    Activity activity = AddNewCardActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_card);
        viewModel = ViewModelProviders.of(this).get(WithdrawViewModel.class);
        super.onCreate(savedInstanceState);
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
    @Override
    public void initializeViews() {
        apicalls = ApiCalls.getInstance(this);
        viewModel.response_validator_add_bank.observe(this, transaction_response_observer_add_eft);
        viewModel.validator.observe(this,observer);
        binding.etExpirydate.setOnClickListener(this);
        binding.llUpper.ivBack.setOnClickListener(this);
        binding.llUpper.tvTitle.setText(R.string.add_apt_send_account);
    }
    Observer<GetProfileResponse> response_observer = countriesResponse -> {
        assert countriesResponse != null;

    };
    Observer<AddEftAccountResponse> transaction_response_observer_add_eft= new Observer<AddEftAccountResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable AddEftAccountResponse addEftAccountResponse) {
          if(addEftAccountResponse.getStatus())
          {
              startActivity(new Intent(AddNewCardActivity.this, PaymentCardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
              finish();
          }
          else
          {
              Utils.showToast(getApplicationContext(),addEftAccountResponse.getMessage());
          }
        }
    };




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvContinue:
                addCard();
                break;

            case R.id.etExpirydate:
                hideKeyboard(this);
                pickADate();
                break;



        }
    }


    private void addCard() {
        Utils.showDialog(this, getString(R.string.please_wait));
        viewModel.addCard(binding ,apicalls);

    }
    void pickADate() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH)+1 ,0);
        endDate.set(2050, 0,0);


        @SuppressLint("SimpleDateFormat") TimePickerBuilder timePickerBuilder = new TimePickerBuilder(this, (date, v1) -> {
              selectedDate = Calendar.getInstance();
            selectedDate.setTime(date);
            binding.etExpirydate.setText(new SimpleDateFormat("yyyy-MM").format(date));
        })
                .setType(new boolean[]{true, true, false, false, false, false})
                .setCancelText(getString(R.string.cancel))
                .setSubmitText(getString(R.string.done))
                .setOutSideCancelable(true)
                .setTitleText("YYYY-MM")
                .setTitleColor(0xFFFFFFFF)
                .isCyclic(false)
                .setSubmitColor(0xFFFFFFFF)
                .setCancelColor(0xFFFFFFFF)
                .setTitleBgColor(0xFF000000)
                .setBgColor(0xFF0B0815)
                .setContentTextSize(25)
                .setDividerColor(0xFF000000)
                .setTextColorCenter(0xFFFFFFFF)
                .setRangDate(startDate, endDate)
                .setLabel("", "", "", "", "", "")
                .isDialog(false);

        if (selectedDate != null) timePickerBuilder.setDate(selectedDate);
        else timePickerBuilder.setDate(startDate);
        timePickerBuilder.build().show();
    }

    Observer observer = (Observer<Integer>) value -> {
        switch (value) {
            case AppUtils.ENTER_DEBUTNUMBER:
                Utils.hideProgressDialog();
                Utils.showToast(AddNewCardActivity.this, getString(R.string.please_enter_debut_num));
                break;
            case AppUtils.NO_INTERNET:
                Utils.hideProgressDialog();
                Utils.showToast(AddNewCardActivity.this, getString(R.string.internet_connection));
                break;
            case AppUtils.SERVER_ERROR:
                Utils.hideProgressDialog();
                Utils.showToast(AddNewCardActivity.this, getString(R.string.server_error));
                break;
            case AppUtils.INVALID_DEBUTNUMBER:
                Utils.hideProgressDialog();
                Utils.showToast(AddNewCardActivity.this, getString(R.string.invalid_debit_card_number));
                break;


        }
    };

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