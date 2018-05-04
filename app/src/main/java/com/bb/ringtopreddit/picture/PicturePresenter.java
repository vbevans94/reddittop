package com.bb.ringtopreddit.picture;

import android.graphics.Bitmap;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.ImageSaver;
import com.bb.ringtopreddit.utils.PermissionsManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PicturePresenter implements PictureContract.Presenter {

    private static final String TAG = "PicturePresenter";

    private final ImageSaver imageSaver;
    private final PermissionsManager permissionsManager;
    private PictureContract.View view;

    @Inject
    PicturePresenter(ImageSaver imageSaver, PermissionsManager permissionsManager) {
        this.imageSaver = imageSaver;
        this.permissionsManager = permissionsManager;
    }

    @Override
    public void takeView(PictureContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void onSavePicture(Bitmap bitmap, Picture picture) {
        if (permissionsManager.hasWriteExternalStoragePermissions()) {
            imageSaver.saveImage(bitmap, picture.getTitle(), picture.getUrl());
            view.showMessage(R.string.message_saved);
        } else {
            view.requestWritePermission();
        }
    }
}
