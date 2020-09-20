import com.aliyun.fc.runtime.FunctionComputeLogger;

/**
 * Created by az on 2020/9/18.
 */
public class TestLogger implements FunctionComputeLogger {
    @Override
    public void trace(String s) {

    }

    @Override
    public void debug(String s) {

    }

    @Override
    public void info(String s) {
        System.out.println(s);
    }

    @Override
    public void warn(String s) {

    }

    @Override
    public void error(String s) {
        System.err.println(s);
    }

    @Override
    public void fatal(String s) {

    }

    @Override
    public void setLogLevel(Level level) {

    }
}
