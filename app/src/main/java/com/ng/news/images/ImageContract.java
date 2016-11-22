package com.ng.news.images;

import com.ng.news.base.BasePresenter;
import com.ng.news.base.BaseView;
import com.ng.news.bean.ImageBean;

import java.util.List;

/**
 * Created by niangang on 2016/10/17.
 */

public interface ImageContract {
    /**
     * View 相关操作
     */
    interface View extends BaseView<Presenter> {
        void addImages(List<ImageBean> list);

        void loadImageListSuccess(List<ImageBean> list);

        void loadImageListFailure(String msg, Exception e);
    }

    /**
     * 逻辑处理相关操作
     */
    interface Presenter extends BasePresenter {
        void loadImageList();


    }
}
