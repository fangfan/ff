package org.wit.ff.ps.publisher.redis;

import org.wit.ff.ps.IMessage;
import org.wit.ff.ps.publisher.IPublisher;
import org.wit.ff.ps.serialize.ISerializer;
import org.wit.ff.ps.serialize.ProtoStuffSerializer;
import redis.clients.jedis.Jedis;


/**
 * 
 * <pre>
 * 消息发布者.
 * 直接创建实例调用采用.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: JedisMessagePublisher.java, v 0.1 2014年12月18日 上午3:03:31 F.Fang Exp $
 */
public class JedisMessagePublisher implements IPublisher {

    private Jedis               jedis;

    public JedisMessagePublisher(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void close() throws Exception {
        if (jedis != null) {
            jedis.close();
        }
    }

    @Override
    public <M> boolean send(String channel, IMessage<M> msg) throws Exception {
        ISerializer<M> serializer = new ProtoStuffSerializer<>();
        byte[] message = serializer.serialize(msg.getBody());
        return jedis.publish(channel.getBytes(), message) == 1L;
    }

}
