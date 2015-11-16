package org.wit.ff.jdbc.converter;

/**
 * Created by F.Fang on 2015/3/31.
 * 用于参数转换的接口.
 * Version :2015/3/31
 */
public interface ParamsConverter<T> {
    Object[] convert(T obj);
}
