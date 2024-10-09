import java.io.Closeable;
import javax.management.remote.JMXConnector;
import javax.management.remote.rmi.RMIConnection;
import javax.management.remote.rmi.RMIConnectionImpl;
import javax.management.remote.rmi.RMIConnectionImpl_Stub;
import javax.management.remote.rmi.RMIConnector;
import javax.management.remote.rmi.RMIJRMPServerImpl;
import javax.management.remote.rmi.RMIServerImpl;

public class CloseableTest {

    private static final Class[] closeArray = { JMXConnector.class, RMIConnector.class, RMIConnection.class, RMIConnectionImpl.class, RMIConnectionImpl_Stub.class, RMIServerImpl.class, RMIJRMPServerImpl.class };

    static int error;

    static void test(Class<?> c) {
        System.out.println("\nTest " + c);
        if (Closeable.class.isAssignableFrom(c)) {
            System.out.println("Test passed!");
        } else {
            error++;
            System.out.println("Test failed!");
        }
    }

    static void test(String cn) {
        try {
            test(Class.forName(cn));
        } catch (ClassNotFoundException ignore) {
            System.out.println("\n" + cn + " not tested.");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Test that all the JMX Remote API classes that " + "define\nthe method \"void close() throws " + "IOException;\" extend\nor implement the " + "java.io.Closeable interface.");
        for (Class<?> c : closeArray) {
            test(c);
        }
        test("org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub");
        if (error > 0) {
            final String msg = "\nTest FAILED! Got " + error + " error(s)";
            System.out.println(msg);
            throw new IllegalArgumentException(msg);
        } else {
            System.out.println("\nTest PASSED!");
        }
    }
}
