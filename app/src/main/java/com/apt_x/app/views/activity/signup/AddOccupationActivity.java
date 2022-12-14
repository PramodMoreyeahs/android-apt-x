package com.apt_x.app.views.activity.signup;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddOccupationBinding;
import com.apt_x.app.model.CreateWalletResponse;
import com.apt_x.app.model.GetOccupationResponse;
import com.apt_x.app.model.SignUpResponseBean;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AddOccupationActivity extends BaseActivity {
    SignUpViewModel viewModel;
    private ActivityAddOccupationBinding binding;
    public ApiCalls apiCalls;
    Context context = AddOccupationActivity.this;
    Activity activity = AddOccupationActivity.this;


    private static final String TAG = "MainActivity";
    ObservableBoolean terms1 = new ObservableBoolean();
    ObservableBoolean terms2 = new ObservableBoolean();
    ObservableBoolean terms3 = new ObservableBoolean();
    List<String> occupationList= new ArrayList<>();
    List<String> field_occupationList= new ArrayList<>();
    String firstName="",lastName="",mobile="",email="",password="",countryId="",occupation="",field_occupation="";


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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_occupation);
        initializeViews();
    }

    @Override
    public void initializeViews() {
        Bundle bundle=getIntent().getBundleExtra("bundle");
         email=bundle.getString("email");
        firstName=bundle.getString("firstName");
         lastName=bundle.getString("lastName");
         mobile=bundle.getString("number");
         password=bundle.getString("password");
         countryId=bundle.getString("countryId");

        viewModel=ViewModelProviders.of(this).get(SignUpViewModel.class);
        apiCalls=ApiCalls.getInstance(this);

        viewModel.response_validator.observe(this, response_observer);
        viewModel.response_validator_create_wallet.observe(this, response_observer_create_wallet);
        viewModel.response_occupation.observe(this,response_observer_occupation);
        viewModel.getOccupation(apiCalls);
        // binding.setActivity(this);
        binding.setTerms1(terms1);
        binding.setTerms2(terms2);
        binding.setTerms3(terms3);
        makeTextViewResizable(binding.tvTerms, 2, "see more", true);
        binding.occupationSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupation = occupationList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.fieldOccupationSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                field_occupation = field_occupationList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    Observer<SignUpResponseBean> response_observer = new Observer<SignUpResponseBean>() {

        @Override
        public void onChanged(@Nullable SignUpResponseBean loginBean) {
           /* if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }*/

            if(loginBean.getStatus())
            {

                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.ACCESS_TOKEN, loginBean.getToken());
                Pref.setAccessToken(AddOccupationActivity.this, "Bearer "+loginBean.getToken());
                Pref.setBoolean(AddOccupationActivity.this, true, Pref.REMEMBER_ME);
                Pref.setBoolean(AddOccupationActivity.this, true, Pref.IS_LOGIN);
                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.APPID, String.valueOf(loginBean.getData().getAptCard_Id()));
                viewModel.createWallet( MyPref.getInstance(AddOccupationActivity.this).readPrefs(MyPref.APPID),apiCalls);

               /* startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                finish();*/
            }


            else {
               // logoutGoogle();
              Toast.makeText(getApplicationContext(),""+loginBean.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }

        }
    };

    Observer<CreateWalletResponse> response_observer_create_wallet= new Observer<CreateWalletResponse>() {

        @Override
        public void onChanged(@Nullable CreateWalletResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if(loginBean.getStatus())
            {

                MyPref.getInstance(getApplicationContext()).writePrefs(MyPref.WALLET_ID, String.valueOf(loginBean.getData().getWalletCreateId()));
                // startActivity(new Intent(getApplicationContext(), KYCActivity.class));
                startActivity(new Intent(getApplicationContext(), VerifyPhoneActivity.class)
                        .putExtra(Keys.EMAIL,email));


                //todo change address screen
                /*startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                finish();*/
            }


            else {
                //logoutGoogle();
                Utils.showToast(context,loginBean.getMessage());
            }

        }
    };
    Observer<GetOccupationResponse> response_observer_occupation = new Observer<GetOccupationResponse>() {

        @Override
        public void onChanged(@Nullable GetOccupationResponse data) {
        if(data.getStatus())
        {
          occupationList.clear();
          field_occupationList.clear();
          occupationList.add("Occupation");
          field_occupationList.add("Field of occupation");
          occupationList.addAll(data.getData().getOccupation1());
          field_occupationList.addAll(data.getData().getOccupation2());
            ArrayAdapter ad1
                    = new ArrayAdapter(AddOccupationActivity.
                    this,
                    R.layout.spinner_item,
                    occupationList);
            ad1.setDropDownViewResource(R.layout.spinner_item);
            binding.occupationSP.setPopupBackgroundResource(R.drawable.iv_bg);

            binding.occupationSP.setAdapter(ad1);
            ArrayAdapter ad2
                    = new ArrayAdapter(AddOccupationActivity.
                    this,
                    R.layout.spinner_item,
                    field_occupationList);
            ad2.setDropDownViewResource(R.layout.spinner_item);
            binding.fieldOccupationSP.setPopupBackgroundResource(R.drawable.iv_bg);
            binding.fieldOccupationSP.setAdapter(ad2);

        }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check1:
                if (terms1.get() == true) {
                    terms1.set(false);
                } else {
                    terms1.set(true);
                }
                break;
            case R.id.check2:
                if (terms2.get() == true) {
                    terms2.set(false);
                } else {
                    terms2.set(true);
                }


                break;
            case R.id.check3:
                if (terms3.get() == true) {
                    terms3.set(false);
                } else {
                    terms3.set(true);
                }

                break;

            case R.id.tvContinue:
                validate();
                break;
            case R.id.ivBack:
                onBackPressed();
        }
    }

    private void validate() {

        if(occupation.equals("Occupation"))
        {
         Utils.showToast(getApplicationContext(),"Select Occupation");
        }
        else if(field_occupation.equals("Field of occupation"))
        {
            Utils.showToast(getApplicationContext(),"Select Field Occupation");
        }
        else if(terms1.get()==false)
        {
            Utils.showToast(getApplicationContext(),"Check all terms & Condition");
        }
        else if(terms2.get()==false)
        {
            Utils.showToast(getApplicationContext(),"Check all terms & Condition");
        }
        else if( terms3.get()== false)
        {
            Utils.showToast(getApplicationContext(),"Check all terms & Condition");
        }
        else
        {
            Utils.showDialog(this, getString(R.string.please_wait));
           // viewModel.doSignUp(email,password,mobile,firstName,lastName, countryId,occupation, apiCalls);
        }
    }

    //load the country codes









    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#2FB1F8"));
        }


        @Override
        public void onClick(View widget) {


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
