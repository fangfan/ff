package com.adchina.dmp.ndal.mongodb;

import com.adchina.dmp.ndal.JsonUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.*;

/**
 * Created by F.Fang on 2015/2/27.
 * Version :2015/2/27
 */
public final class MongoUtils {

    private MongoUtils() {
    }

    /**
     * 对象转换.
     * BasicDBObject-->Map.
     * BasicDBList-->DBList.
     * @param obj
     * @return
     */
    public static Object translate(DBObject obj) {
        if (obj == null) {
            return null;
        }
        Object result = null;
        if(obj instanceof BasicDBList){
            List<Object> list = new ArrayList<>();
            fillList((BasicDBList) obj, list);
            result = list;
        } else if(obj instanceof BasicDBObject){
            Map<String, Object> map = new HashMap<>();
            fill((BasicDBObject)obj, map);
            result = map;
        } else{
            throw new IllegalArgumentException("No support other type except BasicDBList and BasicDBObject!");
        }
        return result;
    }

    private static void fill(BasicDBObject obj, Map<String, Object> result) {
        for (String key : obj.keySet()) {
            Object val = obj.get(key);
            if (val instanceof BasicDBObject) {
                Map<String, Object> newMap = new HashMap<>();
                fill((BasicDBObject) val, newMap);
                result.put(key, newMap);
            } else if (val instanceof BasicDBList) {
                BasicDBList dbList = (BasicDBList) val;
                List<Object> list = new ArrayList<>();
                fillList(dbList, list);
                result.put(key, list);
            } else {
                result.put(key, val);
            }
        }
    }

    private static void fillList(BasicDBList dbList, List<Object> list) {
        for (String sk : dbList.keySet()) {
            Object el = dbList.get(sk);
            //BasicDBList 内部不存在嵌套BasicDBList,故dbList的元素类型一定不是BasicDBList.
            if (el instanceof BasicDBObject) {
                Map<String, Object> newMap = new HashMap<>();
                fill((BasicDBObject) el, newMap);
                list.add(el);
            } else {
                list.add(el);
            }
        }
    }

    /**
     * 将一个DBObject对象转换成目标类型的实例.
     *
     * @param obj
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T build(DBObject obj, Class<?> targetClass) {
        return JsonUtil.convert(obj, targetClass);
    }

    /**
     * 将一个DBObject对象列表转换为具体类型的对象列表.
     * 针对map结构做适应,以最快的方式获取数据.
     *
     * @param targetClass
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Collection<T> build(Class<?> targetClass, List<DBObject> list) {
        if (targetClass == null || list == null || list.isEmpty()) {
            return null;
        }

        List<T> result = new ArrayList<T>();
        for (DBObject obj : list) {
            T entity = JsonUtil.convert(obj, targetClass);
            result.add(entity);
        }
        return result;
    }

}
