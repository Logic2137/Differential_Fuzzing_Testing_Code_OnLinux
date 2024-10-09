

package jdk.test.failurehandler.value;

public class InvalidValueException extends Exception {
    public InvalidValueException() { }

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String s, Throwable e) {
        super(s, e);
    }

    public InvalidValueException(Throwable cause) {
        super(cause);
    }
}
