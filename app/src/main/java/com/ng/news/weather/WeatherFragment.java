package com.ng.news.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ng.news.R;
import com.ng.news.bean.WeatherBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niangang on 2016/10/17.
 */

public class WeatherFragment extends Fragment implements WeatherContract.View {

    private WeatherContract.Presenter presenter;
    private TextView mTodayTV;
    private ImageView mTodayWeatherImage;
    private TextView mTodayTemperatureTV;
    private TextView mTodayWindTV;
    private TextView mTodayWeatherTV;
    private TextView mCityTV;
    private ProgressBar mProgressBar;
    private LinearLayout mWeatherLayout;
    private LinearLayout mWeatherContentLayout;
    private FrameLayout mRootLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WeatherPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);
        mTodayTV = (TextView) view.findViewById(R.id.today);
        mTodayWeatherImage = (ImageView) view.findViewById(R.id.weatherImage);
        mTodayTemperatureTV = (TextView) view.findViewById(R.id.weatherTemp);
        mTodayWindTV = (TextView) view.findViewById(R.id.wind);
        mTodayWeatherTV = (TextView) view.findViewById(R.id.weather);
        mCityTV = (TextView) view.findViewById(R.id.city);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mWeatherLayout = (LinearLayout) view.findViewById(R.id.weather_layout);
        mWeatherContentLayout = (LinearLayout) view.findViewById(R.id.weather_content);
        mRootLayout = (FrameLayout) view.findViewById(R.id.root_layout);
        presenter.loadLocation(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
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
    public void showWeatherLayout() {
        mWeatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCity(String city) {
        mCityTV.setText(city);
    }

    @Override
    public void setToday(String data) {
        mTodayTV.setText(data);
    }

    @Override
    public void setTemperature(String temperature) {
        mTodayTemperatureTV.setText(temperature);
    }

    @Override
    public void setWind(String wind) {
        mTodayWindTV.setText(wind);
    }

    @Override
    public void setWeather(String weather) {
        mTodayWeatherTV.setText(weather);
    }

    @Override
    public void setWeatherImage(int res) {
        mTodayWeatherImage.setImageResource(res);
    }

    @Override
    public void setWeatherData(List<WeatherBean> lists) {
        List<View> adapterList = new ArrayList<View>();
        for (WeatherBean weatherBean : lists) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_weather, null, false);
            TextView dateTV = (TextView) view.findViewById(R.id.date);
            ImageView todayWeatherImage = (ImageView) view.findViewById(R.id.weatherImage);
            TextView todayTemperatureTV = (TextView) view.findViewById(R.id.weatherTemp);
            TextView todayWindTV = (TextView) view.findViewById(R.id.wind);
            TextView todayWeatherTV = (TextView) view.findViewById(R.id.weather);

            dateTV.setText(weatherBean.getWeek());
            todayTemperatureTV.setText(weatherBean.getTemperature());
            todayWindTV.setText(weatherBean.getWind());
            todayWeatherTV.setText(weatherBean.getWeather());
            todayWeatherImage.setImageResource(weatherBean.getImageRes());
            mWeatherContentLayout.addView(view);
            adapterList.add(view);
        }
    }

    @Override
    public void loadLocationSuccess(String cityName) {
        setCity(cityName);
        presenter.loadWeatherData(cityName);
    }

    @Override
    public void loadLocationFailure(String msg, Exception e) {
        showErrorToast("定位失败");
        setCity("深圳");
        presenter.loadWeatherData("深圳");
    }

    @Override
    public void loadWeatherSuccess(List<WeatherBean> list) {
        if (list != null && list.size() > 0) {
            WeatherBean todayWeather = list.remove(0);
            setToday(todayWeather.getDate());
            setTemperature(todayWeather.getTemperature());
            setWeather(todayWeather.getWeather());
            setWind(todayWeather.getWind());
            setWeatherImage(todayWeather.getImageRes());
        }
        setWeatherData(list);
        hideProgress();
        showWeatherLayout();
    }

    @Override
    public void loadWeatherFailure(String msg, Exception e) {
        hideProgress();
        showErrorToast("获取天气数据失败");

    }

    @Override
    public void showErrorToast(String msg) {
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_SHORT).show();
    }
}
