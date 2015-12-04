package exception;

/**
 * Created by F.Fang on 2015/5/27.
 * Version :2015/5/27
 */
public class ResourceLoadException extends RuntimeException {

    public ResourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceLoadException(String message) {
        super(message);
    }

}
