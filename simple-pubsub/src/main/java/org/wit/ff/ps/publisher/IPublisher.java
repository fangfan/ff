package org.wit.ff.ps.publisher;

import org.wit.ff.ps.IMessage;

/**
 * 
 * <pre>
 * 发布者.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: IPublisher.java, v 0.1 2014年12月22日 上午6:19:53 F.Fang Exp $
 */
public interface IPublisher {
    
    <M> boolean send(String channel, IMessage<M> msg) throws Exception;

    void close() throws Exception;

}
