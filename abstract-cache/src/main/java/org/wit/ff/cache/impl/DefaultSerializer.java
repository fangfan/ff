package org.wit.ff.cache.impl;

import org.springframework.util.SerializationUtils;
import org.wit.ff.cache.ISerializer;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
public class DefaultSerializer implements ISerializer{

    @Override
    public byte[] serialize(Object obj) {
        return SerializationUtils.serialize(obj);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
