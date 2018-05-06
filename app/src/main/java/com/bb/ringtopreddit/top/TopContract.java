package com.bb.ringtopreddit.top;

import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.data.model.RedditLink;
import com.bb.ringtopreddit.utils.BasePresenter;
import com.bb.ringtopreddit.utils.BaseView;

import java.util.List;

public class TopContract {

    public interface View extends BaseView {

        void showProgress();

        void hideProgress();

        void showError(int errorResId);

        void showData(List<RedditLink> links);

        void openPictureActivity(Picture picture);
    }

    public interface Presenter extends BasePresenter<View> {

        void loadMore();

        void onSelectPicture(Picture picture);

        void onNoPicture();

        boolean isLastPage();
    }
}
