package com.apt_x.app.views.activity.sendMoney;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddNewRecipientBinding;
import com.apt_x.app.model.ExistingP2PResponse;
import com.apt_x.app.model.GetIdentityResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;

public class AddNewRecepientActivity extends BaseActivity {
    private ActivityAddNewRecipientBinding binding;
    SendMoneyViewModel viewModel;
    ApiCalls apiCalls;
    String walletId="",userID="",fname="",lname="";
    Context context = AddNewRecepientActivity.this;
    Activity activity = AddNewRecepientActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_recipient);
        viewModel = ViewModelProviders.of(this).get(SendMoneyViewModel.class);
        //setContentView(R.layout.activity_new_transaction);
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
        apiCalls = ApiCalls.getInstance(this);
        viewModel.validator.observe(this,observer);
        viewModel.get_identity_validator.observe(this,response_identity);
        viewModel.user_existing_p2p_validator.observe(this,user_exiting_res);

        //binding.ivBack.setOnClickListener(this);
        /*binding.rlAddNew.setOnClickListener(this);*/
        binding.llUpper.tvTitle.setText(getString(R.string.add_new));
        binding.tvContinue.setOnClickListener(this);
        binding.llUpper.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
     /*   if (view == binding.tvContinue) {
            startActivity(new Intent(this, SendingActivity.class));
        }*//*else if(view == binding.rlWithdraw) {
            startActivity(new Intent(this, AddNewRecepientActivity.class));
        }*/
         if(view ==binding.llUpper.ivBack)
        {
            onBackPressed();
        }
        else if(view==binding.tvContinue)
        {
           validated();
          // viewModel.verifyUser(binding,apiCalls);
        }
    }

    private void validated() {
       /* if(binding.etFirstName.getText().toString().trim().isEmpty())
        {
            Utils.hideProgressDialog();
            Utils.showToast(getApplicationContext(), getString(R.string.enter_first_name));
        }
        else if(binding.etLastName.getText().toString().trim().isEmpty())
        {
            Utils.hideProgressDialog();
            Utils.showToast(getApplicationContext(), getString(R.string.enter_last_name));
        }
        else*/
            if(binding.stEmail.getText().toString().trim().isEmpty())
        {
            Utils.hideProgressDialog();
            Utils.showToast(getApplicationContext(), getString(R.string.enter_emailid));
        }

        else if(binding.etVeryEmail.getText().toString().trim().isEmpty())
        {
            Utils.hideProgressDialog();
            Utils.showToast(getApplicationContext(), getString(R.string.please_enter_verify_email));
        }
        else if(!Utils.isValideEmail(binding.stEmail.getText().toString().trim())){
            Utils.hideProgressDialog();
            Utils.showToast(getApplicationContext(), getString(R.string.enter_valid_employeeid));
        }
        else if(!binding.etVeryEmail.getText().toString().equals(binding.stEmail.getText().toString().trim()))
        {
            Utils.hideProgressDialog();
            Utils.showToast(getApplicationContext(), getString(R.string.email_not_match));
        }
        else {
            Utils.showDialog(this,"Loading");
            viewModel.getIdentity(apiCalls,binding.etVeryEmail.getText().toString());

        }
    }

    Observer observer = (Observer<Integer>) value -> {
        switch (value) {
            case AppUtils.first_name:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.enter_first_name));
                break;
            case AppUtils.last_name:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.enter_last_name));
                break;
            case AppUtils.empty_id:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.enter_emailid));
                break;
            case AppUtils.empty_verify_email:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.please_enter_verify_email));
                break;
            case AppUtils.invalid_mail:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.enter_valid_employeeid));
                break;
            case AppUtils.confirm_email:
                Utils.hideProgressDialog();
                Utils.showToast(getApplicationContext(), getString(R.string.email_not_match));
                break;
        }
    };
    Observer<GetIdentityResponse> response_identity= new Observer<GetIdentityResponse>() {
        @Override
        public void onChanged(GetIdentityResponse getIdentityResponse) {



                if(getIdentityResponse.getStatus().equals("true"))
                {
                   // Utils.showToast(getApplicationContext(),"User exit");
                    walletId=getIdentityResponse.getData().getWalletId();
                    userID=getIdentityResponse.getData().getId();
                    fname=getIdentityResponse.getData().getFirst_name();
                    lname=getIdentityResponse.getData().getLast_name();
                  /*  binding.etFirstName.setText(""+getIdentityResponse.getData().getFirst_name());
                    binding.etLastName.setText(""+getIdentityResponse.getData().getLast_name());*/


                    viewModel.getExistingP2P(apiCalls,binding.etVeryEmail.getText().toString().trim());

                }
                else
                {
                   binding.invite.ll.setVisibility(View.VISIBLE);
                   binding.invite.tvContinue.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Utils.hideKeyboard(AddNewRecepientActivity.this);
                           Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + binding.etVeryEmail.getText().toString()));
                           emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Information");
                           emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                           //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

                           startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                       }
                   });
                    Utils.showToast(getApplicationContext(),"Recipient not exit");
                }

        }
    };
    Observer<ExistingP2PResponse> user_exiting_res= new Observer<ExistingP2PResponse>() {
        @Override
        public void onChanged(ExistingP2PResponse existingP2PResponse) {
             if(existingP2PResponse.getStatus())
             {
                 showDialog(existingP2PResponse);
             }
             else
             {
                 startActivity(new Intent(getApplicationContext(), SendingActivity.class)
                         .putExtra("name",fname+" "+lname)
                         .putExtra("email",binding.etVeryEmail.getText().toString())
                         .putExtra("user_wallet_id",walletId)
                         .putExtra("userID",userID));
             }
        }
    };

    public void showDialog(ExistingP2PResponse data) {
        final Dialog dialog = Utils.showCustomDialog(this, R.layout.dialog_exiting_user);
        Button yes = dialog.findViewById(R.id.bt);
        TextView userName=dialog.findViewById(R.id.tvName);
        TextView userIcon=dialog.findViewById(R.id.tvIcon);
        TextView userEmail=dialog.findViewById(R.id.tvEmail);
        TextView tv_cancel=dialog.findViewById(R.id.tvcancel);
        userName.setText(data.getData().getFirstName());
        userIcon.setText(data.getData().getFirstName().substring(0,1).toUpperCase());
        userEmail.setText(data.getData().getEmailid());
        yes.setOnClickListener(v -> {
            dialog.dismiss();

            startActivity(new Intent(getApplicationContext(), SendingActivity.class)
                    .putExtra("name",data.getData().getFirstName())
                    .putExtra("email",data.getData().getEmailid())
                    .putExtra("user_wallet_id",walletId)
                    .putExtra("existing",data.getData().getId())
                    .putExtra("userID",userID));





        });
        tv_cancel.setOnClickListener(view -> {
            dialog.dismiss();

        });

        dialog.show();
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