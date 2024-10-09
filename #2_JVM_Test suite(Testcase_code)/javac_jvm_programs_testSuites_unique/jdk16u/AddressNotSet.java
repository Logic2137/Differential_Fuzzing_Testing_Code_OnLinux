



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

import static java.lang.System.out;

public class AddressNotSet {

    final InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
    final DatagramSocket serversock;
    int i;
    AddressNotSet() throws Exception {
        serversock = new DatagramSocket(0, loopbackAddress);
    }

    public static void main (String args[]) throws Exception {
        new AddressNotSet().run();
    }

    public void run() throws Exception {
        try (var ss = serversock) {
            try (DatagramSocket sock = new DatagramSocket()) {
                test(sock);
            }
            try (DatagramSocket sock = new MulticastSocket()) {
                test(sock);
            }
            try (DatagramSocket sock = DatagramChannel.open().socket()) {
                test(sock);
            }
        }
    }

    private void test(DatagramSocket sock) throws Exception {
        out.println("Testing with " + sock.getClass());
        InetAddress addr = loopbackAddress;
        byte[] buf;
        DatagramPacket p;
        int port = serversock.getLocalPort();
        SocketAddress connectedAddress = serversock.getLocalSocketAddress();

        out.println("Checking send to non-connected address ...");
        try {
            out.println("Checking send with no packet address");
            buf = ("Hello, server"+(++i)).getBytes();
            p = new DatagramPacket(buf, buf.length);
            sock.send(p);
            throw new AssertionError("Expected IllegalArgumentException not received");
        } catch (IllegalArgumentException x) {
            out.println("Got expected exception: " + x);
        }

        out.println("Checking send to valid address");
        buf = ("Hello, server"+(++i)).getBytes();
        p = new DatagramPacket(buf, buf.length, addr, port);
        sock.send(p);
        serversock.receive(p);

        out.println("Connecting to server address: " + connectedAddress);
        sock.connect(connectedAddress);

        try {
            out.println("Checking send with different address than connected");
            buf = ("Hello, server"+(++i)).getBytes();
            p = new DatagramPacket(buf, buf.length, addr, port+1);
            sock.send(p);
            throw new AssertionError("Expected IllegalArgumentException not received");
        } catch (IllegalArgumentException x) {
            out.println("Got expected exception: " + x);
        }

        out.println("Checking send to valid address");
        buf = ("Hello, server"+(++i)).getBytes();
        p = new DatagramPacket(buf, buf.length, addr, port);
        sock.send(p);
        serversock.receive(p);

        if (sock instanceof MulticastSocket) {
            sock.disconnect();
            testTTL((MulticastSocket)sock);
        }
    }

    private void testTTL(MulticastSocket sock) throws Exception {
        out.println("Testing deprecated send TTL with " + sock.getClass());
        final byte ttl = 100;
        InetAddress addr = loopbackAddress;
        byte[] buf;
        DatagramPacket p;
        int port = serversock.getLocalPort();

        out.println("Checking send to non-connected address ...");
        try {
            out.println("Checking send with no packet address");
            buf = ("Hello, server"+(++i)).getBytes();
            p = new DatagramPacket(buf, buf.length);
            sock.send(p,ttl);
            throw new AssertionError("Expected IllegalArgumentException not received");
        } catch (IllegalArgumentException x) {
            out.println("Got expected exception: " + x);
        }

        out.println("Connecting to connected address: " + sock);
        sock.connect(addr, port);

        try {
            out.println("Checking send with different address than connected");
            buf = ("Hello, server"+(++i)).getBytes();
            p = new DatagramPacket(buf, buf.length, addr, port+1);
            sock.send(p, ttl);
            throw new AssertionError("Expected IllegalArgumentException not received");
        } catch (IllegalArgumentException x) {
            out.println("Got expected exception: " + x);
        }
    }
}
