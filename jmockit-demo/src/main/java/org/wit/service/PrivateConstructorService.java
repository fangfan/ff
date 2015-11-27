package org.wit.service;

public class PrivateConstructorService {
    
    private PrivateConstructorService(){
        
    }
    
    public String getDetail(String arg){
        return "private service " + arg;
    }
    
    public static PrivateConstructorService createInstance(){
        return new PrivateConstructorService();
    }

}
