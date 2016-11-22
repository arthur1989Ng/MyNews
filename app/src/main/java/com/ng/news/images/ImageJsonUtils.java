package com.ng.news.images;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ng.news.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

import com.ng.news.utils.JsonUtils;
import com.ng.news.utils.LogUtils;

/**
 * Created by niangang on 2016/9/7.
 */

public class ImageJsonUtils {
    private final static String TAG = "ImageJsonUtils";

    /**
     * 获取图片列表
     *
     * @param str
     * @return
     */
    public static List<ImageBean> getJsonImageBeans(String str) {
        List<ImageBean> beans = new ArrayList<ImageBean>();
        try {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(str).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                ImageBean imageBean = JsonUtils.deserialize(jsonObject, ImageBean.class);
                beans.add(imageBean);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "getJsonImageBeans error", e);

        }
        return beans;
    }

}
