package common;

/**
 * Created by F.Fang on 2015/5/25.
 * 目前只配置一点点内容,未来可靠率使用ProtoStuff将对象序列化存储.
 * Version :2015/5/25
 */
public class BaseConfig {

    private String nodeName;

    private String host;

    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
