package com.apt_x.app.views.fragment.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.CrossBorderHistoryResponse;
import com.apt_x.app.model.GetTransactionHistoryResponse;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;

import io.reactivex.observers.DisposableObserver;


public class HomeFragmentViewModel extends ViewModel implements DialogClickListener {
    public final MutableLiveData<GetWalletBalanceResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<GetTransactionHistoryResponse> response_validator_transaction = new MutableLiveData<>();
    public final MutableLiveData<CrossBorderHistoryResponse> response_validator_crossborder_history = new MutableLiveData<>();


    public void getWalletBalance( ApiCalls apiCalls) {

        apiCalls.getWalletBalance(WalletBalanceDes);


    }

    public void getTransaction(String walletId, ApiCalls apiCalls) {

        apiCalls.getTransaction(transactionDes);


    }
    public void getCrossBorderTransaction(String id, ApiCalls apiCalls) {

        apiCalls.getCrossborderHistory(crossbordertransactionDes,id);


    }



    DisposableObserver<GetTransactionHistoryResponse> transactionDes = new DisposableObserver<GetTransactionHistoryResponse>() {

        @Override
        public void onNext(GetTransactionHistoryResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_transaction.setValue(loginResponse);

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
    DisposableObserver<CrossBorderHistoryResponse> crossbordertransactionDes = new DisposableObserver<CrossBorderHistoryResponse>() {

        @Override
        public void onNext(CrossBorderHistoryResponse data) {
            Utils.hideProgressDialog();
            response_validator_crossborder_history.setValue(data);

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


    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
