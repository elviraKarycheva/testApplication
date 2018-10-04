package com.example.karyc.testapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.karyc.testapplication.databinding.ActivityMainBinding;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_TAKE_PHOTO = 10;
    private static final int RC_CAMERA_PERMISSION = 15;
    private static final String STATE_PHOTO_URI = "statePhotoUri";

    private ActivityMainBinding binding;

    @Nullable
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.setInteractor(new ActivityInteractor());

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+[00000000000]",
                binding.phoneField,
                null
        );

        binding.phoneField.addTextChangedListener(listener);
        binding.phoneField.setOnFocusChangeListener(listener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        photoUri = savedInstanceState.getParcelable(STATE_PHOTO_URI);
        loadPhoto();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(STATE_PHOTO_URI, photoUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            loadPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        getPhoto();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Snackbar.make(binding.getRoot(), R.string.error_permission, Snackbar.LENGTH_SHORT).show();
    }

    private void getPhoto() {
        photoUri = Utils.dispatchTakePictureIntent(this, RC_TAKE_PHOTO);
    }

    private void loadPhoto() {
        if (photoUri != null) {
            Glide.with(this).load(photoUri).apply(RequestOptions.circleCropTransform()).into(binding.cameraImage);
        }
    }

    public class ActivityInteractor {
        public void onClickTakePhoto(@SuppressWarnings("unused") View view) {
            if (EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.CAMERA)) {
                getPhoto();
            } else {
                EasyPermissions.requestPermissions(
                        MainActivity.this,
                        getString(R.string.request_permission),
                        RC_CAMERA_PERMISSION,
                        Manifest.permission.CAMERA);
            }
        }

        public void onClickPreview(@SuppressWarnings("unused") View view) {
            String email = binding.emailField.getText().toString().toLowerCase().trim();
            String phone = binding.phoneField.getText().toString().trim();
            String password = binding.passwordField.getText().toString().trim();
            boolean errorFound = false;

            if (!Utils.isValidEmail(email)) {
                binding.emailInputLayout.setError(getString(R.string.error_email));
                errorFound = true;
            } else {
                binding.emailInputLayout.setError(null);
            }

            if (!Utils.isValidPhone(phone)) {
                binding.phoneInputLayout.setError(getString(R.string.error_phone));
                errorFound = true;
            } else {
                binding.phoneInputLayout.setError(null);
            }

            if (!Utils.isValidPassword(password)) {
                binding.passwordInputLayout.setError(getString(R.string.error_password));
                errorFound = true;
            } else {
                binding.passwordInputLayout.setError(null);
            }

            if (photoUri == null) {
                Snackbar.make(binding.getRoot(), R.string.error_camera, Snackbar.LENGTH_SHORT).show();
                errorFound = true;
            }

            if (!errorFound) {
                Data data = new Data(email, phone, password, photoUri.toString());
                PreviewActivityStarter.start(MainActivity.this, data);
            }
        }
    }
}
