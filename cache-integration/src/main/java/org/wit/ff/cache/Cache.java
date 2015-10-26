package org.wit.ff.cache;


import java.lang.annotation.*;

/**
 * Created by F.Fang on 2015/10/22.
 * Version :2015/10/22
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

}
