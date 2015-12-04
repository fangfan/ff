package org.wit.ff.ps.listener;

/**
 * 
 * <pre>
 * 消息监听器.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: IMessageListener.java, v 0.1 2014年12月19日 上午12:43:20 F.Fang Exp $
 */
public interface IMessageListener<M>  {
    
    /**
     * 
     * <pre>
     * 处理消息
     * </pre>
     *
     * @param msg 消息体.
     */
    void doMessage(M msg);
    
    /**
     * 
     * <pre>
     * 获取消息体类型.
     * </pre>
     *
     * @return
     */
    Class<M> getMsgBodyType();

}
