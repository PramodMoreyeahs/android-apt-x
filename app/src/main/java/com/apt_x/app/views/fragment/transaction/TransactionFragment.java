package com.apt_x.app.views.fragment.transaction;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityMyTransactionsBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.CrossBorderHistoryResponse;
import com.apt_x.app.model.GetTransactionHistoryResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
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
import java.util.Objects;

public class TransactionFragment extends BaseFragment implements DialogClickListener {

    private Context context;
    private ActivityMyTransactionsBinding binding;
    TransactionFragmentViewModel viewModel;
    ApiCalls apicalls;
    HomeActivity _activity;
    final ArrayList<GetTransactionHistoryResponse.WalletTransactionsEntity> transactionArrayList = new ArrayList<>();
    TransactionAdapter myAdapter;
    double sum = 0.00;
    double total;
    private boolean isAscending;
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

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_my_transactions, container, false);
        viewModel = ViewModelProviders.of(this).get(TransactionFragmentViewModel.class);
        initializeViews(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) _activity = (HomeActivity) context;
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public void initializeViews(View rootView) {
        context = getActivity();
        apicalls = ApiCalls.getInstance(getActivity());
        _activity.binding.frameLayout.setVisibility(View.GONE);
        _activity.binding.lvBottom.setVisibility(View.GONE);
       // viewModel.response_validator_transaction.observe(this, transaction_response_observer);
        viewModel.response_validator_crossborder_history.observe(this, responseObserver_crossborder);
        // initAdapter();
        binding.ivBack.setOnClickListener(v -> _activity.onBackPressed());

     /*   transactionArrayList.add(new GetTransactionHistoryResponse.DataEntity("John","-30","Apr 05 20"));
        transactionArrayList.add(new GetTransactionHistoryResponse.DataEntity("Mark","+100","Apr 05 20"));
        transactionArrayList.add(new GetTransactionHistoryResponse.DataEntity("Emma Watson","-40","Apr 05 20"));
        transactionArrayList.add(new GetTransactionHistoryResponse.DataEntity("Bill Nighy","+50","Apr 05 20"));*/

        //getWalletBalanceApi();
        getTransaction();


        // myAdapter.setList(transactionArrayList);


    }

    private void initAdaptor() {


        myAdapter = new TransactionAdapter(this,context, crossboarderArrayList, false);
        binding.rvTransaction.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    private void getTransaction() {
        Utils.showDialog(context, "loading");
        viewModel.getCrossBorderTransaction(MyPref.getInstance(context).readPrefs(MyPref.APPID), apicalls);
       // viewModel.getTransaction(MyPref.getInstance(context).readPrefs(MyPref.WALLET_ID), apicalls);
    }


   /* Observer<GetTransactionHistoryResponse> transaction_response_observer = new Observer<GetTransactionHistoryResponse>() {

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
                      if(Double.valueOf(data.getData().getWallettransactions().get(i).getAmount())>0)
                      {
                        sum=  sum-Double.valueOf(data.getData().getWallettransactions().get(i).getAmount());
                      }
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
                } catch (Exception e) {
                    e.printStackTrace();
                }*//*

                initAdaptor();

                for (int i = 0; i < crossboarderArrayList.size(); i++) {
                  //  if (crossboarderArrayList.get(i).getAmount() < 0) {
                        sum = sum + Math.abs(crossboarderArrayList.get(i).getAmount());


                    //}
                   *//* total = Math.abs(sum);
                    if (!crossboarderArrayList.get(i).getTransaction_id().isEmpty()) {
                        total = total + Math.abs(crossboarderArrayList.get(i).getAmount());
                    }*//*

                }
                binding.tvTotalAmount.setText("$ " + new DecimalFormat("###.##").format(Math.abs(sum)));
            }

        }
    };*/

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

    Observer<CrossBorderHistoryResponse> responseObserver_crossborder = new Observer<CrossBorderHistoryResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(CrossBorderHistoryResponse data) {
            if (data.getStatus()) {

              /*  for (int i=0;i<data.getData().size();i++)
                {
                 //   if(data.getData().get(i).getRecieverName()!=null)
                   // {
                        crossboarderArrayList.addAll(data.getData());
                    //}

                }*/

                crossboarderArrayList.clear();
                crossboarderArrayList.addAll(data.getData().getTransaction());
                Log.e("Crossboarder History", "" + crossboarderArrayList.size());
                initAdaptor();
                sum=0.00;
                for(int i=0;i<crossboarderArrayList.size();i++)
                {
                    if(crossboarderArrayList.get(i).getAmount().contains("-") && !crossboarderArrayList.get(i).getTransactionStatus().equals("FAILED"))
                    {
                        System.out.println("Total amount in amount" + crossboarderArrayList.get(i).getAmount().replaceAll("-",""));
                        System.out.println("Total amount in service fee" + crossboarderArrayList.get(i).getAmount().replaceAll("-",""));
                        sum=sum+Float.parseFloat(crossboarderArrayList.get(i).getAmount().replaceAll("-",""))+Float.parseFloat(crossboarderArrayList.get(i).getTransferFee());
                    }
                }


                System.out.println("Sum of transaction" + Utils.convertDecimalFormate2(sum));
                binding.tvTotalAmount.setText("$ "+Utils.convertDecimalFormate2(sum));


                String[] decimalSplit = Utils.convertDecimalFormate2(100.00).split("\\.");

              /*  System.out.println("Whole Number: " + decimalSplit[0]);
                System.out.println("Decimal Part: " + decimalSplit[1]);
*/
            /*    if(Objects.equals(decimalSplit[1], "00")){

                    binding.tvTotalAmount.setText("$ "+decimalSplit[0]);
                } else{

                    binding.tvTotalAmount.setText("$ "+Utils.convertDecimalFormate2(sum));
                }*/

            }
        }
    };

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