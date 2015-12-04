package org.wit.ff.ps.listener;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.DisposableBean;

/**
 * 
 * <pre>
 * 默认监听控制中心.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: DefaultMessageListenerContainer.java, v 0.1 2014年12月18日 上午3:03:53 F.Fang Exp $
 */
public abstract class DefaultMessageListenerContainer implements DisposableBean{
    
    protected ExecutorService listenerTaskExec;
    
    public abstract void init() ;

}
