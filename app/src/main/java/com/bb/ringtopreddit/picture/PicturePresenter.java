package com.bb.ringtopreddit.picture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.ImageUtils;
import com.bb.ringtopreddit.utils.Names;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PicturePresenter implements PictureContract.Presenter {

    private static final String TAG = "PicturePresenter";

    private final Context context;
    private PictureContract.View view;

    @Inject
    PicturePresenter(@Named(Names.APPLICATION) Context context) {
        this.context = context;
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
    public void onSavePicture(Picture picture) {
        Glide.with(context)
                .asFile()
                .load(picture.getUrl())
                .into(new InsertTarget(context, picture));
    }

    private static class InsertTarget extends SimpleTarget<File> {

        private final Context context;
        private final Picture picture;

        InsertTarget(Context context, Picture picture) {
            this.context = context;
            this.picture = picture;
        }

        @Override
        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
            ImageUtils.insertImage(context.getContentResolver(), resource, picture.getTitle(), picture.getTitle());
        }
    };
}
