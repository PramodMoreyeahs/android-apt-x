package com.apt_x.app.views.activity.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.application.MyApp;
import com.apt_x.app.model.bean.AddEftAccountResponse;
import com.apt_x.app.model.bean.GetBankListResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.withdraw.AddNewEftAccountActivity;
import com.apt_x.app.views.activity.withdraw.WithdrawViewModel;
import com.apt_x.app.views.adapter.GetBankAndCardAdapter;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityPaymentBinding;

import java.util.ArrayList;

public class PaymentActivity extends BaseActivity {
    private ActivityPaymentBinding binding;
    WithdrawViewModel viewModel;
    ApiCalls apicalls;
    final ArrayList<GetBankListResponse.DataEntity> transactionArrayList = new ArrayList<>();
    final ArrayList<GetBankListResponse.DataEntity> bankArrayList = new ArrayList<>();
    Context context = PaymentActivity.this;
    Activity activity = PaymentActivity.this;


    GetBankAndCardAdapter bankAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        viewModel = ViewModelProviders.of(this).get(WithdrawViewModel.class);
        apicalls=ApiCalls.getInstance(this);
        viewModel.response_validator_bank_list.observe(this, transaction_response_observer);
        viewModel.response_validator_add_bank.observe(this, transaction_response_observer_add_eft);

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
        binding.ivBack.setOnClickListener(this);
        binding.addBankAccount.setOnClickListener(this);

        getAccountList();

    }

    private void getAccountList() {
        String  payeeId= (MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));
        Utils.showDialog(this, getString(R.string.please_wait));
        viewModel.getAccountList(apicalls,payeeId);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.addBankAccount) {
            goToAddBankAccount();
        }

    }



    private void goToAddBankAccount() {
        startActivity(new Intent(this, AddNewEftAccountActivity.class));

    }

    Observer<GetBankListResponse> transaction_response_observer= new Observer<GetBankListResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetBankListResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(PaymentActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if(loginBean.getStatus()) {
                transactionArrayList.clear();

                    transactionArrayList.addAll(loginBean.getData());



                intAdapter();
                listShort();
                bankAdapter.setList(bankArrayList);

            }

        }
    };

    private void listShort() {
        for (int i=0;i< transactionArrayList.size();i++){
           if (transactionArrayList.get(i).getType()==4){
                 if(bankArrayList.size()==0)
                 {
                     bankArrayList.add(transactionArrayList.get(i));
                 }

            }
        }
        Log.e("List DAta*******",""+bankArrayList.size());

    }

    Observer<AddEftAccountResponse> transaction_response_observer_add_eft= new Observer<AddEftAccountResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable AddEftAccountResponse loginBean) {


        }
    };


    private void intAdapter() {
        bankAdapter = new GetBankAndCardAdapter(this, transactionArrayList);
        binding.rvBank.setAdapter(bankAdapter);
        bankAdapter.notifyDataSetChanged();


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