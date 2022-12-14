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
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.apt_x.app.R;
import com.apt_x.app.model.GetProfileResponse;
import com.apt_x.app.model.PorfilePictureUrlResponse;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.LocaleHelper;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.NewHomeActivity;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.apt_x.app.views.base.BaseActivity;
import com.apt_x.app.databinding.EditProfileActivityBinding;
import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class EditProfileActivity extends BaseActivity {
    private EditProfileActivityBinding binding;
    ProfileViewModel viewModel;
    ApiCalls apicalls;
    Calendar selectedDate;
    boolean getFromCamera;
    boolean getAsSquare;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private File photoFile;
    private File file;
    String name,firstName,lastName,street,street2,city,state,zip,mobile,country,dob;
    Context context = EditProfileActivity.this;
    Activity activity = EditProfileActivity.this;

    String  profilePicture;
    private Uri photoUri;
     BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.edit_profile_activity);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        super.onCreate(savedInstanceState);
        initializeViews();
    }

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
        binding.ivBack.setOnClickListener(this);
        binding.tvUpdate.setOnClickListener(this);
        binding.ivProfile.setOnClickListener(this);
        binding.etAddress.setOnClickListener(this);

        apicalls = ApiCalls.getInstance(this);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.response_validator_picture.observe(this, response_observer_picture);
        viewModel.response_validator_update_profile.observe(this, response_observer_update_picture);

        viewModel.getProfile(apicalls);
    }
    Observer<GetProfileResponse> response_observer = new Observer<GetProfileResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;
            if (countriesResponse.getUser() != null) {
                if(countriesResponse.getUser().getCity()!=null)
                    binding.etAddress.setText(countriesResponse.getUser().getStreet()+", "
                            +MyProfileActivity.streetaddress2
                            +countriesResponse.getUser().getZip()+", "
                            +countriesResponse.getUser().getCity()+", "
                            +countriesResponse.getUser().getState());
                if(countriesResponse.getUser().getFirstName()!=null)
                    binding.etName.setText(countriesResponse.getUser().getFirstName());
                if(countriesResponse.getUser().getLastName()!=null)
                    binding.etLastName.setText(countriesResponse.getUser().getLastName());

                if(countriesResponse.getUser().getMobile()!=null)
                    binding.etMobile.setText(countriesResponse.getUser().getMobile());
                if(countriesResponse.getUser().getEmail()!=null)
                    binding.etEmail.setText(countriesResponse.getUser().getEmail());


                if (countriesResponse.getUser().getProfilePicture()!=null){
                    MyPref.getInstance(EditProfileActivity.this)
                            .writePrefs(MyPref.USER_SELFI, countriesResponse.getUser().getProfilePicture());
                    Glide
                            .with(EditProfileActivity.this)
                            .asBitmap()
                            .load(countriesResponse.getUser().getProfilePicture())
                            //.placeholder()
                            .into(binding.ivProfile);
                }
                else {

                    byte[] b = Base64.decode(MyPref.getInstance(EditProfileActivity.this).readPrefs(MyPref.USER_SELFI1), Base64.DEFAULT);
                    Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
                    binding.ivProfile.setImageBitmap(bitmapImage);
                }


                firstName=countriesResponse.getUser().getFirstName();
                lastName=countriesResponse.getUser().getLastName();
                mobile=countriesResponse.getUser().getMobile();
                city=countriesResponse.getUser().getCity();
                state=countriesResponse.getUser().getState();
                street=countriesResponse.getUser().getStreet();
                street2=MyProfileActivity.streetaddress2;
                country=countriesResponse.getUser().getCountry();
                zip=countriesResponse.getUser().getZip();
                profilePicture=countriesResponse.getUser().getProfilePicture();
                dob=countriesResponse.getUser().getDateOfBirth();



            }
        }
    };
    Observer<PorfilePictureUrlResponse> response_observer_picture = new Observer<PorfilePictureUrlResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable PorfilePictureUrlResponse countriesResponse) {
            if (countriesResponse.getData().getFlag()){
                profilePicture=countriesResponse.getData().getData().getFilepath();

            }

        }
    };
    Observer<GetProfileResponse> response_observer_update_picture = new Observer<GetProfileResponse>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetProfileResponse countriesResponse) {
            assert countriesResponse != null;
            Utils.showToast(getApplicationContext(),getString(R.string.user_details_update));
                startActivity(new Intent(EditProfileActivity.this, MyProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


        }
    };



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvUpdate:
                updateProfile();
                break;
            case R.id.etAddress:
                goToAddress();
                break;

            case R.id.ivProfile:
                launchImagePickerDialog(true);
                break;
        }
    }

    private void goToAddress() {
        startActivity(new Intent(this, UpdateAddressActivity.class)
                .putExtra("comeFrom","Profile").putExtra("dob",dob));
    }

    public void launchImagePickerDialog(boolean getAsSquare) {
        this.getAsSquare = getAsSquare;

         bottomSheetDialog = new BottomSheetDialog(this);
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
                photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.apt_x.app", photoFile);
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
                        Uri uri = FileProvider.getUriForFile(this, "com.apt_x.app", photoFile);
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
       /* if (getAsSquare) {
            cropResult.launch(CropImage.activity(path).setMaxCropResultSize(2500,2500).setMinCropResultSize(2500,2500).getIntent(this));
        } else {
            cropResult.launch(CropImage.activity(path).setMaxCropResultSize(2500,2500).setMinCropResultSize(2500,2500).getIntent(this));
        }*/
        if (getAsSquare) {
            cropResult.launch(CropImage.activity(path).setFixAspectRatio(true).getIntent(this));
        } else {
            cropResult.launch(CropImage.activity(path).setFixAspectRatio(true).getIntent(this));
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
                        Utils.showDialog(this,getString(R.string.loading));
                        viewModel.uploadProfile(file,apicalls);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


    private File saveBitmap(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private final File randomFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();

        return new File(this!= null ? this.getFilesDir() : null, "pic-" + UUID.randomUUID() + '-' + now.format(formatter) + ".jpg");
    }
    private void updateProfile() {
        firstName = binding.etName.getText().toString();
        lastName =binding.etLastName.getText().toString();
        mobile = binding.etMobile.getText().toString();
        MyPref.getInstance(getApplicationContext())
                .writePrefs(MyPref.USER_SELFI, profilePicture);
        if(firstName.isEmpty())
        {
           Utils.showToast(getApplicationContext(),getResources().getString(R.string.enter_first_name));
        }
        else if(lastName.isEmpty())
        {
            Utils.showToast(getApplicationContext(),getResources().getString(R.string.enter_last_name));
        }
        else if(mobile.isEmpty())
        {
            Utils.showToast(getApplicationContext(),getResources().getString(R.string.please_enter_mobile_number));
        }
        else
        {
            Utils.showDialog(this, getString(R.string.loading));
            viewModel.doUpdateProfile(firstName, lastName, mobile, profilePicture, apicalls);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bottomSheetDialog!=null && bottomSheetDialog.isShowing() )
        {
            bottomSheetDialog.dismiss();
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