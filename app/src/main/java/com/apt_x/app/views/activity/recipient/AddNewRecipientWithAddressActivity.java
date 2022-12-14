package com.apt_x.app.views.activity.recipient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivitySentMoneyRecipientBinding;
import com.apt_x.app.model.AddAddressResponse;
import com.apt_x.app.model.AddCountryResponse;
import com.apt_x.app.model.AddNewRecipientREsposne;
import com.apt_x.app.model.AddRecipientRequest;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.model.GetBankResponse;
import com.apt_x.app.model.GetExistingUserResponse;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.model.bean.PostCreateTransactionBody;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.PlacesActivity;
import com.apt_x.app.views.activity.activeCountry.MoneyConverterActivity;
import com.apt_x.app.views.activity.newTransactions.RecipientActivity;
import com.apt_x.app.views.activity.signup.SignUpViewModel;
import com.apt_x.app.views.adapter.DialCodeAdapter;
import com.apt_x.app.views.adapter.GetBankAdapter;
import com.apt_x.app.views.base.BaseActivity;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewRecipientWithAddressActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private ActivitySentMoneyRecipientBinding binding;
    SignUpViewModel viewModel;
    ApiCalls apicalls;
    final ArrayList<GetBankResponse.DataEntity> transactionArrayList = new ArrayList<>();
    GetBankAdapter myAdapter;
    PostCreateTransactionBody postCreateTransactionBody;
    PostCreateTransactionBody.ReceiverEntity receiverEntity;
    PostCreateTransactionBody.TransactionEntity transactionEntity;
    GetUserByEmail.Data userData;
    String countryCode = "";
    public static String countrycode1 = "", country1 = "", service_fee1 = "", currency1 = "", flag1 = "", dial_code1 = "";
    String countryId = "";
    String adressDesc = "";
    String totalkAmount = "";
    public static String newfirst = "", newlastname = "", newnumber = "", email = "";
    String currency = "", service_fee;
    String userId = "", country = "";
    float country_rate = 0;
    float country_rate1 = 0;
    String flag = "", dial_code = "";
    Context context = AddNewRecipientWithAddressActivity.this;
    Activity activity = AddNewRecipientWithAddressActivity.this;

    String selectedaddress, selectedcity, selectedstate, selectedpincode;
    ObservableBoolean emailcheck = new ObservableBoolean();
    ArrayList<CountriesResponse.Data> countryCodeList = new ArrayList<>();
    Dialog dialog;
    DialCodeAdapter dialCodeAdapter;
    AppCompatEditText search;
    String[] statelist = new String[]{"Province / State", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};


    String selectedstatename = "";

    String addressLineRegex = "/^[0-9]+\\\\s+[A-Za-z0-9 ]+$/";
    String addressLine2Regex = "/^[A-Za-z ]+\\\\s*[0-9]*$/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sent_money_recipient);
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        apicalls = ApiCalls.getInstance(this);
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
        binding.setEmailCheck(emailcheck);
        binding.llUpper.tvTitle.setText(getString(R.string.add_new_recipient));
        if (getIntent().getStringExtra("IsShow").equals("true")) {
            countryCode = getIntent().getStringExtra(Keys.COUNTRY_CODE);
            country = getIntent().getStringExtra(Keys.COUNTRY);
            country_rate = getIntent().getFloatExtra(Keys.COUNTRY_RATE, 0);
            System.out.println("Currencyrate normal flow****" + country_rate);
            service_fee = getIntent().getStringExtra(Keys.SERVICE_FEE);
            currency = getIntent().getStringExtra(Keys.CURRENCY_CODE);
            flag = getIntent().getStringExtra(Keys.COUNTRY_FLAG);
            dial_code = getIntent().getStringExtra(Keys.COUNTRY_DIAL_CODE);

            countrycode1 = countryCode;
            country1 = country;
            country_rate1 = country_rate;
            Pref.CRRate = getIntent().getFloatExtra(Keys.COUNTRY_RATE, 0);
            System.out.println("Currencyrate1 normal flow****" + country_rate1);
            service_fee1 = service_fee;
            currency1 = currency;
            flag1 = flag;
            dial_code1 = dial_code;

            if (countryCode.equals("US")) {
                binding.addresssearchid.setVisibility(View.GONE);
               // binding.etHouse.setHint("Address Line 2");
                binding.etHouse.setVisibility(View.GONE);

                binding.etSelectPro.setVisibility(View.GONE);
                binding.stateprovinceLyt.setVisibility(View.VISIBLE);
            } else {
                binding.addresssearchid.setVisibility(View.VISIBLE);
                binding.etSelectPro.setVisibility(View.VISIBLE);
                binding.stateprovinceLyt.setVisibility(View.GONE);
            }


        } else {
            countryCode = countrycode1;
            country = country1;
            country_rate = Pref.CRRate;
            service_fee = service_fee1;
            currency = currency1;
            flag = flag1;
            dial_code = dial_code1;

            System.out.println("Currencyrate1 in flow2****" + country_rate1);
            System.out.println("Currencyrate in flow2****" + country_rate);
            System.out.println(" Pref.CRRate in flow2****" + Pref.CRRate);
            System.out.println("Currencyrate2" + countryCode);
            System.out.println("Currencyrate2" + country);
            System.out.println("Currencyrate2" + service_fee);
            System.out.println("Currencyrate2" + currency);
            System.out.println("Currencyrate2" + flag);
            System.out.println("Currencyrate2" + dial_code);

            selectedaddress = getIntent().getStringExtra("Selectedaddress");
            selectedcity = getIntent().getStringExtra("Selectedcity");
            selectedpincode = getIntent().getStringExtra("SelectedpostalCode");
            selectedstate = getIntent().getStringExtra("Selectedstate");

            System.out.println("Choosed datas from places" + selectedpincode + selectedcity + selectedstate);

            binding.etAddress.setText(selectedaddress);
            binding.etCity.setText(selectedcity);
            binding.etZipCode.setText(selectedpincode);
            binding.etSelectPro.setText(selectedstate);

            System.out.println("first name  flow2****" + newfirst);
            binding.etFirstName.setText(newfirst, TextView.BufferType.EDITABLE);
            System.out.println("first name flow2****" + newfirst);
            binding.etLastName.setText(newlastname);
            binding.etNumber.setText(newnumber);
            binding.etEmail.setText(email);


        }
        binding.editCountryCode.setText("" + dial_code);


        byte[] b = Base64.decode(flag, Base64.DEFAULT);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
        binding.ivFlag.setImageBitmap(bitmapImage);
        binding.etEmail.addTextChangedListener(emailTextWatcher);
        binding.etAddress.addTextChangedListener(typeaddress);
        binding.etHouse.addTextChangedListener(typeaddress2);
        binding.etCity.addTextChangedListener(typecity);
        binding.etSelectPro.addTextChangedListener(typestate);
        binding.etZipCode.addTextChangedListener(typepincode);


        viewModel.response_user_validator.observe(this, userObserver);
        viewModel.response_existing_user.observe(this, exiting_user_Observer);
        viewModel.response_validator_add_country.observe(this, country_response_observer);
        viewModel.response_validator_address.observe(this, addressResponseObserver);


        viewModel.response_validator_recipient.observe(this, response_observer);
        // viewModel.response_validator_transaction.observe(this, transaction_response_observer);
        //getBank();
        //binding.ivBack.setOnClickListener(this);

        // binding.llUpper.tvTitle.setText(getIntent().getStringExtra("bankName")!=null?getIntent().getStringExtra("bankName"):"");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item2, statelist);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        binding.statespinner.setPopupBackgroundResource(R.drawable.iv_bg);
        binding.statespinner.setAdapter(adapter);
        binding.statespinner.setOnItemSelectedListener(this);


        binding.llUpper.ivBack.setOnClickListener(this);
        if (getIntent().getSerializableExtra("data") != null) {
            userData = (GetUserByEmail.Data) getIntent().getSerializableExtra("data");
            binding.llName.setVisibility(View.GONE);

        } else {
            binding.llName.setVisibility(View.VISIBLE);

        }
        getCountryId();
        // viewModel.getActiveCountry(apicalls);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedstate = adapterView.getItemAtPosition(position).toString();

        String value = adapterView.getItemAtPosition(position).toString();
        System.out.println("value of dropdown and statelist" + value + statelist[0]);

        if (value.equals(statelist[0])) {
            // (selectedItemView as TextView).setTextColor(Color.parseColor("#676767"))
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextColor(Color.parseColor("#676767"));
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        binding.etSelectPro.setText("");

    }

    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {
                //  viewModel.getuserValidate(apicalls,charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    };

    TextWatcher typeaddress = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {

                System.out.println("Typed address" + charSequence.toString());
                if (countryCode.equals("US")) {
                    binding.etHouse.setText(charSequence);
                }

                if (!isAddressCheck(charSequence.toString())) {
                    binding.etAddress.setError("Enter valid address");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher typeaddress2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {

                System.out.println("Typed address" + charSequence.toString());
                if (!isAddressCheck(charSequence.toString())) {
                    binding.etHouse.setError("Enter valid address");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher typepincode = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {

                System.out.println("Typed address" + charSequence.toString());
                if (!isAddressCheck(charSequence.toString())) {
                    binding.etZipCode.setError("Enter valid zip");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher typecity = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {

                System.out.println("Typed address" + charSequence.toString());
                if (!isAddressCheck(charSequence.toString())) {
                    binding.etCity.setError("Enter valid city");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher typestate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() > 0) {

                System.out.println("Typed address" + charSequence.toString());
                if (!isAddressCheck(charSequence.toString())) {
                    binding.etSelectPro.setError("Enter valid state");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private void getCountryId() {
        Utils.showDialog(this, "Loading");
        viewModel.addCountry(apicalls, countryCode);
    }


    private void intAdapter() {
        /*    myAdapter = new GetBankAdapter(this, transactionArrayList);
          //  binding.rvBank.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();*/
    }


    private void createTransacton() {
        if (binding.etFirstName.getText().toString().isEmpty()) {

            Utils.showToast(getApplicationContext(), getResources().getString(R.string.enter_first_name));
        } else if (binding.etLastName.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.enter_last_name));
        } else if (binding.etEmail.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.enter_last_name));
        } else if (!Utils.isValideEmail(binding.etEmail.getText().toString())) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.enter_valid_employeeid));
        } else if (binding.etNumber.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_mobile_number));
        } else if (binding.etAddress.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_address));
        } else if (binding.etZipCode.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_postal_code));
        } else if (binding.etCity.getText().toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_city));
        }
      /*  else if(!binding.etAddress.getText().toString().matches(addressLineRegex)){
            Utils.showToast(getApplicationContext(),"Enter valid address");
        }*/
    /*    else if(!binding.etHouse.getText().toString().matches(addressLine2Regex)){
            Utils.showToast(getApplicationContext(),"Enter valid address");
        }*/

        else if (binding.etSelectPro.getText().toString().isEmpty() && !countryCode.equals("US")) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_province));
        } else if (emailcheck.get()) {
            Utils.showToast(getApplicationContext(), getResources().getString(R.string.email_id_already_register));
        } else if (countryCode.equals("US")) {

            if (!binding.etAddress.getText().toString().equals(binding.etHouse.getText().toString())) {
                Utils.showToast(getApplicationContext(), getResources().getString(R.string.Addresstand2));
            } else if (selectedstate.equals("Province / State")) {
                Utils.showToast(getApplicationContext(), getResources().getString(R.string.please_enter_province));
            } else {
                Utils.showDialog(this, "Loading");
                viewModel.getExistingUser(apicalls, binding.etEmail.getText().toString());

            }
        } else {
            Utils.showDialog(this, "Loading");
            viewModel.getExistingUser(apicalls, binding.etEmail.getText().toString());

            //  viewModel.addRecipientSignUp(binding, countryId, apicalls);


        }



    /*    String payeeId =(MyPref.getInstance(this).readPrefs(MyPref.APPID));
        transactionEntity = new PostCreateTransactionBody.TransactionEntity("","fgg","","","","","CAD","BANK_DEPOSIT",10);
        receiverEntity = new PostCreateTransactionBody.ReceiverEntity(String.valueOf(payeeId));

        postCreateTransactionBody=new PostCreateTransactionBody(transactionEntity,receiverEntity);
        viewModel.createTransaction(postCreateTransactionBody,apicalls);*/


    }

    Observer<GetBankResponse> transaction_response_observer = new Observer<GetBankResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetBankResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(AddNewRecipientWithAddressActivity.this, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (loginBean.getStatus()) {
                transactionArrayList.clear();
                transactionArrayList.addAll(loginBean.getData());
                intAdapter();
                //  myAdapter.setList(transactionArrayList);
            }

        }
    };

    Observer<AddNewRecipientREsposne> response_observer = new Observer<AddNewRecipientREsposne>() {

        @Override
        public void onChanged(@Nullable AddNewRecipientREsposne data) {
            if (data.isStatus()) {
                userId = String.valueOf(data.getData().getAptCardId());
                //  Utils.showDialog(SentMoenyRecipientActivity.this,"Loading");
                //  viewModel.address(binding,apicalls);

                startActivity(new Intent(AddNewRecipientWithAddressActivity.this, MoneyConverterActivity.class)
                        .putExtra(Keys.COUNTRY_CODE, countryCode)
                        .putExtra(Keys.USER_ID, userId)
                        .putExtra(Keys.CURRENCY_CODE, currency)
                        .putExtra(Keys.TOTAL_AMOUNT, totalkAmount)
                        .putExtra(Keys.COUNTRY_RATE, country_rate)
                        .putExtra(Keys.SERVICE_FEE, service_fee)
                        .putExtra(Keys.COUNTRY, country)
                        .putExtra(Keys.COUNTRY_FLAG, flag)
                        .putExtra(Keys.COUNTRY_DIAL_CODE, dial_code)

                );

               /* startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                finish();*/
            } else {
                Utils.showToast(getApplicationContext(), data.getMessage());
            }


        }
    };
    Observer<AddCountryResponse> country_response_observer = new Observer<AddCountryResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable AddCountryResponse data) {
            if (data.getStatus()) {
                countryId = data.getData().getId();
            }

        }
    };

    Observer<AddAddressResponse> addressResponseObserver = addAddressResponse -> {

        if (addAddressResponse.getStatus()) {

        }
    };
    Observer<CountriesResponse> response_observer_countries = new Observer<CountriesResponse>() {

        @Override
        public void onChanged(@Nullable CountriesResponse countriesResponse) {
            if (countriesResponse == null) {
                Utils.showAlert(getApplicationContext(), "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            countryCodeList.clear();
            if (countriesResponse.getData() != null && !countriesResponse.getData().isEmpty()) {
                countryCodeList.addAll(countriesResponse.getData());
            }
            if (!countryCodeList.isEmpty()) {
                binding.editCountryCode.setText(countryCodeList.get(0).getCountryCode());
                //countryId = countryCodeList.get(0).get_id();
                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(countryCodeList.get(0).getFlagUrl())
                        .into(binding.ivFlag);

            }
        }
    };

    void showCountryCodes() {
        dialog = Utils.showCustomDialog(AddNewRecipientWithAddressActivity.this, R.layout.dialog_country_dial_code);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv_dial_code);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddNewRecipientWithAddressActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dialCodeAdapter = new DialCodeAdapter(AddNewRecipientWithAddressActivity.this, countryCodeList);
        recyclerView.setAdapter(dialCodeAdapter);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(AddNewRecipientWithAddressActivity.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(AddNewRecipientWithAddressActivity.this, R.drawable.decorater_selctor));
        recyclerView.addItemDecoration(itemDecorator);
        // dialCodeAdapter.setOnItemClickListener(SenderInfoActivity.this);
        search = dialog.findViewById(R.id.sv_doctor);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dialCodeAdapter.getFilter().filter(editable);
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.tvContinue) {
            createTransacton();
        }
        if (view == binding.llUpper.ivBack) {
            onBackPressed();
        }
        if (view == binding.addresssearchid) {
            //   startActivity(new Intent(getApplicationContext(), GoogleplacesActivity.class));
            //   startActivity(new Intent(getApplicationContext(), GoogleplacesActivity.class));

            newfirst = binding.etFirstName.getText().toString();
            System.out.println("first name normal flow****" + newfirst);
            newlastname = binding.etLastName.getText().toString();
            newnumber = binding.etNumber.getText().toString();
            email = binding.etEmail.getText().toString();


            startActivity(new Intent(getApplicationContext(), PlacesActivity.class));

        }
       /* if(view == binding.ivFlag){
            showCountryCodes();
        }*/

    }

    Observer<GetUserByEmail> userObserver = new Observer<GetUserByEmail>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetUserByEmail getUserByEmail) {
            if (getUserByEmail.getData().size() == 0) {
                emailcheck.set(false);
            } else {

                emailcheck.set(true);
            }


        }
    };
    Observer<GetExistingUserResponse> exiting_user_Observer = new Observer<GetExistingUserResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetExistingUserResponse data) {

            if (data.getStatus()) {
                showDialog(data);
            } else {


                String email = binding.etEmail.getText().toString();
                String password = "123456";
                String phone = binding.etNumber.getText().toString();
                String name = binding.etFirstName.getText().toString();
                String lastname = binding.etLastName.getText().toString();
                // String countryCode = binding.editCountryCode.getText().toString();
                AddRecipientRequest addRecipientRequest = new AddRecipientRequest();
                addRecipientRequest.setFirstName(name);
                addRecipientRequest.setLastName(lastname);
                addRecipientRequest.setEmail(email);
                addRecipientRequest.setPassword("123456");
                addRecipientRequest.setMobile(phone);
                addRecipientRequest.setCountryid(countryId);
                addRecipientRequest.setStreet(binding.etAddress.getText().toString());
                addRecipientRequest.setCity(binding.etCity.getText().toString());

                if (binding.etHouse.getText().toString().isEmpty()) {
                    addRecipientRequest.setStreetLine2("-");
                } else {
                    addRecipientRequest.setStreetLine2(binding.etHouse.getText().toString());
                }


                addRecipientRequest.setZip(binding.etZipCode.getText().toString());
                if (binding.stateprovinceLyt.getVisibility() == View.VISIBLE) {
                    addRecipientRequest.setState(selectedstate);
                } else {
                    addRecipientRequest.setState(binding.etSelectPro.getText().toString());
                }
                System.out.println("Currencyrate3" + country_rate);
                addRecipientRequest.setCountry_flag(flag);
                startActivity(new Intent(AddNewRecipientWithAddressActivity.this, MoneyConverterActivity.class)
                        .putExtra(Keys.COUNTRY_CODE, countryCode)
                        .putExtra(Keys.USER_ID, userId)
                        .putExtra(Keys.CURRENCY_CODE, currency)
                        .putExtra(Keys.TOTAL_AMOUNT, totalkAmount)
                        .putExtra(Keys.COUNTRY_RATE, country_rate)
                        .putExtra(Keys.SERVICE_FEE, service_fee)
                        .putExtra(Keys.COUNTRY, country)
                        .putExtra(Keys.COUNTRY_FLAG, flag)
                        .putExtra(Keys.COUNTRY_DIAL_CODE, dial_code)
                        .putExtra(Keys.EXISTING_USER_DATA, (Serializable) addRecipientRequest)

                );
            }


        }
    };

    public boolean isIfscCodeValid(String IFSCCode) {
        String regExp = "^[A-Z]{4}[0][A-Z0-9]{6}$";
        boolean isvalid = false;

        if (IFSCCode.length() > 0) {
            isvalid = IFSCCode.matches(regExp);
        }
        return isvalid;
    }

    public boolean isAddressCheck(String address) {
        //  String regExp = "^[A-Z]{4}[0][A-Z0-9]{6}$";

        Pattern regex = Pattern.compile("^[a-zA-Z0-9 ]*$");
        Matcher match = regex.matcher(address);
        return match.matches();
    }


    /*@Override
    public void onItemClick(int position, int id) {
        binding.editCountryCode.setText(""+countryCodeList.get(position).getCountryCode());
    }*/


    public void showDialog(GetExistingUserResponse data) {
        final Dialog dialog = Utils.showCustomDialog(this, R.layout.dialog_exiting_user);
        Button yes = dialog.findViewById(R.id.bt);
        TextView userName = dialog.findViewById(R.id.tvName);
        TextView userIcon = dialog.findViewById(R.id.tvIcon);
        TextView userEmail = dialog.findViewById(R.id.tvEmail);
        TextView tv_cancel = dialog.findViewById(R.id.tvcancel);
        userName.setText(data.getData().getFirst_name() + " " + data.getData().getLast_name());
        userIcon.setText(data.getData().getFirst_name().substring(0, 1).toUpperCase());
        userEmail.setText(data.getData().getEmail_id());
        yes.setOnClickListener(v -> {
            dialog.dismiss();


            startActivity(new Intent(AddNewRecipientWithAddressActivity.this, MoneyConverterActivity.class)
                    .putExtra(Keys.COUNTRY_CODE, countryCode)
                    .putExtra("existing", (Serializable) data)
                    .putExtra(Keys.CURRENCY_CODE, currency)
                    .putExtra(Keys.TOTAL_AMOUNT, totalkAmount)
                    .putExtra(Keys.COUNTRY_RATE, country_rate)
                    .putExtra(Keys.SERVICE_FEE, service_fee)
                    .putExtra(Keys.COUNTRY, country)
                    .putExtra(Keys.COUNTRY_FLAG, flag)
                    .putExtra(Keys.COUNTRY_DIAL_CODE, dial_code)

            );


        });
        tv_cancel.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(AddNewRecipientWithAddressActivity.this, RecipientActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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