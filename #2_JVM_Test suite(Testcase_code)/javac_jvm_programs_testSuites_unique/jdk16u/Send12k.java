



import java.net.*;
import java.io.*;

public class Send12k {


    public static void main(String args[]) throws Exception {

         int SEND_SIZE=0;

         if(System.getProperty("os.name").contains("Mac")) {
             SEND_SIZE = 16 * 576;
         } else {
             SEND_SIZE = 16 * 1024;
         }
        InetAddress localHost = InetAddress.getLocalHost();
        DatagramSocket s1 = new DatagramSocket();
        DatagramSocket s2 = new DatagramSocket(0, localHost);

        byte b1[] = new byte[ SEND_SIZE ];
        DatagramPacket p1 = new DatagramPacket(b1, 0, b1.length,
                                               localHost,
                                               s2.getLocalPort());
        boolean sendOkay = true;
        try {
            System.out.println("Sending to: [" + localHost + "]:" + s2.getLocalPort());
            s1.send(p1);
        } catch (IOException e) {
            
            System.out.println("Sending failed: " + e);
            sendOkay = false;
        }

        if (sendOkay) {
            byte b2[] = new byte[ SEND_SIZE * 2];
            DatagramPacket p2 = new DatagramPacket( b2, SEND_SIZE * 2 );
            s2.setSoTimeout(2000);

            try {
                s2.receive(p1);
            } catch (InterruptedIOException ioe) {
                throw new Exception("Datagram not received within timeout");
            }

            if (p1.getLength() != SEND_SIZE) {
                throw new Exception("Received datagram incorrect size");
            }
        }

    }

}
