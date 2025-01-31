



import java.lang.reflect.Method;
import java.net.*;
import java.util.*;

public class OptionsTest {

    static class Test {
        Test(SocketOption<?> option, Object testValue) {
            this.option = option;
            this.testValue = testValue;
        }
        static Test create (SocketOption<?> option, Object testValue) {
            return new Test(option, testValue);
        }
        Object option;
        Object testValue;
    }

    
    
    

    static Test[] socketTests = new Test[] {
        Test.create(StandardSocketOptions.SO_KEEPALIVE, Boolean.TRUE),
        Test.create(StandardSocketOptions.SO_SNDBUF, Integer.valueOf(10 * 100)),
        Test.create(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(8 * 100)),
        Test.create(StandardSocketOptions.SO_REUSEADDR, Boolean.FALSE),
        Test.create(StandardSocketOptions.SO_REUSEPORT, Boolean.FALSE),
        Test.create(StandardSocketOptions.SO_LINGER, Integer.valueOf(80)),
        Test.create(StandardSocketOptions.IP_TOS, Integer.valueOf(100))
    };

    static Test[] serverSocketTests = new Test[] {
        Test.create(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(8 * 100)),
        Test.create(StandardSocketOptions.SO_REUSEADDR, Boolean.FALSE),
        Test.create(StandardSocketOptions.SO_REUSEPORT, Boolean.FALSE),
        Test.create(StandardSocketOptions.IP_TOS, Integer.valueOf(100))
    };

    static Test[] dgSocketTests = new Test[] {
        Test.create(StandardSocketOptions.SO_SNDBUF, Integer.valueOf(10 * 100)),
        Test.create(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(8 * 100)),
        Test.create(StandardSocketOptions.SO_REUSEADDR, Boolean.FALSE),
        Test.create(StandardSocketOptions.SO_REUSEPORT, Boolean.FALSE),
        Test.create(StandardSocketOptions.IP_TOS, Integer.valueOf(100))
    };

    static Test[] mcSocketTests = new Test[] {
        Test.create(StandardSocketOptions.IP_MULTICAST_IF, getNetworkInterface()),
        Test.create(StandardSocketOptions.IP_MULTICAST_TTL, Integer.valueOf(10)),
        Test.create(StandardSocketOptions.IP_MULTICAST_LOOP, Boolean.TRUE)
    };

