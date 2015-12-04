package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by F.Fang on 2015/5/26.
 * Version :2015/5/26
 */
public class TestGetProperties {

    public static void main(String[] args) {
        Properties defaultConf = new Properties();

        try {
            defaultConf.load(new FileInputStream("Conf/default.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("user.dir"));
    }
}
