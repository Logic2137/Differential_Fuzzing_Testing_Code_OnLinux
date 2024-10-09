


import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import static java.lang.System.out;

public class ReceiveISA {

    public static void main(String args[]) throws Exception {

        String regex = "Dia duit![0-2]";

        try (DatagramChannel dc1 = DatagramChannel.open();        
             DatagramChannel dc2 = DatagramChannel.open();        
             DatagramChannel dc3 = DatagramChannel.open();
             DatagramChannel dc4 = DatagramChannel.open()) {      

            dc3.socket().bind((SocketAddress) null); 

            
            InetAddress lh = InetAddress.getLocalHost();
            InetSocketAddress isa = new InetSocketAddress(lh, dc3.socket().getLocalPort());

            ByteBuffer bb = ByteBuffer.allocateDirect(100);
            bb.put("Dia duit!0".getBytes());
            bb.flip();

            ByteBuffer bb1 = ByteBuffer.allocateDirect(100);
            bb1.put("Dia duit!1".getBytes());
            bb1.flip();

            ByteBuffer bb2 = ByteBuffer.allocateDirect(100);
            bb2.put("Dia duit!2".getBytes());
            bb2.flip();

            ByteBuffer bb3 = ByteBuffer.allocateDirect(100);
            bb3.put("garbage".getBytes());
            bb3.flip();

            dc1.send(bb, isa);      
            dc4.send(bb3, isa);     
            dc1.send(bb1, isa);     
            dc2.send(bb2, isa);     


            
            dc3.socket().setSoTimeout(1000);
            ByteBuffer rb = ByteBuffer.allocateDirect(100);
            SocketAddress sa[] = new SocketAddress[3];

            for (int i = 0; i < 3;) {
                SocketAddress receiver = dc3.receive(rb);
                rb.flip();
                byte[] bytes = new byte[rb.limit()];
                rb.get(bytes, 0, rb.limit());
                String msg = new String(bytes);

                if (msg.matches("Dia duit![0-2]")) {
                    if (msg.equals("Dia duit!0")) {
                        sa[0] = receiver;
                        i++;
                    }
                    if (msg.equals("Dia duit!1")) {
                        sa[1] = receiver;
                        i++;
                    }
                    if (msg.equals("Dia duit!2")) {
                        sa[2] = receiver;
                        i++;
                    }
                } else {
                    out.println("Interfered packet sender address is : " + receiver);
                    out.println("random interfered packet is : " + msg);
                }
                rb.clear();
            }

            

            if (!sa[0].equals(sa[1])) {
                throw new Exception("Source address for packets 1 & 2 should be equal");
            }

            if (sa[1].equals(sa[2])) {
                throw new Exception("Source address for packets 2 & 3 should be different");
            }
        }
    }
}
