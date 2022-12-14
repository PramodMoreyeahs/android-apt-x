package com.apt_x.app.views.activity.withdraw;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apt_x.app.application.MyApp;
import com.apt_x.app.databinding.ActivityAddNewCardBinding;
import com.apt_x.app.databinding.ActivityAddNewEftAccountBinding;
import com.apt_x.app.databinding.ActivityMoneySendingToBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.AddBankDisburesmentRequest;
import com.apt_x.app.model.AddCardDisbursmentRequest;
import com.apt_x.app.model.AddDisburesementResponse;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.model.bean.AddEftAccountResponse;
import com.apt_x.app.model.bean.GetBankListResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Connectivity;
import com.apt_x.app.utils.Utils;

import java.util.Objects;
import java.util.Random;

import io.reactivex.observers.DisposableObserver;


public class WithdrawViewModel extends ViewModel implements DialogClickListener {

    public final MutableLiveData<GetWalletBalanceResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<GetBankListResponse> response_validator_bank_list = new MutableLiveData<>();
    public final MutableLiveData<AddDisburesementResponse> response_add_disbursment = new MutableLiveData<>();
    public final MutableLiveData<AddEftAccountResponse> response_validator_add_bank = new MutableLiveData<>();
    public AddBankDisburesmentRequest _addBankDisburesmentRequest;
    public AddCardDisbursmentRequest _addAddCardDisbursmentRequest;

