
package compiler.lib.ir_framework.shared;

public class TestFrameworkException extends RuntimeException {

    public TestFrameworkException(String message) {
        super("Internal Test Framework exception - please file a bug:" + System.lineSeparator() + message);
    }

    public TestFrameworkException(String message, Throwable e) {
        super("Internal Test Framework exception - please file a bug:" + System.lineSeparator() + message, e);
    }
}
