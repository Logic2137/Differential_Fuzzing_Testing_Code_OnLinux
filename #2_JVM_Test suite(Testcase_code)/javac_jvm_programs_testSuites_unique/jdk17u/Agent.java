import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;

public class Agent {

    public static void agentmain(String args) throws IOException {
        System.out.println("Agent running...");
        int port = Integer.parseInt(args);
        System.out.println("Agent connecting back to Tool....");
        Socket s = new Socket();
        s.connect(new InetSocketAddress(port));
        System.out.println("Agent connected to Tool.");
        s.close();
    }
}
