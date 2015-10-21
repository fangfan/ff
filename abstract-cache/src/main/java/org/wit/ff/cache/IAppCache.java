package org.wit.ff.cache;

import java.util.List;
import java.util.Map;

/**
 * Created by F.Fang on 2015/9/23.
 * Version :2015/9/23
 */
public interface IAppCache {
    /**
     *
     * @param key 键
     * @param <K>
     * @return 目标缓存中是否存在键
     */
    <K> boolean contains(K key);

    /**
     *
     * @param key 键
     * @param value 值
     * @param <K>
     * @param <V>
     * @return 存储到目标缓存是否成功
     */
    <K,V> boolean put(K key, V value);

    /**
     *
     * @param key 键
     * @param value 值
     * @param option 超时，同步异步控制
     * @param <K>
     * @param <V>
     * @return 存储到目标缓存是否成功
     */
    <K,V> boolean put(K key, V value, Option option);

    /**
     *
     * @param key 键
     * @param type 值
     * @param <K>
     * @param <V>
     * @return 返回缓存系统目标键对应的值
     */
    <K,V> V get(K key, Class<V> type);

    /**
     *
     * @param key 键
     * @param <K>
     * @return 删除目标缓存键是否成功
     */
    <K> boolean remove(K key);

    /**
     * 往缓存key对应的Map结构中增加键(F)值(V)对
     * @param key
     * @param field
     * @param value
     * @param <K>
     * @param <F>
     * @param <V>
     * @return
     */
    <K,F,V> boolean putMapEntry(K key, F field, V value);

    /**
     * 往缓存存储键为K类型，值为Map<F,V>类型的结构
     * @param key
     * @param value
     * @param option 超时，同步异步控制
     * @param <K>
     * @param <F>
     * @param <V>
     * @return
     */
    <K,F,V> boolean putMap(K key, Map<F, V> value, Option option);

    /**
     * 往缓存存储键为K类型，值为Map<F,V>类型的结构
     * @param key
     * @param value
     * @param <K>
     * @param <F>
     * @param <V>
     * @return
     */
    <K,F,V> boolean putMap(K key, Map<F, V> value);

    /**
     *
     * @param key
     * @param field
     * @param type
     * @param <K>
     * @param <F>
     * @param <V>
     * @return
     */
    <K,F,V> V getMapEntryValue(K key, F field, Class<V> type);

    /**
     *
     * @param key
     * @param fType
     * @param vType
     * @param <K>
     * @param <F>
     * @param <V>
     * @return
     */
    <K,F,V> Map<F,V> getMap(K key, Class<F> fType, Class<V> vType);

    /**
     *
     * @param key
     * @param value
     * @param option 超时，同步异步控制
     * @param <K>
     * @param <V>
     * @return
     */
    <K,V> boolean putList(K key, List<V> value, Option option);

    /**
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    <K,V> boolean putList(K key, List<V> value);

    /**
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    <K,V> boolean putListElement(K key, V value);

    /**
     *
     * @param key
     * @param type
     * @param <K>
     * @param <V>
     * @return
     */
    <K,V> List<V> getList(K key, Class<V> type);
}
