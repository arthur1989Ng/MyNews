package com.ng.news.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by niangang on 2016/10/12.
 * OkHttp网络连接封装工具类
 */

public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";


    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);

        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtils getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }


    private void getRequest(String url, ResultCallBack callback) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }


    private void postRequest(String url, final ResultCallBack callback, List<Param> params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }


    private void deliveryResult(final ResultCallBack callBack, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallBack(callBack, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if (callBack.mType == String.class) {
                        sendSuccessCallBack(callBack, str);
                    } else {
                        Object object = JsonUtils.deserialize(str, callBack.mType);
                        sendSuccessCallBack(callBack, object);
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "convert json failure", e);
                    sendFailCallBack(callBack, e);
                }


            }
        });
    }

    private void sendFailCallBack(final ResultCallBack callBack, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(e);
                }
            }
        });
    }


    private void sendSuccessCallBack(final ResultCallBack callBack, final Object object) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(object);
                }
            }
        });
    }
    private Request buildPostRequest(String url, List<Param> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**********************
     * 对外接口
     ************************/

    /**
     * get请求
     *
     * @param url
     * @param callBack
     */

    public static void get(String url, ResultCallBack callBack) {
        getInstance().getRequest(url, callBack);
    }

    public static void post(String url, ResultCallBack callBack, List<Param> params) {
        getInstance().postRequest(url, callBack, params);
    }


    /**
     * http请求回调类,回调方法在UI线程中执行
     *
     * @param <T>
     */
    public static abstract class ResultCallBack<T> {
        Type mType;

        public ResultCallBack() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         *
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         *
         * @param e
         */
        public abstract void onFailure(Exception e);

    }

    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

}
