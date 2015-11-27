package org.wit.service;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeanUtils;

public class BeanUtilsInvoker {
    
    public Object invoke(Constructor ctor, Object ...args){
        return BeanUtils.instantiateClass(ctor, args);
    }

}
