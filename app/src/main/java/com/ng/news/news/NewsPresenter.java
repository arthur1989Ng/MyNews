package com.ng.news.news;

import com.ng.news.bean.NewsBean;
import com.ng.news.constant.Urls;
import com.ng.news.utils.LogUtils;
import com.ng.news.utils.OkHttpUtils;

import java.util.List;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

/**
 * Created by niangang on 2016/10/17.
 */

public class NewsPresenter implements NewsContract.Presenter {

    private NewsContract.View view;

    public NewsPresenter(NewsContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void loadNews(final int type, int pageIndex) {
        String url = getUrl(type, pageIndex);
        LogUtils.d(TAG, url);
        //只有第一页的或者刷新的时候才显示刷新进度条
        if (pageIndex == 0) {
            view.showProgress();
        }
        OkHttpUtils.ResultCallBack<String> loadNewsCallBack = new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBean(response, getID(type));
                view.loadNewsListSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                view.loadNewsListFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallBack);
    }


    /**
     * 根据类别和页面索引创建url
     *
     * @param type
     * @param pageIndex
     * @return
     */
    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }


    /**
     * 获取ID
     *
     * @param type
     * @return
     */
    private String getID(int type) {
        String id;
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }
}
