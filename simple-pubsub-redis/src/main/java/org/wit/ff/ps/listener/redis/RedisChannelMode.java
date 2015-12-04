package org.wit.ff.ps.listener.redis;

/**
 * 
 * <pre>
 * Redis通道模型.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: RedisChannelMode.java, v 0.1 2014年12月18日 上午3:04:21 F.Fang Exp $
 */
public class RedisChannelMode {

    /**
     * 消息队列名.
     */
    private String  channel;

    /**
     * 正则匹配(ture) or 文本匹配(false).
     */
    private boolean pattern;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isPattern() {
        return pattern;
    }

    public void setPattern(boolean pattern) {
        this.pattern = pattern;
    }

}
