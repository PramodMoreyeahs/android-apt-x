package com.apt_x.app.views.fragment.FingerPrintBottomSheet;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.apt_x.app.R;
import com.apt_x.app.interfaces.DialogClickListener;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.utils.FingerprintHandler;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.databinding.FragmentBottomSheetLoginBinding;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import io.reactivex.annotations.NonNull;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;


/**
 * Created by shivanivani on 22/4/21
 */

public class FingerPrintBottomLoginFragment extends BottomSheetDialogFragment
        implements DialogClickListener , FingerprintHandler.login{
    FragmentBottomSheetLoginBinding binding;
    FingerprintManager.CryptoObject cryptoObject;
    FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    private Cipher cipher;
    FingerprintHandler helper;
    int INTENT_AUTHENTICATE = 111;
    private static final String KEY_NAME = "APTCARD";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_login, container, false);
        initializeViews();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initializeViews() {
        binding.tvEnable.setOnClickListener(v -> MyPref.getInstance(getActivity()).writeBooleanPrefs(MyPref.USE_FINGER_PRINT, true));
        if (getDialog() != null) {
            getDialog().setOnShowListener(dialog -> {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                FrameLayout bottomSheetLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheetDialog.setCancelable(false);
                if (bottomSheetLayout == null) {
                    return;
                }
                bottomSheetLayout.setBackground(null);
            });
        }
        binding.tvPin.setOnClickListener(v -> {
            usePattern();
            dismiss();
        });
        enableFingerPrint();
    }

    void usePattern() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            KeyguardManager km = (KeyguardManager) Objects.requireNonNull(getActivity()).getSystemService(KEYGUARD_SERVICE);
            if (km.isKeyguardSecure()) {
                Intent authIntent = km.createConfirmDeviceCredentialIntent(getString(R.string.dialog_title_auth), getString(R.string.dialog_msg_auth));
                startActivityForResult(authIntent, INTENT_AUTHENTICATE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == INTENT_AUTHENTICATE) {
            Toast.makeText(getContext(), getString(R.string.verified), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void enableFingerPrint() {
        KeyguardManager keyguardManager = (KeyguardManager) Objects.requireNonNull(getActivity()).getSystemService(KEYGUARD_SERVICE);
        helper = new FingerprintHandler(getContext(), this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cryptoObject = new FingerprintManager.CryptoObject(cipher);
        }
        if(fingerprintManager!=null && fingerprintManager.isHardwareDetected())
        {
            fingerprintManager = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);
            assert fingerprintManager != null;
            if (!fingerprintManager.isHardwareDetected()) {
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                Utils.showToast(getActivity(), getString(R.string.your_device));
                binding.tvFingerprint.setVisibility(View.GONE);
//            binding.tvPin.setVisibility(View.VISIBLE);
            } else {
                binding.tvFingerprint.setVisibility(View.VISIBLE);
//            binding.tvPin.setVisibility(View.GONE);
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                    Utils.showToast(getActivity(), getString(R.string.fingerprintauth));
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {

                        Utils.showToast(getActivity(), getString(R.string.register_atleas));
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            Utils.showToast(getActivity(), getString(R.string.lock));
                        } else {
                            generateKey();
                            if (cipherInit()) {
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

    @Override
    public void login() {
        dismiss();
    }
}