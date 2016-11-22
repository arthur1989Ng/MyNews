package com.ng.news.weather;

import android.content.Context;

import com.ng.news.base.BasePresenter;
import com.ng.news.base.BaseView;
import com.ng.news.bean.WeatherBean;

import java.util.List;

/**
 * Created by niangang on 2016/10/17.
 */

public interface WeatherContract {

    interface View extends BaseView<Presenter> {
        void showWeatherLayout();

        void setCity(String city);

        void setToday(String data);

        void setTemperature(String temperature);

        void setWind(String wind);

        void setWeather(String weather);

        void setWeatherImage(int res);

        void setWeatherData(List<WeatherBean> lists);

        void loadLocationSuccess(String cityName);

        void loadLocationFailure(String msg, Exception e);

        void loadWeatherSuccess(List<WeatherBean> list);

        void loadWeatherFailure(String msg, Exception e);

    }

    interface Presenter extends BasePresenter {
        void loadLocation(Context context);

        void loadWeatherData(String cityName);
    }

}
