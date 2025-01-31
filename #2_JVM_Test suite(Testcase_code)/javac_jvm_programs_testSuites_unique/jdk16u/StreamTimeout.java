



import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class StreamTimeout {
    static final PrintStream log = System.err;
    static String charset = "US-ASCII";

    private static class Client extends Thread implements Closeable {
        private final Socket so;

        Client(int port) throws IOException {
            so = new Socket(InetAddress.getLoopbackAddress(), port);
        }

        @Override
        public void run() {
            try {
                Writer wr = new OutputStreamWriter(so.getOutputStream(),
                        charset);
                wr.write("ab");
                wr.flush();
            } catch (IOException x) {
                log.print("Unexpected exception in writer: ");
                x.printStackTrace();
            }
        }

        @Override
        public void close() throws IOException {
            so.close();
        }
    }

    private static void gobble(InputStream is, Reader rd,
            int ec, boolean force)
                    throws Exception
                    {
        int a = is.available();
        boolean r = rd.ready();
        log.print("" + a + " bytes available, "
                + "reader " + (r ? "" : "not ") + "ready");
        if (!r && !force) {
            log.println();
            return;
        }
        int c;
        try {
            c = rd.read();
        } catch (InterruptedIOException x) {
            log.println();
            throw x;
        }
        log.println(", read() ==> "
                + (c >= 0 ? ("'" + (char)c + "'" ): "EOF"));
        if (c != ec)
            throw new Exception("Incorrect value read: Expected "
                    + ec + ", read " + (char)c);
    }

    public static void main(String[] args) throws Exception {

        if (args.length > 0)
            charset = args[0];

        try(ServerSocket ss = new ServerSocket(0);
            Client cl = new Client(ss.getLocalPort())) {

            cl.start();

            try(Socket s = ss.accept()) {
                s.setSoTimeout(150);

                try(InputStream is = s.getInputStream();
                    Reader rd = new InputStreamReader(is, charset)) {

                    while (is.available() <= 0)
                        Thread.yield();

                    gobble(is, rd, 'a', false);
                    gobble(is, rd, 'b', false);
                    gobble(is, rd, -1, false);

                    boolean caught = false;
                    try {
                        gobble(is, rd, -1, true);
                    } catch (InterruptedIOException e) {
                        log.println("Read timed out, as expected");
                        caught = true;
                    }
                    if (!caught) {
                        log.println("Read did not time out, test inapplicable");
                        return;
                    }

                    caught = false;
                    try {
                        gobble(is, rd, -1, true);
                    } catch (InterruptedIOException x) {
                        log.println("Second read timed out, as expected");
                        caught = true;
                    }
                    if (!caught)
                        throw new Exception("Second read completed");
                }
            }

            cl.join();
        }
    }
}
