



import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketGrowth {

    public static void main(String[] args) throws IOException {
        InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
        try (ServerSocket ss = new ServerSocket(0, 0, loopbackAddress)) {
            try (Socket s = new Socket(loopbackAddress, ss.getLocalPort());
                    Socket peer = ss.accept()) {
                for (int i=0; i<1000000; i++) {
                    
                    s.getOutputStream();
                    
                    s.getInputStream();
                    if (i % 100000 == 0) System.out.println(i);
                }
            }
        }
    }
}
