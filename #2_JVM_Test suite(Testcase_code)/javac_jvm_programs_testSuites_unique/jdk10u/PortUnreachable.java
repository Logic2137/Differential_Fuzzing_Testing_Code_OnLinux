


import java.net.BindException;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class PortUnreachable {

    DatagramSocket clientSock;
    int serverPort;
    int clientPort;

    public void serverSend() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            Thread.currentThread().sleep(1000);
            
            
            byte b[] = "A late msg".getBytes();
            DatagramPacket packet = new DatagramPacket(b, b.length, addr,
                                                       serverPort);
            clientSock.send(packet);

            DatagramSocket sock = recreateServerSocket(serverPort);
            b = "Greetings from the server".getBytes();
            packet = new DatagramPacket(b, b.length, addr, clientPort);
            sock.send(packet);
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DatagramSocket recreateServerSocket (int serverPort) throws Exception {
        DatagramSocket serverSocket = null;
        int retryCount = 0;
        System.out.println("Attempting to recreate server socket with port: " +
                serverPort);
        while (serverSocket == null) {
            try {
                serverSocket = new DatagramSocket(serverPort);
            } catch (BindException bEx) {
                if (retryCount++ < 5) {
                    Thread.sleep(500);
                } else {
                    System.out.println("Give up after 5 retries");
                    throw bEx;
                }
            }
        }

        System.out.println("PortUnreachableTest.recreateServerSocket: returning socket == "
                + serverSocket.getLocalAddress() + ":" + serverSocket.getLocalPort());
        return serverSocket;
    }

    PortUnreachable() throws Exception {

        clientSock = new DatagramSocket();
        clientPort = clientSock.getLocalPort();

    }

    void execute () throws Exception{

        
        DatagramSocket sock2 = new DatagramSocket();
        serverPort = sock2.getLocalPort();

        
        
        
        InetAddress addr = InetAddress.getLocalHost();
        byte b[] = "Hello me".getBytes();
        DatagramPacket packet = new DatagramPacket(b, b.length, addr,
                                                   serverPort);
        
        sock2.close();
        for (int i=0; i<100; i++)
            clientSock.send(packet);

        serverSend();
        
        b = new byte[25];
        packet = new DatagramPacket(b, b.length, addr, serverPort);
        clientSock.setSoTimeout(10000);
        clientSock.receive(packet);
        System.out.println("client received data packet " + new String(packet.getData()));

        
        clientSock.close();
    }

    public static void main(String[] args) throws Exception {
        PortUnreachable test = new PortUnreachable();
        test.execute();
    }

}

