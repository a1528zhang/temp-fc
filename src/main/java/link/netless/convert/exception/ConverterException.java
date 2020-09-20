package link.netless.convert.exception;

/**
 * Created by az on 2019/4/13.
 */
public class ConverterException extends RuntimeException  {
    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(Throwable cause) {
        super(cause);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }

}
