package com.apt_x.app.views.activity.newTransactions;

import static com.apt_x.app.utils.Utils.showCustomDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityRecipientBinding;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.activeCountry.ActiveCountryServicveActivity;
import com.apt_x.app.views.activity.activeCountry.MoneyConverterActivity;
import com.apt_x.app.views.activity.exchangeRate.CountryListActivity;
import com.apt_x.app.views.adapter.UserRecipientAdapter;
import com.apt_x.app.views.base.BaseActivity;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipientActivity extends BaseActivity  {

    private ActivityRecipientBinding binding;
    private boolean check=false;
    ObservableBoolean ischecked =new ObservableBoolean(false);
    ObservableBoolean ischeckedx =new ObservableBoolean(false);
    SenderViewModel viewModel;
    ApiCalls apiCalls;
    Context context = RecipientActivity.this;
    Activity activity = RecipientActivity.this;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    ArrayList<GetUserByEmail.Data> userList=new ArrayList<>();
    UserRecipientAdapter userRecipientAdapter;
 //   String countryCode="";
  //  String totalAMount="";
   // String currency="";
  //  String iban="",flag="",dial_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipient);
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
        viewModel = ViewModelProviders.of(this).get(SenderViewModel.class);
        apiCalls = ApiCalls.getInstance(RecipientActivity.this);
        viewModel.response_user_validator.observe(this,userObserver);
        viewModel.response_validator_delete_user.observe(this,delete_response);
        binding.ivBack.setOnClickListener(this);
        binding.llExit.setOnClickListener(this);
        binding.llNewRec.setOnClickListener(this);
        ischecked.set(true);
        binding.setIscheckn(ischecked);
        binding.setIscheckx(ischeckedx);
      //  countryCode=getIntent().getStringExtra(Keys.COUNTRY_CODE);
      //  totalAMount=getIntent().getStringExtra(Keys.TOTAL_AMOUNT);
      //  dial_code=getIntent().getStringExtra(Keys.COUNTRY_DIAL_CODE);
     //   flag=getIntent().getStringExtra(Keys.COUNTRY_FLAG);
      //  currency=getIntent().getStringExtra(Keys.CURRENCY_CODE);

     /*   try {
           iban=getIntent().getStringExtra("iban");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/



        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /*   if(s.length()>0)
                  {
                      viewModel.getuser(apiCalls,s.toString());
                  }*/

                if(s.length()>2)
                {
                    viewModel.getuser(apiCalls,s.toString());
                }
                if(s.length()==0)
                {
                    userList.clear();
                    binding.rv.setAdapter(userRecipientAdapter);

                }



            }

            @Override
            public void afterTextChanged(Editable s) {
             //   if(s.length()==2)
            /*    if(s.length()!=0)
                {
                    viewModel.getuser(apiCalls,s.toString());
                }
                if(s.length()==0)
                {
                    userList.clear();
                    binding.rv.setAdapter(userRecipientAdapter);

                }*/

            }
        });


       /* handlerEmp = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(binding.etSearch.getText())) {
                        viewModel.getuser(apiCalls,binding.etSearch.getText().toString());
                    }
                }
                return false;
            }
        });*/

    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ll_exit:
                Animation animationUp= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_bottom);
                binding.tvContinue.startAnimation(animationUp);
                ischecked.set(false);
                ischeckedx.set(true);


                break;
            case R.id.ll_new_rec:
                Animation animationUp1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_top);
                binding.tvContinue.startAnimation(animationUp1);
                ischecked.set(true);
                ischeckedx.set(false);
                binding.etSearch.setText("");
                break;
            case R.id.tvContinue:
                navigate(null);
                break;
            case R.id.tvViewExchangeRate:
                MyPref.getInstance(this).writeBooleanPrefs(Pref.COME_FROM,true);
                startActivity(new Intent(RecipientActivity.this, CountryListActivity.class));
        }

    }
    Observer<GetUserByEmail> userObserver= new Observer<GetUserByEmail>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetUserByEmail getUserByEmail) {
            if(getUserByEmail.getStatus())
            {
                userList.clear();
                userList.addAll(getUserByEmail.getData());
             //   System.out.println("exisitng response" + getUserByEmail.getData().get(0).getBankid());
               /* userAdaptor= new UserAdaptor(RecipientActivity.this,R.layout.user_item,userList);
                binding.etSearch.setAdapter(userAdaptor);*/
                userRecipientAdapter=new UserRecipientAdapter(RecipientActivity.this, userList);
                binding.rv.setAdapter(userRecipientAdapter);
                userRecipientAdapter.notifyDataSetChanged();

            }
            else
            {
                Utils.showToast(getApplicationContext(),getString(R.string.no_user_found));
            }


        }
    };
    public void navigate(GetUserByEmail.Data data)
    {
        if(data==null)
        {
            startActivity(new Intent(RecipientActivity.this, ActiveCountryServicveActivity.class)
                 /*   .putExtra("data",data)
                    .putExtra(Keys.COUNTRY_CODE,countryCode)
                    .putExtra(Keys.TOTAL_AMOUNT,totalAMount)
                    .putExtra(Keys.CURRENCY_CODE,currency)
                    .putExtra(Keys.COUNTRY_FLAG,flag)
                    .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                    .putExtra("iban",iban)*/
            );
        }
        else
        {
       //     System.out.println("Bankid on RA" + data.getBankid());
            startActivity(new Intent(RecipientActivity.this, MoneyConverterActivity.class)
                    .putExtra("existing",(Serializable) data)

            );
        }
    }

    public void confirmDialog(GetUserByEmail.Data data,int pos)
    {
        final Dialog dialog = showCustomDialog(this, R.layout.user_delete_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView ok = (TextView) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
            userList.remove(pos);
            userRecipientAdapter.notifyDataSetChanged();
           viewModel.deleteUser(apiCalls,data.getId());
        });
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    Observer<JsonObject> delete_response= new Observer<JsonObject>() {
        @Override
        public void onChanged(JsonObject jsonObject) {
             try {
                 JSONObject jsonObject1= new JSONObject(jsonObject.toString());

                 boolean status=jsonObject1.getBoolean("status");
                 String message=jsonObject1.getString("message");
                 if(status)
                 {
                  Utils.showToast(getApplicationContext(),getString(R.string.user_deleted));
                 }
                 else
                 {
                     Utils.showToast(getApplicationContext(),getString(R.string.something_went_wrong));
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