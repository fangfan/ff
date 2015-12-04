package common;

import exception.ServerInitException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by F.Fang on 2015/5/27.
 * Version :2015/5/27
 */
public class DefaultConfigLoader {

    public Properties loadDefaultConfig(){

        String dir = System.getProperty("user.dir");
        // load default config.
        Properties defaultConf = loadConfig("Conf/default.properties");

        if (!defaultConf.containsKey("zkConnStr")) {
            throw new ServerInitException("default config must contains key 'zkConnStr'!");
        }
        return defaultConf;
    }

    public Properties loadConfig(String path)  {
        Properties conf = new Properties();
        try {
            conf.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new ServerInitException("load config file("+path+") occur exception!",e);
        }
        return conf;
    }
}
