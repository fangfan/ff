package server;

import common.BaseConfig;
import common.Constants;
import common.DefaultConfigLoader;
import exception.CommonException;
import exception.ServerInitException;
import netty.NServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zoomkeeper.ZkClient;
import zoomkeeper.ZkCommandHandler;
import zoomkeeper.ZkNodeHandler;

import java.util.Properties;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class TcpServer {

    private static final Logger LOG = LoggerFactory.getLogger(TcpServer.class);

    public TcpServer(String name) {
        DefaultConfigLoader configLoader = new DefaultConfigLoader();
        Properties defaultConf = configLoader.loadDefaultConfig();
        String zkConnStr = defaultConf.get("zkConnStr").toString().trim();

        ZkClient zkClient = new ZkClient(name, zkConnStr);
        String nodePath = Constants.APP_NODE + "/" + name;
        initNode(nodePath, zkClient);
        String commandPath = Constants.COMMAND_NODE + "/" + name;
        initNode(commandPath, zkClient);

        BaseConfig config = null;
        try {
            byte[] data = zkClient.getData(Constants.CONFIG_NODE + "/" + name);
            if (data != null) {
                String text = new String(data);
                String ip = text.split(":")[0];
                int port = Integer.valueOf(text.split(":")[1]);
                config = new BaseConfig();
                config.setHost(ip);
                config.setPort(port);
                config.setNodeName(name);
            }
        } catch (CommonException e) {
            // LOG.error("load config from zk error!", e);
            LOG.info("load local config from config/" + name + ".properties!");
            try {
                Properties nodeConfig = configLoader.loadConfig("Conf/" + name + ".properties");
                config = new BaseConfig();
                config.setHost(nodeConfig.getProperty("host"));
                config.setPort(Integer.valueOf(nodeConfig.getProperty("port")));
                config.setNodeName(name);
            } catch (ServerInitException e1) {
                LOG.error("load local node config occur exception", e);
                throw new ServerInitException("load local node config occur exception!", e);
            }
        }

        if (config == null) {
            throw new ServerInitException("load node config fail!");
        }


        BaseConfig neighbourConfig = getNeighbourConfig(defaultConf, name, zkClient);
        //
        NServer nServer = NServer.getInstance(config.getPort());

        ZkCommandHandler zkCommandHandler = new ZkCommandHandler(nServer, config, zkClient);
        ZkNodeHandler zkNodeHandler = new ZkNodeHandler(zkClient, config, neighbourConfig);

        zkClient.watch(Constants.APP_NODE, zkNodeHandler);
        zkClient.watch(Constants.COMMAND_NODE, zkCommandHandler);
    }

    private BaseConfig getNeighbourConfig(Properties defaultConf, String name, ZkClient zkClient) {
        String nodeList = defaultConf.getProperty("nodeList").trim();
        String[] nodes = nodeList.split(",");
        int nextIndex = -1;
        for (int i = 0, len = nodes.length; i < len; ++i) {
            if (nodes[i].equals(name)) {
                nextIndex = i + 1;
            }
        }

        BaseConfig config = null;
        if (nextIndex == -1 || nextIndex == nodes.length) {
            LOG.warn("current node(" + name + ") has not neighbour node");
        } else {
            String neighbourNode = nodes[nextIndex];
            LOG.warn("current node(" + name + ") has neighbour node(" + neighbourNode + ")");
            try {
                byte[] data = zkClient.getData(Constants.CONFIG_NODE + "/" + neighbourNode);
                if (data != null) {
                    String text = new String(data);
                    String ip = text.split(":")[0];
                    int port = Integer.valueOf(text.split(":")[1]);
                    config = new BaseConfig();
                    config.setHost(ip);
                    config.setPort(port);
                    config.setNodeName(neighbourNode);
                }
            } catch (CommonException e) {
                LOG.warn("load node(" + neighbourNode + ") config occur exception",e);
            }
        }
        return config;
    }

    private void initNode(String path, ZkClient zkClient){
        try {
            if (zkClient.exists(path)) {
                zkClient.deleteNode(path);
            }
            zkClient.createNode(path);
        } catch (CommonException e) {
            String errorMsg = "create zk node (" + path+ ") occur exception";
            LOG.error(errorMsg, e);
            throw new ServerInitException(errorMsg, e);
        }
    }

}