package exception;

/**
 * Created by F.Fang on 2015/5/25.
 * Version :2015/5/25
 */
public class ServerInitException extends RuntimeException {

    public ServerInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerInitException(String message) {
        super(message);
    }
}
