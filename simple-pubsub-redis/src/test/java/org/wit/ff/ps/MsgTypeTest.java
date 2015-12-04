package org.wit.ff.ps;

import java.util.Arrays;

public class MsgTypeTest {

    public static void main(String[] args) {
        
        System.out.println(isIMessageSubClass(User.class));
        
        System.out.println(Arrays.toString(User.class.getInterfaces()));

    }
    
    private static boolean isIMessageSubClass(Class<?> targetClass){
        boolean result = false;
        Class<?>[] clsArr = targetClass.getInterfaces();
        if(clsArr!=null){
            for(Class<?> cls: clsArr){
                if(cls.equals(IMessage.class)){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}
