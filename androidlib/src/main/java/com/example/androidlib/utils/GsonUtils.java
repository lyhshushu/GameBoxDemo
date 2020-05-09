package com.example.androidlib.utils;

import com.google.gson.Gson;

/**
 * json 解析工具类
 */
public class GsonUtils {
    public static <T> T jsonToBean(String result, Class<T> clazz) {
        try {
            Gson gson=new Gson();
            T t = gson.fromJson(result,clazz);
            return t;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String objectToJsonStr(Object object){
        try {
            Gson gson=new Gson();
            String t= gson.toJson(object);
            return t;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
