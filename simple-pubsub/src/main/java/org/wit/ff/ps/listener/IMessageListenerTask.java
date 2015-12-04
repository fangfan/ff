package org.wit.ff.ps.listener;

/**
 * 
 * <pre>
 * 消息监听任务接口.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: IMessageListenerTask.java, v 0.1 2014年12月19日 上午12:45:41 F.Fang Exp $
 */
public interface IMessageListenerTask extends Runnable{
    
    void destroy();
    
    String getDescription();

}
