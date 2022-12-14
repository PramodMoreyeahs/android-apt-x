package com.apt_x.app.views.activity.loadfund;


import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityInteraceEtransferBinding;
import com.apt_x.app.model.AddFundResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.newTransactions.SenderViewModel;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class InteracETransferActivity extends BaseActivity {

    Context context = InteracETransferActivity.this;
    Activity activity = InteracETransferActivity.this;
    ActivityInteraceEtransferBinding binding;
    ApiCalls apiCalls;

    SenderViewModel viewModel;

    @Override
    public void initializeViews() {
        apiCalls=ApiCalls.getInstance(this);
        viewModel = ViewModelProviders.of(this).get(SenderViewModel.class);
        viewModel.response_validator_load_fund.observe(this,add_fund_response);
       binding.tvAppId.setText(MyPref.getInstance(this).readPrefs(MyPref.APPID));
       binding.tvEmail.setText("Etransfer@apt-xb.com");
       binding.tvCopyEmail.setOnClickListener(this);
       binding.tvContinue.setOnClickListener(this);
       binding.tvCopyId.setOnClickListener(this);
       binding.ivBack.setOnClickListener(this);
      // amount=getIntent().getStringExtra("Amount");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_interace_etransfer);
        initializeViews();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvCopyEmail:
                ClipboardManager clipboardManager2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager2.setText(binding.tvEmail.getText().toString());
                Utils.showToast(this, binding.tvEmail.getText().toString() + " Copied");


                break;
            case R.id.tvCopyId:
                ClipboardManager clipboardManager1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager1.setText(binding.tvAppId.getText().toString());
                Utils.showToast(this, binding.tvAppId.getText().toString() + " Copied");
                break;
            case R.id.tvContinue:
               /* String message="Hello ,\n" +
                        "\n" +
                        "The  amount of  $"+amount+" will be successfully loaded in your wallet in 2-3 business days.\n" +
                        "\n" +
                        "Thankyou.";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{binding.tvEmail.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Load fund");
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));*/

                addWalletFund();
                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private void addWalletFund() {
        String message="Hello "+MyPref.getInstance(this).readPrefs(MyPref.FULL_NAME)+", \n" +
                "Thanks for using the Apt-X mobile app for your Cross-border transactions. \n" +
                "⦁\tLog into your online or mobile banking app and select the account.\n" +
                "⦁\tChoose or add new recipient’s email “etransfer@apt-xb.com”\n" +
                "⦁\tEnter the amount and enter the Identification number: "+MyPref.getInstance(this).readPrefs(MyPref.APPID)+" into the message field of the e-transfer. \n" +
                "Thank you for choosing Apt-X\n";
        String submessage="<html><p>Hello "+MyPref.getInstance(this).readPrefs(MyPref.FULL_NAME)+",<br><br><br>Thanks for using the Apt-X mobile app for your Cross-border transactions. In order to load funds, please follow the steps below:<br>    • Log into your online or mobile banking app and select the account.<br>   • Choose or add new recipient's email etransfer@apt-xb.com.<br>   • Enter the amount and enter the Identification number: "+MyPref.getInstance(this).readPrefs(MyPref.APPID)+  "    into the message field of the e-transfer.<br><br>The funds should show up into your account within 30 minutes of the transfer.<br><br>Thank you for choosing Apt-X<br><br><br>Apt-X<br><br>Apt Pay Inc<br>121 Bloor Street East, Suite 410<br>Toronto, Ontario, M5W 3M5<br>Email: [customerservice@apt-xb.com]support@aptpay.com<br>Call: 1-866-303-3955<br>[www.apt-xb.com]www.apt-xb.com</p></html>";
        Utils.showDialog(this,"Loading");
        viewModel.loadFundEmail(apiCalls,"Load fund",submessage);
    }

    DisposableObserver<AddFundResponse> disposableObserver = new DisposableObserver<AddFundResponse>() {
        @Override
        public void onNext(@NonNull AddFundResponse getCompanyConstantResponse) {
            Utils.hideProgressDialog();
            System.out.println("Response ::" + new Gson().toJson(getCompanyConstantResponse));
           // Utils.showToast(InteracETransferActivity.this, "Success");
            Intent intent = new Intent(InteracETransferActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            //Utils.showToast(InteracETransferActivity.this, e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

    Observer<JsonObject> add_fund_response= new Observer<JsonObject>() {
        @Override
        public void onChanged(JsonObject jsonObject) {
            try {
                JSONObject jsonObject1= new JSONObject(jsonObject.toString());

                boolean status=jsonObject1.getBoolean("status");
                String message=jsonObject1.getString("message");
                if(status)
                {
                    Utils.showToast(getApplicationContext(),message);
                    startActivity(new Intent(InteracETransferActivity.this, HomeActivity.class));

                }
                else
                {
                    Utils.showToast(getApplicationContext(),message);
                    startActivity(new Intent(InteracETransferActivity.this, HomeActivity.class));
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