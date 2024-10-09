

import java.net.*;
import java.io.*;
import java.util.*;

public class Tests {

    static final boolean isWindows =
            System.getProperty("os.name").startsWith("Windows");
    static final boolean isMacOS =
            System.getProperty("os.name").contains("OS X");

    
    public static void simpleDataExchange (Socket s1, Socket s2)
        throws Exception {

        InputStream i1 = s1.getInputStream();
        InputStream i2 = s2.getInputStream();
        OutputStream o1 = s1.getOutputStream();
        OutputStream o2 = s2.getOutputStream();

        startSimpleWriter("SimpleWriter-1", o1, 100);
        startSimpleWriter("SimpleWriter-2", o2, 200);
        simpleRead (i2, 100);
        simpleRead (i1, 200);
    }

    static void startSimpleWriter(String threadName, final OutputStream os, final int start) {
        (new Thread(new Runnable() {
            public void run() {
                try { simpleWrite(os, start); }
                catch (Exception e) {unexpected(e); }
            }}, threadName)).start();
    }

    static void unexpected(Exception e ) {
        System.out.println("Unexcepted Exception: " + e);
        e.printStackTrace();
    }

    
    public static void simpleDataExchange (DatagramSocket s1, InetAddress ia1,
                                           DatagramSocket s2, InetAddress ia2)
        throws Exception {

        SocketAddress dest1 = new InetSocketAddress (ia1, s1.getLocalPort());
        dprintln ("dest1 = " + dest1);
        SocketAddress dest2 = new InetSocketAddress (ia2, s2.getLocalPort());
        dprintln ("dest2 = " + dest2);

        byte[] ba = "Hello world".getBytes();
        byte[] bb = "HELLO WORLD1".getBytes();
        DatagramPacket p1 = new DatagramPacket (ba, ba.length, dest1);
        DatagramPacket p2 = new DatagramPacket (ba, ba.length, dest2);

        DatagramPacket r1 = new DatagramPacket (new byte[256], 256);
        DatagramPacket r2 = new DatagramPacket (new byte[256], 256);

        s2.send (p1);
        s1.send (p2);
        s1.receive (r1);
        s2.receive (r2);
        comparePackets (p1, r1);
        comparePackets (p2, r2);
    }

    

    public static void datagramEcho (DatagramSocket s1, DatagramSocket s2,
                                     InetAddress ia2)
        throws Exception {

        byte[] ba = "Hello world".getBytes();
        DatagramPacket p1;

        SocketAddress dest2 = null;
        if (ia2 != null) {
            dest2 = new InetSocketAddress (ia2, s2.getLocalPort());
            p1 = new DatagramPacket (ba, ba.length, dest2);
        } else {
            p1 = new DatagramPacket (ba, ba.length);
        }

        dprintln ("dest2 = " + dest2);


        DatagramPacket r1 = new DatagramPacket (new byte[256], 256);
        DatagramPacket r2 = new DatagramPacket (new byte[256], 256);

        s1.send (p1);
        s2.receive (r1);
        s2.send (r1);
        s1.receive (r2);
        comparePackets (p1, r1);
        comparePackets (p1, r2);
    }

    public static void comparePackets (DatagramPacket p1, DatagramPacket p2)
        throws Exception {

        byte[] b1 = p1.getData();
        byte[] b2 = p2.getData();
        int len = p1.getLength () > p2.getLength() ? p2.getLength()
                                                   : p1.getLength();
        for (int i=0; i<len; i++) {
            if (b1[i] != b2[i]) {
                throw new Exception ("packets not the same");
            }
        }
    }

    
    public static void checkTime (long got, long expected) {
        checkTime(got, expected, expected);
    }

    
    public static void checkTime(long got, long start, long end) {
        dprintln("checkTime: got = " + got + " start = " + start + " end = " + end);
        long upper = end + (end / 2);
        long lower = start - (start / 2);
        if (got > upper || got < lower) {
            throw new RuntimeException("checkTime failed: got " + got
                    + ", expected between " + start + " and " + end);
        }
    }

    static boolean debug = false;

    public static void checkDebug (String[] args) {
        debug = args.length > 0 && args[0].equals("-d");
    }

    public static void dprint (String s) {
        if (debug) {
            System.out.print (s);
        }
    }

    public static void dprintln (String s) {
        if (debug) {
            System.out.println (s);
        }
    }

    static int numberInterfaces () {
        try {
            Enumeration ifs = NetworkInterface.getNetworkInterfaces();
            int nifs=0;
            while (ifs.hasMoreElements()) {
                nifs++;
                ifs.nextElement();
            }
            return nifs;
        } catch (SocketException e) {
            return 0;
        }
    }

