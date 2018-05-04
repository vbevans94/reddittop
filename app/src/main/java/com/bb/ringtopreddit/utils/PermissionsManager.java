package com.bb.ringtopreddit.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Utility class to help with runtime permissions.
 */
@Singleton
public class PermissionsManager {

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private Context context;

    @Inject
    public PermissionsManager(@Named(Names.APPLICATION) Context context) {
        this.context = context;
    }

    public boolean hasWriteExternalStoragePermissions() {
        return hasPermission(WRITE_EXTERNAL_STORAGE);
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestWriteExternalStoragePermissions(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionsManager.REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public boolean permissionGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
