package com.apt_x.app.views.fragment.transaction;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityTransactionsDetailsBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.CrossBorderHistoryResponse;
import com.apt_x.app.model.GetTransactionHistoryResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.DateParser;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.adapter.TransactionAdapter;
import com.apt_x.app.views.base.BaseFragment;
import com.google.android.material.transition.MaterialSharedAxis;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailFragment extends BaseFragment implements DialogClickListener {

    private Context context;
    private ActivityTransactionsDetailsBinding binding;
    TransactionFragmentViewModel viewModel;
    ApiCalls apicalls;
    HomeActivity _activity;
    final ArrayList<GetTransactionHistoryResponse.WalletTransactionsEntity> transactionArrayList = new ArrayList<>();
    TransactionAdapter myAdapter;
    double sum = 0;
    double total;
    private boolean isAscending;
    CrossBorderHistoryResponse.DataEntity _data;
    final ArrayList<CrossBorderHistoryResponse.DataEntity> crossboarderArrayList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
        setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@io.reactivex.annotations.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_transactions_details, container, false);
        viewModel = ViewModelProviders.of(this).get(TransactionFragmentViewModel.class);
        initializeViews(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) _activity = (HomeActivity) context;
    }

    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    @Override
    public void initializeViews(View rootView) {
        context = getActivity();
        apicalls = ApiCalls.getInstance(getActivity());
        _activity.binding.frameLayout.setVisibility(View.GONE);
        _activity.binding.lvBottom.setVisibility(View.GONE);

//        System.out.println("BAnk name in trans his" + _data.getBankName().toString());

       // viewModel.response_validator_transaction.observe(this, transaction_response_observer);
     binding.tvCopy.setOnClickListener(this);
        // initAdapter();
        binding.frameLayout2.setOnClickListener(v -> _activity.onBackPressed());
        _data= (CrossBorderHistoryResponse.DataEntity) getArguments().getSerializable("bundle");
        Log.e("Transaction Data ",""+_data.getAmount());

        binding.tvTransferTo.setText(_data.getDescription()!=null?""+getString(R.string.to)+" : "+_data.getDescription():"");
        binding.tvTransactionId.setText(_data.getTransactionId()!=null?_data.getTransactionId():"");
        binding.tvBought.setText(_data.getBought()!=null?_data.getBought():"");

        binding.tvSold.setText(_data.getSold()!=null?"$ "+ Utils.convertDecimalFormate((Float.parseFloat(_data.getSold().replaceAll("\\$","")))) :"");
       // binding.tvSold.setText(_data.getSold()!=null? _data.getSold() :"");

        binding.tvUSend.setText(_data.getSold()!=null?"$ "+ Utils.convertDecimalFormate((Float.parseFloat(_data.getSold().replaceAll("\\$","")))):"");

        binding.tvRate.setText(_data.getRate()!=null?Utils.convertDecimalFormate4(Float.parseFloat(_data.getRate())):"");

     //   System.out.println("BANK NAME in TRANS DETAIl" + _data.getBankName());



        binding.tvBankName.setText(_data.getBankName()==null?"-":_data.getBankName());
        System.out.println("BANK NAME in TRANS DETAIl" + _data.getBankName());
                if(_data.getBankName().equals("-")){
            binding.banknamelayout.setVisibility(View.GONE);
        }else{
            binding.banknamelayout.setVisibility(View.VISIBLE);
        }
        binding.tvCountryName.setText(_data.getCountryName()!=null?_data.getCountryName():"");

        binding.tvTransferFee.setText(_data.getTransferFee()!=null?"$ "+ Utils.convertDecimalFormate(Float.parseFloat(_data.getTransferFee())):"");
        System.out.println("check u spent" + Utils.convertDecimalFormate(Float.parseFloat(_data.getTransferFee())));

        binding.tvRecieved.setText(_data.getBought()!=null?_data.getBought():"");
        binding.tvStatus.setText(""+_data.getTransactionStatus());

        binding.tvUser.setText( MyPref.getInstance(getApplicationContext()).readPrefs(MyPref.APPID));


       try {
           float amount= Float.parseFloat(_data.getAmount().replaceAll("-",""));
           binding.tvTotalAmount.setText("$ "+Utils.convertDecimalFormate((amount+Float.parseFloat(_data.getTransferFee()))));
        //   double amountdisplay=amount-1.257;
        //   binding.tvUSend.setText("$ "+new DecimalFormat("###.##").format(amountdisplay));
           binding.tvTotalPaid.setText("$ "+  Utils.convertDecimalFormate((amount+Float.parseFloat(_data.getTransferFee()))));
           binding.tvdate.setText(""+ DateParser.simpleOnlyDate(_data.getDate()));
           binding.tvtime.setText(""+ DateParser.simpleTime(_data.getDate()));
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }








    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.llSendMoney:
                startActivity(new Intent(context, NewTransactionActivity.class));
                break;
            case R.id.llLoadFund:
                startActivity(new Intent(context, LoadFundActivity.class));
                break;*/

            case R.id.tv_copy:
                ClipboardManager clipboardManager1 = (ClipboardManager) _activity.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager1.setText(binding.tvTransactionId.getText().toString());
                Utils.showToast(_activity, binding.tvTransactionId.getText().toString() + " Copied");

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA || Utils.onRequestPermissionsResult(permissions, grantResults)) {
            // startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
        } else {
            Utils.showToast(context, getString(R.string.allow_camera_permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }



    public static Date stringToDate(String strDate) {
        if (strDate == null)
            return null;

        // change the date format whatever you have used in your model class.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = null;
        try {
            date = format.parse(strDate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}