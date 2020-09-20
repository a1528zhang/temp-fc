package link.netless.convert.bo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by az on 2020/9/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailedMessage {
    private String message;
    private String stack;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
