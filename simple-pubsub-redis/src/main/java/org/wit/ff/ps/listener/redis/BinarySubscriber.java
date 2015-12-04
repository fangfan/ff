package org.wit.ff.ps.listener.redis;

import java.util.Arrays;

import org.slf4j.Logger;

import org.wit.ff.ps.listener.IMessageListener;
import org.wit.ff.ps.serialize.ISerializer;
import org.wit.ff.ps.serialize.ProtoStuffSerializer;
import redis.clients.jedis.BinaryJedisPubSub;


/**
 * 
 * <pre>
 * 消息订阅者.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: BinarySubscriber.java, v 0.1 2014年12月19日 上午12:10:08 F.Fang Exp $
 */
public class BinarySubscriber<M> extends BinaryJedisPubSub {
    
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(BinarySubscriber.class);

    /**
     * 消息监听器.
     */
    private IMessageListener<M> messageListener;
    
    private ISerializer<M> serializer;

    public BinarySubscriber(IMessageListener<M> messageListener) {
        this.messageListener = messageListener;
        this.serializer = new ProtoStuffSerializer<M>();
    }

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        handle(channel, message);
    }

    @Override
    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
        handle(channel, message);
    }

    @Override
    public void onPSubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override
    public void onSubscribe(byte[] channel, int subscribedChannels) {
    }

    @Override
    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
    }

    private void handle(byte[] channel, byte[] message) {
        Class<M> targetClass = messageListener.getMsgBodyType();
        // 对类型做校验.
        M msg = null;
        try {
            msg = serializer.deserialize(message, targetClass);
        } catch (Exception e) {
            // LOG
            LOG.error("序列化消息失败,消息类型("+targetClass+"),消息内容("+Arrays.toString(message)+")", e);
        }
        messageListener.doMessage(msg);
    }

}
