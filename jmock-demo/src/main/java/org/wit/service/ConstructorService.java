package org.wit.service;

public class ConstructorService {
    
    private String tag;
    
    public ConstructorService(String tag){
        this.tag = tag;
    }
    
    public String doNoting(){
        return tag+" doNoting!";
    }

}
