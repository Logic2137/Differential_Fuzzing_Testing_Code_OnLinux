import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class ReuseAddressTest {

    String getInfo(DatagramSocket soc) {
        if (soc == null) {
            return null;
        }
        return "localPort: " + soc.getLocalPort() + "; localAddress: " + soc.getLocalAddress() + "; remotePort: " + soc.getPort() + "; remoteAddress: " + soc.getInetAddress() + "; isClosed: " + soc.isClosed() + "; isBound: " + soc.isBound();
    }

    static InetSocketAddress createSocketAddress(int testMcastPort) throws Exception {
        InetAddress localAddress = InetAddress.getLocalHost();
        InetSocketAddress localSocketAddress = new InetSocketAddress(localAddress, testMcastPort);
        return localSocketAddress;
    }

    public static void main(String[] argv) throws Exception {
        ReuseAddressTest test = new ReuseAddressTest();
        test.DatagramSocket0029();
        test.DatagramSocket0030();
        test.DatagramSocket0031();
        test.DatagramSocket0032();
        test.DatagramSocket0034();
        test.DatagramSocket0035();
        test.DatagramSocket2028();
        test.DatagramSocket2029();
        test.DatagramSocket2030();
    }

    public void DatagramSocket0029() throws Exception {
        String testCaseID = "DatagramSocket0029";
        System.out.println(" >> " + testCaseID + ": " + "public void setReuseAddress(boolean on) throws SocketException");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(null);
            ds.setReuseAddress(false);
            if (ds.getReuseAddress() == true) {
                throw new RuntimeException("SO_REUSEADDR is not set to false");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected: " + e);
        } catch (SecurityException e) {
            System.out.println("Security restriction");
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket0030() throws Exception {
        String testCaseID = "DatagramSocket0030";
        System.out.println(" >> " + testCaseID + ": " + "public void setReuseAddress(boolean on) throws SocketException");
        MulticastSocket ms1 = null;
        MulticastSocket ms2 = null;
        try {
            InetSocketAddress addr = createSocketAddress(0);
            ms1 = new MulticastSocket(null);
            ms1.setReuseAddress(true);
            if (!ms1.getReuseAddress()) {
                System.out.println("Cannot check: " + " safety for SO_REUSEADDR option is not guaranteed");
            }
            try {
                ms1.bind(addr);
            } catch (SocketException e) {
                throw new RuntimeException("cannot bind first socket to " + addr + " unexpected " + e);
            }
            addr = createSocketAddress(ms1.getLocalPort());
            ms2 = new MulticastSocket(null);
            ms2.setReuseAddress(true);
            if (!ms2.getReuseAddress()) {
                System.out.println("Cannot check: " + " safety for SO_REUSEADDR option is not guaranteed");
            }
            try {
                ms2.bind(addr);
            } catch (SocketException e) {
                throw new RuntimeException("cannot bind second socket to " + addr + " unexpected " + e);
            }
            if (ms1.getLocalPort() != addr.getPort() || !ms1.isBound() || ms2.getLocalPort() != addr.getPort() || !ms2.isBound()) {
                System.out.println("bind() fails with: " + addr);
                System.out.println("  ms1 [" + getInfo(ms1) + "]");
                System.out.println("  ms2 [" + getInfo(ms2) + "]");
                System.out.println("  getReuseAddress(): " + ms2.getReuseAddress());
                throw new RuntimeException("bind() fails with: " + addr);
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected: " + e);
        } catch (SecurityException e) {
            System.out.println("Security restriction");
        } finally {
            if (ms1 != null) {
                ms1.close();
            }
            if (ms2 != null) {
                ms2.close();
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket0031() throws Exception {
        String testCaseID = "DatagramSocket0031";
        System.out.println(" >> " + testCaseID + ": " + "public void setReuseAddress(boolean on) throws SocketException");
        MulticastSocket ms1 = null;
        MulticastSocket ms2 = null;
        try {
            InetSocketAddress addr = createSocketAddress(0);
            ms1 = new MulticastSocket(null);
            try {
                ms1.bind(addr);
            } catch (SocketException e) {
                throw new RuntimeException("cannot bind first socket to " + addr + " unexpected " + e);
            }
            addr = createSocketAddress(ms1.getLocalPort());
            ms2 = new MulticastSocket(null);
            ms2.setReuseAddress(false);
            try {
                ms2.bind(addr);
                System.out.println("No exceptions: ");
                System.out.println("  addr: " + addr);
                System.out.println("  ms1 [" + getInfo(ms1) + "]");
                System.out.println("  ms2 [" + getInfo(ms2) + "]");
                System.out.println("  getReuseAddress(): " + ms2.getReuseAddress());
                throw new RuntimeException("no exceptions from bind() with " + addr);
            } catch (SocketException e) {
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected: " + e);
        } catch (SecurityException e) {
            System.out.println("Security restriction");
        } finally {
            if (ms1 != null) {
                ms1.close();
            }
            if (ms2 != null) {
                ms2.close();
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket0032() throws Exception {
        String testCaseID = "DatagramSocket0032";
        System.out.println(" >> " + testCaseID + ": " + "public void setReuseAddress(boolean on) throws SocketException");
        DatagramSocket ds1 = null;
        DatagramSocket ds2 = null;
        try {
            InetSocketAddress isa1 = createSocketAddress(0);
            InetAddress addr = isa1.getAddress();
            InetAddress wildcard = InetAddress.getByName("0.0.0.0");
            if (addr.equals(wildcard) || addr.isLoopbackAddress()) {
                System.out.println("Cannot check: addresses are equal");
            }
            ds1 = new DatagramSocket(null);
            ds1.setReuseAddress(true);
            if (!ds1.getReuseAddress()) {
                System.out.println("Cannot check: " + " safety for SO_REUSEADDR option is not guaranteed");
            }
            ds1.bind(isa1);
            InetSocketAddress isa2 = new InetSocketAddress(wildcard, ds1.getLocalPort());
            ds2 = new DatagramSocket(null);
            ds2.setReuseAddress(true);
            if (!ds2.getReuseAddress()) {
                System.out.println("Cannot check: " + " safety for SO_REUSEADDR option is not guaranteed");
            }
            try {
                ds2.bind(isa2);
            } catch (SocketException e) {
                throw new RuntimeException("cannot bind second socket to " + isa2 + " unexpected " + e);
            }
            if (ds1.getLocalPort() != ds2.getLocalPort() || !ds1.isBound() || !ds2.isBound()) {
                System.out.println("bind() fails with: " + addr);
                System.out.println("  ds1 [" + getInfo(ds1) + "]");
                System.out.println("  ds2 [" + getInfo(ds2) + "]");
                System.out.println("  getReuseAddress(): " + ds2.getReuseAddress());
                throw new RuntimeException("bind() fails with: " + addr);
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected: " + e);
        } catch (SecurityException e) {
            System.out.println("Security restriction");
        } finally {
            if (ds1 != null) {
                ds1.close();
            }
            if (ds2 != null) {
                ds2.close();
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket2028() throws Exception {
        String testCaseID = "DatagramSocket2028";
        System.out.println(" >> " + testCaseID + ": " + "public int getTrafficClass() throws SocketException");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            int tc = ds.getTrafficClass();
            if (tc < 0 || tc > 255) {
                throw new RuntimeException("getTrafficClass() returns: " + tc);
            }
        } catch (SecurityException e) {
            System.out.println("Security restriction: " + e);
        } catch (SocketException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("Unexpected exception : " + e);
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket2029() throws Exception {
        String testCaseID = "DatagramSocket2029";
        System.out.println(" >> " + testCaseID + ": " + "public void setTrafficClass(int tc) throws SocketException");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
        } catch (SecurityException e) {
            System.out.println("Security restriction: " + e);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("cannot create socket: " + e);
        }
        int[] values = { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1000, -2, -1, 256, 257, 1000, 50000, Integer.MAX_VALUE - 1, Integer.MAX_VALUE };
        for (int i = 0; i < values.length; i++) {
            try {
                ds.setTrafficClass(values[i]);
                System.out.println("No exception with: " + values[i]);
                System.out.println("getTrafficClass() returns: " + ds.getTrafficClass());
                ds.close();
                throw new RuntimeException("setTrafficClass() fails with : " + values[i]);
            } catch (SocketException e) {
                ds.close();
                e.printStackTrace(System.out);
                throw new RuntimeException("setTrafficClass() throws : " + e);
            } catch (IllegalArgumentException e) {
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket2030() throws Exception {
        String testCaseID = "DatagramSocket2030";
        System.out.println(" >> " + testCaseID + ": " + "public void setTrafficClass(int tc) throws SocketException");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
        } catch (SecurityException e) {
            System.out.println("Security restriction: " + e);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("cannot create socket: " + e);
        }
        for (int i = 0; i <= 255; i++) {
            try {
                ds.setTrafficClass(i);
            } catch (SocketException e) {
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket0034() throws Exception {
        String testCaseID = "DatagramSocket0034";
        System.out.println(" >> " + testCaseID + ": " + "public void setBroadcast(boolean on) throws SocketException");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            ds.setBroadcast(false);
            if (ds.getBroadcast() == true) {
                throw new RuntimeException("SO_BROADCAST is not set to false");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected: " + e);
        } catch (SecurityException e) {
            System.out.println("Security restriction");
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        System.out.println("OKAY");
    }

    public void DatagramSocket0035() throws Exception {
        String testCaseID = "DatagramSocket0035";
        System.out.println(" >> " + testCaseID + ": " + "public void setBroadcast(boolean on) throws SocketException");
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            ds.setBroadcast(true);
            if (ds.getBroadcast() == false) {
                throw new RuntimeException("SO_BROADCAST is not set to true");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected: " + e);
        } catch (SecurityException e) {
            System.out.println("Security restriction");
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        System.out.println("OKAY");
    }
}
