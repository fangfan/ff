package org.wit.ff.cache;

import org.junit.Test;
import org.wit.ff.util.JsonUtil;

import static org.junit.Assert.assertEquals;

/**
 * Created by F.Fang on 2015/10/26.
 * Version :2015/10/26
 */
public class JsonTest {

    @Test
    public void demo(){

        CacheKey key1 = new CacheKey();
        Object[] arr1 = new Object[]{1,2,3};
        key1.setMethod("get");
        key1.addParam(arr1);

        CacheKey key2 = new CacheKey();
        key2.setMethod("get");
        Object[] arr2 = new Object[]{1,2,3};
        key2.addParam(arr2);

        String json1 = JsonUtil.objectToJson(key1);
        System.out.println(json1);
        String json2 = JsonUtil.objectToJson(key2);
        System.out.println(json2);
        assertEquals(json1, json2);

    }
}
