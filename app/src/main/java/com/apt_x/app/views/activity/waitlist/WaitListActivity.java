package com.apt_x.app.views.activity.waitlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.apt_x.app.R;
import com.apt_x.app.model.WaitListResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.WaitlistActivityBinding;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by shivanivani on 22/4/21.
 */
public class WaitListActivity extends BaseActivity {

    WaitlistActivityBinding binding;
    WaitListViewModel viewModel;
    ArrayList<Integer> images;
    String FCMToken;
    Timer timer;
    final long DELAY_MS = 500;
    //delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000;
    final long NUM_PAGES = 4;
    ApiCalls apicalls;
    Context context = WaitListActivity.this;
    Activity activity = WaitListActivity.this;

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
        apicalls = ApiCalls.getInstance(this);
        binding.tvContinue.setOnClickListener(this);
        viewModel.response_validator.observe(this, response_observer);
        //initPager();
    }


    Observer<WaitListResponse> response_observer = countriesResponse -> {
        Utils.showToast(WaitListActivity.this, getString(R.string.submitted));
        
    };

//    public void initPager() {
//        wViewPager = findViewById(R.id.vp_walk_through);
//        walkThroughAdapter = new WalkThroughAdapter(WaitListActivity.this, walkThroughImages());
//        wViewPager.setAdapter(walkThroughAdapter);
//        binding.ciWalkThroughIndicator.setViewPager(wViewPager);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w("Splash", "getInstanceId failed", task.getException());
//                        return;
//                    }
//                    FCMToken = Objects.requireNonNull(task.getResult()).getToken();
//                    Log.e("token:::", "" + task.getResult().getToken());
//                    // Log and toast
//                });
//
//
//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = () -> {
//            if (currentPage == NUM_PAGES - 1) {
//                currentPage = 0;
//            }
//            wViewPager.setCurrentItem(currentPage++, true);
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
//        wViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                System.out.println("page selected::" + position);
//
//            }
//
//            public void onPageSelected(int position) {
//                System.out.println("page selected111::" + position);
//            }
//
//            public void onPageScrollStateChanged(int state) {
////                System.out.println("page selected2222::"+position);
//
//            }
//        });


    //}

    public ArrayList<Integer> walkThroughImages() {
        images = new ArrayList<>();
        images.add(R.drawable.ic_cross);
        images.add(R.drawable.ic_rewards);
        images.add(R.drawable.ic_experience);
        return images;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitlist_activity);
        binding = DataBindingUtil.setContentView(this, R.layout.waitlist_activity);
        viewModel = ViewModelProviders.of(this).get(WaitListViewModel.class);

        initializeViews();
        context = this;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvContinue) {
            String text = binding.etEmail.getText().toString();
            if (Utils.isStringsEmpty(text)) {
                Utils.showToast(this, getString(R.string.enter_valid_employeeid));
            } else if (!Utils.isValideEmail(text)) {
                Utils.showToast(this, getString(R.string.enter_valid_employeeid));
            } else {
                Utils.showDialog(this, getString(R.string.loading));
                viewModel.postWaitList(apicalls, FCMToken, binding.etEmail.getText().toString());
            }
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