    public static Enumeration ipv4Addresses() {
        return new AddrEnum (Inet4Address.class);
    }

    public static Inet4Address getFirstLocalIPv4Address () {
        Enumeration e = ipv4Addresses();
        if (!e.hasMoreElements()) {
            return null;
        }
        return (Inet4Address)e.nextElement();
    }

    public static Inet6Address getFirstLocalIPv6Address () {
        Enumeration e = ipv6Addresses();
        if (!e.hasMoreElements()) {
            return null;
        }
        return (Inet6Address)e.nextElement();
    }

    public static Enumeration ipv6Addresses() {
        return new AddrEnum (Inet6Address.class);
    }

    

    private static class AddrEnum implements Enumeration {

        Enumeration ifs;
        NetworkInterface currIf = null;
        InetAddress nextAddr=null;
        Enumeration addrs=null;
        Class filter;

        static final byte[] fe80_loopback = new byte [] {
            (byte)0xfe,(byte)0x80,0,0,0,0,0,0,0,0,0,0,0,0,0,1
        };

        AddrEnum (Class filter) {
            this.filter = filter;
            try {
                ifs = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {}
        }

        public boolean hasMoreElements () {
            if (nextAddr == null) {
                nextAddr = getNext();
            }
            return (nextAddr != null);
        }

        public Object nextElement () {
            if (!hasMoreElements()) {
                throw new NoSuchElementException ("no more addresses");
            }
            Object next = nextAddr;
            nextAddr = null;
            return next;
        }

        private InetAddress getNext() {
            while (true) {
                if (currIf == null) {
                    currIf = getNextIf();
                    if (currIf == null) {
                        return null;
                    }
                    addrs = currIf.getInetAddresses();
                }
                while (addrs.hasMoreElements()) {
                    InetAddress addr = (InetAddress) addrs.nextElement();
                    if (filter.isInstance (addr) && !addr.isLoopbackAddress()
                            && !addr.isAnyLocalAddress()) {
                        if (Arrays.equals (addr.getAddress(), fe80_loopback)) {
                            continue;
                        }
                        return addr;
                    }
                }
                currIf = null;
            }
        }

        private NetworkInterface getNextIf () {
            if (ifs != null) {
                while (ifs.hasMoreElements()) {
                    NetworkInterface nic = (NetworkInterface)ifs.nextElement();
                    
                    if (isWindows) {
                        String dName = nic.getDisplayName();
                        if (dName != null && dName.contains("Teredo"))
                            continue;
                    } else if (isMacOS && nic.getName().contains("awdl")) {
                        continue;
                    }
                    try {
                        if (nic.isUp() && !nic.isLoopback())
                            return nic;
                    } catch (SocketException e) {
                        
                    }
                }
            }

            return null;
        }
    }

    
    public static void t_assert (boolean assertion) {
        if (assertion) {
            return;
        }
        Throwable t = new Throwable();
        StackTraceElement[] strace = t.getStackTrace();
        String msg = "Assertion failed at: " + strace[1].toString();
        throw new RuntimeException (msg);
    }

    private static void simpleRead (InputStream is, int start) throws Exception {
        byte b[] = new byte [2];
        for (int i=start; i<start+100; i++) {
            int x = is.read (b);
            if (x == 1) {
                x += is.read (b,1,1);
            }
            if (x!=2) {
                throw new Exception ("read error");
            }
            int r = bytes (b[0], b[1]);
            if (r != i) {
                throw new Exception ("read " + r + " expected " +i);
            }
        }
    }

    
    private static int bytes (byte b1, byte b2) {
        int i1 = (int)b1 & 0xFF;
        int i2 = (int)b2 & 0xFF;
        return i1 * 256 + i2;
    }

    static void simpleWrite (OutputStream os, int start) throws Exception {
        byte b[] = new byte [2];
        for (int i=start; i<start+100; i++) {
            b[0] = (byte) (i / 256);
            b[1] = (byte) (i % 256);
            os.write (b);
        }
    }

    private static class Runner extends Thread {
        Runnable runnee;
        long delay;

        Runner (Runnable runnee, long delay) {
            super();
            this.runnee = runnee;
            this.delay = delay;
        }

        public void run () {
            try {
                Thread.sleep (delay);
                runnee.run ();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    public static void runAfter (long millis, Runnable runnee) {
        Runner runner = new Runner (runnee, millis);
        runner.start ();
    }

    static String osname;

    static {
        osname = System.getProperty ("os.name");
    }

    static boolean isLinux () {
        return osname.equals ("Linux");
    }

    static boolean isWindows () {
        return osname.startsWith ("Windows");
    }
}
