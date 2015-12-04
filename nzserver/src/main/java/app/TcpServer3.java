package app;

import server.TcpServer;

/**
 * Created by F.Fang on 2015/5/26.
 * Version :2015/5/26
 */
public class TcpServer3 {
    public static void main(String[] args) throws Exception{
        String name = null;
        if (args != null && args.length >= 1) {
            name = args[0];
        } else {
            // throw new ServerInitException("the node name must not be blank!");
            name = "node3";
        }
        new TcpServer(name);
    }
}
