package org.wit.ff.jdbc.exception;

/**
 * Created by F.Fang on 2015/11/16.
 * Version :2015/11/16
 */
public class DbUtilsDataAccessException extends RuntimeException {

    public DbUtilsDataAccessException(String message) {
        super(message);
    }

    public DbUtilsDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
