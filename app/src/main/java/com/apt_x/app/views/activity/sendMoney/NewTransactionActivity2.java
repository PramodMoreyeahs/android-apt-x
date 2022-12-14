package com.apt_x.app.views.activity.sendMoney;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddNewRecipientNewTransactionBinding;
import com.apt_x.app.model.RecipientModel;
import com.apt_x.app.model.UserListP2p;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.newTransactions.RecipientActivity;
import com.apt_x.app.views.adapter.RecipientAdapter;
import com.apt_x.app.views.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class NewTransactionActivity2 extends BaseActivity {
    private ActivityAddNewRecipientNewTransactionBinding binding;
    RecipientAdapter _madaptor;
    ArrayList<RecipientModel> recipientModelArrayList=new ArrayList<>();
    SendMoneyViewModel viewModel;
    ApiCalls apiCalls;
    ArrayList<UserListP2p.DataEntity> dataEntityArrayList= new ArrayList<>();

    Context context = NewTransactionActivity2.this;
    Activity activity = NewTransactionActivity2.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_recipient_new_transaction);
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
        //binding.ivBack.setOnClickListener(this);
        binding.llUpper.tvTitle.setText("New Transaction");
        binding.rlAddNew.setOnClickListener(this);
        binding.rlWithdraw.setOnClickListener(this);
        binding.rlInter.setOnClickListener(this);
        apiCalls = ApiCalls.getInstance(this);
        binding.llUpper.ivBack.setOnClickListener(this);

        viewModel.user_list_p2p_validator.observe(this,user_list_p2p_res);
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
        getUserList();



    }

    private void getUserList() {
        Utils.showDialog(getApplicationContext(),"Loading");
        viewModel.getUserP2P(apiCalls, MyPref.getInstance(NewTransactionActivity2.this).readPrefs(MyPref.WALLET_ID));
    }

    void filter(String text){
        List<UserListP2p.DataEntity> temp = new ArrayList();
        for(UserListP2p.DataEntity d: dataEntityArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFirstName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        _madaptor.setList(temp);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.rlAddNew) {
            startActivity(new Intent(this, AddNewRecepientActivity.class));
        }else if(view == binding.rlWithdraw) {
            startActivity(new Intent(this, NewTransactionActivity2.class));
        }
        else if(view == binding.rlInter) {
            startActivity(new Intent(this, RecipientActivity.class));
        }

        else if(view ==binding.llUpper.ivBack)
        {
            onBackPressed();
        }

    }
    Observer<UserListP2p> user_list_p2p_res= new Observer<UserListP2p>() {
        @Override
        public void onChanged(UserListP2p userListP2p) {
                if(userListP2p.getStatus())
                {
                    dataEntityArrayList.clear();
                    dataEntityArrayList.addAll(userListP2p.getData());
                    _madaptor= new RecipientAdapter(NewTransactionActivity2.this,dataEntityArrayList);
                    binding.rvRecipient.setAdapter(_madaptor);
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