package com.bb.ringtopreddit.picture;

import android.graphics.Bitmap;

import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.BasePresenter;
import com.bb.ringtopreddit.utils.BaseView;

public class PictureContract {

    public interface View extends BaseView {

        void showMessage(int messageResId);

        void requestWritePermission();
    }

    public interface Presenter extends BasePresenter<View> {

        void onSavePicture(Bitmap bitmap, Picture picture);
    }
}
