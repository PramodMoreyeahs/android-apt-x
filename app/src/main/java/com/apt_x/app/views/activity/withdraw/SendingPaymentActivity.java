package com.apt_x.app.views.activity.withdraw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.application.MyApp;
import com.apt_x.app.databinding.ActivityMoneySendingToBinding;
import com.apt_x.app.model.AddDisburesementResponse;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.model.bean.GetBankListResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseActivity;

public class SendingPaymentActivity extends BaseActivity {
    private ActivityMoneySendingToBinding binding;
    WithdrawViewModel viewModel;
    ApiCalls apicalls;
    GetBankListResponse.DataEntity _data;
    Context context = SendingPaymentActivity.this;
    Activity activity = SendingPaymentActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_money_sending_to);
        viewModel = ViewModelProviders.of(this).get(WithdrawViewModel.class);
        apicalls = ApiCalls.getInstance(this);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.validator.observe(this, observer);
        viewModel.response_add_disbursment.observe(this, response_addDisbursment_observer);

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
        binding.llUpper.ivBack.setOnClickListener(this);
        binding.llUpper.tvTitle.setText("Sending to");
        viewModel.response_validator.observe(this,response_observer);
        binding.tv10.setOnClickListener(this);
        binding.tv20.setOnClickListener(this);
        binding.tv50.setOnClickListener(this);
        binding.tv100.setOnClickListener(this);
        binding.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                       if(s.length()<0)
                       {
                          binding.tvDollar.setTextColor(getResources().getColor(R.color.text_color));
                       } else {
                           binding.tvDollar.setTextColor(getResources().getColor(R.color.white));
                       }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(getIntent()!=null)
        {
            if(getIntent().getStringExtra("TYPE").equals("card"))
            {
                _data = (GetBankListResponse.DataEntity) getIntent().getSerializableExtra("bandData");
            }
            else
            {
                _data = (GetBankListResponse.DataEntity) getIntent().getSerializableExtra("bandData");
            }
        }



        getAccountList();

    }

    private void getAccountList() {
        String payeeId = (MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));
        Utils.showDialog(this,"Loading");
        viewModel.getWalletBalance(apicalls);




    }

    @Override
    public void onClick(View view) {

        if (view == binding.llUpper.ivBack) {
            onBackPressed();
        }
        if (view == binding.tvContinue) {
            Utils.showDialog(this, getString(R.string.please_wait));
            if(getIntent().getStringExtra("TYPE").equals("card"))
            {
                viewModel.addCardDisbursment(binding, _data, apicalls);
            }
            else
            {
                viewModel.addDisbursment(binding, _data, apicalls);
            }
        }
        if (view == binding.tv10) {
           binding.etAmount.setText("10");
        }
          if (view == binding.tv20) {
           binding.etAmount.setText("20");
        }
        if (view == binding.tv50) {
            binding.etAmount.setText("50");
        }

          if (view == binding.tv100) {
           binding.etAmount.setText("100");
        }
    }

    Observer<GetWalletBalanceResponse> response_observer = new Observer<GetWalletBalanceResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetWalletBalanceResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(SendingPaymentActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (loginBean.getStatus()) {
                binding.tvBalance.setText(Double.parseDouble(loginBean.getData().getBalance())==0?
                        "$ 0":loginBean.getData().getBalance());

            }

        }
    };

    Observer<AddDisburesementResponse> response_addDisbursment_observer = new Observer<AddDisburesementResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable AddDisburesementResponse addDisburesementResponse) {
            if (addDisburesementResponse == null) {
                Utils.showAlert(SendingPaymentActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (addDisburesementResponse.getStatus()) {
                startActivity(new Intent(SendingPaymentActivity.this, PaymentSucessActivity.class));
                finish();

            }
            else
            {
                Utils.showToast(getApplicationContext(),addDisburesementResponse.getMessage());
            }

        }
    };

    Observer observer = (Observer<Integer>) value -> {
        switch (value) {
            case AppUtils.ENTER_AMOUNT:
                Utils.hideProgressDialog();
                Utils.showToast(SendingPaymentActivity.this, getString(R.string.please_enter_amou));
                break;
            case AppUtils.NO_INTERNET:
                Utils.hideProgressDialog();
                Utils.showToast(SendingPaymentActivity.this, getString(R.string.internet_connection));
                break;
            case AppUtils.SERVER_ERROR:
                Utils.hideProgressDialog();
                Utils.showToast(SendingPaymentActivity.this, getString(R.string.server_error));
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