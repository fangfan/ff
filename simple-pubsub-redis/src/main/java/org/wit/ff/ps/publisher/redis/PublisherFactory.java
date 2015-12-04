package org.wit.ff.ps.publisher.redis;

import org.wit.ff.ps.publisher.IPublisher;
import org.wit.ff.ps.publisher.IPublisherFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;


public class PublisherFactory implements IPublisherFactory {

    private IPublisher publisher;

    public PublisherFactory(String ip, int port, String password) {
        JedisShardInfo config = new JedisShardInfo(ip, port);
        config.setPassword(password);
        Jedis jedis = new Jedis(config);
        this.publisher = new JedisMessagePublisher(jedis);
    }

    public PublisherFactory(String ip, int port) {
        this.publisher = new JedisMessagePublisher(new Jedis(ip, port));
    }

    public PublisherFactory(Jedis jedis) {
        this.publisher = new JedisMessagePublisher(jedis);
    }

    @Override
    public IPublisher getPublisherIntance() {
        return publisher;
    }

}
