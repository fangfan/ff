package zoomkeeper;

import exception.CommonException;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class ZkClient {

    private static final Logger LOG = LoggerFactory.getLogger(ZkClient.class);

    private String name;

    private String zkUrl;

    private CuratorFramework client;

    public ZkClient(String name, String zkUrl) {
        this.name = name;
        this.zkUrl = zkUrl;
        init();
    }

    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        //client = CuratorFrameworkFactory.builder().connectString("localhost:2181").retryPolicy(retryPolicy).namespace("nodes").build();
        client.start();
    }

    public void watch(String path, final IZkEventHandler eventHandler) {
        PathChildrenCacheListener nodePathListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                eventHandler.handle(curatorFramework, event);
            }
        };
        try {
            PathChildrenCache nodeCache = new PathChildrenCache(client, path, false);
            nodeCache.getListenable().addListener(nodePathListener);
            nodeCache.start();
        } catch (Exception e) {
            LOG.error("watch node path(" + path + ") occur exception", e);
        }
    }

    public boolean exists(String path) throws CommonException {
        Stat stat = null;
        try {
            stat = client.checkExists().forPath(path);
        } catch (Exception e) {
            throw new CommonException("client check path("+path+") exist occur exception", e);
        }
        return stat != null;
    }

    public void createNode(String path) throws CommonException {
        try {
            client.create().forPath(path);
        } catch (Exception e) {
            throw new CommonException("client create path("+path+") occur exception", e);
        }
    }

    public void createNode(String path, byte[] data) throws CommonException {
        try {
            client.create().forPath(path,data);
        } catch (Exception e) {
            throw new CommonException("client create path("+path+") occur exception", e);
        }
    }

    public void updateNode(String path, byte[] data) throws CommonException{
        try {
            client.setData().forPath(path, data);
        } catch (Exception e) {
            throw new CommonException("update path("+path+") occur exception", e);
        }
    }

    public void deleteNode(String path) throws CommonException {
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            throw new CommonException("client delete path("+path+") occur exception", e);
        }
    }

    public byte[] getData(String path) throws CommonException {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            throw new CommonException("get path(" + path + ") data occur exception", e);
        }
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public String getName() {
        return name;
    }
}
