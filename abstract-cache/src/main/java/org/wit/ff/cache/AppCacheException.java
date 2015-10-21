package org.wit.ff.cache;

/**
 * Created by F.Fang on 2015/9/25.
 * Version :2015/9/25
 */
public class AppCacheException extends RuntimeException{

    public AppCacheException(String message) {
        super(message);
    }

    public AppCacheException(Throwable cause) {
        super(cause);
    }

    public AppCacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
