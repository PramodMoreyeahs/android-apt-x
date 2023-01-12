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
import com.apt_x.app.databinding.ActivityBankDepositBinding;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.GetBankModel;
import com.apt_x.app.model.GetExistingUserResponse;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.newTransactions.AddIBANActivity;
import com.apt_x.app.views.activity.newTransactions.SenderInfoActivity;
import com.apt_x.app.views.adapter.GetBankAdapter;
import com.apt_x.app.views.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankDepositActivity extends BaseActivity {
    private ActivityBankDepositBinding binding;
    BankDepositViewModel viewModel;
    ApiCalls apicalls;
    String receiverCountryCode,receiverCurrencyCode;
    final ArrayList<GetBankModel.DataEntity> bankList = new ArrayList<>();
    GetBankAdapter myAdapter;
    String totalAmount="",flag="",dial_code="",userId,country="";
    AddRecipientRequest addRecipientRequest;
    GetUserByEmail.Data exitingUserData;
    GetExistingUserResponse getExistingUserResponse;
    float totalAMount =0;
    Context context = BankDepositActivity.this;
    Activity activity = BankDepositActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bank_deposit);
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
        viewModel.response_validator_transaction.observe(this, transaction_response_observer);
        receiverCountryCode = getIntent().getStringExtra(Keys.COUNTRY_CODE);
        userId = getIntent().getStringExtra(Keys.USER_ID);
        receiverCurrencyCode = getIntent().getStringExtra(Keys.CURRENCY_CODE);
        flag = getIntent().getStringExtra(Keys.COUNTRY_FLAG);
        totalAmount = getIntent().getStringExtra(Keys.TOTAL_AMOUNT);
        country = getIntent().getStringExtra(Keys.COUNTRY);
        dial_code = getIntent().getStringExtra(Keys.COUNTRY_DIAL_CODE);
        getBank();
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

        try {
            exitingUserData= ( GetUserByEmail.Data) getIntent().getSerializableExtra("exitingUserData1");
            Log.e("Existing User Data*",""+exitingUserData.getAptCardId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getExistingUserResponse = (GetExistingUserResponse) getIntent().getSerializableExtra("exitingUserData");

        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    private void intAdapter() {
            myAdapter = new GetBankAdapter(this, bankList);
            binding.rvBank.setAdapter(myAdapter);

    }

    void filter(String text){
        List<GetBankModel.DataEntity> temp = new ArrayList();
        for(GetBankModel.DataEntity d: bankList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        myAdapter.setList(temp);
    }

    private void getBank() {
        Utils.showDialog(this,"Loading");
        viewModel.getBank(receiverCountryCode,apicalls);
     /*   bankList.clear();
        bankList.add(new GetBankModel("Bank of America Corp.",1,getResources().getDrawable(R.drawable.ic_bank_of_america)));
        bankList.add(new GetBankModel("Bank of New York Mellon...",2,getResources().getDrawable(R.drawable.ic_bank_of_america)));
        bankList.add(new GetBankModel("Citigroup Inc.",3,getResources().getDrawable(R.drawable.ic_bank_of_america)));
        bankList.add(new GetBankModel("Capital One Financial Corp.",4,getResources().getDrawable(R.drawable.ic_bank_of_america)));
        bankList.add(new GetBankModel("Citizens Financial Group",5,getResources().getDrawable(R.drawable.ic_bank_of_america)));*/

        myAdapter = new GetBankAdapter(this, bankList);
        binding.rvBank.setAdapter(myAdapter);



    }
    Observer<GetBankModel> transaction_response_observer= new Observer<GetBankModel>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetBankModel getBankModel) {
            if (getBankModel.getData() == null) {
                Utils.showAlert(BankDepositActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if(getBankModel.getStatus()) {
                bankList.clear();
                bankList.addAll(getBankModel.getData());
                intAdapter();
                myAdapter.setList(bankList);
            }

        }
    };

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
    }

    public void navigate( GetBankModel.DataEntity obj)
    {

        System.out.println("BankId for phil flow" + obj.getName() + obj.getId());

        if(receiverCountryCode.equals("PK") || receiverCountryCode.equals("PH")){

            System.out.println("total amount in BANK DEPO info" + totalAmount);
            startActivity(new Intent(this, SenderInfoActivity.class)
                    .putExtra("bankName",obj.getName())
                    .putExtra("bankId",obj.getId())
                    .putExtra(Keys.COUNTRY_CODE, receiverCountryCode)
                    .putExtra(Keys.CURRENCY_CODE, receiverCurrencyCode)
                    .putExtra("exitingUserData", (Serializable) getExistingUserResponse)
                    .putExtra(Keys.EXISTING_USER_DATA, (Serializable) addRecipientRequest)
                    .putExtra("exitingUserData1",(Serializable) exitingUserData)
                    .putExtra(Keys.TOTAL_AMOUNT, totalAmount)
                    .putExtra(Keys.COUNTRY_FLAG, flag)
                    .putExtra(Keys.COUNTRY_DIAL_CODE, dial_code)
                    .putExtra(Keys.USER_ID, userId)
                    .putExtra(Keys.COUNTRY, country)
            );
        }

        else if(obj.getName().equals("IBAN"))
        {

            startActivity(new Intent(BankDepositActivity.this, AddIBANActivity.class)
                    .putExtra("bankName",obj.getName())
                    .putExtra("bankId",obj.getId())
                    .putExtra(Keys.COUNTRY_CODE,receiverCountryCode)
                    .putExtra(Keys.TOTAL_AMOUNT,totalAmount)
                    .putExtra(Keys.CURRENCY_CODE,receiverCurrencyCode)
                    .putExtra(Keys.COUNTRY_FLAG,flag)
                    .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                    .putExtra(Keys.USER_ID,userId)
                    .putExtra(Keys.COUNTRY,country)
                    .putExtra(Keys.EXISTING_USER_DATA,addRecipientRequest)
            );
        }
        else
        {
            startActivity(new Intent(BankDepositActivity.this, BankBranchesActivity.class)
                    .putExtra("bankName",obj.getName())
                    .putExtra("bankId",obj.getId())
                    .putExtra(Keys.COUNTRY_CODE,receiverCountryCode)
                    .putExtra(Keys.TOTAL_AMOUNT,totalAmount)
                    .putExtra(Keys.CURRENCY_CODE,receiverCurrencyCode)
                    .putExtra(Keys.COUNTRY_FLAG,flag)
                    .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                    .putExtra(Keys.USER_ID,userId)
                    .putExtra(Keys.COUNTRY,country)
                    .putExtra(Keys.EXISTING_USER_DATA,addRecipientRequest)
            );
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