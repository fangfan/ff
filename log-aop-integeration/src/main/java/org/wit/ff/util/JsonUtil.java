package org.wit.ff.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * json工具.
 * @author F.Fang
 *
 */
public final class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    private JsonUtil(){}

    public static String objectToJson(Object bean){
        String result = null;
        try {
            if(bean!=null){
                result = mapper.writeValueAsString(bean);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象生成Json发生异常!",e);
        }
        return result;
    }

    public static <T> T jsonToObject(String json, Class<T> targetClass) {
        T obj = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                obj = mapper.readValue(json, targetClass);
            } catch (IOException e) {
                throw new RuntimeException("由Json转换成对象发生异常!",e);
            }
        }
        return obj;
    }

}
