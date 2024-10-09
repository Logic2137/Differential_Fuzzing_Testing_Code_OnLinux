

package jdk.test.lib.dcmd;


public class CommandExecutorException extends RuntimeException {
    private static final long serialVersionUID = -7039597746579144280L;

    public CommandExecutorException(String message, Throwable e) {
        super(message, e);
    }
}
