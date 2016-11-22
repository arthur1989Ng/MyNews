package com.ng.news.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ng.news.R;
import com.ng.news.bean.NewsBean;
import com.ng.news.bean.NewsDetailBean;
import com.ng.news.utils.ImageLoaderUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * Created by niangang on 2016/10/12.
 */

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailContract.View {
    private NewsBean mNews;
    private HtmlTextView mTVNewsContent;
    private ProgressBar mProgressBar;
    private NewsDetailContract.Presenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNews = (NewsBean) getIntent().getSerializableExtra("news");
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mTVNewsContent = (HtmlTextView) findViewById(R.id.htNewsContent);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mNews.getTitle());
        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mNews.getImgsrc());

        presenter = new NewsDetailPresenter(this);

        presenter.loadNewsDetail(mNews.getDocid());
    }

    public void showNewsDetailContent(String content) {
        mTVNewsContent.setHtmlFromString(content, new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorToast(String msg) {

    }

    @Override
    public void loadNewsDetailSuccess(NewsDetailBean newsDetailBean) {
        if (newsDetailBean != null) {
            showNewsDetailContent(newsDetailBean.getBody());
        }
        hideProgress();
    }

    @Override
    public void loadNewsDetailFailure(String msg, Exception e) {
        hideProgress();
    }
}
