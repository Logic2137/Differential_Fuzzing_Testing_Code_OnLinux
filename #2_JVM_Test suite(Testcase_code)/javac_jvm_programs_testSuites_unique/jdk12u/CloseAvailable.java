



import java.net.*;
import java.io.*;


public class CloseAvailable {

    public static void main(String[] args) throws Exception {
        testClose();

        testEOF(true);
        testEOF(false);
        testIOEOnClosed(true);
        testIOEOnClosed(false);
    }

    static void testClose() throws IOException {
        boolean error = true;
        InetAddress addr = InetAddress.getLocalHost();
        ServerSocket ss = new ServerSocket(0);
        int port = ss.getLocalPort();

        Thread t = new Thread(new Thread("Close-Available-1") {
            public void run() {
                try {
                    Socket s = new Socket(addr, port);
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        Socket  soc = ss.accept();
        ss.close();

        DataInputStream is = new DataInputStream(soc.getInputStream());
        is.close();

        try {
            is.available();
        }
        catch (IOException ex) {
            error = false;
        }
        if (error)
            throw new RuntimeException("Available() can be called after stream closed.");
    }

    
    
    static void testEOF(boolean readUntilEOF) throws IOException {
        System.out.println("testEOF, readUntilEOF: " + readUntilEOF);
        InetAddress addr = InetAddress.getLoopbackAddress();
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(addr, 0), 0);
        int port = ss.getLocalPort();

        try (Socket s = new Socket(addr, port)) {
            s.getOutputStream().write(0x42);
            s.shutdownOutput();

            try (Socket soc = ss.accept()) {
                ss.close();

                InputStream is = soc.getInputStream();
                int b = is.read();
                assert b == 0x42;
                assert !s.isClosed();
                if (readUntilEOF) {
                    b = is.read();
                    assert b == -1;
                }

                int a;
                for (int i = 0; i < 100; i++) {
                    a = is.available();
                    System.out.print(a + ", ");
                    if (a != 0)
                        throw new RuntimeException("Unexpected non-zero available: " + a);
                }
                assert !s.isClosed();
                assert is.read() == -1;
            }
        }
        System.out.println("\ncomplete");
    }

    
    
    static void testIOEOnClosed(boolean readUntilEOF) throws IOException {
        System.out.println("testIOEOnClosed, readUntilEOF: " + readUntilEOF);
        InetAddress addr = InetAddress.getLoopbackAddress();
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(addr, 0), 0);
        int port = ss.getLocalPort();

        try (Socket s = new Socket(addr, port)) {
            s.getOutputStream().write(0x43);
            s.shutdownOutput();

            try (Socket soc = ss.accept()) {
                ss.close();

                InputStream is = soc.getInputStream();
                int b = is.read();
                assert b == 0x43;
                assert !s.isClosed();
                if (readUntilEOF) {
                    b = is.read();
                    assert b == -1;
                }
                is.close();
                try {
                    b = is.available();
                    throw new RuntimeException("UNEXPECTED successful read: " + b);
                } catch (IOException expected) {
                    System.out.println("caught expected IOException:" + expected);
                }
            }
        }
        System.out.println("\ncomplete");
    }
}
