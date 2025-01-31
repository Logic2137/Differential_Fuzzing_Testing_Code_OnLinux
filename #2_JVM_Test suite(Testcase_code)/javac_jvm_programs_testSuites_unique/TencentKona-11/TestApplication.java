


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestApplication {
    public static void main(String[] args) throws IOException {
        
        if (args.length > 0 && args[0].equals("-exit")) {
            return;
        }

        
        ServerSocket ss = new ServerSocket(0);
        int port = ss.getLocalPort();

        
        System.out.println("port:" + port);
        System.out.println("waiting for the manager ...");
        System.out.flush();

        
        Socket s = ss.accept();
        s.close();
        ss.close();
    }
}
