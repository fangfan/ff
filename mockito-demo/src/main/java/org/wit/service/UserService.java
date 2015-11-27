package org.wit.service;

public class UserService {
    
    private String tag;
    
    public void sayHi(String arg){
        System.out.println("real"+arg+"!");
    }
    
    public String sayHello(String arg){
        return "real"+arg+"!";
    }
    
    public void secreteSay(String arg){
        secreteSayHi(arg);
        System.out.println(secreteSayHello(arg));
    }

    private void secreteSayHi(String arg){
        System.out.println("real"+arg+"!");
    }
    
    private String secreteSayHello(String arg){
        return "real"+arg+"!";
    }
    
    public final String sayFinal(String arg){
        return "real " + arg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    

}
