package com.adchina.dmp.ndal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * json工具.
 *
 * @author F.Fang
 */
public final class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static String objectToJson(Object bean) {
        if(bean==null){
            throw new IllegalArgumentException("the object translate to json can't be NULL!");
        }
        String result = null;
        try {
            result = mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("obj translate to json error!", e.fillInStackTrace());
        }
        return result;
    }

    public static <T> T jsonToObject(String json, Class<?> targetClass) {
        if (StringUtils.isBlank(json) || targetClass == null) {
            throw new IllegalArgumentException("json str or targetClass can't be NULL!");
        }
        T obj = null;
        try {
            obj = (T) mapper.readValue(json, targetClass);
        } catch (IOException e) {
            throw new RuntimeException("json translate to obj!", e.fillInStackTrace());
        }
        return obj;
    }

    public static <T> List<T> jsonToObjectList(String json, Class<?> targetClass) {
        if (StringUtils.isBlank(json) || targetClass == null) {
            throw new IllegalArgumentException("json str or targetClass can't be NULL!");
        }
        List<T> objList = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                final CollectionType javaType =
                        mapper.getTypeFactory().constructCollectionType(List.class, targetClass);
                objList = mapper.readValue(json, javaType);
            } catch (IOException e) {
                throw new RuntimeException("json translate to obj!", e.fillInStackTrace());
            }
        }
        return objList;
    }

    public static <T> T convert(Object obj, Class<?> targetClass) {
        if (obj == null || targetClass == null) {
            throw new IllegalArgumentException("obj=" + obj + ", targetClass =" + targetClass + ",obj or targetClass can't be NULL!");
        }
        T result = (T) mapper.convertValue(obj, targetClass);
        return result;
    }

}

