package com.ng.news.news;

import com.ng.news.bean.NewsDetailBean;
import com.ng.news.constant.Urls;
import com.ng.news.utils.OkHttpUtils;

/**
 * Created by niangang on 2016/10/17.
 */

public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    private NewsDetailContract.View view;

    public NewsDetailPresenter(NewsDetailContract.View view) {
        this.view = view;

    }


    @Override
    public void loadNewsDetail(final String docId) {
        String url = getDetailUrl(docId);
        OkHttpUtils.ResultCallBack<String> loadNewsCallback = new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docId);
                view.loadNewsDetailSuccess(newsDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                view.loadNewsDetailFailure("load news detail info failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }
}
