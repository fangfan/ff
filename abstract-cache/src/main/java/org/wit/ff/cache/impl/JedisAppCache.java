package org.wit.ff.cache.impl;


import org.wit.ff.cache.ExpireType;
import org.wit.ff.cache.IAppCache;
import org.wit.ff.cache.ISerializer;
import org.wit.ff.cache.Option;
import org.wit.ff.util.ByteUtil;
import org.wit.ff.util.ClassUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by F.Fang on 2015/9/16.
 * 目前的实现虽然不够严密,但是基本够用.
 * 因为对于put操作,对于目前的业务场景是允许失败的,因为下次执行正常业务逻辑处理时仍然可以重建缓存.
 * Version :2015/9/16
 */
public class JedisAppCache implements IAppCache {

    /**
     * redis连接池.
     */
    private JedisPool pool;

    /**
     * 序列化工具.
     */
    private ISerializer serializer;

    /**
     * 全局超时选项.
     */
    private Option option;

    public JedisAppCache() {
        serializer = new DefaultSerializer();
        option = new Option();
    }

    @Override
    public <K> boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            return jedis.exists(kBytes);
        }
    }

    @Override
    public <K, V> boolean put(K key, V value) {
        return put(key, value, option);
    }

    @Override
    public <K, V> boolean put(K key, V value, Option option) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key,value can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            byte[] vBytes = translateObjToBytes(value);
            // 暂时不考虑状态码的问题, 成功状态码为OK.
            String code = jedis.set(kBytes, vBytes);
            // 如果设置了合法的过期时间才设置超时.
            setExpire(kBytes, option, jedis);
            return "OK".equals(code);
        }
    }

    @Override
    public <K, V> V get(K key, Class<V> type) {
        if (key == null || type == null) {
            throw new IllegalArgumentException("key or type can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            byte[] vBytes = jedis.get(kBytes);
            if (vBytes == null) {
                return null;
            }
            return translateBytesToObj(vBytes, type);
        }
    }

    @Override
    public <K> boolean remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            // 状态码为0或1(key数量)都可认为是正确的.0表示key原本就不存在.
            jedis.del(kBytes);
            // 暂时不考虑状态码的问题.
            return true;
        }
    }

    @Override
    public <K, F, V> boolean putMapEntry(K key, F field, V value) {
        if (key == null || field == null || value == null) {
            throw new IllegalArgumentException("key,field, value can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            byte[] fBytes = translateObjToBytes(field);
            byte[] vBytes = translateObjToBytes(value);
            // 暂时不考虑状态码的问题, 0和1都是正确的, 0表示原有的field已存在.
            jedis.hset(kBytes, fBytes, vBytes);
            return true;
        }
    }

    @Override
    public <K, F, V> boolean putMap(K key, Map<F, V> value, Option option) {
        if (key == null || value == null || value.isEmpty()) {
            throw new IllegalArgumentException("key,value can't be null or empty!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            Map<byte[], byte[]> valueMap = new HashMap<>();
            for (Map.Entry<F, V> entry : value.entrySet()) {
                byte[] fBytes = translateObjToBytes(entry.getKey());
                byte[] vBytes = translateObjToBytes(entry.getValue());
                valueMap.put(fBytes, vBytes);
            }
            // 暂时不考虑状态码的问题, 成功状态码为OK.
            String code = jedis.hmset(kBytes, valueMap);
            // 如果设置了合法的过期时间才设置超时.
            setExpire(kBytes, option, jedis);
            return "OK".equals(code);
        }
    }

    @Override
    public <K, F, V> boolean putMap(K key, Map<F, V> value) {
        return putMap(key, value, option);
    }

    @Override
    public <K, F, V> V getMapEntryValue(K key, F field, Class<V> type) {
        if (key == null || field == null || type == null) {
            throw new IllegalArgumentException("key,field,type can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            byte[] fBytes = translateObjToBytes(field);
            byte[] vBytes = jedis.hget(kBytes, fBytes);
            return translateBytesToObj(vBytes, type);
        }
    }

    @Override
    public <K, F, V> Map<F, V> getMap(K key, Class<F> fType, Class<V> vType) {
        if (key == null || fType == null || vType == null) {
            throw new IllegalArgumentException("key,fType,vType can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            Map<byte[], byte[]> data = jedis.hgetAll(kBytes);
            Map<F, V> result = new HashMap<>();
            if (data != null && !data.isEmpty()) {
                for (Map.Entry<byte[], byte[]> entry : data.entrySet()) {
                    F field = translateBytesToObj(entry.getKey(), fType);
                    V value = translateBytesToObj(entry.getValue(), vType);
                    result.put(field, value);
                }
            }
            return result;
        }
    }

    @Override
    public <K, V> boolean putList(K key, List<V> value, Option option) {
        if (key == null || value == null || value.isEmpty()) {
            throw new IllegalArgumentException("key,value can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            int len = value.size();
            byte[][] arr = new byte[len][];
            for (int i = 0; i < len; ++i) {
                byte[] vBytes = translateObjToBytes(value.get(i));
                arr[i] = vBytes;
            }
            // rpush 返回的实际上是列表的长度,正常判断应该和len全等.
            long code = jedis.rpush(kBytes, arr);
            // 如果设置了合法的过期时间才设置超时.
            setExpire(kBytes, option, jedis);
            return len == code;
        }
    }

    @Override
    public <K, V> boolean putList(K key, List<V> value) {
        return putList(key, value, option);
    }

    @Override
    public <K, V> boolean putListElement(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            byte[] vBytes = translateObjToBytes(value);
            // rpush 返回的实际上是列表的长度,正常判断应该和len全等, 即单个元素插入返回为1.
            long code = jedis.rpush(kBytes, vBytes);
            return code == 1L;
        }
    }

    @Override
    public <K, V> List<V> getList(K key, Class<V> type) {
        if (key == null || type == null) {
            throw new IllegalArgumentException("key,type can't be null!");
        }
        try (Jedis jedis = pool.getResource()) {
            byte[] kBytes = translateObjToBytes(key);
            long len = jedis.llen(kBytes);
            List<V> result = new ArrayList<>();
            List<byte[]> bytesList = jedis.lrange(kBytes, 0, len);
            if (bytesList != null && !bytesList.isEmpty()) {
                for (byte[] el : bytesList) {
                    V value = translateBytesToObj(el, type);
                    result.add(value);
                }
            }
            return result;
        }
    }


    private <T> byte[] translateObjToBytes(T val) {
        byte[] valBytes;
        if (val instanceof String) {
            valBytes = ((String) val).getBytes();
        } else {
            Class<?> classType = ClassUtil.getWrapperClassType(val.getClass().getSimpleName());
            if (classType != null) {
                // 如果是基本类型. Boolean,Void不可能会出现在参数传值类型的位置.
                if (classType.equals(Integer.TYPE)) {
                    valBytes = ByteUtil.intToByte4((Integer) val);
                } else if (classType.equals(Character.TYPE)) {
                    valBytes = ByteUtil.charToByte2((Character) val);
                } else if (classType.equals(Long.TYPE)) {
                    valBytes = ByteUtil.longToByte8((Long) val);
                } else if (classType.equals(Double.TYPE)) {
                    valBytes = ByteUtil.doubleToByte8((Double) val);
                } else if (classType.equals(Float.TYPE)) {
                    valBytes = ByteUtil.floatToByte4((Float) val);
                } else {
                    throw new IllegalArgumentException("unsupported value type, classType is:" + classType);
                }
            } else {
                // 其它均采用序列化
                valBytes = serializer.serialize(val);
            }
        }
        return valBytes;
    }

    private <T> T translateBytesToObj(byte[] bytes, Class<T> type) {
        Object obj;
        if (type.equals(String.class)) {
            obj = new String(bytes);
        } else {
            Class<?> classType = ClassUtil.getWrapperClassType(type.getSimpleName());
            if (classType != null) {
                // 如果是基本类型. Boolean,Void不可能会出现在参数传值类型的位置.
                if (classType.equals(Integer.TYPE)) {
                    obj = ByteUtil.byte4ToInt(bytes);
                } else if (classType.equals(Character.TYPE)) {
                    obj = ByteUtil.byte2ToChar(bytes);
                } else if (classType.equals(Long.TYPE)) {
                    obj = ByteUtil.byte8ToLong(bytes);
                } else if (classType.equals(Double.TYPE)) {
                    obj = ByteUtil.byte8ToDouble(bytes);
                } else if (classType.equals(Float.TYPE)) {
                    obj = ByteUtil.byte4ToFloat(bytes);
                } else {
                    throw new IllegalArgumentException("unsupported value type, classType is:" + classType);
                }
            } else {
                // 其它均采用序列化
                obj = serializer.deserialize(bytes);
            }
        }
        return (T) obj;
    }

    private void setExpire(byte[] kBytes,Option option, Jedis jedis) {
        if (option.getExpireType().equals(ExpireType.SECONDS)) {
            int seconds = (int)option.getExpireTime()/1000;
            if(seconds > 0){
                jedis.expire(kBytes, seconds);
            }
        } else {
            jedis.expireAt(kBytes, option.getExpireTime());
        }
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
