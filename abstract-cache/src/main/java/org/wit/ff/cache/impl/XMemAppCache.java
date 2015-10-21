package org.wit.ff.cache.impl;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.wit.ff.cache.AppCacheException;
import org.wit.ff.cache.ExpireType;
import org.wit.ff.cache.IAppCache;
import org.wit.ff.cache.Option;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by F.Fang on 2015/9/24.
 * 基于xmemcached.
 * Version :2015/9/24
 */
public class XMemAppCache implements IAppCache {

    /**
     * memcached客户端.
     */
    private MemcachedClient client;

    /**
     * 选项.
     */
    private Option option;

    public XMemAppCache(){
        option = new Option();
    }

    @Override
    public <K> boolean contains(K key) {
        String strKey = translateToStr(key);
        try {
            return client.get(strKey) != null;
        } catch (InterruptedException | MemcachedException |TimeoutException e){
            throw new AppCacheException(e);
        }
    }

    @Override
    public <K, V> boolean put(K key, V value) {
        return put(key,value,option);
    }

    @Override
    public <K, V> boolean put(K key, V value, Option option) {
        if(option.getExpireType().equals(ExpireType.DATETIME)){
            throw new UnsupportedOperationException("memcached no support ExpireType(DATETIME) !");
        }
        // 目前考虑 set, add方法如果key已存在会发生异常.
        // 当前对缓存均不考虑更新操作.
        int seconds = (int)option.getExpireTime()/1000;
        String strKey = translateToStr(key);
        try {
            if(option.isAsync()){
                // 异步操作.
                client.setWithNoReply(strKey, seconds, value);
                return true;
            } else {
                return client.set(strKey, seconds, value);
            }

        } catch (InterruptedException | MemcachedException |TimeoutException e){
            throw new AppCacheException(e);
        }
    }

    @Override
    public <K, V> V get(K key, Class<V> type) {
        String strKey = translateToStr(key);
        try {
            return client.get(strKey);
        } catch (InterruptedException | MemcachedException |TimeoutException e){
            throw new AppCacheException(e);
        }
    }

    @Override
    public <K> boolean remove(K key) {
        String strKey = translateToStr(key);
        try {
            return client.delete(strKey);
        } catch (InterruptedException | MemcachedException |TimeoutException e){
            throw new AppCacheException(e);
        }
    }

    @Override
    public <K, F, V> boolean putMapEntry(K key, F field, V value) {
        return false;
    }

    @Override
    public <K, F, V> boolean putMap(K key, Map<F, V> value, Option option) {
        return false;
    }

    @Override
    public <K, F, V> boolean putMap(K key, Map<F, V> value) {
        return false;
    }

    @Override
    public <K, F, V> V getMapEntryValue(K key, F field, Class<V> type) {
        return null;
    }

    @Override
    public <K, F, V> Map<F, V> getMap(K key, Class<F> fType, Class<V> vType) {
        return null;
    }

    @Override
    public <K, V> boolean putList(K key, List<V> value, Option option) {
        return false;
    }

    @Override
    public <K, V> boolean putList(K key, List<V> value) {
        return false;
    }

    @Override
    public <K, V> boolean putListElement(K key, V value) {
        return false;
    }

    @Override
    public <K, V> List<V> getList(K key, Class<V> type) {
        return null;
    }

    private <K> String translateToStr(K key) {
        if(key instanceof String){
            return (String)key;
        }
        return key.toString();
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
