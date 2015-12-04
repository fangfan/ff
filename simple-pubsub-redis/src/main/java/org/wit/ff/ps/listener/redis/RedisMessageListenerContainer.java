package org.wit.ff.ps.listener.redis;

import java.util.List;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.wit.ff.ps.listener.DefaultMessageListenerContainer;


/**
 * 
 * <pre>
 * Redis监听控制中心.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: RedisMessageListenerContainer.java, v 0.1 2014年12月18日 上午3:04:06 F.Fang Exp $
 */
public class RedisMessageListenerContainer extends DefaultMessageListenerContainer {
    
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RedisMessageListenerContainer.class);

    /**
     * 通过配置文件注入.
     */
    private List<IRedisMessageListenerTask>         messageTasks;

    public void init() {
        LOG.debug("开始初始化消息监听容器相关资源!");
        if (!messageTasks.isEmpty()) {
            int size = messageTasks.size();
            listenerTaskExec = Executors.newFixedThreadPool(size);
            for (IRedisMessageListenerTask task : messageTasks) {
                listenerTaskExec.execute(task);
            }
        }
        LOG.debug("初始化消息监听容器相关资源完成!");
    }

    public void destroy() {
        LOG.debug("开始销毁消息监听容器相关资源!");
        for (IRedisMessageListenerTask task : messageTasks) {
            try {
                task.destroy();
            } catch (Exception e) {
                //e.printStackTrace();
                // LOG
                LOG.error("销毁监听任务"+task.getDescription()+"失败!",e);
            }
        }
        if (listenerTaskExec != null && !listenerTaskExec.isShutdown()) {
            listenerTaskExec.shutdown();
        }
        LOG.debug("销毁消息监听容器相关资源完成!");
    }

    public void setMessageTasks(List<IRedisMessageListenerTask> messageTasks) {
        this.messageTasks = messageTasks;
    }

}
