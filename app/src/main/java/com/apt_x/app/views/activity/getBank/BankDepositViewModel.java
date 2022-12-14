package com.apt_x.app.views.activity.getBank;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.GetBankBranch;
import com.apt_x.app.model.GetBankBranchesResponse;
import com.apt_x.app.model.GetBankModel;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.model.VerifyOtpResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;

import java.util.Objects;

import io.reactivex.observers.DisposableObserver;


public class BankDepositViewModel extends ViewModel implements DialogClickListener {
    public final MutableLiveData<GetWalletBalanceResponse> response_validator = new MutableLiveData<>();
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<GetBankBranchesResponse> response_validator_transaction1 = new MutableLiveData<>();

    public final MutableLiveData<GetBankModel> response_validator_transaction = new MutableLiveData<>();
    public final MutableLiveData<GetBankBranch> response_validator_bank_branch = new MutableLiveData<>();
    public final MutableLiveData<VerifyOtpResponse> response_validator_otp = new MutableLiveData<>();

    public void convertMoney(String paymentMode, String receiveCurrency,String sourceCurrency, String receiveCountry,String sentAmount, ApiCalls apiCalls) {
        apiCalls.convertMoney(paymentMode,receiveCurrency,sourceCurrency,receiveCountry,sentAmount ,convertMoneydes);
    }
    public void getBank( String country,ApiCalls apiCalls) {
        apiCalls.getBank( country,transactionDes);

    }
    public void getBankBranch( String bankId,ApiCalls apiCalls) {
        apiCalls.getBankBranch( bankId,bankbranchDes);

    }

    /*public void createTransaction(PostCreateTransactionBody postCreateTransactionBody, ApiCalls apiCalls) {
        apiCalls.createTransaction( postCreateTransactionBody,transactionDes);

    }*/
    public void getBankBranches( String bankId,ApiCalls apiCalls) {
        apiCalls.getBankBranches( bankId,transactionDes1);

    }

    DisposableObserver<VerifyOtpResponse> convertMoneydes = new DisposableObserver<VerifyOtpResponse>() {

        @Override
        public void onNext(VerifyOtpResponse verifyOtpResponse) {
            Utils.hideProgressDialog();
            response_validator_otp.setValue(verifyOtpResponse);
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

    DisposableObserver<GetBankModel> transactionDes = new DisposableObserver<GetBankModel>() {

        @Override
        public void onNext(GetBankModel getBankModel) {
            Utils.hideProgressDialog();
            response_validator_transaction.setValue(getBankModel);

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
    DisposableObserver<GetBankBranch> bankbranchDes = new DisposableObserver<GetBankBranch>() {

        @Override
        public void onNext(GetBankBranch getBankModel) {
            Utils.hideProgressDialog();
            response_validator_bank_branch.setValue(getBankModel);

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
    DisposableObserver<GetBankBranchesResponse> transactionDes1 = new DisposableObserver<GetBankBranchesResponse>() {

        @Override
        public void onNext(GetBankBranchesResponse loginResponse) {
            Utils.hideProgressDialog();
            response_validator_transaction1.setValue(loginResponse);

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
