package com.apt_x.app.views.activity.verification;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddAddressBinding;
import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.PlacesActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.profile.EditProfileActivity;
import com.apt_x.app.views.activity.signup.ThankYouActivity;
import com.apt_x.app.views.base.BaseActivity;

public class AddAddressActivity extends BaseActivity {

    ActivityAddAddressBinding binding;
    AddressViewModel addressViewModel;
    ApiCalls apiCalls;
    String comeFrom;
    String dob = "";
    String selectedaddress = "", selectedcity = "", selectedstate = "", selectedpincode = "";
    Context context = AddAddressActivity.this;
    Activity activity = AddAddressActivity.this;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        binding.setViewModel(addressViewModel);
        initializeViews();
    }

    Observer<GetProfileResponse> response_observer_get = new Observer<GetProfileResponse>() {
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;
            if (countriesResponse.getUser() != null) {
                try {
                    binding.etAddress.setText(countriesResponse.getUser().getStreet());
                    binding.etCity.setText(countriesResponse.getUser().getCity());
                    binding.etZipCode.setText(countriesResponse.getUser().getZip());
                    binding.acTvState.setText(countriesResponse.getUser().getState());
                    binding.etHouse.setText(countriesResponse.getUser().getStreet_line_2());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void initializeViews() {
        binding.tvContinue.setOnClickListener(this);
        if(getIntent()!=null)
        {
            comeFrom=getIntent().getStringExtra("comeFrom");

        }
      if (getIntent().getStringExtra("isFirsttime").equals("false")){


          selectedaddress = getIntent().getStringExtra("Selectedaddress");
          selectedcity = getIntent().getStringExtra("Selectedcity");
          selectedpincode = getIntent().getStringExtra("SelectedpostalCode");
          selectedstate = getIntent().getStringExtra("Selectedstate");

          if(!selectedaddress.isEmpty()){

              binding.etAddress.setText(selectedaddress);

              binding.etCity.setText(selectedcity);
              binding.etZipCode.setText(selectedpincode);
              binding.acTvState.setText(selectedstate);
          }
      }
        if(comeFrom!=null){
            binding.tvSkip.setVisibility(View.GONE);
        }


        addressViewModel.validator.observe(this, observer);
        addressViewModel.response_validator_get.observe(this, response_observer_get);
        addressViewModel.response_validator.observe(this, addressResponseObserver);
        apiCalls = ApiCalls.getInstance(AddAddressActivity.this);
        if(comeFrom!=null)
        {
            addressViewModel.getProfile(apiCalls);
        }

        try {
            dob=getIntent().getStringExtra("dob");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(comeFrom!=null){
           super.onBackPressed();
        }
        else {
            Utils.exitApp(this);
        }
    }

    Observer<Integer> observer = integer -> {
        switch (integer) {
            case AppUtils.empty_street:
                Utils.showToast(AddAddressActivity.this, getString(R.string.address_empty));
                break;

         /*   case AppUtils.empty_house:
                Utils.showToast(AddAddressActivity.this, getString(R.string.house_empty));
                break;*/

            case AppUtils.empty_city:
                Utils.showToast(AddAddressActivity.this, getString(R.string.city_empty));
                break;
            case AppUtils.empty_zip:
                Utils.showToast(AddAddressActivity.this, getString(R.string.zip_empty));
                break;
            case AppUtils.empty_state:
                Utils.showToast(AddAddressActivity.this, getString(R.string.state_empty));
                break;

        }
    };

    Observer<AddAddressResponse> addressResponseObserver = addAddressResponse -> {

        if (addAddressResponse.getStatus()) {
            if (addAddressResponse.getData() != null) {
                Pref.setBoolean(this, true, Pref.IS_ADDRESS_FILLED);

                if(comeFrom!=null){
                    binding.tvSkip.setVisibility(View.GONE);
                   // finish();
                    startActivity(new Intent(AddAddressActivity.this, EditProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                else {
                    MyPref.getInstance(getApplicationContext()).writeBooleanPrefs(Pref.IS_ADDRESS_FILLED,true);
                    startActivity(new Intent(AddAddressActivity.this, HomeActivity.class));
                    finish();
                }
            }
        } else if (addAddressResponse.getMessage() != null) {

            Utils.showToast(AddAddressActivity.this, addAddressResponse.getMessage());
        } else {
            Utils.showToast(AddAddressActivity.this, getString(R.string.something_went_wrong));
        }
    };

    @Override
    public void onClick(View v) {
        if (v == binding.tvContinue) {
            if (addressViewModel.validateAddress(binding)) {
                Utils.showDialog(this, getString(R.string.please_wait));

                addressViewModel.address(binding, apiCalls, dob);
            }
        }
        if (v == binding.tvSkip) {
            MyPref.getInstance(getApplicationContext()).writeBooleanPrefs(Pref.IS_ADDRESS_FILLED, true);
            startActivity(new Intent(AddAddressActivity.this, ThankYouActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finishAffinity();
        }
        if (v == binding.ivBack) {
            onBackPressed();
        }
        if (v == binding.addresssearchid) {
            startActivity(new Intent(getApplicationContext(), PlacesActivity.class));
            Pref.SIGNUPAUTO = "true";
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