    public void getAccountList( ApiCalls apiCalls,String payeeId) {
        apiCalls.getAccountList(bankListDes,payeeId);

    }
    public void addCard(ActivityAddNewCardBinding binding, ApiCalls apiCalls) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
        } else if (TextUtils.isEmpty(binding.etDebitCardNumber.getText().toString())) {
            validator.setValue(AppUtils.ENTER_DEBUTNUMBER);
        }
        else if (binding.etDebitCardNumber.getText().toString().length()<16) {
            validator.setValue(AppUtils.INVALID_DEBUTNUMBER);
        }
        else {

            apiCalls.addCard(binding.etAccountName.getText().toString(), binding.etDebitCardNumber.getText().toString(), binding.etExpirydate.getText().toString(), addEftAccountDes);
        }
    }
    public void addDisbursment(ActivityMoneySendingToBinding binding, GetBankListResponse.DataEntity _data,ApiCalls apiCalls) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
        } else if (TextUtils.isEmpty(binding.etAmount.getText().toString())) {
            validator.setValue(AppUtils.ENTER_AMOUNT);
        } else {
            Random random = new Random(System.nanoTime());
            int randomInt = random.nextInt(1000000000);
            _addBankDisburesmentRequest = new AddBankDisburesmentRequest();
            _addBankDisburesmentRequest.setAmount(Integer.parseInt(binding.etAmount.getText().toString()));
            _addBankDisburesmentRequest.setPayeeid((MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID)));
            _addBankDisburesmentRequest.setTransactiontype("EFT");
            _addBankDisburesmentRequest.setCurrency("CAD");
            _addBankDisburesmentRequest.setReferenceid(String.valueOf(randomInt));
            _addBankDisburesmentRequest.setBanknumber(_data.getBanknumber());
            _addBankDisburesmentRequest.setBranchtransitnumber("12345");
            _addBankDisburesmentRequest.setAccountnumber(_data.getDisbursementnumber());
            _addBankDisburesmentRequest.setInstrumentid(_data.getId());
            _addBankDisburesmentRequest.setProgram(11);

            apiCalls.addDisburesement2(_addBankDisburesmentRequest, addDisbursment);
        }
    }


    public void getWalletBalance( ApiCalls apiCalls) {

        apiCalls.getWalletBalance(WalletBalanceDes);


    }
    public void addCardDisbursment(ActivityMoneySendingToBinding binding, GetBankListResponse.DataEntity _data,ApiCalls apiCalls) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
        } else if (TextUtils.isEmpty(binding.etAmount.getText().toString())) {
            validator.setValue(AppUtils.ENTER_AMOUNT);
        } else {
            Random random = new Random(System.nanoTime());
            int randomInt = random.nextInt(1000000000);
            _addAddCardDisbursmentRequest=new AddCardDisbursmentRequest();
          //  _addAddCardDisbursmentRequest.setDisbursementNumber(_data.getDisbursementnumber());
            _addAddCardDisbursmentRequest.setPayeeId(MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID));
            _addAddCardDisbursmentRequest.setAmount(Integer.parseInt(binding.etAmount.getText().toString()));
            _addAddCardDisbursmentRequest.setTransactionType("CARD");
            _addAddCardDisbursmentRequest.setCurrency("INR");
             _addAddCardDisbursmentRequest.setExpirationDate(_data.getExpirationdate());
             _addAddCardDisbursmentRequest.setReferenceId(String.valueOf(randomInt));
             _addAddCardDisbursmentRequest.setInstrumentId(_data.getId());

            apiCalls.addCardDisburesement(_addAddCardDisbursmentRequest, addDisbursment);
        }
    }
    public void addEftAccount(ActivityAddNewEftAccountBinding binding, ApiCalls apiCalls) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
        } else if (TextUtils.isEmpty(binding.etAccountNumber.getText().toString())) {
            validator.setValue(AppUtils.ENTER_ACCOUNT_NUMBER);
        } else {
            apiCalls.addNewEftAccount(binding.etBankId.getText().toString(), binding.etTransitNumber.getText().toString(), binding.etAccountNumber.getText().toString(), addEftAccountDes);
        }
    }

    DisposableObserver<GetBankListResponse> bankListDes = new DisposableObserver<GetBankListResponse>() {

        @Override
        public void onNext(GetBankListResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_bank_list.setValue(loginResponse);

        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.USERREGISTERED);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    DisposableObserver<GetWalletBalanceResponse> WalletBalanceDes = new DisposableObserver<GetWalletBalanceResponse>() {

        @Override
        public void onNext(GetWalletBalanceResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator.setValue(loginResponse);

        }

        @Override
        public void onError(Throwable e) {
            validator.setValue(AppUtils.USERREGISTERED);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };


        DisposableObserver<AddEftAccountResponse> addEftAccountDes = new DisposableObserver<AddEftAccountResponse>() {

        @Override
        public void onNext(AddEftAccountResponse signUpResponseBean) {
            Utils.hideProgressDialog();
            response_validator_add_bank.setValue(signUpResponseBean);
        }

        @Override
        public void onError(Throwable e) {
            try {
                if (Objects.requireNonNull(e.getMessage()).contains("400")) {

                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

   /* public boolean validateAddBank(ActivityMoneySendingToBinding binding, GetBankListResponse.DataEntity _data) {
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return false;
        } else if (TextUtils.isEmpty(binding.etAmount.getText().toString())) {
            validator.setValue(AppUtils.ENTER_AMOUNT);
            return false;
        }
        else
        {

            _addBankRequest=new AddBankRequest();
            _addBankRequest.setAmount(Integer.parseInt(binding.etAmount.getText().toString()));
            _addBankRequest.setPayeeid((MyPref.getInstance(MyApp.getInstance()).readPrefs(MyPref.APPID)));
            _addBankRequest.setTransactiontype("EFT");
            _addBankRequest.setCurrency("CAD");
            _addBankRequest.setReferenceid("oren#2w23h");
            _addBankRequest.setBanknumber(_data.getBanknumber());
            _addBankRequest.setBranchtransitnumber("");
            _addBankRequest.setAccountnumber(_data.getDisbursementnumber());
            _addBankRequest.setInstrumentid(_data.getId());
            _addBankRequest.setProgram(11);






            return  true;

        }

    }*/

    DisposableObserver<AddDisburesementResponse> addDisbursment = new DisposableObserver<AddDisburesementResponse>() {

        @Override
        public void onNext(AddDisburesementResponse addDisburesementResponse) {
            Utils.hideProgressDialog();
            response_add_disbursment.setValue(addDisburesementResponse);
        }

        @Override
        public void onError(Throwable e) {
            try {
                if (Objects.requireNonNull(e.getMessage()).contains("400")) {

                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            validator.setValue(AppUtils.SERVER_ERROR);
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

}
