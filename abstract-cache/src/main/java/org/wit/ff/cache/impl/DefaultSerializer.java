package org.wit.ff.cache.impl;

import org.springframework.util.SerializationUtils;
import org.wit.ff.cache.ISerializer;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
public class DefaultSerializer<T> implements ISerializer<T>{

    @Override
    public byte[] serialize(T obj) {
        return SerializationUtils.serialize(obj);
    }

    @Override
    public T deserialize(byte[] bytes, Class<T> type) {
        return (T)SerializationUtils.deserialize(bytes);
    }
}
