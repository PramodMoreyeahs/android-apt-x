package com.apt_x.app.views.activity.withdraw;

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
import com.apt_x.app.databinding.ActivityAddNewEftAccountBinding;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.bean.AddEftAccountResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.profile.PaymentActivity;
import com.apt_x.app.views.base.BaseActivity;

public class AddNewEftAccountActivity extends BaseActivity {
    private ActivityAddNewEftAccountBinding binding;
    WithdrawViewModel viewModel;
    ApiCalls apicalls;
    String name,firstName,lastName,mobile;
    String  profilePicture;
    Context context = AddNewEftAccountActivity.this;
    Activity activity = AddNewEftAccountActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_eft_account);
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
        binding.llUpper.ivBack.setOnClickListener(this);
        viewModel.validator.observe(this,observer);
        binding.llUpper.tvTitle.setText("Add EFT Account");
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
                startActivity(new Intent(AddNewEftAccountActivity.this, PaymentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                addEFTAccount();
                break;


        }
    }


    private void addEFTAccount() {
        viewModel.addEftAccount(binding ,apicalls);

    }
    Observer observer = (Observer<Integer>) value -> {
        switch (value) {
            case AppUtils.ENTER_ACCOUNT_NUMBER:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.please_enter_account_num));
                break;
            case AppUtils.NO_INTERNET:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.internet_connection));
                break;
            case AppUtils.SERVER_ERROR:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.server_error));
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