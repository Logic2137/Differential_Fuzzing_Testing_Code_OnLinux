



import java.net.*;
import java.util.*;
import java.io.InterruptedIOException;

public class SendDatagramToBadAddress {

    static boolean debug = false;

    public static boolean OSsupportsFeature () {
        Properties p = System.getProperties ();
        String v;
        if (p.getProperty ("os.name").equals ("Windows 2000"))
            return (true);
        if (p.getProperty ("os.name").equals ("Linux"))
            return (true);
        if (p.getProperty ("os.name").startsWith ("Mac OS"))
            return (true);
        
        v = p.getProperty ("os.arch");
        if (!v.equalsIgnoreCase ("sparc"))
            return (false);
        v = p.getProperty ("os.name");
        if (!v.equalsIgnoreCase ("Solaris") && !v.equalsIgnoreCase ("SunOS"))
            return (false);
        v = p.getProperty ("os.version");
        if (v.equals ("5.8") || v.equals ("8"))
            return (false);
        return (true);
    }

    static void print (String s) {
        if (debug)
            System.out.println (s);
    }

    class Server {

        DatagramSocket server;
        byte[] buf = new byte [128];
        DatagramPacket pack = new DatagramPacket (buf, buf.length);

        public Server (DatagramSocket s) {
            server = s;
        }

        public void receive (int loop, boolean expectError) throws Exception {
            for (int i=0; i<loop; i++) {
                try {
                    server.receive (pack);
                } catch (Exception e) {
                    if (expectError) {
                        print ("Got expected error: " + e);
                        continue;
                    } else {
                        print ("Got: " + new String (pack.getData()));
                        print ("Expected: " + new String (buf));
                        throw new Exception ("Error reading data: Iter " +i);
                    }
                }
                String s1 = "Hello, server"+i;
                byte[] buf      = s1.getBytes();
                if (!s1.equals (new String (pack.getData(),
                                            pack.getOffset(),pack.getLength()))) {
                    print ("Got: " + new String (pack.getData()));
                    print ("Expected: " + new String (buf));
                    throw new Exception ("Error comparing data: Iter " +i);
                }
            }
        }
    };

    public static void main (String args[]) throws Exception {
        if (args.length >=1 && args[0].equals ("-d")) {
            debug = true;
        }
        SendDatagramToBadAddress ud = new SendDatagramToBadAddress ();
        ud.run ();
    }

    public void run() throws Exception {
        if (OSsupportsFeature()) {
            print ("running on OS that supports ICMP port unreachable");
        }
        try (DatagramSocket sock = new DatagramSocket()) {
            test(sock);
        }
    }

    private void test(DatagramSocket sock) throws Exception {
        print("Testing with " + sock.getClass());
        InetAddress addr = InetAddress.getLoopbackAddress();
        DatagramSocket serversock = new DatagramSocket(0);
        DatagramPacket p;
        byte[] buf;
        int port = serversock.getLocalPort ();
        final int loop = 5;
        Server s = new Server (serversock);
        int i;

        print ("Checking send to connected address ...");
        sock.connect(addr, port);

        for (i = 0; i < loop; i++) {
            try {
                buf = ("Hello, server"+i).getBytes();
                if (i % 2 == 1)
                    p = new DatagramPacket(buf, buf.length, addr, port);
                else
                    p = new DatagramPacket(buf, buf.length);
                sock.send(p);
            } catch (Exception ex) {
                print ("Got unexpected exception: " + ex);
                throw new Exception ("Error sending data: ");
            }
        }

        s.receive (loop, false);

        

        print ("Checking send to non-connected address ...");
        sock.disconnect ();
        buf = ("Hello, server"+0).getBytes();
        p = new DatagramPacket(buf, buf.length, addr, port);
        sock.send (p);
        s.receive (1, false);

        
        

        print ("Checking send to invalid address ...");
        sock.connect(addr, port);
        serversock.close ();
        try {
            sock.setSoTimeout (4000);
        } catch (Exception e) {
            print ("could not set timeout");
            throw e;
        }

        boolean goterror = false;

        for (i = 0; i < loop; i++) {
            try {
                buf = ("Hello, server"+i).getBytes();
                p = new DatagramPacket(buf, buf.length, addr, port);
                sock.send(p);
                p = new DatagramPacket(buf, buf.length, addr, port);
                sock.receive (p);
            } catch (InterruptedIOException ex) {
                print ("socket timeout");
            } catch (Exception ex) {
                print ("Got expected exception: " + ex);
                goterror = true;
            }
        }

        if (!goterror && OSsupportsFeature ()) {
            print ("Didnt get expected exception: ");
            throw new Exception ("send did not return expected error");
        }
    }
}
