package com.apt_x.app.views.fragment.mycard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.MycardFragmentLayoutBinding;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.model.GetWalletBalanceResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.AppUtils;
import com.apt_x.app.utils.ConnectivityReceiver;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.card.BlockCardActivity;
import com.apt_x.app.views.activity.card.CardPinActivity;
import com.apt_x.app.views.activity.card.ViewPinActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.base.BaseFragment;
import com.apt_x.app.views.fragment.home.HomeFragmentViewModel;
import com.google.android.material.transition.MaterialSharedAxis;

import org.jetbrains.annotations.NotNull;

public class MyCardFragment extends BaseFragment implements DialogClickListener , ConnectivityReceiver.ConnectivityReceiverListener{

    private Context context;
    private MycardFragmentLayoutBinding binding;
    HomeFragmentViewModel viewModel;
    ApiCalls apicalls;
    HomeActivity _activity;
    String passcode="1234";



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

        binding = DataBindingUtil.inflate(inflater, R.layout.mycard_fragment_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        initializeViews(binding.getRoot());
        return binding.getRoot();
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) _activity = (HomeActivity) context;
    }

    @Override
    public void initializeViews(View rootView) {
        context = getActivity();
        apicalls=ApiCalls.getInstance(getActivity());
        _activity.binding.frameLayout.setVisibility(View.GONE);
        _activity.binding.lvBottom.setVisibility(View.GONE);
        viewModel.response_validator.observe(this,response_observer);
       // viewModel.response_validator_transaction.observe(this, transaction_response_observer);

        binding.ivBack.setOnClickListener(v -> _activity.onBackPressed());
      //  binding.llchangePin.setOnClickListener(this);
        binding.llSeeCard.setOnClickListener(this);
        binding.llblock.setOnClickListener(this);
        binding.llchangePin.setOnClickListener(this);
        binding.llCard.setOnClickListener(this);
        binding.tvExpiry.setOnClickListener(this);


        getWalletBalanceApi();
      //  getTransaction();

     /* binding.ivClose.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              flipcardReverse();
          }
      });*/



       /* binding.etOtp.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==0)
                {
                    binding.tvError.setVisibility(View.GONE);
                }
                char[] cArr = s.toString().trim().toCharArray();
                binding.tvCodeOne.setText("");
                binding.tvCodeTwo.setText("");
                binding.tvCodeThree.setText("");
                binding.tvCodeFour.setText("");
                for (int i = 0; i < s.length(); i++) {
                    if (i == 0) {
                        binding.tvCodeOne.setText(String.valueOf(cArr[i]));
                    }
                    if (i == 1) {
                        binding.tvCodeTwo.setText(String.valueOf(cArr[i]));
                    }
                    if (i == 2) {
                        binding.tvCodeThree.setText(String.valueOf(cArr[i]));
                    }
                    if (i == 3) {
                        binding.tvCodeFour.setText(String.valueOf(cArr[i]));

                     if(passcode.equals(binding.tvCodeOne.getText().toString()+binding.tvCodeTwo.getText().toString()+binding.tvCodeThree.getText().toString()+
                             binding.tvCodeFour.getText().toString()))
                     {
                      binding.tvCardNo.setText("8500 1456 8000 1800");
                      binding.tvExpiry.setText("12/2022");
                         binding.tvError.setVisibility(View.GONE);
                         binding.ivCopy.setVisibility(View.VISIBLE);
                      Utils.hideKeyboard(_activity);
                      flipcardReverse();
                     }
                     else
                     {
                         binding.ivCopy.setVisibility(View.GONE);
                         binding.tvError.setVisibility(View.VISIBLE);
                         AndroidDeviceVibrate();
                     }

                    }
                }
               *//* if (binding.etOtp.getText().toString().length() == 4) {
                    binding.tvContinue.setEnabled(true);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(VerifyPhoneActivity.this, R.drawable.btn_bg));
                } else {
                    binding.tvContinue.setEnabled(false);
                    binding.tvContinue.setBackground(ContextCompat.getDrawable(VerifyPhoneActivity.this, R.drawable.btn_bg));
                }*//*
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });*/
    }

    private void getWalletBalanceApi() {
        Utils.showDialog(context,"Loading");
        viewModel.getWalletBalance(apicalls);
    }


    private void getTransaction() {
        viewModel.getTransaction(MyPref.getInstance(context).readPrefs(MyPref.WALLET_ID),apicalls);

    }




    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSeeCard:
                startActivity(new Intent(context, CardPinActivity.class));
                break;
            case R.id.llblock:
                startActivity(new Intent(context, BlockCardActivity.class));
                break;
            case R.id.llchangePin:
                startActivity(new Intent(context, ViewPinActivity.class));
                break;
            case R.id.tv_expiry:
                /*if(binding.tvExpiry.getText().toString().equals(getString(R.string.see_card_details)))
                {
                    flipcard();
                }
*/
               // flipCard(context,binding.llCard,binding.llCardPin);
                break;
         /*   case R.id.iv_copy:
                ClipboardManager clipboardManager1 = (ClipboardManager) _activity.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager1.setText(binding.tvCardNo.getText().toString());
                Utils.showToast(_activity,    binding.tvCardNo.getText().toString() + " Copied");
                break;*/



        }
    }

 /*   private void flipcard() {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(binding.llCard, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(binding.llCardPin, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.llCard.setVisibility(View.GONE);
                binding.llCardPin.setVisibility(View.VISIBLE);
               // binding.llCard.setBackground(getResources().getDrawable(R.drawable.martercard_rec));
                oa2.start();
            }
        });


        oa1.start();
    }*/
  /*  private void flipcardReverse() {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(binding.llCardPin, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(binding. llCard, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.llCard.setVisibility(View.VISIBLE);
                binding.llCardPin.setVisibility(View.GONE);
                // binding.llCard.setBackground(getResources().getDrawable(R.drawable.martercard_rec));
                oa2.start();
            }
        });


        oa1.start();
    }*/

   /* void flipCard(Context context, View visibleView, View inVisibleView) {
        float scale = getResources().getDisplayMetrics().density;
        LinearLayout front = binding.llCard;
        LinearLayout back = binding.llCardPin;
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);
        try {

            inVisibleView.setVisibility(View.VISIBLE);
            Animator flipOutAnimatorSet =
                    AnimatorInflater.loadAnimator(
                            context,
                            R.animator.flip_out
                    );
            flipOutAnimatorSet.setTarget(visibleView);
            Animator flipInAnimationSet =
                    AnimatorInflater.loadAnimator(
                            context,
                            R.animator.flip_in
                    );
            flipInAnimationSet.setTarget(inVisibleView);
            flipOutAnimatorSet.start();
            flipInAnimationSet.start();
         *//*   flipInAnimationSet.doOnEnd {
                inVisibleView.visibility = View.GONE
            }*//*
            flipInAnimationSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    visibleView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } catch (Exception e) {

        }
    }*/
  /*  void flipCardReverse(Context context, View visibleView, View inVisibleView) {
        float scale = getResources().getDisplayMetrics().density;
        LinearLayout front = binding.llCard;
        LinearLayout back = binding.llCardPin;
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);
        try {

            inVisibleView.setVisibility(View.VISIBLE);
            Animator flipOutAnimatorSet =
                    AnimatorInflater.loadAnimator(
                            context,
                            R.animator.flip_out
                    );
            flipOutAnimatorSet.setTarget(visibleView);
            Animator flipInAnimationSet =
                    AnimatorInflater.loadAnimator(
                            context,
                            R.animator.flip_in
                    );
            flipInAnimationSet.setTarget(inVisibleView);
            flipOutAnimatorSet.start();
            flipInAnimationSet.start();
         *//*   flipInAnimationSet.doOnEnd {
                inVisibleView.visibility = View.GONE
            }*//*
            flipInAnimationSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    visibleView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } catch (Exception e) {

        }
    }*/

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


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected)
        {
            Utils.showCustomDialog(context,R.layout.exit_dialog);
        }
        else {
            Utils.showToast(context,"Coonected");
        }
    }

    Observer<GetWalletBalanceResponse> response_observer= new Observer<GetWalletBalanceResponse>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetWalletBalanceResponse loginBean) {
            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if(loginBean.getStatus()) {
                binding.tvTotalBalance.setText("$"+loginBean.getData().getBalance());

            }

        }
    };

    private void AndroidDeviceVibrate() { // Android Device Vibration
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); // Vibrate for 500 milliseconds only
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500); // deprecated in API 26
        }
    }
}