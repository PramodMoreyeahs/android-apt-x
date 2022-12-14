package com.apt_x.app.views.activity.getBank;

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
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityBankBranchesBinding;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.GetBankBranchesResponse;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.newTransactions.SenderInfoActivity;
import com.apt_x.app.views.adapter.GetBankBranchesAdapter;
import com.apt_x.app.views.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankBranchesActivity extends BaseActivity {
    private ActivityBankBranchesBinding binding;
    BankDepositViewModel viewModel;
    ApiCalls apicalls;
    String bankId;
    String countryCode="",currency="";
    final ArrayList<GetBankBranchesResponse.DataEntity> transactionArrayList = new ArrayList<>();
    GetBankBranchesAdapter myAdapter;
    String totalAmount="",flag="",dial_code="",userId,bankName="",country="";
    AddRecipientRequest addRecipientRequest;
    Context context = BankBranchesActivity.this;
    Activity activity = BankBranchesActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bank_branches);
        viewModel = ViewModelProviders.of(this).get(BankDepositViewModel.class);
        apicalls=ApiCalls.getInstance(this);

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
        viewModel.response_validator_transaction1.observe(this, transaction_response_observer);
        bankId = getIntent().getStringExtra("bankId");
        countryCode = getIntent().getStringExtra(Keys.COUNTRY_CODE);
        totalAmount = getIntent().getStringExtra(Keys.TOTAL_AMOUNT);
        currency = getIntent().getStringExtra(Keys.CURRENCY_CODE);
        userId = getIntent().getStringExtra(Keys.USER_ID);
        flag = getIntent().getStringExtra(Keys.COUNTRY_FLAG);
        dial_code = getIntent().getStringExtra(Keys.COUNTRY_DIAL_CODE);
        bankName = getIntent().getStringExtra("bankName");
        country = getIntent().getStringExtra(Keys.COUNTRY);
        getBankBranches();
        binding.ivBack.setOnClickListener(this);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        try {
            addRecipientRequest= (AddRecipientRequest) getIntent().getSerializableExtra(Keys.EXISTING_USER_DATA);
            Log.e("Existing User Data*",""+addRecipientRequest.getFirstName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void intAdapter() {
            myAdapter = new GetBankBranchesAdapter(this, transactionArrayList);
            binding.rvBank.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
    }

    private void getBankBranches() {
        Utils.showDialog(this,"Loading");
        viewModel.getBankBranches(bankId,apicalls);

    }
    void filter(String text){
        List<GetBankBranchesResponse.DataEntity> temp = new ArrayList();
        for(GetBankBranchesResponse.DataEntity d: transactionArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBankbranchname().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        myAdapter.setList(temp);
    }
    Observer<GetBankBranchesResponse> transaction_response_observer= new Observer<GetBankBranchesResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetBankBranchesResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(BankBranchesActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if(loginBean.getStatus()) {
                transactionArrayList.clear();
                transactionArrayList.addAll(loginBean.getData());
                intAdapter();
                myAdapter.setList(transactionArrayList);
            }

        }
    };

    public void navigate(GetBankBranchesResponse.DataEntity data){
        startActivity(new Intent(BankBranchesActivity.this, SenderInfoActivity.class)
                .putExtra("bankId",data.getBankid())
                .putExtra("bankName",bankName)
                .putExtra("branchName",data.getBankbranchid())
                .putExtra(Keys.COUNTRY_CODE,countryCode)
                .putExtra(Keys.TOTAL_AMOUNT,totalAmount)
                .putExtra(Keys.CURRENCY_CODE,currency)
                .putExtra(Keys.COUNTRY_FLAG,flag)
                .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                .putExtra(Keys.USER_ID,userId)
                .putExtra(Keys.COUNTRY,country)
                .putExtra(Keys.EXISTING_USER_DATA,(Serializable) addRecipientRequest)
        );
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
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