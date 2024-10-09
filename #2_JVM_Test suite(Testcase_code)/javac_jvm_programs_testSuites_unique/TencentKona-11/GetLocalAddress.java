import java.net.*;

public class GetLocalAddress implements Runnable {

    static ServerSocket ss;

    static InetAddress addr;

    static int port;

    public static void main(String[] args) throws Exception {
        testBindNull();
        boolean error = true;
        int linger = 65546;
        int value = 0;
        addr = InetAddress.getLocalHost();
        ss = new ServerSocket(0);
        port = ss.getLocalPort();
        Thread t = new Thread(new GetLocalAddress());
        t.start();
        Socket soc = ss.accept();
        if (addr.equals(soc.getLocalAddress())) {
            error = false;
        }
        if (error)
            throw new RuntimeException("Socket.GetLocalAddress failed.");
        soc.close();
    }

    public void run() {
        try {
            Socket s = new Socket(addr, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testBindNull() throws Exception {
        try (Socket soc = new Socket()) {
            soc.bind(null);
            if (!soc.isBound())
                throw new RuntimeException("should be bound after bind(null)");
            if (soc.getLocalPort() <= 0)
                throw new RuntimeException("bind(null) failed, local port: " + soc.getLocalPort());
            if (soc.getLocalAddress() == null)
                throw new RuntimeException("bind(null) failed, local address is null");
        }
    }
}
