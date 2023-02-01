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
import com.apt_x.app.model.TransactionEmailBody;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.ActivityDeliveryNotificationBinding;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class DeliveryNotificationActivity extends BaseActivity {

    private ActivityDeliveryNotificationBinding binding;
    private SenderViewModel senderViewModel;
    private ApiCalls apiCalls;
    PostCreateTransactionBody createTransactionBody;
    String recipientname="",bankName="",transactionId="",countryCode="",abarouting = "", ifccode = "";
    private TransactionEmailBody emailBody;
    Context context = DeliveryNotificationActivity.this;
    Activity activity = DeliveryNotificationActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_notification);
        senderViewModel= ViewModelProviders.of(this).get(SenderViewModel.class);
        apiCalls=ApiCalls.getInstance(this);

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
        senderViewModel.response_validator_send_email.observe(this,send_email_response);
        binding.ivBack.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
        binding.tvNo.setOnClickListener(this);
        createTransactionBody= (PostCreateTransactionBody) getIntent().getSerializableExtra("transactionModel");
        recipientname=getIntent().getStringExtra("recipientName");
        transactionId=getIntent().getStringExtra("transactionId");
        bankName=getIntent().getStringExtra("bankName");
        countryCode =getIntent().getStringExtra("countryCode");
        abarouting =getIntent().getStringExtra("abarouting");
        ifccode =getIntent().getStringExtra("ifc");

    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.tvContinue) {

            //senderViewModel.sendEmail(apiCalls,);
            sendEmail();

        }
        if (view == binding.tvNo) {
            startActivity(new Intent(DeliveryNotificationActivity.this, HomeActivity.class));
        }
    }

    private void sendEmail() {
        emailBody=new TransactionEmailBody();
        emailBody.setAmount(""+MyPref.getInstance(this).readPrefs(MyPref.USER_AMOUNT));
        emailBody.setBought(""+MyPref.getInstance(this).readPrefs(MyPref.USER_AMOUNT)+" "+createTransactionBody.getTransaction().getReceivecurrency());
        emailBody.setCostdeducted(""+createTransactionBody.getTransaction().getAmount());
        emailBody.setCurrency(createTransactionBody.getTransaction().getReceivecurrency());
        emailBody.setRate(""+MyPref.getInstance(this).readPrefs(MyPref.EXCHANGE_RATE));

        System.out.println("IFC" + ifccode + "aba routing" + abarouting);
        emailBody.setRecipientBank(bankName==null?"IBAN":bankName);
        emailBody.setRecipientname(recipientname);
        emailBody.setReceivingcountry(countryCode);
        emailBody.setTransactionnumber(transactionId);
        emailBody.setTransferfees(MyPref.getInstance(this).readPrefs(MyPref.SERVICE_FEE));

        emailBody.setSold("$ "+createTransactionBody.getTransaction().getAmount());
        emailBody.setValuedate(Utils.getCurrentDate());


        Log.e("Email Body"," " +emailBody);
        Utils.showDialog(this,"Loading");
        senderViewModel.sendEmail(apiCalls,emailBody);


    }
    Observer<JsonObject> send_email_response= new Observer<JsonObject>() {
        @Override
        public void onChanged(JsonObject jsonObject) {
            try {
                JSONObject jsonObject1= new JSONObject(jsonObject.toString());

                boolean status=jsonObject1.getBoolean("status");
                String message=jsonObject1.getString("message");
                if(status)
                {
                   // Utils.showToast(getApplicationContext(),message);
                    startActivity(new Intent(DeliveryNotificationActivity.this, HomeActivity.class));

                }
                else
                {
                   // Utils.showToast(getApplicationContext(),message);
                    startActivity(new Intent(DeliveryNotificationActivity.this, HomeActivity.class));
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