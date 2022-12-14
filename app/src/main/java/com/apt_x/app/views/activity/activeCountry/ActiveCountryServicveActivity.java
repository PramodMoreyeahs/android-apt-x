package com.apt_x.app.views.activity.activeCountry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityNewTransactionBinding;
import com.apt_x.app.model.GetCountryServiceResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.adapter.ActiveCountryServiceAdapter;
import com.apt_x.app.views.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ActiveCountryServicveActivity extends BaseActivity {
    private ActivityNewTransactionBinding binding;
    ActiveCountryViewModel viewModel;
    ApiCalls apicalls;
    final ArrayList<GetCountryServiceResponse.DataEntity> transactionArrayList = new ArrayList<>();
    ActiveCountryServiceAdapter myAdapter;
    float rate=0;
    Context context = ActiveCountryServicveActivity.this;
    Activity activity = ActiveCountryServicveActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_transaction);
        viewModel = ViewModelProviders.of(this).get(ActiveCountryViewModel.class);
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
    @Override
    public void initializeViews() {

        viewModel.response_validator_transaction.observe(this, transaction_response_observer);
        getActiveCountryService();
        binding.ivBack.setOnClickListener(this);


        binding.etSearch.addTextChangedListener(searchtext);
    }
    TextWatcher searchtext= new TextWatcher() {
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
    };

    void filter(String text){
        List<GetCountryServiceResponse.DataEntity> temp = new ArrayList();
        for(GetCountryServiceResponse.DataEntity d: transactionArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        myAdapter.setList(temp);
    }

    private void intAdapter() {
            myAdapter = new ActiveCountryServiceAdapter(this, transactionArrayList,viewModel,apicalls);
            binding.rvCountry.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
    }

    private void getActiveCountryService() {
        Utils.showDialog(this,"Loading");
        viewModel.getActiveCountryService(apicalls);

    }
    Observer<GetCountryServiceResponse> transaction_response_observer= new Observer<GetCountryServiceResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetCountryServiceResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(ActiveCountryServicveActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if(loginBean.getStatus()) {
                transactionArrayList.clear();
                transactionArrayList.addAll(loginBean.getData());
                intAdapter();
                myAdapter.setList(transactionArrayList);
            }

        }
    };


    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
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