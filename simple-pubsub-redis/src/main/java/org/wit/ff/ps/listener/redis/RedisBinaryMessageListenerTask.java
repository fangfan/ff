package org.wit.ff.ps.listener.redis;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import org.wit.ff.ps.listener.IMessageListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * <pre>
 * 二进制消息监听任务.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: RedisBinaryMessageListenerTask.java, v 0.1 2014年12月19日 上午12:15:42 F.Fang Exp $
 */
public class RedisBinaryMessageListenerTask<M> implements IRedisMessageListenerTask {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RedisMessageListenerContainer.class);
    
    /**
     * 通道模式.
     * 包含通道名称和是否是正则匹配.
     */
    private RedisChannelMode    channelMode;

    /**
     * jedis客户端连接池.
     */
    private JedisPool           jedisPool;

    /**
     * 订阅者.
     */
    private BinarySubscriber<M> subscriber;

    public RedisBinaryMessageListenerTask(JedisPool jedisPool, RedisChannelMode channelMode,
                                          IMessageListener<M> messageListener) {
        this.channelMode = channelMode;
        this.jedisPool = jedisPool;
        this.subscriber = new BinarySubscriber<M>(messageListener);
        if(messageListener.getMsgBodyType()==null){
            throw new RuntimeException("消息监听中消息类型不允许为空!");
        }
        if(channelMode==null || StringUtils.isEmpty(channelMode.getChannel())){
            throw new RuntimeException("消息监听模式或监听队列不允许为空!");
        }
    }

    @Override
    public void run() {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            if (channelMode.isPattern()) {
                jedis.psubscribe(subscriber, channelMode.getChannel().getBytes());
            } else {
                jedis.subscribe(subscriber, channelMode.getChannel().getBytes());
            }
        } catch (Exception e) {
            broken = true;
            LOG.error("监听队列("+channelMode.getChannel()+")发生异常!", e);
        } finally {
            if (jedis != null) {
                try {
                    if (broken) {
                        jedisPool.returnBrokenResource(jedis);
                    } else {
                        jedisPool.returnResource(jedis);
                    }
                } catch (Exception e) {
                    // LOG
                    LOG.error("回收jedis客户端资源发生异常!",e);
                }
            }
        }
    }

    @Override
    public void destroy() {
        if (subscriber != null) {
            if (channelMode.isPattern()) {
                subscriber.punsubscribe();
            } else {
                subscriber.unsubscribe();
            }
        }
    }

    @Override
    public String getDescription() {
        if (channelMode != null) {
            return "[channel:" + channelMode.getChannel() + ", isPattern:"
                   + channelMode.isPattern() + "]";
        }
        return "Not Initial!";
    }

}
