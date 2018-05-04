package com.bb.ringtopreddit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ImageSaver {

    private final Context context;

    @Inject
    ImageSaver(@Named(Names.APPLICATION) Context context) {
        this.context = context;
    }

    public void saveImage(Bitmap bitmap, String title, String description) {
        MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                bitmap,
                title,
                description);
    }
}
