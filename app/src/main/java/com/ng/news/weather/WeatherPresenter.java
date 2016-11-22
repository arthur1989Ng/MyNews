package com.ng.news.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;

import com.ng.news.bean.WeatherBean;
import com.ng.news.constant.Urls;
import com.ng.news.utils.LogUtils;
import com.ng.news.utils.OkHttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;


/**
 * Created by niangang on 2016/10/17.
 */

public class WeatherPresenter implements WeatherContract.Presenter {

    private WeatherContract.View view;


    public WeatherPresenter(WeatherContract.View view) {
        this.view = view;
//        this.view.setPresenter(this);

    }

    @Override
    public void loadLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                LogUtils.e(TAG, "location failure.");
                view.loadLocationFailure("location failure.", null);
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            LogUtils.e(TAG, "location failure.");
            view.loadLocationFailure("location failure.", null);
            return;
        }
        double latitude = location.getLatitude();     //经度
        double longitude = location.getLongitude(); //纬度
        String url = getLocationURL(latitude, longitude);
        OkHttpUtils.ResultCallBack<String> callback = new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                String city = WeatherJsonUtils.getCity(response);
                if (TextUtils.isEmpty(city)) {
                    LogUtils.e(TAG, "load location info failure.");
                    view.loadLocationFailure("load location info failure.", null);
                } else {
                    view.loadLocationSuccess(city);
                }
            }

            @Override
            public void onFailure(Exception e) {
                LogUtils.e(TAG, "load location info failure.", e);
                view.loadLocationFailure("load location info failure.", e);
            }
        };
        OkHttpUtils.get(url, callback);
    }

    @Override
    public void loadWeatherData(String cityName) {
        try {
            String url = Urls.WEATHER + URLEncoder.encode(cityName, "utf-8");
            OkHttpUtils.ResultCallBack<String> callback = new OkHttpUtils.ResultCallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    List<WeatherBean> lists = WeatherJsonUtils.getWeatherInfo(response);
                    view.loadWeatherSuccess(lists);
                }

                @Override
                public void onFailure(Exception e) {
                    view.loadWeatherFailure("load weather data failure.", e);
                }
            };
            OkHttpUtils.get(url, callback);
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(TAG, "url encode error.", e);
        }
    }


    private String getLocationURL(double latitude, double longitude) {
        StringBuffer sb = new StringBuffer(Urls.INTERFACE_LOCATION);
        sb.append("?output=json").append("&referer=32D45CBEEC107315C553AD1131915D366EEF79B4");
        sb.append("&location=").append(latitude).append(",").append(longitude);
        LogUtils.d(TAG, sb.toString());
        return sb.toString();
    }
}
