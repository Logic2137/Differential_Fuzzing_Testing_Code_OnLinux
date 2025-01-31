import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.server.RMIFailureHandler;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CloseServerSocketOnTermination {

    private static long TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {
        System.err.println("\nRegression test for bug 4924577\n");
        RMISocketFactory.setFailureHandler(new RMIFailureHandler() {

            public boolean failure(Exception e) {
                return false;
            }
        });
        tryWith(new IOException());
        tryWith(new NullPointerException());
        tryWith(new OutOfMemoryError());
        tryWith(new NoClassDefFoundError());
        tryWith(new InternalError());
        tryWith(new Throwable());
        System.err.println("TEST PASSED");
    }

    private static void tryWith(Throwable t) throws Exception {
        Remote impl = new Remote() {
        };
        try {
            CountDownLatch latch = new CountDownLatch(1);
            UnicastRemoteObject.exportObject(impl, 0, null, new SSF(t, latch));
            if (!latch.await(TIMEOUT, TimeUnit.MILLISECONDS)) {
                throw new Error("server socket not closed");
            }
        } finally {
            UnicastRemoteObject.unexportObject(impl, true);
        }
    }

    private static class SSF implements RMIServerSocketFactory {

        private final Throwable acceptFailure;

        private final CountDownLatch closedLatch;

        SSF(Throwable acceptFailure, CountDownLatch closedLatch) {
            this.acceptFailure = acceptFailure;
            this.closedLatch = closedLatch;
        }

        public ServerSocket createServerSocket(int port) throws IOException {
            return new ServerSocket(port) {

                private int acceptInvocations = 0;

                public synchronized Socket accept() throws IOException {
                    if (acceptInvocations++ == 0) {
                        throwException(acceptFailure);
                    }
                    return super.accept();
                }

                public void close() throws IOException {
                    closedLatch.countDown();
                    super.close();
                }
            };
        }

        private static void throwException(Throwable t) {
            try {
                toThrow.set(t);
                Thrower.class.newInstance();
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InstantiationException e) {
                throw new AssertionError();
            } finally {
                toThrow.remove();
            }
        }

        private static ThreadLocal<Throwable> toThrow = new ThreadLocal<Throwable>();

        private static class Thrower {

            Thrower() throws Throwable {
                throw toThrow.get();
            }
        }
    }
}