    static NetworkInterface getNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)nifs.nextElement();
                if (ni.supportsMulticast()) {
                    return ni;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    static void doSocketTests() throws Exception {
        try (
            ServerSocket srv = new ServerSocket(0);
            Socket c = new Socket("127.0.0.1", srv.getLocalPort());
            Socket s = srv.accept();
        ) {
            Set<SocketOption<?>> options = c.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);
            for (int i=0; i<socketTests.length; i++) {
                Test test = socketTests[i];
                if (!(test.option == StandardSocketOptions.SO_REUSEPORT && !reuseport)) {
                    c.setOption((SocketOption)test.option, test.testValue);
                    Object getval = c.getOption((SocketOption)test.option);
                    Object legacyget = legacyGetOption(Socket.class, c,test.option);
                    if (!getval.equals(legacyget)) {
                        Formatter f = new Formatter();
                        f.format("S Err %d: %s/%s", i, getval, legacyget);
                        throw new RuntimeException(f.toString());
                    }
                }
            }
        }
    }

    static void doDgSocketTests() throws Exception {
        try (
            DatagramSocket c = new DatagramSocket(0);
        ) {
            Set<SocketOption<?>> options = c.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);
            for (int i=0; i<dgSocketTests.length; i++) {
                Test test = dgSocketTests[i];
                if (!(test.option == StandardSocketOptions.SO_REUSEPORT && !reuseport)) {
                    c.setOption((SocketOption)test.option, test.testValue);
                    Object getval = c.getOption((SocketOption)test.option);
                    Object legacyget = legacyGetOption(DatagramSocket.class, c,test.option);
                    if (!getval.equals(legacyget)) {
                        Formatter f = new Formatter();
                        f.format("DG Err %d: %s/%s", i, getval, legacyget);
                        throw new RuntimeException(f.toString());
                    }
                }
            }
        }
    }

    static void doMcSocketTests() throws Exception {
        try (
            MulticastSocket c = new MulticastSocket(0);
        ) {
            for (int i=0; i<mcSocketTests.length; i++) {
                Test test = mcSocketTests[i];
                c.setOption((SocketOption)test.option, test.testValue);
                Object getval = c.getOption((SocketOption)test.option);
                Object legacyget = legacyGetOption(MulticastSocket.class, c,test.option);
                if (!getval.equals(legacyget)) {
                    Formatter f = new Formatter();
                    f.format("MC Err %d: %s/%s", i, getval, legacyget);
                    throw new RuntimeException(f.toString());
                }
            }
        }
    }

    static void doServerSocketTests() throws Exception {
        try (
            ServerSocket c = new ServerSocket(0);
        ) {
            Set<SocketOption<?>> options = c.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);
            for (int i=0; i<serverSocketTests.length; i++) {
                Test test = serverSocketTests[i];
                if (!(test.option == StandardSocketOptions.SO_REUSEPORT && !reuseport)) {
                    c.setOption((SocketOption)test.option, test.testValue);
                    Object getval = c.getOption((SocketOption)test.option);
                    Object legacyget = legacyGetOption(
                        ServerSocket.class, c, test.option
                    );
                    if (!getval.equals(legacyget)) {
                        Formatter f = new Formatter();
                        f.format("SS Err %d: %s/%s", i, getval, legacyget);
                        throw new RuntimeException(f.toString());
                    }
                }
            }
        }
    }

    static Object legacyGetOption(
        Class<?> type, Object s, Object option)

        throws Exception
    {
        if (type.equals(Socket.class)) {
            Socket socket = (Socket)s;
            Set<SocketOption<?>> options = socket.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);

            if (option.equals(StandardSocketOptions.SO_KEEPALIVE)) {
                return Boolean.valueOf(socket.getKeepAlive());
            } else if (option.equals(StandardSocketOptions.SO_SNDBUF)) {
                return Integer.valueOf(socket.getSendBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_RCVBUF)) {
                return Integer.valueOf(socket.getReceiveBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_REUSEADDR)) {
                return Boolean.valueOf(socket.getReuseAddress());
            } else if (option.equals(StandardSocketOptions.SO_REUSEPORT) && reuseport) {
                return Boolean.valueOf(socket.getOption(StandardSocketOptions.SO_REUSEPORT));
            } else if (option.equals(StandardSocketOptions.SO_LINGER)) {
                return Integer.valueOf(socket.getSoLinger());
            } else if (option.equals(StandardSocketOptions.IP_TOS)) {
                return Integer.valueOf(socket.getTrafficClass());
            } else if (option.equals(StandardSocketOptions.TCP_NODELAY)) {
                return Boolean.valueOf(socket.getTcpNoDelay());
            } else {
                throw new RuntimeException("unexecpted socket option");
            }
        } else if  (type.equals(ServerSocket.class)) {
            ServerSocket socket = (ServerSocket)s;
            Set<SocketOption<?>> options = socket.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);

            if (option.equals(StandardSocketOptions.SO_RCVBUF)) {
                return Integer.valueOf(socket.getReceiveBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_REUSEADDR)) {
                return Boolean.valueOf(socket.getReuseAddress());
            } else if (option.equals(StandardSocketOptions.SO_REUSEPORT) && reuseport) {
                return Boolean.valueOf(socket.getOption(StandardSocketOptions.SO_REUSEPORT));
            } else if (option.equals(StandardSocketOptions.IP_TOS)) {
                return getServerSocketTrafficClass(socket);
            } else {
                throw new RuntimeException("unexecpted socket option");
            }
        } else if  (type.equals(DatagramSocket.class)) {
            DatagramSocket socket = (DatagramSocket)s;
            Set<SocketOption<?>> options = socket.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);

            if (option.equals(StandardSocketOptions.SO_SNDBUF)) {
                return Integer.valueOf(socket.getSendBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_RCVBUF)) {
                return Integer.valueOf(socket.getReceiveBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_REUSEADDR)) {
                return Boolean.valueOf(socket.getReuseAddress());
            } else if (option.equals(StandardSocketOptions.SO_REUSEPORT) && reuseport) {
                return Boolean.valueOf(socket.getOption(StandardSocketOptions.SO_REUSEPORT));
            } else if (option.equals(StandardSocketOptions.IP_TOS)) {
                return Integer.valueOf(socket.getTrafficClass());
            } else {
                throw new RuntimeException("unexecpted socket option");
            }

        } else if  (type.equals(MulticastSocket.class)) {
            MulticastSocket socket = (MulticastSocket)s;
            Set<SocketOption<?>> options = socket.supportedOptions();
            boolean reuseport = options.contains(StandardSocketOptions.SO_REUSEPORT);

            if (option.equals(StandardSocketOptions.SO_SNDBUF)) {
                return Integer.valueOf(socket.getSendBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_RCVBUF)) {
                return Integer.valueOf(socket.getReceiveBufferSize());
            } else if (option.equals(StandardSocketOptions.SO_REUSEADDR)) {
                return Boolean.valueOf(socket.getReuseAddress());
            } else if (option.equals(StandardSocketOptions.SO_REUSEPORT) && reuseport) {
                return Boolean.valueOf(socket.getOption(StandardSocketOptions.SO_REUSEPORT));
            } else if (option.equals(StandardSocketOptions.IP_TOS)) {
                return Integer.valueOf(socket.getTrafficClass());
            } else if (option.equals(StandardSocketOptions.IP_MULTICAST_IF)) {
                return socket.getNetworkInterface();
            } else if (option.equals(StandardSocketOptions.IP_MULTICAST_TTL)) {
                return Integer.valueOf(socket.getTimeToLive());
            } else if (option.equals(StandardSocketOptions.IP_MULTICAST_LOOP)) {
                return Boolean.valueOf(socket.getLoopbackMode());
            } else {
                throw new RuntimeException("unexecpted socket option");
            }
        }
        throw new RuntimeException("unexecpted socket type");
    }

    public static void main(String args[]) throws Exception {
        doSocketTests();
        doServerSocketTests();
        doDgSocketTests();
        doMcSocketTests();
    }

    
    
    static Object getServerSocketTrafficClass(ServerSocket ss) throws Exception {
        try {
            Class<?> c = Class.forName("jdk.net.Sockets");
            Method m = c.getDeclaredMethod("getOption", ServerSocket.class, SocketOption.class);
            return m.invoke(null, ss, StandardSocketOptions.IP_TOS);
        } catch (ClassNotFoundException e) {
            
            System.out.println("jdk.net module not present, falling back.");
            return Integer.valueOf(ss.getOption(StandardSocketOptions.IP_TOS));
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
