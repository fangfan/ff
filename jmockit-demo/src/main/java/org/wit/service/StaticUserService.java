package org.wit.service;

public class StaticUserService {
    
    public static void sayHi(String arg){
        System.out.println("real"+arg+"!");
    }
    
    public static String sayHello(String arg){
        return "real"+arg+"!";
    }
    
    public static void secreteSay(String arg){
        secreteSayHi(arg);
        System.out.println(secreteSayHello(arg));
    }
    
    private static void secreteSayHi(String arg){
        System.out.println("real"+arg+"!");
    }
    
    private static String secreteSayHello(String arg){
        return "real"+arg+"!";
    }
    
    public static final String sayFinal(String arg){
        return "real " + arg;
    }
    
}
