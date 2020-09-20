import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.Credentials;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.FunctionParam;

/**
 * Created by az on 2020/9/18.
 */
public class TestContext implements Context {
    @Override
    public String getRequestId() {
        return null;
    }

    @Override
    public Credentials getExecutionCredentials() {
        return null;
    }

    @Override
    public FunctionParam getFunctionParam() {
        return null;
    }

    @Override
    public FunctionComputeLogger getLogger() {
        return new TestLogger();
    }
}
