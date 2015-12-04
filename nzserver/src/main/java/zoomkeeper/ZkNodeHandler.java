package zoomkeeper;

import common.BaseConfig;
import common.ByteUtil;
import common.Constants;
import netty.NClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by F.Fang on 2015/5/26.
 * Version :2015/5/26
 */
public class ZkNodeHandler implements IZkEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ZkNodeHandler.class);

    private ZkClient zkClient;

    private BaseConfig config;

    private BaseConfig neighbourConfig;

    public ZkNodeHandler(ZkClient zkClient, BaseConfig config, BaseConfig neighbourConfig) {
        this.zkClient = zkClient;
        this.config = config;
        this.neighbourConfig = neighbourConfig;
    }

    public void handle(CuratorFramework curator, PathChildrenCacheEvent event) {
        String linkPath = event.getData().getPath();
        switch (event.getType()) {
            case CHILD_ADDED:
                LOG.info("node(" + linkPath + ") online!");
                execute(linkPath);
                break;
            case CHILD_REMOVED:
                LOG.info("node(" + linkPath + ") offline!");
            default:
                LOG.warn("no handle event!");
        }
    }

    private void execute(String linkPath) {
        // 如果有邻接节点,并且邻接节点启动消息产生.
        if (neighbourConfig != null && linkPath.endsWith(neighbourConfig.getNodeName())) {
            NClient nClient = null;
            String msg = config.getNodeName() + " say hello to " + neighbourConfig.getNodeName();
            try {
                nClient = NClient.getInstance(neighbourConfig.getHost(), neighbourConfig.getPort());
                byte[] byteMsg = msg.getBytes();
                byte[] arr = new byte[byteMsg.length + 3];
                arr[0] = Constants.BEGIN_SEND_FILE;
                byte[] msgSizeBytes = ByteUtil.shortToByte2((short) byteMsg.length);
                arr[1] = msgSizeBytes[0];
                arr[2] = msgSizeBytes[1];
                for (int i = 3, len = arr.length; i < len; ++i) {
                    arr[i] = byteMsg[i-3];
                }
                nClient.sendMsg(arr);
            } catch (Exception e) {
                LOG.error("msg (" + msg + ") send occur exception!", e);
            } finally {
                if (nClient != null) {
                    nClient.shutdown();
                }
            }
        }
    }
}
