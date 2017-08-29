package com.yyd.littletest.Util;

import com.google.gson.Gson;

/**
 * <pre>
 *     author : ZhanXuzhao
 *     e-mail : zhanxuzhao@yydrobot.com
 *     time   : 2017/07/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class JsonUtil {

    public static <T>T fromJson(String json, Class<T> c) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }
}
