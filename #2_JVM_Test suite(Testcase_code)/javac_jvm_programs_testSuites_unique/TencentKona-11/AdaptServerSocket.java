



import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;


public class AdaptServerSocket {

    static java.io.PrintStream out = System.out;
    static volatile boolean clientStarted = false;
    static volatile Exception clientException = null;
    static volatile Thread client = null;

    static void startClient(final int port, final int dally)
        throws Exception
    {
        Thread t = new Thread() {
                public void run() {
                    try (Socket so = new Socket()) {
                        out.println("client:  " + so);
                        clientStarted = true;
                        if (dally > 0)
                            Thread.sleep(dally);
                        so.connect(new InetSocketAddress(port));
                        if (Thread.interrupted()) {
                            out.println("client interrupted");
                            return;
                        }
                        out.println("client:  " + so);
                        int a = so.getInputStream().read();
                        out.println("client:  read " + a);
                        a += 1;
                        so.getOutputStream().write(a);
                        out.println("client:  wrote " + a);
                    } catch (Exception x) {
                        if (x instanceof InterruptedException)
                            return;
                        clientException = x;
                        x.printStackTrace();
                    }
                }
            };
        t.setDaemon(true);
        t.start();
        client = t;
    }

    static void test(int clientDally, int timeout, boolean shouldTimeout)
        throws Exception
    {
        boolean needClient = !shouldTimeout;
        client = null;
        clientException = null;
        clientStarted = false;
        out.println();

        try (ServerSocketChannel ssc = ServerSocketChannel.open();
             ServerSocket sso = ssc.socket()) {
            out.println("created: " + ssc);
            out.println("         " + sso);
            if (timeout != 0)
                sso.setSoTimeout(timeout);
            out.println("timeout: " + sso.getSoTimeout());
            sso.bind(null);
            out.println("bound:   " + ssc);
            out.println("         " + sso);
            if (needClient) {
                startClient(sso.getLocalPort(), clientDally);
                while (!clientStarted) {
                    Thread.sleep(20);
                }
            }
            Socket so = null;
            try {
                so = sso.accept();
            } catch (SocketTimeoutException x) {
                if (shouldTimeout)
                    out.println("Accept timed out, as expected");
                else
                    throw x;
            }
            if (shouldTimeout && (so != null))
                throw new Exception("Accept did not time out");

            if (so != null) {
                int a = 42;
                so.getOutputStream().write(a);
                int b = so.getInputStream().read();
                if (b != a + 1)
                    throw new Exception("Read incorrect data");
                out.println("server:  read " + b);
            }
        }
        if (needClient) {
            client.interrupt();
            client.join();
            if (clientException != null)
                throw clientException;
        }
    }

    public static void main(String[] args) throws Exception {
        test(0, 0, false);
        test(50, 5000, false);
        test(500, 50, true);
    }

}
