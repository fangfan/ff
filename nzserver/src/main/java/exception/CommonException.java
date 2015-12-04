package exception;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class CommonException extends Exception {

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }
}
