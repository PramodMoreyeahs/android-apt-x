
package com.apt_x.app.views.activity.home;


import static com.apt_x.app.utils.Utils.showCustomDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.apt_x.app.R;
import com.apt_x.app.app.MyApp;
import com.apt_x.app.databinding.ActivityNewHomeBinding;
import com.apt_x.app.model.LeftMenuDrawerItems;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.Connectivity;
import com.apt_x.app.utils.ConnectivityReceiver;
import com.apt_x.app.utils.FingerPrintEnable;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.SupportActivity;
import com.apt_x.app.views.activity.exchangeRate.CountryListActivity;
import com.apt_x.app.views.activity.profile.MyProfileActivity;
import com.apt_x.app.views.adapter.LeftDrawerListAdapter;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.views.fragment.FingerPrintBottomSheet.FingerPrintBottomLoginFragment;
import com.apt_x.app.views.fragment.home.HomeFragment;
import com.apt_x.app.views.fragment.mycard.MyCardFragment;
import com.apt_x.app.views.fragment.transaction.TransactionFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shivanivani on 22/4/21
 */
public class HomeActivity extends BaseActivity implements LeftDrawerListAdapter.OnItemClickListener, FingerPrintEnable.onResult,
        ConnectivityReceiver.ConnectivityReceiverListener {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    List<LeftMenuDrawerItems> leftMenuDrawerItemses = new ArrayList<>();
    public BottomNavigationOnClickListener _btmNavigation = new BottomNavigationOnClickListener();

    LeftDrawerListAdapter leftDrawerListAdapter;
    View drawerView;
    Fragment fragment;
    private static boolean activityVisible;
    public ActivityNewHomeBinding binding;
    int INTENT_AUTHENTICATE = 111;
    String profileUrl = "";
    Context context = HomeActivity.this;
    Activity activity = HomeActivity.this;
    FingerprintManager fingerprintManager;
    FingerPrintEnable fingerPrintEnable;
    ConnectivityReceiver connectivityReceiver;
    int SPLASH_TIME_OUT = 5000;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_AUTHENTICATE) {
            if (resultCode == RESULT_OK) {
                //do something you want when pass the security
            }
        }
        if (resultCode == RESULT_OK) {
            Utils.showToast(this, getString(R.string.verified_iden));
        } else {

            FingerPrintBottomLoginFragment bottomSheet = new FingerPrintBottomLoginFragment();
            bottomSheet.show(getSupportFragmentManager(), "bottom_Sheet");
        }
        // check if the request code is same as what is passed  here it is 2
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_home);
        initializeViews();
        connectivityReceiver = new ConnectivityReceiver();
        // Utils.replaceFragment(HomeActivity.this, new HomeFragment());

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initializeViews() {
        if (!Connectivity.isConnected()) {
            Utils.showToast(this, "Please connect to ");
        }


        fingerPrintEnable = new FingerPrintEnable(this, true, this);
        fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);


        if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {
            if (MyPref.getInstance(context).readBooleanPrefs(MyPref.USE_FINGER_PRINT)) {

                System.out.println("bio check new " + Pref.IS_FIRST);

                if (Pref.IS_FIRST.equals("1")) {
                    Pref.IS_FIRST = "0";
                    fingerPrintEnable.enableFingerPrint();

                }

                if (!MyPref.getInstance(context).readBooleanPrefs(Pref.IS_BOIMETRIC)) {
                    MyPref.getInstance(context).writeBooleanPrefs(Pref.IS_BOIMETRIC, true);

                }

            }
        }
        /* if(MyPref.getInstance(this).readBooleanPrefs(MyPref.USE_FINGER_PRINT)){
                FingerPrintBottomLoginFragment bottomSheet = new FingerPrintBottomLoginFragment();
                bottomSheet.show(getSupportFragmentManager(), "bottom_Sheet");
            }*//*
        }*/


        try {

            context = HomeActivity.this;
            binding.ivUser.setOnClickListener(this);
            bottomNavigationNavControllerSetup();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        binding.llHome.setOnClickListener(_btmNavigation);
        binding.llTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(HomeActivity.this, R.id.main_nav_host);
                navController.navigateUp();
                navController.navigate(R.id.transactionFragment);
            }
        });
        binding.p2p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPref.getInstance(context).writeBooleanPrefs(Pref.COME_FROM, false);
                startActivity(new Intent(HomeActivity.this, CountryListActivity.class));
                // startActivity(new Intent(HomeActivity.this, ConfirmPaymentActivity.class));
                //Utils.showToast(getApplicationContext(),getResources().getString(R.string.coming_soon));
            }
        });

        binding.llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, SupportActivity.class));

            }
        });


        profileUrl = MyPref.getInstance(HomeActivity.this)
                .readPrefs(MyPref.USER_SELFI);
        Log.i("TAG", "initializeViews:User Profile " + profileUrl);
        byte[] b = Base64.decode(MyPref.getInstance(HomeActivity.this).readPrefs(MyPref.USER_SELFI1), Base64.DEFAULT);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);

        if (!profileUrl.equals("")) {
            Glide
                    .with(HomeActivity.this)
                    .asBitmap()
                    .load(profileUrl)
                    // .placeholder(bitmapImage)
                    .into(binding.ivUser);
        } else {
            binding.ivUser.setImageBitmap(bitmapImage);
        }

    }

    void bottomNavigationNavControllerSetup() {
        final NavController _navController = getNavHostFragmentDelegate().getNavController();
        NavigationUI.setupWithNavController(binding.btmNavigation, _navController);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.getInstance().setConnectivityListener(this);
        loadProfilePicture();
        Log.e("OnresumeActivity", "*******");
        Log.e("ImagePOic***", "*******" + MyPref.getInstance(HomeActivity.this)
                .readPrefs(MyPref.USER_SELFI));

       /* try {
            fragment = getSupportFragmentManager().findFragmentById(R.id.mainContent);
            if (fragment != null)
                fragment.onResume();

            setMyDrawer(fragment);

            activityResumed();

        } catch (Exception ex) {
            ex.printStackTrace();
        }*/


    }


    @Override
    protected void onPause() {
        super.onPause();

        //  activityPaused();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    protected void onDestroy() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();

    }


    @Override
    public void onItemClick(int position) {
        if (!Connectivity.isConnected()) {
            Utils.showToast(context, getString(R.string.internet_connection));
            return;
        }

        mDrawerLayout.closeDrawer(drawerView);

        /*switch (position) {

            case 0://Home
                setDrawerHover(position);
//                title_tv.setText(getString(R.string.txt_home));
                Utils.replaceFragment(HomeActivity.this, new HomeFragment());
                break;
            case 1://
                setDrawerHover(position);

                break;
            case 2://
                setDrawerHover(position);
                Utils.replaceFragment(HomeActivity.this, new ProfileFragment());
                break;
            case 3: //
                setDrawerHover(position);
                Utils.replaceFragment(HomeActivity.this, new SettingFragment());
                break;
            case 4://
                setDrawerHover(position);
                break;
            case 5://
                setDrawerHover(position);

                break;
            case 6: //
                setDrawerHover(position);
                break;
        }*/
    }


    private void setMyDrawer(Fragment fragment) {

        try {
            if (fragment instanceof HomeFragment) {
                setDrawerHover(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void setDrawerHover(int position) {

        try {
           /* if (!leftMenuDrawerItemses.get(position).isMenuIsSelected()) {
                leftMenuDrawerItemses.get(position).setMenuIsSelected(true);
            }

            if (previousPosition != -1 && previousPosition != position) {
                leftMenuDrawerItemses.get(previousPosition).setMenuIsSelected(false);
                leftDrawerListAdapter.notifyDataSetChanged();
            } else {
                leftDrawerListAdapter.notifyDataSetChanged();
            }
            previousPosition = position;*/
            if (!leftMenuDrawerItemses.get(position).isMenuIsSelected()) {
                for (int i = 0; i < leftMenuDrawerItemses.size(); i++) {
                    leftMenuDrawerItemses.get(i).setMenuIsSelected(false);
                }
                leftMenuDrawerItemses.get(position).setMenuIsSelected(true);
                leftDrawerListAdapter.notifyDataSetChanged();
            }
//            leftDrawerListAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setMyActionBar(Fragment fragment) {
        try {
            if (fragment instanceof HomeFragment) {
//                title_tv.setVisibility(View.VISIBLE);
//                //setDrawerHover(3);
//                title_tv.setText((getString(R.string.txt_home)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exitApp(Context activity) {
        final Dialog dialog = showCustomDialog(activity, R.layout.exit_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView ok = (TextView) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity();
        });
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = (Fragment) getNavHostFragmentDelegate().getChildFragmentManager().getFragments().get(0);
        Log.e(getClass().getName(), "onBackPressed: ");

        {
            boolean highLightHomeButton2 = fragment instanceof HomeFragment;
            if (highLightHomeButton2)
                exitApp(this);
            else {

                super.onBackPressed();
            }
        }

        boolean highLightHomeButton = fragment instanceof HomeFragment || fragment instanceof TransactionFragment || fragment instanceof MyCardFragment;
        if (highLightHomeButton) binding.llHome.performClick();

    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public void activityPaused() {
        activityVisible = false;

        new Handler().postDelayed(() -> {
            System.out.println("ScreenTimeout");
            finishAffinity();

        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivUser:
                Intent intent = new Intent(this, MyProfileActivity.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onclick(Boolean status) {
        System.out.println("Biometrics in Home activity call back" + status);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            // Utils.showToast(getApplicationContext(),"Please connect to the internet");
            showDialog();
        }

    }

    public void showDialog() {
        final Dialog dialog = Utils.showCustomDialog(this, R.layout.dialog_view);
        Button yes = dialog.findViewById(R.id.bt);

        yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint("SuspiciousIndentation")
    public void loadProfilePicture() {

        profileUrl = MyPref.getInstance(HomeActivity.this)
                .readPrefs(MyPref.USER_SELFI);
        Glide
                .with(HomeActivity.this)
                .asBitmap()
                .load(profileUrl)
                // .placeholder(bitmapImage)
                .into(binding.ivUser);

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
