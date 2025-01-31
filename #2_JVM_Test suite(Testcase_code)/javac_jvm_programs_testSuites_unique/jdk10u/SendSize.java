



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendSize {
    static final int bufferLength = 512;
    static final int packetLength = 256;

    public static void main(String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket();
        new ServerThread(serverSocket).start();
        new ClientThread(serverSocket.getLocalPort()).start();
    }

    static class ServerThread extends Thread {
        DatagramSocket server;

        ServerThread(DatagramSocket server) {
            this.server = server;
        }

        public void run() {
            try {
                System.err.println("started server thread: " + server);
                byte[] buf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buf,
                                                                  buf.length);
                server.receive(receivePacket);
                int len = receivePacket.getLength();
                switch(len) {
                case packetLength:
                    System.out.println("receive length is: " + len);
                    break;
                default:
                    throw new RuntimeException(
                        "receive length is: " + len +
                        ", send length: " + packetLength +
                        ", buffer length: " + buf.length);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("caugth: " + e);
            } finally {
                if (server != null) { server.close(); }
            }
        }
    }

    static class ClientThread extends Thread {

        int serverPort;
        DatagramSocket client;
        InetAddress host;

        ClientThread(int serverPort)throws IOException {
            this.serverPort = serverPort;
            this.host = InetAddress.getLocalHost();
            this.client = new DatagramSocket();
        }

        public void run() {
            try {
                System.err.println("started client thread: " + client);
                byte[] buf = new byte[bufferLength];
                DatagramPacket sendPacket =
                    new DatagramPacket(buf, packetLength,
                                       host, serverPort);
                for (int i = 0; i < 10; i++) {
                    client.send(sendPacket);
                }
                System.err.println("sent 10 packets");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("caught: " + e);
            } finally {
                if (client != null) { client.close(); }
            }
        }
    }
}
