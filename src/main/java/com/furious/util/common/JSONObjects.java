package com.furious.util.common;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * JSONObject tools
 * key支持嵌套
 * 只支持JSONObject对象，暂不支持JSONArray
 */
public enum JSONObjects {
    ;

    /**
     * 获取json对象对应key的值
     *
     * @param json JSONObject 对象
     * @param key  key，如果是嵌套的key，以key1.key2.key3...的形式传递
     * @return 若存在，则返回对应的值，反之，返回空
     */
    public static Object get(JSONObject json, String key) {
        if (Objects.isNull(json) || StringUtils.isEmpty(key)) {
            return null;
        }
        //a.b.c
        String[] keys = key.split("\\.");
        JSONObject object = json;
        String k;
        try {
            for (int i = 0, len = keys.length; i < len; i++) {
                if (i == len - 1) {
                    return object.get(keys[i]);
                }
                k = keys[i];
                object = object.getJSONObject(k);
            }
        } catch (Exception e) {
            //NPE
            return null;
        }
        return null;
    }

    public static void put(JSONObject json, String key, Object value) {
        if (Objects.isNull(json) || StringUtils.isBlank(key) || Objects.isNull(value)) {
            return;
        }
        //a.b.c
        String[] keys = key.split("\\.");
        JSONObject object = json;
        String k;
        try {
            for (int i = 0, len = keys.length; i < len; i++) {
                k = keys[i];
                if (i == len - 1) {
                    object.put(k, value);
                    break;
                }
                if (Objects.isNull(object.getJSONObject(k))) {
                    object.put(k, new JSONObject());
                }
                object = object.getJSONObject(k);
            }
        } catch (Exception e) {
            //NPE
        }
    }
}
