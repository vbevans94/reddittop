package com.bb.ringtopreddit.utils;

public interface BasePresenter<V extends BaseView> {

    void takeView(V view);

    void dropView();
}
