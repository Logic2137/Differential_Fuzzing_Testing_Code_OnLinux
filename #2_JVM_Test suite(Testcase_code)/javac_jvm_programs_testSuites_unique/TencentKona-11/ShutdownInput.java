



import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ShutdownInput {
    static boolean failed = false;

    public static void main(String args[]) throws Exception {
        InetAddress iaddr = InetAddress.getLocalHost();

        try ( ServerSocket ss = new ServerSocket(0);
              Socket s1 = new Socket(iaddr, ss.getLocalPort());
              Socket s2 = ss.accept() ) {

            test(s1, s2, "Testing NET");
        }

        
        try (ServerSocketChannel sc = ServerSocketChannel.open().bind(null);
             SocketChannel s1 = SocketChannel.open(
                     new InetSocketAddress(iaddr, sc.socket().getLocalPort()));
             SocketChannel s2 = sc.accept() ) {

            test(s1.socket(), s2.socket(), "Testing NIO");
        }

        if (failed) {
            throw new RuntimeException("Failed: check output");
        }
    }

    public static void test(Socket s1, Socket s2, String mesg) throws Exception {
        OutputStream os = s1.getOutputStream();
        os.write("This is a message".getBytes("US-ASCII"));

        InputStream in = s2.getInputStream();
        s2.shutdownInput();

        if (in.available() != 0) {
            failed = true;
            System.out.println(mesg + ":" + s2 + " in.available() should be 0, " +
                               "but returns "+ in.available());
        }

        byte[] ba = new byte[2];
        if (in.read() != -1 ||
            in.read(ba) != -1 ||
            in.read(ba, 0, ba.length) != -1) {

            failed = true;
            System.out.append(mesg + ":" + s2 + " in.read() should be -1");
        }
    }
}
