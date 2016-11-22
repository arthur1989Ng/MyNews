package com.ng.news.images;

import com.ng.news.bean.ImageBean;
import com.ng.news.constant.Urls;
import com.ng.news.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by niangang on 2016/10/17.
 */

public class ImagePresenter implements ImageContract.Presenter {

    private ImageContract.View view;

    public ImagePresenter(ImageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void loadImageList() {
        String url = Urls.IMAGES_URL;
        OkHttpUtils.ResultCallBack<String> loadNewsCallback = new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                List<ImageBean> imageBeanList = ImageJsonUtils.getJsonImageBeans(response);
                view.loadImageListSuccess(imageBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                view.loadImageListFailure("load image list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }
}
