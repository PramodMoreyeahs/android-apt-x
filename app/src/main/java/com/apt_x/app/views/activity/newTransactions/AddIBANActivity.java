package com.apt_x.app.views.activity.newTransactions;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddIbanBinding;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;

public class AddIBANActivity extends BaseActivity {
    private ActivityAddIbanBinding binding;
    private SenderViewModel senderViewModel;
    String countryCode="",currency="",totalAmount="",flag="",dial_code="",userId="",country="";
    AddRecipientRequest addRecipientRequest;
    ApiCalls apiCalls;
    Context context = AddIBANActivity.this;
    Activity activity = AddIBANActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_iban);
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
        countryCode=getIntent().getStringExtra(Keys.COUNTRY_CODE);
        totalAmount=getIntent().getStringExtra(Keys.TOTAL_AMOUNT);
        currency=getIntent().getStringExtra(Keys.CURRENCY_CODE);
        flag=getIntent().getStringExtra(Keys.COUNTRY_FLAG);
        dial_code=getIntent().getStringExtra(Keys.COUNTRY_DIAL_CODE);
        userId=getIntent().getStringExtra(Keys.USER_ID);
        country=getIntent().getStringExtra(Keys.COUNTRY);
       // binding.tvCountryCode.setText(countryCode);
        senderViewModel= ViewModelProviders.of(this).get(SenderViewModel.class);
        senderViewModel.response_validator_iban.observe(this,iban_response);

        apiCalls=ApiCalls.getInstance(this);
        binding.ivBack.setOnClickListener(this);
        try {
            addRecipientRequest= (AddRecipientRequest) getIntent().getSerializableExtra(Keys.EXISTING_USER_DATA);
            Log.e("Existing User Data*",""+addRecipientRequest.getFirstName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view==binding.tvContinue)
        {



            if(binding.etIban.getText().toString().isEmpty())
            {
                Utils.showToast(getApplicationContext(),getResources().getString(R.string.enter_iban_number));
            }
            else
            {
                Utils.showDialog(this,"Loading");
                senderViewModel.validatedIban(apiCalls,binding.etIban.getText().toString());

            }


        }
    }
    Observer<JsonObject> iban_response= new Observer<JsonObject>() {
        @Override
        public void onChanged(JsonObject jsonObject) {
            try {
                JSONObject jsonObject1= new JSONObject(jsonObject.toString());

                boolean status=jsonObject1.getBoolean("status");
                String message=jsonObject1.getString("message");
                if(status)
                {
                  //  Utils.showToast(getApplicationContext(),message);
                    JSONObject jsonObject2=jsonObject1.getJSONObject("data");
                    String branch=jsonObject2.getString("swift");
                    String bankName=jsonObject2.getString("bankName");
                    System.out.println("total amount in IBAN info" + totalAmount);
                    startActivity(new Intent(AddIBANActivity.this,SenderInfoActivity.class)
                            .putExtra(Keys.COUNTRY_CODE,countryCode)
                            .putExtra(Keys.CURRENCY_CODE,currency)
                            .putExtra(Keys.TOTAL_AMOUNT,totalAmount)
                            .putExtra(Keys.COUNTRY_FLAG,flag)
                            .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                            .putExtra(Keys.USER_ID,userId)
                            .putExtra(Keys.COUNTRY,country)
                            .putExtra("branchName",branch)
                            .putExtra("bankName",bankName)
                            .putExtra(Keys.EXISTING_USER_DATA,(Serializable) addRecipientRequest)
                            .putExtra("iban",binding.etIban.getText().toString()));
                }
                else
                {
                    Utils.showToast(getApplicationContext(),getString(R.string.iban_not_valid));

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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