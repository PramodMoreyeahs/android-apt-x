package com.apt_x.app.views.activity.sendMoney;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddNewRecipientReviewBinding;
import com.apt_x.app.model.ErrorModel;
import com.apt_x.app.model.P2PRequest;
import com.apt_x.app.model.P2PResponse;
import com.apt_x.app.model.SendMoneyWalletRequest;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.withdraw.PaymentSucessActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class ReviewActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {
    private ActivityAddNewRecipientReviewBinding binding;
    String name="",email="",userId="",user_wallet_id,existing="";
    double totalAmount=0.0;
    double amount=0.0;
    SendMoneyViewModel viewModel;
    ApiCalls apiCalls;
    SendMoneyWalletRequest sendMoneyWalletRequest= new SendMoneyWalletRequest();
    Context context = ReviewActivity.this;
    Activity activity = ReviewActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_recipient_review);
        //setContentView(R.layout.activity_new_transaction);
        viewModel = ViewModelProviders.of(this).get(SendMoneyViewModel.class);
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
        binding.myseek.setProgress(3);
        binding.llUpper.ivBack.setOnClickListener(this);
        binding.llUpper.tvTitle.setText(getString(R.string.Review));
        //binding.rlAddNew.setOnClickListener(this);
        //binding.tvContinue.setOnClickListener(this);
        binding.myseek.setOnSeekBarChangeListener(this);
        binding.rlView.setOnClickListener(this);
        apiCalls = ApiCalls.getInstance(this);
        viewModel.send_money_validator.observe(this,responseObserver_send_money);
        viewModel.user_add_validator.observe(this,responseObserver_user_add);



        if(getIntent()!=null)
        {
         name=getIntent().getStringExtra("name");
         email=getIntent().getStringExtra("email");
         amount=Double.parseDouble(getIntent().getStringExtra("amount"));

         userId=getIntent().getStringExtra("userID");
         user_wallet_id=getIntent().getStringExtra("user_wallet_id");
            existing=getIntent().getStringExtra("existing")!=null?
                    getIntent().getStringExtra("existing"):"";

        }
        binding.tvName.setText(name);
        binding.tvEmail.setText(email);
        binding.tvAmount.setText("$"+ new DecimalFormat("#.00").format(amount));
        binding.tvYouSend.setText("$"+new DecimalFormat("#.00").format(amount));
        binding.tvTheyReceive.setText("$"+new DecimalFormat("#.00").format(amount));
        totalAmount=amount;
        binding.tvTotal.setText("$"+new DecimalFormat("#.00").format(totalAmount));

    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress > 95) {
            seekBar.setThumb(getResources().getDrawable(R.drawable.ic_slide_button));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        binding.sliderText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("onStopTrackingTouch", "onStopTrackingTouch");
        if (seekBar.getProgress() < 80) {
            seekBar.setProgress(3);
           // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);
            binding.sliderText.setVisibility(View.VISIBLE);
            binding.sliderText.setText(getString(R.string.slide_to_send));

        } else {
            seekBar.setProgress(99);
            binding.sliderText.setVisibility(View.INVISIBLE);
            Utils.showDialog(this,"Loading");
            binding.myseek.setVisibility(View.INVISIBLE);
            binding.afterSlideButton.setVisibility(View.VISIBLE);
            sendMoneyWalletRequest.setWallet(MyPref.getInstance(this).readPrefs(MyPref.WALLET_ID));
            sendMoneyWalletRequest.setAmount(totalAmount);
            sendMoneyWalletRequest.setPayeeId(Long.valueOf(userId));
            sendMoneyWalletRequest.setReferenceId("ref103");
            viewModel.sendMoney(apiCalls,sendMoneyWalletRequest);


        }
    }
    @Override
    public void onClick(View v) {

        binding.myseek.setVisibility(View.VISIBLE);
        binding.myseek.setProgress(3);
        binding.afterSlideButton.setVisibility(View.INVISIBLE);
       // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);
        binding.sliderText.setVisibility(View.VISIBLE);
        binding.sliderText.setText(getString(R.string.slide_to_send));
         if(v ==binding.llUpper.ivBack)
        {
            onBackPressed();
        }
    }
    Observer<JsonObject> responseObserver_send_money= new Observer<JsonObject>() {
        @Override
        public void onChanged(JsonObject data) {

                     try {

                         JSONObject jsonObject = new JSONObject(data.toString());
                         boolean status = jsonObject.getBoolean("status");
                         if (status) {
                          if(existing.equals(""))
                          {
                              viewModel.addUser(apiCalls,new P2PRequest(name,email,user_wallet_id,userId));
                          }
                          else {
                              startActivity(new Intent(ReviewActivity.this, PaymentSucessActivity.class)
                                      .putExtra("review", "reviewpage"));
                          }


                         } else
                         {
                             ErrorModel errorModel= new Gson().fromJson(jsonObject.getJSONObject("message").toString(),ErrorModel.class);
                             Utils.showToast(getApplicationContext(),errorModel.getErrors().get(0));
                             startActivity(new Intent(ReviewActivity.this, HomeActivity.class)
                                   );
                         }


                     } catch (Exception e) {
                         e.printStackTrace();
                     }


        }
    };

    Observer<P2PResponse> responseObserver_user_add= new Observer<P2PResponse>() {
        @Override
        public void onChanged(P2PResponse data) {
             if(data.getStatus())
             {
                 startActivity(new Intent(ReviewActivity.this, PaymentSucessActivity.class)
                         .putExtra("review", "reviewpage"));
             }
             else
             {
               binding.myseek.setProgress(3);
                 // binding.myseek.setBackgroundResource(R.drawable.ic_slide_button);
                 binding.sliderText.setVisibility(View.VISIBLE);
                 binding.sliderText.setText(getString(R.string.slide_to_send));
                 Utils.showToast(getApplicationContext(),data.getMessage());
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