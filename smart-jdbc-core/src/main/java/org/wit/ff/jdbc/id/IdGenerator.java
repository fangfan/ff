package org.wit.ff.jdbc.id;

/**
 * Created by F.Fang on 2015/2/16.
 * Version :2015/2/16
 */
public interface IdGenerator {
    /**
     * 参数为数组的原因是考虑联合主键,虽然暂时不会有对联合主键的支持.
     * @param value
     */
    void parseGenKey(Object[] value);
}
