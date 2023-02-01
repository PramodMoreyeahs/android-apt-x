package com.apt_x.app.views.activity.confirmPayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityConfirmPaymentBinding;
import com.apt_x.app.model.CreateTransactionResponse;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.newTransactions.DeliveryNotificationActivity;
import com.apt_x.app.views.activity.newTransactions.SenderViewModel;
import com.apt_x.app.views.base.BaseActivity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfirmPaymentActivity extends BaseActivity {
    private ActivityConfirmPaymentBinding binding;
    SenderViewModel viewModel;
    ApiCalls apicalls;
    PostCreateTransactionBody createTransactionBody;
    private String recipientName="",bankName="",countryCode="",totalAmount="", abarouting = "", ifccode = "";
    ObservableBoolean terms1 = new ObservableBoolean();
    ObservableBoolean terms2 = new ObservableBoolean();
    Context context = ConfirmPaymentActivity.this;
    Activity activity = ConfirmPaymentActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_payment);
        viewModel = ViewModelProviders.of(this).get(SenderViewModel.class);
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
    @SuppressLint("SetTextI18n")
    @Override
    public void initializeViews() {
        binding.setTerms1(terms1);
        binding.setTerms2(terms2);
        viewModel.response_create_transation.observe(this, response_observer_create_transactoin);
        createTransactionBody= (PostCreateTransactionBody) getIntent().getSerializableExtra("transactionBody");
        binding.llUpper.tvTitle.setText(getText(R.string.confirm_payment));
        recipientName=getIntent().getStringExtra("recipientName");
        bankName=getIntent().getStringExtra("bankName");
        totalAmount=getIntent().getStringExtra("totalAmount");
        countryCode=getIntent().getStringExtra("countryCode");
        if(countryCode.equals("IN")){
            ifccode=getIntent().getStringExtra("ifc");
        }else if(countryCode.equals("US")){
            abarouting=getIntent().getStringExtra("abarouting");
        }

        customTextView2(binding.cfrmpageterms);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            binding.tvEarlieDate.setText(dtf.format(now));
        }
//#676767 grey text
        String text="<font color=#FFFFFF>"+getString(R.string.the_amount)+"</font> <font color=#2FB1F8>"+"$" + Utils.convertDecimalFormate(Float.parseFloat(totalAmount))+"</font>"+
                " "+"<font color=#FFFFFF>"+getString(R.string.transfer_will_be_)+"</font>"+"</font> ";

        binding.tvHeader.setText(Html.fromHtml(text));

        String text2="<font color=#FFFFFF>"+getString(R.string.you_can_top_up_your_account)+"</font> <font color=#2FB1F8>"+" $"+Utils.convertDecimalFormate(Float.parseFloat(totalAmount))+"</font>";

        binding.tvAptBalance.setText(Html.fromHtml(text2));

        Log.e("Bank Name"," "+bankName);
        System.out.println("Payment mode in confirm activity:: " +createTransactionBody.getTransaction().getPaymentmode());
        System.out.println("Payment mode in confirm activity:: " +createTransactionBody.getTransaction().getPaymentmode());
      //  binding.tvAptBalance.setText(Html.fromHtml(text2));

        binding.tvRecipient.setText(""+recipientName);
        binding.tvAmount.setText(""+ MyPref.getInstance(this).readPrefs(MyPref.USER_AMOUNT)+" "+createTransactionBody.getTransaction().getReceivecurrency());
        binding.tvFixRate.setText("$"+ Utils.convertDecimalFormate4(Float.parseFloat(MyPref.getInstance(this).readPrefs(MyPref.EXCHANGE_RATE))));
        binding.tvServiceFee.setText("$ "+  Utils.convertDecimalFormate(Float.parseFloat(MyPref.getInstance(this).readPrefs(MyPref.SERVICE_FEE))));
       // binding.tvEarlieDate.setText(""+Utils.getCurrentDate());
        binding.tvTotal.setText("$"+ Utils.convertDecimalFormate(Float.parseFloat(totalAmount)));

        float inverse= Float.parseFloat(MyPref.getInstance(this).readPrefs(MyPref.EXCHANGE_RATE));
         float inverse_rate= 1/inverse;


         binding.tvInverseRate.setText("$ "+Utils.convertDecimalFormate(inverse_rate));

        binding.llUpper.ivBack.setOnClickListener(this);



    }


    Observer<CreateTransactionResponse> response_observer_create_transactoin = new Observer<CreateTransactionResponse>() {

        @Override
        public void onChanged(@Nullable CreateTransactionResponse data) {
            if (data.getStatus()) {
                Utils.showToast(getApplicationContext(),getString(R.string.transaction_sucessfully));
               Intent intent=new Intent(ConfirmPaymentActivity.this, DeliveryNotificationActivity.class);
               intent.putExtra("transactionModel", (Serializable)createTransactionBody);
               intent.putExtra("bankName",bankName);
               intent.putExtra("recipientName",recipientName);
               intent.putExtra("countryCode",countryCode);
                intent.putExtra("abarouting", abarouting);
                intent.putExtra("ifc", ifccode);
               intent.putExtra("transactionId",data.getData().getPrepaid().getTransactionId());
               startActivity(intent);
                finish();

            } else {

                if(data.getError().getErrors().get(0) != null && !data.getError().getErrors().get(0).equals("")){
                    Utils.showToast(getApplicationContext(), data.getError().getErrors().get(0).toString() );
                }else{
                    Utils.showToast(getApplicationContext(), getString(R.string.bank_detail_not_correct) );
                }


               /* Intent intent1=new Intent(ConfirmPaymentActivity.this, DeliveryNotificationActivity.class);
                intent1.putExtra("transactionModel", (Serializable)createTransactionBody);
                intent1.putExtra("bankName",bankName);
                intent1.putExtra("recipientName",recipientName);
                intent1.putExtra("countryCode",countryCode);
                intent1.putExtra("transactionId",data.getData().getPrepaid().getTransactionId());
                startActivity(intent1);*/

            }


        }
    };






    @Override
    public void onClick(View view) {
        if (view == binding.llUpper.ivBack) {
            onBackPressed();
        }
        if(view == binding.tvContinue){

            if(terms1.get()==false)
            {
               // Utils.showToast(getApplicationContext(),"Please check all privacy policy");
            }
            else if(terms2.get()==false)
            {
              //  Utils.showToast(getApplicationContext(),"Please check all privacy policy");
            }
            else{
                Utils.showDialog(this, "Loading");
                viewModel.createTransaction(apicalls,createTransactionBody);
            }

        }
        if(view == binding.check1){
            if (terms1.get() == true) {
                terms1.set(false);
            } else {
                terms1.set(true);
            }
        }


        if(view == binding.check2) {
            if (terms2.get() == true) {
                terms2.set(false);
            } else {
                terms2.set(true);
            }
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