package org.wit.ff.cache;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
public interface ISerializer<T> {
    byte[] serialize(T obj);

    T deserialize(byte[] bytes, Class<T> type);
}
