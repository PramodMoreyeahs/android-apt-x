package com.apt_x.app.views.fragment.home;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apt_x.app.R;
import com.apt_x.app.model.CrossBorderHistoryResponse;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.FingerPrintEnable;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.loadfund.InteracETransferActivity;
import com.apt_x.app.views.activity.newTransactions.RecipientActivity;
import com.apt_x.app.views.adapter.TransactionAdapter;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.GetTransactionHistoryResponse;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.base.BaseFragment;
import com.apt_x.app.databinding.HomeFragmentLayoutBinding;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends BaseFragment implements DialogClickListener,FingerPrintEnable.onResult {

    private Context context;
    HomeActivity _activity;
    FingerprintManager fingerprintManager;
    FingerPrintEnable fingerPrintEnable;

    private HomeFragmentLayoutBinding binding;
    HomeFragmentViewModel viewModel;
    ApiCalls apicalls;
    private boolean isAscending;

    final ArrayList<GetTransactionHistoryResponse.WalletTransactionsEntity> transactionArrayList = new ArrayList<>();
    final ArrayList<CrossBorderHistoryResponse.DataEntity> crossboarderArrayList = new ArrayList<>();
    TransactionAdapter myAdapter;
    ObservableBoolean loadingState= new ObservableBoolean();

    @Nullable
    @Override
    public View onCreateView(@io.reactivex.annotations.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        initializeViews(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) _activity = (HomeActivity) context;
    }




    @SuppressLint({"FragmentLiveDataObserve", "UseCompatLoadingForDrawables"})
    @Override
    public void initializeViews(View rootView) {


      

        context = getActivity();

        apicalls = ApiCalls.getInstance(getActivity());
        binding.llSendMoney.setOnClickListener(this);
        binding.llLoadFund.setOnClickListener(this);
        viewModel.response_validator.observe(this, response_observer);
        //viewModel.response_validator_transaction.observe(this, transaction_response_observer);
        viewModel.response_validator_crossborder_history.observe(this, responseObserver_crossborder);
        _activity.binding.frameLayout.setVisibility(View.VISIBLE);
        _activity.binding.lvBottom.setVisibility(View.VISIBLE);

        if(MyPref.getInstance(context).readPrefs(MyPref.LANG_TYPE_PREF).equals("fr")){
            binding.llLoadFund.setImageDrawable(getResources().getDrawable(R.drawable.ic_load_fund_fr));
            binding.llSendMoney.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_fund_fr));
        }
        else
        {
            binding.llLoadFund.setImageDrawable(getResources().getDrawable(R.drawable.ic_load_fund_));
            binding.llSendMoney.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_fund_));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {



        /*    if(fingerprintManager!=null&& fingerprintManager.isHardwareDetected()) {

                if(MyPref.getInstance(getApplicationContext()).readBooleanPrefs(Pref.IS_BOIMETRIC)){
                    fingerPrintEnable.enableFingerPrint();
                    Pref.IS_FIRST = "0";
                }
            }
            else
            {

            }*/
        }
        binding.tvViewAll.setOnClickListener(v -> {
            NavController navHostController = new NavHostFragment().findNavController(this);
            navHostController.navigate(R.id.action_homeFragment_to_transactionFragment);


        });
        binding.swipRefresh.setOnRefreshListener(listner);

        getWalletBalanceApi();
        getTransaction();



     /*   if( MyPref.getInstance(context).readBooleanPrefs(MyPref.USE_FINGER_PRINT))
        {
            if(!MyPref.getInstance(context).readBooleanPrefs(Pref.IS_BOIMETRIC))
            {
                MyPref.getInstance(context).writeBooleanPrefs(Pref.IS_BOIMETRIC,true);
                fingerPrintEnable.enableFingerPrint();
            }

        }*/
           /* if(MyPref.getInstance(this).readBooleanPrefs(MyPref.USE_FINGER_PRINT)){
                FingerPrintBottomLoginFragment bottomSheet = new FingerPrintBottomLoginFragment();
                bottomSheet.show(getSupportFragmentManager(), "bottom_Sheet");
            }*/



    }

    SwipeRefreshLayout.OnRefreshListener listner= new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getWalletBalanceApi();
            getTransaction();

        }
    };

    private void initAdaptor()
    {

       /* Collections.sort(crossboarderArrayList, new Comparator<CrossBorderHistoryResponse.Data>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    @Override
                    public int compare(CrossBorderHistoryResponse.Data data, CrossBorderHistoryResponse.Data t1) {
                        try {
                          return f.parse(data.getTransaction_date()).compareTo(f.parse(t1.getTransaction_date()));
                        }
                        catch (Exception e)
                        {
                            throw  new IllegalArgumentException(e);
                        }
                    }



                });*/

            myAdapter = new TransactionAdapter(this,context, crossboarderArrayList,true);
        binding.rvTransaction.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }


    private void getTransaction() {
        Utils.showDialog(context,"Loading");
        viewModel.getCrossBorderTransaction(MyPref.getInstance(context).readPrefs(MyPref.APPID), apicalls);



    }

    private void getWalletBalanceApi() {
      loadingState.set(true);
        viewModel.getWalletBalance(apicalls);

    }


    Observer<GetWalletBalanceResponse> response_observer = new Observer<GetWalletBalanceResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetWalletBalanceResponse loginBean) {
            loadingState.set(false);
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (loginBean.getStatus()) {

                binding.tvTotalBalance.setText("$" + loginBean.getData().getBalance());
                MyPref.getInstance(context).writePrefs(MyPref.WALLET_BALANCE, loginBean.getData().getBalance());

            }

        }
    };

    /*Observer<GetTransactionHistoryResponse> transaction_response_observer = new Observer<GetTransactionHistoryResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetTransactionHistoryResponse data) {
            if (data == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (data.getStatus()) {



                for (int i = 0; i < data.getData().getWallettransactions().size(); i++) {
                    crossboarderArrayList.add(new CrossBorderHistoryResponse.Data("",
                            data.getData().getWallettransactions().get(i).getDate(), Double.valueOf(data.getData().getWallettransactions().get(i).getAmount())
                            , 0, data.getData().getWallettransactions().get(i).getDescription(), 0));
                }
               *//*  try {
                     Collections.sort(crossboarderArrayList, (item1, item2) -> {
                         Date date1 = stringToDate(item1.getTransaction_date());
                         Date date2 = stringToDate(item2.getTransaction_date());

                         if (date1 != null && date2 != null) {
                             boolean b1;
                             boolean b2;
                             if (isAscending) {
                                 b1 = date2.after(date1);
                                 b2 = date2.before(date1);
                             } else {
                                 b1 = date1.after(date2);
                                 b2 = date1.before(date2);
                             }

                             if (b1 != b2) {
                                 if (b1) {
                                     return -1;
                                 }
                                 if (!b1) {
                                     return 1;
                                 }
                             }
                         }
                         return 0;
                     });
                 }
                 catch (Exception e)
                 {
                     e.printStackTrace();
                 }*//*



                initAdaptor();

            }

        }
    };*/


    Observer<CrossBorderHistoryResponse> responseObserver_crossborder = new Observer<CrossBorderHistoryResponse>() {
        @Override
        public void onChanged(CrossBorderHistoryResponse data) {
            if (data.getStatus()) {

                    crossboarderArrayList.clear();
                    crossboarderArrayList.addAll(data.getData().getTransaction());
                binding.swipRefresh.setRefreshing(false);



                if(data.getData().getTransaction().size()>0){
                    binding.llTransaction.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.llTransaction.setVisibility(View.GONE);
                }
                initAdaptor();
               // viewModel.getTransaction(MyPref.getInstance(context).readPrefs(MyPref.WALLET_ID), apicalls);

            }else
            {
                binding.swipRefresh.setRefreshing(false);
            }
        }
    };

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_send_money:
                startActivity(new Intent(context, RecipientActivity.class));
                break;
            case R.id.ll_load_fund:
                startActivity(new Intent(context, InteracETransferActivity.class));
                break;
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

    @Override
    public void onclick(Boolean status) {
        System.out.println("Biometrics in Home fragment call back" + status);
    }
}