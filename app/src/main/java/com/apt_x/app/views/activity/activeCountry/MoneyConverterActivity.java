package com.apt_x.app.views.activity.activeCountry;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityConvertMoneyBinding;
import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.GetBankDetailResponse;
import com.apt_x.app.model.GetCountryServiceResponse;
import com.apt_x.app.model.GetExchangeRateModel;
import com.apt_x.app.model.GetExistingUserResponse;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.getBank.BankDepositActivity;
import com.apt_x.app.views.activity.newTransactions.SenderInfoActivity;
import com.apt_x.app.views.adapter.ActiveCountryServiceAdapter;
import com.apt_x.app.views.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class MoneyConverterActivity extends BaseActivity {
    private ActivityConvertMoneyBinding binding;
    ActiveCountryViewModel viewModel;
    ApiCalls apicalls;
    String receiverCountryCode, receiverCurrencyCode;
    final ArrayList<GetCountryServiceResponse.DataEntity> transactionArrayList = new ArrayList<>();
    ActiveCountryServiceAdapter myAdapter;
    String flag = "", dial_code = "", userId = "", country_flag = "", country = "", service_fee = "";
    float rate = 0, totalAMount = 0;
    GetCountryServiceResponse.DataEntity data = new GetCountryServiceResponse.DataEntity();
    GetBankDetailResponse getExitingUserData;
    AddRecipientRequest addRecipientRequest;
    GetUserByEmail.Data exitingUserData;

    GetExistingUserResponse getExistingUserResponse;
    Context context = MoneyConverterActivity.this;
    Activity activity = MoneyConverterActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_convert_money);
        viewModel = ViewModelProviders.of(this).get(ActiveCountryViewModel.class);
        apicalls = ApiCalls.getInstance(this);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void initializeViews() {
        viewModel.response_validator_transaction.observe(this, transaction_response_observer);
        binding.tvContinue.setOnClickListener(this);
        viewModel.response_validator_exchange.observe(this, response_observer_exchange);
        System.out.println("country name in money converter" + getIntent() != null ? getIntent().getStringExtra(Keys.COUNTRY) : "");

        String countrytitile = getIntent() != null ? getIntent().getStringExtra(Keys.COUNTRY) : "";
       //binding.llUpper.tvTitle.setText(getIntent() != null ? getIntent().getStringExtra(Keys.COUNTRY) : "");

        if(countrytitile == null){
            binding.llUpper.tvTitle.setText(countrytitile);
        }else{
            binding.llUpper.tvTitle.setText(countrytitile.toUpperCase(Locale.ROOT));
        }


        viewModel.response_validator_add_country.observe(this, country_response_observer);
        viewModel.response_validator_bank_detail.observe(this, response_observer_bank_detail);

        // viewModel.addCountry(apicalls,"IN");
        // binding.etAmount.setText(new DecimalFormat("###.##").format(watcher));
        binding.etAmount.addTextChangedListener(watcher);

        try {

            receiverCountryCode = getIntent().getStringExtra(Keys.COUNTRY_CODE);
            receiverCurrencyCode = getIntent().getStringExtra(Keys.CURRENCY_CODE);
            country = getIntent().getStringExtra(Keys.COUNTRY);
            dial_code = getIntent().getStringExtra(Keys.COUNTRY_DIAL_CODE);
            rate = getIntent().getFloatExtra(Keys.COUNTRY_RATE, 0);
            System.out.println("Currencyrate4" + rate);
            service_fee = getIntent().getStringExtra(Keys.SERVICE_FEE);

            binding.tvServiceFee.setText("$ " + Utils.convertDecimalFormate(Float.parseFloat(service_fee)));
            flag = getIntent().getStringExtra(Keys.COUNTRY_FLAG);
            byte[] b = Base64.decode(flag, Base64.DEFAULT);
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
            binding.ivFlag.setImageBitmap(bitmapImage);
            Log.e("CurrencyCode", "*****" + receiverCurrencyCode + " User Id" + userId);


        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.llUpper.ivBack.setOnClickListener(this);
        binding.tvReciever.setText(receiverCurrencyCode);
        binding.tvExchangeRate.setText("$ 1.00 CAD  = " + Utils.convertDecimalFormate4(Float.parseFloat(String.valueOf(rate))) + " " + receiverCurrencyCode);

        System.out.println("ExchangeRate:::in MCA" + rate);

        //getConverter();

        try {
            addRecipientRequest = (AddRecipientRequest) getIntent().getSerializableExtra(Keys.EXISTING_USER_DATA);
            Log.e("Existing User Data*", "" + addRecipientRequest.getFirstName());


            getConverter(addRecipientRequest.getCountrycode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            exitingUserData = (GetUserByEmail.Data) getIntent().getSerializableExtra("existing");
            userId = String.valueOf(exitingUserData.getAptCardId());
            Log.e("User Id*************", "" + userId);

            System.out.println("existing user acct type" + exitingUserData.getAccountType());

            System.out.println("Get bank id for pakistan" + exitingUserData.getBankid());
            receiverCountryCode = exitingUserData.getCountryCode();

            receiverCurrencyCode = exitingUserData.getReceivecurrency();

            binding.tvReciever.setText("" + receiverCurrencyCode);

            byte[] b = Base64.decode(exitingUserData.getCountry_flag(), Base64.DEFAULT);
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
            binding.ivFlag.setImageBitmap(bitmapImage);
            getConverter(exitingUserData.getCountryCode());


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getExistingUserResponse = (GetExistingUserResponse) getIntent().getSerializableExtra("existing");
            userId = String.valueOf(getExistingUserResponse.getData().getAptCard_Id());
            Log.e("User Id*************", "" + userId);
            receiverCountryCode = getExistingUserResponse.getData().getCountryCode();
            receiverCurrencyCode = getExistingUserResponse.getData().getReceivecurrency();
            binding.tvReciever.setText("" + receiverCountryCode);
            System.out.println("existing user acct type" + exitingUserData.getAccountType());
            getConverter(getExistingUserResponse.getData().getCountryCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                // binding.tvSymbol.setText(symbol+" ");
                convertAmount(s.toString());
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                binding.tvSymbol.setVisibility(View.GONE);
                binding.tvTheyReceived.setText("0.00");
                binding.tvYouSend.setText("$ 0.00");
                binding.tvReceived.setText("0.00");
                binding.tvTotal.setText("$0.00");
            } else {
                binding.tvSymbol.setVisibility(View.VISIBLE);
            }
          //  System.out.println("Type Transfer amt" + Utils.convertDecimalFormate2(Double.valueOf(binding.etAmount.getText().toString())));

            String str = binding.etAmount.getText().toString();
            if (str.isEmpty()) return;
            String str2 = PerfectDecimal(str, 9, 2);

            if (!str2.equals(str)) {
                binding.etAmount.setText(str2);
                binding.etAmount.setSelection(str2.length());
            }

        }
    };

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }


    @SuppressLint("SetTextI18n")
    private void convertAmount(String input) {
        String input1 = input.replaceAll(",", "");

        float inputAmount = Float.parseFloat(input1);
        float amount = rate * Float.parseFloat(input1);
        System.out.println("decimal inpit MCA::" + inputAmount);
//        binding.etAmount.setText(""+Utils.convertDecimalFormate(inputAmount));
        binding.tvReceived.setText("" + Utils.convertDecimalFormate(amount));
        binding.tvTheyReceived.setText("" + Utils.convertDecimalFormate(amount));
        binding.tvYouSend.setText("$ " + Utils.convertDecimalFormate(inputAmount));
        totalAMount = (float) (Float.parseFloat(input1) + Float.parseFloat(service_fee));
        binding.tvTotal.setText("$" + Utils.convertDecimalFormate(totalAMount));
    }

    private void getConverter(String code) {
        Utils.showDialog(this, "Loading");
        viewModel.getExchangeRate(apicalls, code);

    }

    Observer<GetCountryServiceResponse> transaction_response_observer = new Observer<GetCountryServiceResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetCountryServiceResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(MoneyConverterActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (loginBean.getStatus()) {
                transactionArrayList.clear();
                transactionArrayList.addAll(loginBean.getData());
                myAdapter.setList(transactionArrayList);
            }

        }
    };
    Observer<AddCountryResponse> country_response_observer = new Observer<AddCountryResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable AddCountryResponse data) {
            if (data.getStatus()) {
                country_flag = data.getData().getId();
                byte[] b = Base64.decode(country_flag, Base64.DEFAULT);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
                binding.ivFlag.setImageBitmap(bitmapImage);
            }

        }
    };
    Observer<GetExchangeRateModel> response_observer_exchange = new Observer<GetExchangeRateModel>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetExchangeRateModel getExchangeRateModel) {
            if (getExchangeRateModel.getStatus()) {
                for (int i = 0; i < getExchangeRateModel.getData().size(); i++) {
                    if (getExchangeRateModel.getData().get(i).getModeofpayment().equals("BANK DEPOSIT")) {
                        rate = (float) getExchangeRateModel.getData().get(i).getEndrate();
                        service_fee = getExchangeRateModel.getData().get(i).getServiceFee();
                        binding.tvServiceFee.setText("$ " +Utils.convertDecimalFormate(Float.parseFloat(service_fee)));
                        binding.tvExchangeRate.setText("$1.00 CAD = "  + Utils.convertDecimalFormate4(Float.parseFloat(String.valueOf(rate))) + " " + receiverCurrencyCode);
                        System.out.println("country name in money converter" + getExchangeRateModel.getData().get(i).getReceivecountryname());
                        binding.llUpper.tvTitle.setText(getExchangeRateModel.getData().get(i).getReceivecountryname().toUpperCase(Locale.ROOT));

                    }
                }

            }

        }
    };
    Observer<GetBankDetailResponse> response_observer_bank_detail = new Observer<GetBankDetailResponse>() {
        @Override
        public void onChanged(GetBankDetailResponse getBankDetailResponse) {
            if (getBankDetailResponse.getStatus()) {
                getExitingUserData = getBankDetailResponse;
                Utils.showDialog(getApplicationContext(), "Loading");

                viewModel.getExchangeRate(apicalls, getBankDetailResponse.getData().getCountrycode());
            } else {
                Utils.showToast(getApplicationContext(), "" + getBankDetailResponse.getMessage());
            }

        }
    };


    @Override
    public void onClick(View view) {
        if (view == binding.tvContinue) {
            validate();

        }
        if (view == binding.llUpper.ivBack) {
            onBackPressed();
        }
    }

    private void validate() {
        if (binding.etAmount.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_amou));
        }
       /* else if(Float.parseFloat(binding.tvReceived.getText().toString())<=0){
            Utils.showToast(getApplicationContext(),getResources().getString(R.string.recieving_amount_not_empty));
        }*/
        else {
            MyPref.getInstance(this).writePrefs(MyPref.USER_AMOUNT, binding.tvTheyReceived.getText().toString());
            MyPref.getInstance(this).writePrefs(MyPref.EXCHANGE_RATE, String.valueOf(rate));
            MyPref.getInstance(this).writePrefs(MyPref.SERVICE_FEE, service_fee);
            MyPref.getInstance(this).writePrefs(MyPref.CAD_AMOUNT, binding.etAmount.getText().toString());
         //   if (userId.equals("")) {

            if(receiverCountryCode.equals("IN") || !userId.equals("") || receiverCountryCode.equals("US")){
                senderInfo();
            }
            /*else if(receiverCountryCode.equals("PK")){
                senderInfo();
            }*/
            else {
                bankDepo();
            }

        }


    }

    private void bankDepo() {
        System.out.println("total amount in MoneyCvtr info" + totalAMount);
        startActivity(new Intent(this, BankDepositActivity.class)
                .putExtra(Keys.COUNTRY_CODE, receiverCountryCode)
                .putExtra(Keys.CURRENCY_CODE, receiverCurrencyCode)
                .putExtra(Keys.TOTAL_AMOUNT, String.valueOf(totalAMount))
                .putExtra(Keys.COUNTRY_FLAG, flag)
                .putExtra(Keys.COUNTRY, country)
                .putExtra(Keys.USER_ID, userId)
                .putExtra(Keys.COUNTRY_DIAL_CODE, dial_code)
                .putExtra(Keys.EXISTING_USER_DATA, (Serializable) addRecipientRequest)
                .putExtra("exitingUserData", (Serializable) getExistingUserResponse)
                .putExtra("exitingUserData1",(Serializable) exitingUserData)
        );
    }

    private void senderInfo() {

        startActivity(new Intent(this, SenderInfoActivity.class)
                .putExtra(Keys.COUNTRY_CODE, receiverCountryCode)
                .putExtra(Keys.CURRENCY_CODE, receiverCurrencyCode)
                .putExtra("exitingUserData", (Serializable) getExistingUserResponse)
                .putExtra(Keys.EXISTING_USER_DATA, (Serializable) addRecipientRequest)
                .putExtra("exitingUserData1",(Serializable) exitingUserData)
                .putExtra(Keys.TOTAL_AMOUNT, String.valueOf(totalAMount))
                .putExtra(Keys.COUNTRY_FLAG, flag)
                .putExtra(Keys.COUNTRY_DIAL_CODE, dial_code)
                .putExtra(Keys.USER_ID, userId)
                .putExtra(Keys.COUNTRY, country)
        );
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