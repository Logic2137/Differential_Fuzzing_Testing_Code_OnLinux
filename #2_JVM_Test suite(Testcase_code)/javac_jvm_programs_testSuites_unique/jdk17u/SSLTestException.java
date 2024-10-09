import java.io.IOException;

public class SSLTestException extends IOException {

    private static final long serialVersionUID = -9198430167985282879L;

    public SSLTestException(String reason, Throwable throwable) {
        super(reason, throwable);
    }

    public SSLTestException(String reason) {
        this(reason, null);
    }
}
