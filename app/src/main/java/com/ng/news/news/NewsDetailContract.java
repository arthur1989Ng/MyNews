package com.ng.news.news;

import com.ng.news.base.BasePresenter;
import com.ng.news.base.BaseView;
import com.ng.news.bean.NewsDetailBean;

/**
 * Created by niangang on 2016/10/17.
 */

public interface NewsDetailContract {

    interface View extends BaseView<Presenter> {
        void loadNewsDetailSuccess(NewsDetailBean newsDetailBean);

        void loadNewsDetailFailure(String msg, Exception e);

    }

    interface Presenter extends BasePresenter {

        void loadNewsDetail(String docId);

    }

}
