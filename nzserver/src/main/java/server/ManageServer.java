package server;

import common.Constants;
import common.DefaultConfigLoader;
import exception.CommonException;
import exception.ServerInitException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zoomkeeper.ZkClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by F.Fang on 2015/5/26.
 * Version :2015/5/26
 */
public class ManageServer {

    private static final Logger LOG = LoggerFactory.getLogger(ManageServer.class);

    private ZkClient zkClient;

    public ManageServer() {
        init();
    }

    public void init() {
        LOG.info("user.dir:"+System.getProperty("user.dir"));
        // load default config.
        DefaultConfigLoader configLoader = new DefaultConfigLoader();
        Properties defaultConf = configLoader.loadDefaultConfig();

        String zkConnStr = defaultConf.get("zkConnStr").toString().trim();

        zkClient = new ZkClient("manager", zkConnStr);

        String nodeList = defaultConf.getProperty("nodeList").trim();
        if (StringUtils.isBlank(nodeList)) {
            throw new ServerInitException("default config nodeList must not be blank!");
        }

        try {
            if (!zkClient.exists(Constants.CONFIG_NODE)) {
                zkClient.createNode(Constants.CONFIG_NODE);
            }
            String[] nodes = nodeList.split(",");
            for (String node : nodes) {
                Properties nodeConf = configLoader.loadConfig("Conf/"+node+".properties");
                String nodePath = Constants.CONFIG_NODE + "/" + node;
                if (zkClient.exists(nodePath)) {
                    zkClient.updateNode(nodePath, nodeConf.getProperty("addr").getBytes());
                } else {
                    zkClient.createNode(nodePath, nodeConf.getProperty("addr").getBytes());
                }
            }
        } catch (CommonException e) {
            LOG.error("config nodes error", e);
        }

    }

    private Properties loadConfig(String path) throws IOException {
        Properties conf = new Properties();
        conf.load(new FileInputStream(path));
        return conf;
    }

    public void execute(String command) {
        if (StringUtils.isBlank(command)) {
            return;
        }
        if ("exit".equals(command.toLowerCase())) {
            shutdown();
            return;
        }
        // 规定命令只支持两个单词.
        String cmd = command.replaceAll("[' ']+", " ");
        String[] arr = cmd.split(" ");
        if (arr.length != 2) {
            LOG.error("command only support two words, command is:" + command);
            return;
        }
        String node = arr[1];
        String data = arr[0];
        try {
            String path = Constants.COMMAND_NODE + "/" + node;
            if (zkClient.exists(path)) {
                zkClient.updateNode(path, data.getBytes());
            } else {
                zkClient.createNode(path, data.getBytes());
            }
        } catch (CommonException e) {
            LOG.error("execute command error, command is:" + command, e);
        }
    }

    private void shutdown() {
        if (zkClient != null) {
            zkClient.close();
        }
    }

    public static void main(String[] args) {
        String cmd = "i   am   y".replaceAll("[' ']+"," ");
        String[] arr = cmd.split(" ");
        System.out.println(arr[0]+"|"+arr[1]+"|"+arr[2]);
        System.out.println(arr.length);
    }

}
