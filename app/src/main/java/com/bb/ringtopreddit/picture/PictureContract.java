package com.bb.ringtopreddit.picture;

import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.BasePresenter;
import com.bb.ringtopreddit.utils.BaseView;

public class PictureContract {

    public interface View extends BaseView {
    }

    public interface Presenter extends BasePresenter<View> {

        void onSavePicture(Picture picture);
    }
}
