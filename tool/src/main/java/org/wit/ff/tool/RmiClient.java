package org.wit.ff.tool;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by F.Fang on 2015/11/30.
 */
public class RmiClient {
    public static void main(String[] args) {
        try {
            HelloService service = (HelloService)Naming.lookup("rmi://127.0.0.1/Hello");
            System.out.println(service.generateId());
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
