package org.wit.ff.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wit.ff.cache.CacheKey;
import org.wit.ff.cache.IAppCache;
import org.wit.ff.util.JsonUtil;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
@Aspect
public class BusinessCacheAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCacheAspect.class);

    /**
     * 实际的数据缓存服务提供者.
     */
    private IAppCache appCache;

    @Pointcut("@annotation(org.wit.ff.cache.Cache)")
    public void methodCachePointcut() {

    }

    @Around("methodCachePointcut()")
    public Object record(ProceedingJoinPoint pjp) throws Throwable {
        CacheKey cacheKey = buildCacheKey(pjp);
        // 只要两个CacheKey对象的json值相等,就认为一致.
        // 数组是对象,内容相同的数组执行equals比较时并不想等.
        // 如果先转换成json, 将json字符串转换成bytes数组，作为值比较更合理.
        //appCache.get();
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        // 获取方法返回类型
        Class<?> returnType = ms.getMethod().getReturnType();
        // 返回类型为空,不会应用缓存策略
        if (Void.TYPE.equals(returnType)) {
            // 实际上, 在你并不想改变业务模型的条件下, pjp.proceed()和pjp.proceed(params) 无差别.
            return pjp.proceed();
        }
        // Json化可以避免掉许多的问题, 不必通过重写CacheKey的equals方法来比较, 因为实现会比较的复杂, 并且不见得能做好.
        String key = JsonUtil.objectToJson(cacheKey);
        // 查询缓存,即使缓存失败,也不能影响正常业务逻辑执行.
        Object result = null;
        try {
            result = appCache.get(key, returnType);
        } catch (Exception e) {
            LOGGER.error("get cache catch exception!", e);
        }
        // 若缓存为空, 则处理实际业务.
        if (result == null) {
            // 正常业务处理不要做任何拦截.
            result = pjp.proceed();
            // 暂时不记录是否缓存成功,虽然有boolean返回.
            try {
                appCache.put(key, result);
            }catch (Exception e){
                LOGGER.error("put cache catch exception!",e);
            }
        }
        return result;
    }

    private CacheKey buildCacheKey(ProceedingJoinPoint pjp) {
        CacheKey key = new CacheKey();
        key.setMethod(pjp.getSignature().getName());
        if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
            key.setParams(pjp.getArgs());
        }
        return key;
    }

    public void setAppCache(IAppCache appCache) {
        this.appCache = appCache;
    }
}
