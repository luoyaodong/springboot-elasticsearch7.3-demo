package com.example.demo.common.utils;




import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * json 有关的工具类
 * @author hongc
 *	2018年11月27日 下午2:23:42
 */
@Slf4j
public class JSONUtil {
    static JSONObject jsonObject = new JSONObject();

    /**
     * 最常见的将对象序列化字符串
     */
    public static String toJsonString(Object obj) {
        if(null == obj) {
            return "";
        }
        return JSON.toJSONString(obj);
    }

    public static JSONObject copyJSON(JSONObject json) {
        return JSONObject.parseObject(json.toJSONString());
    }

    public static JSONObject toJSON(String jsonString) {
        return JSONObject.parseObject(jsonString);
    }

    public static String getValueByPath(JSONObject jsonObj,String path) {
        String[] parts = path.split("\\.");
        // JSONObject result = null;
        for(String part : parts) {
            Object partObj = jsonObj.get(part);
            if(null == partObj) {
                return null;
            }else {
                if(partObj instanceof JSONObject) {
                    jsonObj = (JSONObject)partObj;
                }else {
                    return partObj+"";
                }
            }
        }

        return null;


    }
    public static void setValueByPath(JSONObject jsonObj,String path,String newValue) {
        String[] parts = path.split("\\.");
        for(String part : parts) {
            Object partObj = jsonObj.get(part);
            if(null == partObj) {
                return;
            }else {
                if(partObj instanceof JSONObject) {
                    jsonObj = (JSONObject)partObj;
                }else {
                    jsonObj.put(part,newValue);
                }
            }
        }

    }




}

