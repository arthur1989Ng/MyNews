package com.ng.news.news;

import com.ng.news.base.BasePresenter;
import com.ng.news.base.BaseView;
import com.ng.news.bean.NewsBean;

import java.util.List;

/**
 * Created by niangang on 2016/10/17.
 */

public interface NewsContract {

    interface View extends BaseView<Presenter> {

        void loadNewsListSuccess(List<NewsBean> list);

        void loadNewsListFailure(String msg, Exception e);
    }

    interface Presenter extends BasePresenter {
        void loadNews(int type, int page);
    }
}
