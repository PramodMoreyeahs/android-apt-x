package com.apt_x.app.views.activity.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ProfileActivityBinding;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.PorfilePictureUrlResponse;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.activity.withdraw.WithdrawMethodActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MyProfileActivity extends BaseActivity {
    private ProfileActivityBinding binding;
    ProfileViewModel viewModel;
    ApiCalls apicalls;
    Utils utils;
    boolean getFromCamera;
    boolean getAsSquare;
    public static ImageView Myproimg;
    private File photoFile;
    private File file;
    String profilePicture;
    private Uri photoUri;
    public static String streetaddress2;
    Context context = MyProfileActivity.this;
    Activity activity = MyProfileActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.setContentView(this, R.layout.profile_activity);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.response_validator_picture.observe(this, response_observer_picture);
        Myproimg = binding.ivProfile;
        super.onCreate(savedInstanceState);
        initializeViews();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    Observer<PorfilePictureUrlResponse> response_observer_picture = new Observer<PorfilePictureUrlResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable PorfilePictureUrlResponse countriesResponse) {
            if (countriesResponse.getData().getFlag()) {
                profilePicture = countriesResponse.getData().getData().getFilepath();

            }

        }
    };

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

        viewModel.response_validator.observe(this, response_observer);
        viewModel.getProfile(apicalls);
        binding.ivBack.setOnClickListener(this);
        binding.llAccount.setOnClickListener(this);
        binding.ivEditProfile.setOnClickListener(this);
        binding.lhSecurity.setOnClickListener(this);
        binding.lhPayment.setOnClickListener(this);
        binding.lhAppLanguage.setOnClickListener(this);
        binding.lhMoreOption.setOnClickListener(this);

    }

    Observer<GetProfileResponse> response_observer = new Observer<GetProfileResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;
            if (countriesResponse.getUser() != null) {
                if (countriesResponse.getUser().getCity() != null) {
                    binding.tvAddress.setVisibility(View.VISIBLE);
                } else {
                    binding.tvAddress.setVisibility(View.GONE);
                }

                streetaddress2 = countriesResponse.getUser().getStreet_line_2();

                if (streetaddress2 != null) {
                    streetaddress2 = countriesResponse.getUser().getStreet_line_2() + ", ";
                } else {
                    streetaddress2 = "";
                }

                binding.tvAddress.setText(countriesResponse.getUser().getStreet() != null ? countriesResponse.getUser().getStreet() + ", "
                        + streetaddress2
                        + countriesResponse.getUser().getCity() + ", "
                        + countriesResponse.getUser().getZip() + ", "
                        + countriesResponse.getUser().getState() : "");
                binding.tvName.setText(countriesResponse.getUser().getFirstName() + " " + countriesResponse.getUser().getLastName());
                MyPref.getInstance(MyProfileActivity.this).writePrefs(MyPref.EMAIL_ID_USER, countriesResponse.getUser().getEmail());
             /*   MyPref.getInstance(MyProfileActivity.this)
                        .writePrefs(MyPref.USER_SELFI, countriesResponse.getUser().getProfilePicture());*/

                if (countriesResponse.getUser().getProfilePicture() != null) {
                    String profileurl = MyPref.getInstance(MyProfileActivity.this).readPrefs(MyPref.USER_SELFI);

                    Glide
                            .with(MyProfileActivity.this)
                            .asBitmap()
                            .load(profileurl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)

                            .placeholder(R.drawable.loadimg)

                            .into(binding.ivProfile);
                }
                else {

                    String profileurl = MyPref.getInstance(MyProfileActivity.this).readPrefs(MyPref.USER_SELFI);

                    Glide
                            .with(MyProfileActivity.this)
                            .asBitmap()
                            .load(profileurl)
                          /*  .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)*/
                            .placeholder(R.drawable.loadimg)

                            .into(binding.ivProfile);
                /*    byte[] b = Base64.decode(profileurl, Base64.DEFAULT);
                    Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
                    binding.ivProfile.setImageBitmap(bitmapImage);*/
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            onBackPressed();
        }
        if (view == binding.llAccount) {
            goToProfile();
        }
        if (view == binding.ivEditProfile) {
            goToEditProfile();
        }

        if (view == binding.lhSecurity) {
            goToSecurity();
        }
        if (view == binding.lhPayment) {
            goToPayment();
        }
        if (view == binding.lhAppLanguage) {
            goToAppLanguage();
        }
        if (view == binding.lhMoreOption) {
            goToOptions();
        }

        if (view == binding.ivProfile) {
            launchImagePickerDialog(true);

        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

        startActivity(new Intent(this, HomeActivity.class));

    }

    private void goToOptions() {
        startActivity(new Intent(this, MoreActionActivity.class));

    }

    private void goToAppLanguage() {
        startActivity(new Intent(this, ChangeLanguageActivity.class));

    }

    private void goToPayment() {
        startActivity(new Intent(this, WithdrawMethodActivity.class));

    }

    private void goToSecurity() {

        startActivity(new Intent(this, SecurityActivity.class));

    }

    private void goToProfile() {
        startActivity(new Intent(this, PersonalDetailActivity.class));

    }

    public void launchImagePickerDialog(boolean getAsSquare) {
        this.getAsSquare = getAsSquare;

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_image_picker);
        ImageView camera = bottomSheetDialog.findViewById(R.id.camera);
        ImageView gallery = bottomSheetDialog.findViewById(R.id.gallery);

        assert camera != null;
        camera.setOnClickListener(v -> {
            loadImage(true);
            bottomSheetDialog.hide();
        });

        assert gallery != null;
        gallery.setOnClickListener(v -> {
            loadImage(false);
            bottomSheetDialog.hide();
        });

        bottomSheetDialog.show();
    }

    public void loadImage(boolean getFromCamera) {
        this.getFromCamera = getFromCamera;

        TedPermission.with(this).setPermissionListener(permissionlistener).
                setDeniedMessage("Please provide use required permission to continue").
                setPermissions("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onPermissionGranted() {
            if (getFromCamera) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = randomFile();
                photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.aptcard.app", photoFile);
                //Timber.i("photoUri before opening camera: ${photoUri.path}");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraResultLauncher.launch(cameraIntent);

                /*Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .getIntent(_activity);
                cameraResultLauncher.launch(intent);*/

            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                galleryResultLauncher.launch(galleryIntent);

            }
        }

        public void onPermissionDenied(ArrayList<String> arrayList) {

        }
    };
    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        Uri uri = FileProvider.getUriForFile(this, "com.more_yeahs.build_dream_client", photoFile);
                      /* // Uri resultUri = Objects.requireNonNull(CropImage.getActivityResult(result.getData())).getUri();
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(_activity.getContentResolver(), uri);
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        String path = MediaStore.Images.Media.insertImage(_activity.getContentResolver(), bmp, "title", null);*/
                        launchImageCropper(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        launchImageCropper(Uri.parse(result.getData().getData().toString()));

                       /* Uri uri = FileProvider.getUriForFile(_activity, "com.more_yeahs.build_dream_tr", photoFile);
                        launchImageCropper(uri);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    private void launchImageCropper(Uri path) {
        if (getAsSquare) {
            cropResult.launch(CropImage.activity(path).getIntent(this));
        } else {
            cropResult.launch(CropImage.activity(path).getIntent(this));
        }
    }

    ActivityResultLauncher<Intent> cropResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    file = null;
                    try {
                        Uri resultUri = Objects.requireNonNull(CropImage.getActivityResult(result.getData())).getUri();
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        binding.ivProfile.setImageBitmap(bmp);
                        file = saveBitmap(this, bmp, "photoName");
                        viewModel.uploadProfile(file, apicalls);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


    private File saveBitmap(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".png");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.showDialog(this, "Loading");
        viewModel.getProfile(apicalls);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private final File randomFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();

        return new File(this != null ? this.getFilesDir() : null, "pic-" + UUID.randomUUID() + '-' + now.format(formatter) + ".png");
    }

    private void goToEditProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
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