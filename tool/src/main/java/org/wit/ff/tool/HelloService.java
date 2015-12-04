package org.wit.ff.tool;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by F.Fang on 2015/11/30.
 */
public class HelloService extends UnicastRemoteObject implements IHelloService,Serializable{

    private int id = 1;

    protected HelloService() throws RemoteException {
    }

    public int generateId() throws RemoteException{
        System.out.println("id is:" +id++);
        return id;
    }
}
