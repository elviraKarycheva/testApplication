package com.example.karyc.testapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Patterns;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    static public boolean isValidEmail(String email) {
        if (email != null) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    static public boolean isValidPhone(String phone) {
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phone, null);
            return phoneNumberUtil.isValidNumber(parsedNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    static public boolean isValidPassword(String password) {
        if (password != null && password.length() >= MIN_PASSWORD_LENGTH) {
            boolean digitPassword = false;
            boolean letterPassword = false;

            for (int i = 0; i < password.length(); i++) {
                Character currentChar = password.charAt(i);
                if (Character.isDigit(currentChar)) {
                    digitPassword = true;
                } else {
                    if (Character.isLetter(currentChar)) {
                        letterPassword = true;
                    }
                }
            }

            if (digitPassword == true && letterPassword == true) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static File createImageFile(Context context) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            return image;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static Uri dispatchTakePictureIntent(Activity activity, int CAMERA_REQUEST) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = createImageFile(activity);

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName(),
                        photoFile);

                List<ResolveInfo> resolvedIntentActivities = activity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    activity.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                return photoURI;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
