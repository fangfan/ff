package org.wit.ff.ps.serialize;

/**
 * 
 * <pre>
 * 序列化&反序列化.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: ISerializer.java, v 0.1 2014年12月19日 上午12:46:20 F.Fang Exp $
 */
public interface ISerializer<M> {
    
    /**
     * 
     * <pre>
     * 序列化.
     * </pre>
     *
     * @param msg 消息体.
     * @return
     */
    byte[] serialize(M msg);
    
    /**
     * 
     * <pre>
     * 反序列化.
     * </pre>
     *
     * @param msg 消息内容
     * @param targetClass 消息体类型.
     * @return
     */
    M deserialize(byte[] msg, Class<M> targetClass);

}
