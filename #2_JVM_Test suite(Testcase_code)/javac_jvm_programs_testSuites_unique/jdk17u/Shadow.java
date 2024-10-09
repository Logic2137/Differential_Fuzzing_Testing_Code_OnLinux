import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class Shadow {

    static PrintStream log = System.err;

    private static void dump(ServerSocket s) {
        log.println("getInetAddress(): " + s.getInetAddress());
        log.println("getLocalPort(): " + s.getLocalPort());
    }

    private static void dump(Socket s) {
        log.println("getInetAddress(): " + s.getInetAddress());
        log.println("getPort(): " + s.getPort());
        log.println("getLocalAddress(): " + s.getLocalAddress());
        log.println("getLocalPort(): " + s.getLocalPort());
    }

    private static int problems = 0;

    private static void problem(String s) {
        log.println("FAILURE: " + s);
        problems++;
    }

    private static void check(Socket s) {
        if (s.getPort() == 0)
            problem("Socket has no port");
        if (s.getLocalPort() == 0)
            problem("Socket has no local port");
        if (!s.getLocalAddress().equals(s.getInetAddress()))
            problem("Socket has wrong local address");
    }

    public static void main(String[] args) throws Exception {
        boolean useChannels = ((args.length == 0) || Boolean.valueOf(args[0]).booleanValue());
        int port = (args.length > 1 ? Integer.parseInt(args[1]) : -1);
        ServerSocket serverSocket;
        if (useChannels) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            log.println("opened ServerSocketChannel: " + serverSocketChannel);
            serverSocket = serverSocketChannel.socket();
            log.println("associated ServerSocket: " + serverSocket);
        } else {
            serverSocket = new ServerSocket();
            log.println("opened ServerSocket: " + serverSocket);
        }
        SocketAddress bindAddr = new InetSocketAddress((port == -1) ? 0 : port);
        serverSocket.bind(bindAddr);
        log.println("bound ServerSocket: " + serverSocket);
        log.println();
        Socket socket;
        if (useChannels) {
            SocketChannel socketChannel = SocketChannel.open();
            log.println("opened SocketChannel: " + socketChannel);
            socket = socketChannel.socket();
            log.println("associated Socket: " + socket);
        } else {
            socket = new Socket();
            log.println("opened Socket: " + socket);
        }
        SocketAddress connectAddr = new InetSocketAddress(InetAddress.getLoopbackAddress(), serverSocket.getLocalPort());
        socket.connect(connectAddr);
        log.println("connected Socket: " + socket);
        log.println();
        Socket acceptedSocket = serverSocket.accept();
        log.println("accepted Socket: " + acceptedSocket);
        log.println();
        log.println("========================================");
        log.println("*** ServerSocket info: ");
        dump(serverSocket);
        log.println();
        log.println("*** client Socket info: ");
        dump(socket);
        check(socket);
        log.println();
        log.println("*** accepted Socket info: ");
        dump(acceptedSocket);
        check(acceptedSocket);
        log.println();
        if (problems > 0)
            throw new Exception(problems + " tests failed");
    }
}
