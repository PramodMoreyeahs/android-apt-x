package com.apt_x.app.views.activity.newTransactions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivitySenderInfoBinding;
import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.AddNewRecipientREsposne;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.BankDetailOfUserResponse;
import com.apt_x.app.model.CreateTransactionResponse;
import com.apt_x.app.model.GetCountryServiceResponse;
import com.apt_x.app.model.GetExistingUserResponse;
import com.apt_x.app.model.GetPurpose;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.confirmPayment.ConfirmPaymentActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SenderInfoActivity extends BaseActivity implements
        View.OnClickListener {

    private ActivitySenderInfoBinding binding;

    Context context = SenderInfoActivity.this;
    Activity activity = SenderInfoActivity.this;

    SenderViewModel viewModel;
    ApiCalls apiCalls;
    List<String> purposeList = new ArrayList<>();
    String countryCode = "", totalAmount = "", userId = null, currency = "", country = "";
    String reason = "", countryId = "";
    PostCreateTransactionBody createTransactionBody;
    PostCreateTransactionBody.ReceiverEntity receiverEntity;
    PostCreateTransactionBody.Data _transactionData;
    PostCreateTransactionBody.TransactionEntity transactionEntity;
    String iban = "";
    String selectedacctype = "";
    ObservableBoolean isAccountCheck = new ObservableBoolean(false);

    String actNo = "";
    GetCountryServiceResponse.DataEntity dataEntity;

    String paymentMode = "";
    AddRecipientRequest addRecipientRequest;
    GetUserByEmail.Data exitingUserData;
    GetExistingUserResponse getExistingUserResponse;
    ObservableBoolean confirm_check = new ObservableBoolean();
    private String recipientName = "", bankName = "", branchName = "", bankId = "";

    String[] acctypelist = new String[]
            {"Account Type","CHECKING", "SAVINGS"};
String actp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sender_info);
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

    @SuppressLint("LongLogTag")
    @Override
    public void initializeViews() {
        binding.setConfirmCheck(confirm_check);

        try {
            Gson gson = new Gson();
            JSONObject response = new JSONObject(MyPref.getInstance(getApplicationContext()).readPrefs(Pref.COUNTRY_DATA));
            dataEntity = gson.fromJson(response.toString(), GetCountryServiceResponse.DataEntity.class);
            Log.e("Data Pramod *****", "" + dataEntity.getName());
            System.out.println("dataentity::" + dataEntity.getBankdeposit());

            if (dataEntity.getBankdeposit() == true) {
                paymentMode = "BANK_DEPOSIT";
            } else if (dataEntity.getCashpickup() == true) {
                paymentMode = "CASH_PICKUP";
            } else if (dataEntity.getMobilewallet() == true) {
                paymentMode = "MOBILE_WALLET";
            } else {
                paymentMode = "BANK_DEPOSIT";
            }


        } catch (JSONException e) {

        }
        binding.setAccountCheck(isAccountCheck);
        try {
            iban = getIntent().getStringExtra("iban");
            actNo = iban;
            countryCode = getIntent().getStringExtra(Keys.COUNTRY_CODE);
            totalAmount = getIntent().getStringExtra(Keys.TOTAL_AMOUNT);
            currency = getIntent().getStringExtra(Keys.CURRENCY_CODE);
            userId = getIntent().getStringExtra(Keys.USER_ID);
            bankName = getIntent().getStringExtra("bankName");
            bankId = getIntent().getStringExtra("bankId");
            branchName = getIntent().getStringExtra("branchName");
            country = getIntent().getStringExtra(Keys.COUNTRY);
            Log.e("Data******", "IBAN" + iban + "  Country code" + countryCode + " " + totalAmount + " UserID******" + userId + "Currency Code *****" + currency
                    + "Branh Name " + branchName + " BankName " + bankName + "BankId" + bankId);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item2, acctypelist);
            adapter.setDropDownViewResource(R.layout.spinner_item2);
            binding.statespinner.setPopupBackgroundResource(R.drawable.iv_bg);
            binding.statespinner.setAdapter(adapter);
            binding.statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    selectedacctype = adapterView.getItemAtPosition(position).toString();
                    System.out.println("Selected acctype2" + selectedacctype);

                    if (selectedacctype.equals(acctypelist[0])) {
                        // (selectedItemView as TextView).setTextColor(Color.parseColor("#676767"))
                        if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            textView.setTextColor(Color.parseColor("#676767"));
                        }

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




            if (iban == null && userId.equals("")) {
                if (country.equals("India")) {
                    binding.etIfsc.setVisibility(View.VISIBLE);
                    binding.etAccountNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                    binding.etVeryAccount.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (country.equals("Pakistan")) {
                    binding.etIfsc.setVisibility(View.GONE);
                    binding.etAccountNumber.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.etVeryAccount.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.etAccountNumber.setHint("IBAN number");
                    binding.etVeryAccount.setHint("IBAN number");//
                    binding.etVeryAccount.setVisibility(View.GONE);
                } else if(countryCode.equals("US")){
                    binding.etIfsc.setVisibility(View.GONE);
                    binding.etVeryAccount.setVisibility(View.GONE);
                    binding.abarouting.setVisibility(View.VISIBLE);
                    binding.acctypeLyt.setVisibility(View.VISIBLE);
                }


                else {
                    binding.etIfsc.setVisibility(View.GONE);
                }

                isAccountCheck.set(true);
            } else {
                isAccountCheck.set(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.ivBack.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(SenderViewModel.class);
        apiCalls = ApiCalls.getInstance(SenderInfoActivity.this);
        viewModel.getActiveCountry(apiCalls);
        binding.setConfirmCheck(confirm_check);
        viewModel.response_validator_recipient.observe(this, response_observer);
        viewModel.response_create_transation.observe(this, response_observer_create_transactoin);
        viewModel.response_bank_detail.observe(this, response_observer_bank_detail);
        viewModel.response_validator.observe(this, country_response_observer);

        viewModel.response_purpose.observe(this, response_observer_purpose);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason = purposeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {
            addRecipientRequest = (AddRecipientRequest) getIntent().getSerializableExtra(Keys.EXISTING_USER_DATA);
            Log.e("Existing User Data*", "" + addRecipientRequest);
            recipientName = addRecipientRequest.getFirstName() + " " + addRecipientRequest.getLastName();


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            exitingUserData = (GetUserByEmail.Data) getIntent().getSerializableExtra("exitingUserData1");
            Log.e("Existing User Data*", "" + exitingUserData.getAptCardId());
            userId = String.valueOf(exitingUserData.getAptCardId());

            recipientName = exitingUserData.getFirstName() + " " + exitingUserData.getLastName();
            actNo = exitingUserData.getAccountNo();
            paymentMode = exitingUserData.getPaymentMode();
            bankName = exitingUserData.getBankName();
            branchName = exitingUserData.getBranch();
            bankId = exitingUserData.getBankid();
            actp = exitingUserData.getAccountType();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getExistingUserResponse = (GetExistingUserResponse) getIntent().getSerializableExtra("exitingUserData");
            userId = String.valueOf(getExistingUserResponse.getData().getAptCard_Id());
            recipientName = getExistingUserResponse.getData().getFirst_name() + " " + getExistingUserResponse.getData().getLast_name();
            Log.e("User Id*************", "" + userId);
            System.out.println("Account TYpe in sender info" + getExistingUserResponse.getData().getAccountType());
            actNo = getExistingUserResponse.getData().getAccountNo();
             /*actp = getExistingUserResponse.getData().getAccountType();

            System.out.println("ACCOUNT TYPE" + actp);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        getPurposeCall();
        getCountryId();
    }

    private void getCountryId() {
        Utils.showDialog(this, "Loading");
        viewModel.addCountry(apiCalls, countryCode);
    }

    private void getPurposeCall() {
        Utils.showDialog(this, getString(R.string.loading));
        viewModel.getPurpose(apiCalls, countryCode);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
       /* if(view == binding.check1)
        {
            if (confirm_check.get() == true) {
           //     binding.tvContinue.setVisibility(View.GONE);
                confirm_check.set(false);
            } else {
          //      binding.tvContinue.setVisibility(View.VISIBLE);
                confirm_check.set(true);
            }
        }*/
        if (view == binding.tvContinue) {

            checkUser();
        }


    }



    /*private void createTransactionByBank() {
        Utils.showDialog(this, "Loading");
        createTransactionBody = new PostCreateTransactionBody();

        receiverEntity = new PostCreateTransactionBody.ReceiverEntity();
        receiverEntity.setPayeeid("254234167075");
        transactionEntity = new PostCreateTransactionBody.TransactionEntity();
        transactionEntity.setAmount(Float.valueOf(totalAmount));
        transactionEntity.setAccount("SK1234567812345612342233");
        transactionEntity.setAccounttype("SAVINGS");
        transactionEntity.setPurpose(reason);
        transactionEntity.setPaymentmode(paymentMode);
        transactionEntity.setSourcecurrency("CAD");
        transactionEntity.setReceivecurrency("EUR");
        transactionEntity.setBranch("TPAYSKBXXXX");
        transactionEntity.setSourceoffunds("BUSINESS");
        createTransactionBody.setReceiver(receiverEntity);
        createTransactionBody.setTransaction(transactionEntity);
        viewModel.createTransaction(apiCalls, createTransactionBody);
    }*/

    private void createTransactionByIBAN() {
        if (!binding.etAccountNumber.getText().toString().isEmpty()) {
            actNo = binding.etAccountNumber.getText().toString();
        }

        createTransactionBody = new PostCreateTransactionBody();
        receiverEntity = new PostCreateTransactionBody.ReceiverEntity();
        _transactionData = new PostCreateTransactionBody.Data();
        //receiverEntity.setPayeeid("254234167075");
        //MyPref.getInstance(context).readPrefs(MyPref.APPID)
        receiverEntity.setPayeeid(userId);
        transactionEntity = new PostCreateTransactionBody.TransactionEntity();
        transactionEntity.setAmount(Float.parseFloat(MyPref.getInstance(this).readPrefs(MyPref.CAD_AMOUNT)));
        transactionEntity.setAccount(actNo);
        if(!actp.equals("")){

            transactionEntity.setAccounttype(actp);
        }else{
            transactionEntity.setAccounttype("SAVINGS");
        }

        transactionEntity.setPurpose(reason);
        transactionEntity.setPaymentmode(paymentMode);
        transactionEntity.setSourcecurrency("CAD");
/*    if(bankId == null || bankId.equals("")){
      //  transactionEntity.setBankid("-");
    }else {
        transactionEntity.setBankid(bankId);
    }*/

        System.out.println("country...." + countryCode);
        if (countryCode.equals("PK")) {
            transactionEntity.setBankid(bankId);
        }


      /*  transactionEntity.setBought(currency+" "+ MyPref.getInstance(this).readPrefs(MyPref.USER_AMOUNT));
        transactionEntity.setSold("$"+ MyPref.getInstance(this).readPrefs(MyPref.CAD_AMOUNT));
        transactionEntity.setCountryName(countryCode);
        transactionEntity.setTransfer_fee("1.257");
        transactionEntity.setRconate(""+MyPref.getInstance(this).readPrefs(MyPref.EXCHANGE_RATE));
        transactionEntity.setBankName(bankName);*/

        _transactionData.setBankName(bankName);
        _transactionData.setTransfer_fee(MyPref.getInstance(this).readPrefs(MyPref.SERVICE_FEE));
        _transactionData.setBought(currency + " " + MyPref.getInstance(this).readPrefs(MyPref.USER_AMOUNT));
        _transactionData.setSold("$" + MyPref.getInstance(this).readPrefs(MyPref.CAD_AMOUNT));

        _transactionData.setCountryName(countryCode);
        _transactionData.setRate("" + MyPref.getInstance(this).readPrefs(MyPref.EXCHANGE_RATE));

        //transactionEntity.setReceivecurrency("EUR");
        transactionEntity.setReceivecurrency(currency);

        if (branchName == null || branchName.equals("")) {

            if(countryCode.equals("US")) {
                transactionEntity.setBranch(binding.abarouting.getText().toString());

            }else {
                branchName = "0000";
                transactionEntity.setBranch(branchName);
            }

        }  else {
            transactionEntity.setBranch(branchName);
        }
        System.out.println("country...." + countryCode);
        if (countryCode.equals("PK")) {
            transactionEntity.setBankid(bankId);
        }


        //  transactionEntity.setBranch(branchName!=""?branchName:"0000");
        transactionEntity.setSourceoffunds("BUSINESS");

        createTransactionBody.setReceiver(receiverEntity);
        createTransactionBody.setTransaction(transactionEntity);
        createTransactionBody.setData(_transactionData);

        System.out.println("Payment mode in sender info:: " + createTransactionBody.getTransaction().getPaymentmode());


        Intent intent = new Intent(SenderInfoActivity.this, ConfirmPaymentActivity.class);
        intent.putExtra("transactionBody", (Serializable) createTransactionBody);
        intent.putExtra("bankName", bankName);
        intent.putExtra("recipientName", recipientName);
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("countryCode", countryCode);
        System.out.println("total amount in sender info" + totalAmount);
        startActivity(intent);
        binding.tvContinue.setVisibility(View.VISIBLE);
        //viewModel.createTransaction(apiCalls, createTransactionBody);
    }


    Observer<GetPurpose> response_observer_purpose = new Observer<GetPurpose>() {

        @Override
        public void onChanged(@Nullable GetPurpose getPurpose) {
            if (getPurpose.getStatus()) {
                purposeList.clear();
                purposeList.add("Select Purpose");
                purposeList.addAll(getPurpose.getData());
                ArrayAdapter ad
                        = new ArrayAdapter(SenderInfoActivity.
                        this,
                        R.layout.spinner_item,
                        purposeList);
                ad.setDropDownViewResource(R.layout.spinner_item);
                binding.spinner.setPopupBackgroundResource(R.drawable.iv_bg);

                binding.spinner.setAdapter(ad);
                Utils.hideProgressDialog();


            } else {
                Utils.hideProgressDialog();
            }

        }
    };
    Observer<CreateTransactionResponse> response_observer_create_transactoin = new Observer<CreateTransactionResponse>() {

        @Override
        public void onChanged(@Nullable CreateTransactionResponse data) {
            if (data.getStatus()) {
                Utils.showToast(getApplicationContext(), getString(R.string.transaction_sucessfully));
                startActivity(new Intent(SenderInfoActivity.this, DeliveryNotificationActivity.class));
                finish();
            } else {


                Utils.showToast(getApplicationContext(), getString(R.string.bank_detail_not_correct));
                startActivity(new Intent(SenderInfoActivity.this, DeliveryNotificationActivity.class));
                finish();
            }

        }
    };
    Observer<AddCountryResponse> country_response_observer = new Observer<AddCountryResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable AddCountryResponse data) {
            if (data.getStatus()) {
                countryId = data.getData().getId();
            }

        }
    };
    Observer<BankDetailOfUserResponse> response_observer_bank_detail = new Observer<BankDetailOfUserResponse>() {

        @Override
        public void onChanged(@Nullable BankDetailOfUserResponse data) {
            if (data.getStatus()) {


                startActivity(new Intent(SenderInfoActivity.this, DeliveryNotificationActivity.class));
                finish();
            } else {
                //  Utils.showToast(getApplicationContext(), "User Data saved failed" + data.getMessage());
                startActivity(new Intent(SenderInfoActivity.this, DeliveryNotificationActivity.class));
                finish();
            }

        }
    };

  /*  private void checkUser() {
        if(userId==null && iban ==null)
        {
            if (binding.etAccountNumber.getText().toString().isEmpty()) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_account_num));
            } else if (binding.etVeryAccount.getText().toString().isEmpty()) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.re_enter_account_number));
            } else if (!binding.etAccountNumber.getText().toString().equals(binding.etVeryAccount.getText().toString())) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.account_number_not_matech));
            } else if (binding.etIfsc.getText().toString().isEmpty() && country.equals("India")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.enter_ifsc));
            } else if (reason.equals("Select Purpose")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_select_purpose));
            }
            else {


                //  binding.tvContinue.setVisibility(View.GONE);
                addRecipientRequest.setAccountno(binding.etAccountNumber.getText().toString());
                addRecipientRequest.setReceivecurrency(currency);
                if(branchName.equals("")){
                    addRecipientRequest.setBranch("000000");
                }
                else
                {
                    addRecipientRequest.setBranch(branchName);
                }
                addRecipientRequest.setPaymentmode(paymentMode);
                addRecipientRequest.setCountrycode(countryCode);
                addRecipientRequest.setAccounttype("SAVINGS");
                addRecipientRequest.setCountryid(countryId);
                addRecipientRequest.setBankName(bankName);


                createRecipient();
            }
        }
        else if(userId==null){

            if(reason.equals("Select Purpose"))
            {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_select_purpose));
            }
            else {

                // binding.tvContinue.setVisibility(View.GONE);

                addRecipientRequest.setAccountno(""+actNo);
                addRecipientRequest.setReceivecurrency(currency);
                if(branchName.equals("")){
                    addRecipientRequest.setBranch("000000");
                }
                else
                {
                    addRecipientRequest.setBranch(branchName);
                }

                addRecipientRequest.setPaymentmode(paymentMode);
                addRecipientRequest.setCountrycode(countryCode);
                addRecipientRequest.setAccounttype("SAVINGS");
                addRecipientRequest.setBankName(bankName);



                // createTransactionByIBAN();
                createRecipient();
            }
        }
        else
        {
            if(reason.equals("Select Purpose"))
            {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_select_purpose));
            }
            else
            {
                // binding.tvContinue.setVisibility(View.GONE);
                Utils.showDialog(this,"Loading");
                createTransactionByIBAN();
            }


        }

    }*/


    private void checkUser() {
        if (iban == null && userId.equals("")) {
            if (binding.etAccountNumber.getText().toString().isEmpty()) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_account_num));
            } else if (binding.etVeryAccount.getText().toString().isEmpty() && !country.equals("Pakistan") && !countryCode.equals("US")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.re_enter_account_number));
            } else if (!binding.etAccountNumber.getText().toString().equals(binding.etVeryAccount.getText().toString()) && !country.equals("Pakistan") && !countryCode.equals("US")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.account_number_not_matech));
            }


            else if (binding.etIfsc.getText().toString().isEmpty() && country.equals("India")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.enter_ifsc));
            } else if (reason.equals("Select Purpose")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_select_purpose));
            } else {


                //  binding.tvContinue.setVisibility(View.GONE);

                /*HashMap<String, String> map = new HashMap<>();
                map.put("accountNo",binding.etAccountNumber.getText().toString());
                map.put("accountType","SAVINGS");
                map.put("Receivecurrency",currency);
                map.put("paymentMode",paymentMode);
                map.put("countryCode",countryCode);
                map.put("countryId",countryId);
                map.put("bankName",bankName);
                map.put("city",addRecipientRequest.getCity());
                map.put("country_flag",addRecipientRequest.getCountry_flag());
                map.put("last_name", addRecipientRequest.getLastName());
                map.put("email",addRecipientRequest.getEmail());
                map.put("mobile",addRecipientRequest.getMobile());
                map.put("first_name",addRecipientRequest.getFirstName());
                map.put("password",addRecipientRequest.getPassword());
                map.put("state",addRecipientRequest.getState());
                map.put("street_line_2",addRecipientRequest.getStreetLine2());
                map.put("zip",addRecipientRequest.getZip());
                map.put("branch",binding.etIfsc.getText().toString());

                createRecipients(map);*/
                actNo = binding.etAccountNumber.getText().toString();
                addRecipientRequest.setAccountno("" + actNo);
                addRecipientRequest.setReceivecurrency(currency);

                if (country.equals("India")) {
                    branchName = binding.etIfsc.getText().toString();
                } else {
                    branchName = getIntent().getStringExtra("branchName");
                }
                if (bankName == null || bankName.equals("")) {
                    bankName = "-";
                    addRecipientRequest.setBankName(bankName);
                } else {
                    addRecipientRequest.setBankName(bankName);
                }


                if (branchName == null || branchName.equals("")) {


                    if(countryCode.equals("US")) {
                        addRecipientRequest.setBranch(binding.abarouting.getText().toString());

                    }else {
                        addRecipientRequest.setBranch("0000");
                    }

                } else {
                    addRecipientRequest.setBranch(branchName);
                }


                addRecipientRequest.setPaymentmode(paymentMode);
                addRecipientRequest.setCountrycode(countryCode);

                if(!selectedacctype.equals("")){

                    addRecipientRequest.setAccounttype(selectedacctype);
                }else{
                    addRecipientRequest.setAccounttype("SAVINGS");

                }


                System.out.println("country...." + countryCode);
                if (countryCode.equals("PK")) {
                    addRecipientRequest.setBankid(bankId);
                }

                //  createTransactionByIBAN();
                createRecipient();


            }
        }
        else if (iban != null && userId.equals("")) {

            if (reason.equals("Select Purpose")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_select_purpose));
            } else {

                // binding.tvContinue.setVisibility(View.GONE);

                addRecipientRequest.setAccountno("" + actNo);
                addRecipientRequest.setReceivecurrency(currency);
                if (branchName.equals("")) {

                    if(countryCode.equals("US")) {
                        addRecipientRequest.setBranch(binding.abarouting.getText().toString());

                    }else{
                        branchName = "0000";
                        addRecipientRequest.setBranch(branchName);
                    }

                } else {
                    addRecipientRequest.setBranch(branchName);
                }

                if (bankName.equals("")) {
                    bankName = "-";
                    addRecipientRequest.setBankName(bankName);

                } else {
                    addRecipientRequest.setBankName(bankName);

                }


                addRecipientRequest.setPaymentmode(paymentMode);
                addRecipientRequest.setCountrycode(countryCode);
                addRecipientRequest.setAccounttype("SAVINGS");

                System.out.println("country...." + countryCode);
                if (countryCode.equals("PK")) {
                    addRecipientRequest.setBankid(bankId);
                }

                // createTransactionByIBAN();
                createRecipient();
            }
        } else {
            if (reason.equals("Select Purpose")) {

                // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);

                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_select_purpose));
            } else {
                // binding.tvContinue.setVisibility(View.GONE);
                Utils.showDialog(this, "Loading");


                createTransactionByIBAN();

                // createRecipient();
            }


        }

    }

    private void createRecipient() {
        Utils.showDialog(getApplicationContext(), "Loading");
        viewModel.addRecipientSignUp(addRecipientRequest, apiCalls);
    }

    Observer<AddNewRecipientREsposne> response_observer = new Observer<AddNewRecipientREsposne>() {

        @Override
        public void onChanged(@Nullable AddNewRecipientREsposne data) {
            if (data.isStatus()) {
                userId = String.valueOf(data.getData().getAptCardId());
                recipientName = data.getData().getFirstName() + " " + data.getData().getLastName();
                createTransactionByIBAN();

            } else {
                //Utils.showToast(getApplicationContext(), getString(R.string.something_went_wrong));
                Utils.showToast(getApplicationContext(), data.getMessage());
            }


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