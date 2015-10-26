package org.wit.ff.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wit.ff.cache.CacheKey;
import org.wit.ff.cache.IAppCache;

/**
 * Created by F.Fang on 2015/9/15.
 * Version :2015/9/15
 */
@Aspect
public class BusinessCacheAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCacheAspect.class);

    private IAppCache appCache;

    @Pointcut("@annotation(org.wit.ff.cache.Cache)")
    public void methodCachePointcut() {

    }

    @Around("methodCachePointcut()")
    public Object record(ProceedingJoinPoint pjp) throws Throwable {
        CacheKey key1 = buildCacheKey(pjp);
        // 只要两个CacheKey对象的json值相等,就认为一致.

        return pjp.proceed();
    }

    private CacheKey buildCacheKey(ProceedingJoinPoint pjp){
        CacheKey key = new CacheKey();
        key.setMethod(pjp.getSignature().getName());
        if(pjp.getArgs()!=null && pjp.getArgs().length>0){
            // 从数组转换成列表, 原因是因为数组是对象,内容相同的数组执行equals比较时并不想等.
            Object[] params = pjp.getArgs();
            key.setParams(params);
            // 如果先转换成json, 将json字符串转换成bytes数组.
        }
        return key;
    }

    public void setAppCache(IAppCache appCache) {
        this.appCache = appCache;
    }
}
