package zoomkeeper;

import common.BaseConfig;
import common.Constants;
import exception.CommonException;
import netty.NServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by F.Fang on 2015/5/26.
 * Version :2015/5/26
 */
public class ZkCommandHandler implements IZkEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ZkClient.class);

    private NServer nServer;

    private ZkClient zkClient;

    private BaseConfig config;

    public ZkCommandHandler(NServer nServer, BaseConfig config, ZkClient zkClient) {
        this.nServer = nServer;
        this.config = config;
        this.zkClient = zkClient;
    }

    public void handle(CuratorFramework curator, PathChildrenCacheEvent event){
        switch(event.getType()){
            case CHILD_UPDATED:
            case CHILD_ADDED:
                String linkPath = event.getData().getPath();
                if(linkPath.endsWith(config.getNodeName())){
                    execute(linkPath);
                }
                break;
            default:
                LOG.warn("no handle event!");
        }

    }

    /**
     * 目前只处理 kill命令,其它不处理.
     * @param linkPath
     */
    private void execute(String linkPath){
        String command = null;
        try {
            byte[] data = zkClient.getData(linkPath);
            if(data!=null){
                command =new String(data);
            }
        } catch (CommonException e) {
            LOG.error("load command data from path("+linkPath+") occur exception!",e);
        }
        if("kill".equals(command.toLowerCase())){
            if(nServer!=null){
                nServer.shutdown();
            }
            if(zkClient!=null){
                // command path.
                delete(linkPath);
                // node path.
                String nodePath = Constants.APP_NODE+"/"+config.getNodeName();
                delete(nodePath);
                zkClient.close();
            }
        } else if("hello".equals(command.toLowerCase())){
            LOG.info("say hi," + config.getNodeName());
        }
    }

    private void delete(String path){
        // 删除目标路径.
        try {
            if(zkClient.exists(path)){
                zkClient.deleteNode(path);
            }
        } catch (CommonException e) {
            LOG.error("delete path("+path+") occur exception", e);
        }
    }
}
