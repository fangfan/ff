package org.wit.ff.cache;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
public interface ISerializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes);
}
