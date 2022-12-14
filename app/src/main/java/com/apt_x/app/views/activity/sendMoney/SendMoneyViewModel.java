package com.apt_x.app.views.activity.sendMoney;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apt_x.app.databinding.ActivityAddNewRecipientBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.ExistingP2PResponse;
import com.apt_x.app.model.GetIdentityResponse;
import com.apt_x.app.model.P2PRequest;
import com.apt_x.app.model.P2PResponse;
import com.apt_x.app.model.SendMoneyWalletRequest;
import com.apt_x.app.model.UserListP2p;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.Utils;
import com.google.gson.JsonObject;

import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class SendMoneyViewModel extends ViewModel implements DialogClickListener {


    MutableLiveData<Integer> validator=new MutableLiveData<>();
    MutableLiveData<GetIdentityResponse> get_identity_validator= new MutableLiveData<>();
    MutableLiveData<JsonObject> send_money_validator= new MutableLiveData<>();
    MutableLiveData<P2PResponse> user_add_validator= new MutableLiveData<>();
    MutableLiveData<UserListP2p> user_list_p2p_validator= new MutableLiveData<>();
    MutableLiveData<ExistingP2PResponse> user_existing_p2p_validator= new MutableLiveData<>();



    public void getIdentity(ApiCalls apiCalls,String email)
    {
       apiCalls.getIdentity(email,get_idenity_res);
    }
    public void sendMoney(ApiCalls apiCalls, SendMoneyWalletRequest sendMoneyWalletRequest)
    {
        apiCalls.sendMoney(send_money_res,sendMoneyWalletRequest);
    }
    public void addUser(ApiCalls apiCalls, P2PRequest p2PRequest)
    {
        apiCalls.userAdd(user_add_res,p2PRequest);
    }

  public void getUserP2P(ApiCalls apiCalls,String walletId)
    {
        apiCalls.getP2PUserList(user_list_p2p_res,walletId);
    }
    public void getExistingP2P(ApiCalls apiCalls,String email)
    {
        apiCalls.getP2PExistingUser(user_exiting_p2p_res,email);
    }




   public void verifyUser(ActivityAddNewRecipientBinding binding, ApiCalls apiCalls)
   {
      /*if(binding.etFirstName.getText().toString().isEmpty())
      {
          validator.setValue(AppUtils.first_name);
      }
      else if(binding.etLastName.getText().toString().isEmpty())
      {
          validator.setValue(AppUtils.last_name);
      }
      else */
          if(binding.stEmail.getText().toString().isEmpty())
      {
          validator.setValue(AppUtils.empty_id);
      }

       else if(binding.etVeryEmail.getText().toString().isEmpty())
      {
          validator.setValue(AppUtils.empty_verify_email);
      }
       else if(!Utils.isValideEmail(binding.stEmail.getText().toString())){
          validator.setValue(AppUtils.invalid_mail);
       }
       else if(!binding.etVeryEmail.getText().toString().equals(binding.stEmail.getText().toString()))
       {
           validator.setValue(AppUtils.confirm_email);
       }
       else {
          // startActivity(new Intent(this, SendingActivity.class));
       }

   }

   DisposableObserver<GetIdentityResponse> get_idenity_res= new DisposableObserver<GetIdentityResponse>() {
       @Override
       public void onNext(@NonNull GetIdentityResponse jsonObject) {
           Utils.hideProgressDialog();
           get_identity_validator.setValue(jsonObject);
       }

       @Override
       public void onError(@NonNull Throwable e) {
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
    DisposableObserver<JsonObject> send_money_res= new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            Utils.hideProgressDialog();
            send_money_validator.setValue(jsonObject);
        }

        @Override
        public void onError(@NonNull Throwable e) {
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

    DisposableObserver<P2PResponse> user_add_res= new DisposableObserver<P2PResponse>() {
        @Override
        public void onNext(@NonNull P2PResponse jsonObject) {
            Utils.hideProgressDialog();
            user_add_validator.setValue(jsonObject);
        }

        @Override
        public void onError(@NonNull Throwable e) {
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

    DisposableObserver<UserListP2p> user_list_p2p_res= new DisposableObserver<UserListP2p>() {
        @Override
        public void onNext(@NonNull UserListP2p jsonObject) {
            Utils.hideProgressDialog();
            user_list_p2p_validator.setValue(jsonObject);
        }

        @Override
        public void onError(@NonNull Throwable e) {
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
    DisposableObserver<ExistingP2PResponse> user_exiting_p2p_res= new DisposableObserver<ExistingP2PResponse>() {
        @Override
        public void onNext(@NonNull ExistingP2PResponse existingP2PResponse) {
            Utils.hideProgressDialog();
            user_existing_p2p_validator.setValue(existingP2PResponse);
        }

        @Override
        public void onError(@NonNull Throwable e) {
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
}
