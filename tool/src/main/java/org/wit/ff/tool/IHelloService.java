package org.wit.ff.tool;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by F.Fang on 2015/11/30.
 */
public interface IHelloService extends Remote {
    int generateId() throws RemoteException;
}
