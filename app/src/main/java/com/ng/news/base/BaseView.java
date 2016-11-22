package com.ng.news.base;

/**
 * Created by niangang on 2016/10/17.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    boolean isActive(); // ?????

    void showProgress();

    void hideProgress();

    void showErrorToast(String msg);
}
