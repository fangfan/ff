package org.wit.ff.ps.publisher.redis;

import org.wit.ff.ps.IMessage;
import org.wit.ff.ps.publisher.IPublisher;
import org.wit.ff.ps.serialize.ISerializer;
import org.wit.ff.ps.serialize.ProtoStuffSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * 
 * <pre>
 * 消息发布者.
 * spring配置采用.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: JedisMessagePublisher.java, v 0.1 2014年12月18日 上午3:03:31 F.Fang Exp $
 */
public class JedisPoolMessagePublisher implements IPublisher {

    private JedisPool jedisPool;

    @Override
    public <M> boolean send(String channel, IMessage<M> msg) throws Exception {
        try(Jedis jedis = jedisPool.getResource()) {
            /**
             * 使用ProtoStuff作为默认的序列化策略.
             */
            ISerializer<M> serializer = new ProtoStuffSerializer<>();
            byte[] message = serializer.serialize(msg.getBody());
            return jedis.publish(channel.getBytes(), message) == 1L;
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void close() throws Exception {
    }

}
