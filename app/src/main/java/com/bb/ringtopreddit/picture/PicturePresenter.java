package com.bb.ringtopreddit.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.Names;
import com.bb.ringtopreddit.utils.PermissionsManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PicturePresenter implements PictureContract.Presenter {

    private static final String TAG = "PicturePresenter";

    private final Context context;
    private final PermissionsManager permissionsManager;
    private PictureContract.View view;

    @Inject
    PicturePresenter(@Named(Names.APPLICATION) Context context, PermissionsManager permissionsManager) {
        this.context = context;
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
            String savedImageURL = MediaStore.Images.Media.insertImage(
                    context.getContentResolver(),
                    bitmap,
                    picture.getTitle(),
                    picture.getUrl()
            );
            view.showMessage(R.string.message_saved);
        } else {
            view.requestWritePermission();
        }
    }
}
