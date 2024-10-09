

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.util.ArrayList;
import java.util.List;



public class UnsupportedOptionsTest {

    private static final List<SocketOption<?>> socketOptions = new ArrayList<>();

    static {
        socketOptions.add(StandardSocketOptions.IP_MULTICAST_IF);
        socketOptions.add(StandardSocketOptions.IP_MULTICAST_LOOP);
        socketOptions.add(StandardSocketOptions.IP_MULTICAST_TTL);
        socketOptions.add(StandardSocketOptions.IP_TOS);
        socketOptions.add(StandardSocketOptions.SO_BROADCAST);
        socketOptions.add(StandardSocketOptions.SO_KEEPALIVE);
        socketOptions.add(StandardSocketOptions.SO_LINGER);
        socketOptions.add(StandardSocketOptions.SO_RCVBUF);
        socketOptions.add(StandardSocketOptions.SO_REUSEADDR);
        socketOptions.add(StandardSocketOptions.SO_SNDBUF);
        socketOptions.add(StandardSocketOptions.TCP_NODELAY);

        try {
            Class<?> c = Class.forName("jdk.net.ExtendedSocketOptions");
            Field field = c.getField("SO_FLOW_SLA");
            socketOptions.add((SocketOption<?>)field.get(null));
            field = c.getField("TCP_QUICKACK");
            socketOptions.add((SocketOption<?>)field.get(null));
        } catch (ClassNotFoundException e) {
            
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Socket s = new Socket();
        ServerSocket ss = new ServerSocket();
        DatagramSocket ds = new DatagramSocket();
        MulticastSocket ms = new MulticastSocket();

        for (SocketOption option : socketOptions) {
            if (!s.supportedOptions().contains(option)) {
                testUnsupportedSocketOption(s, option);
            }

            if (!ss.supportedOptions().contains(option)) {
                testUnsupportedSocketOption(ss, option);
            }

            if (!ms.supportedOptions().contains(option)) {
                testUnsupportedSocketOption(ms, option);
            }

            if (!ds.supportedOptions().contains(option)) {
                testUnsupportedSocketOption(ds, option);
            }
        }
    }

    
    private static void testUnsupportedSocketOption(Object socket,
                                                    SocketOption option) {
        testSet(socket, option);
        testGet(socket, option);
    }

    private static void testSet(Object socket, SocketOption option) {
        try {
            setOption(socket, option);
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException was throw " +
                    "as expected. Socket: " + socket + " Option: " + option);
            return;
        } catch (Exception e) {
            throw new RuntimeException("FAIL. Unexpected exception.", e);
        }
        throw new RuntimeException("FAIL. UnsupportedOperationException " +
                "hasn't been thrown. Socket: " + socket + " Option: " + option);
    }

    private static void testGet(Object socket, SocketOption option) {
        try {
            getOption(socket, option);
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException was throw " +
                    "as expected. Socket: " + socket + " Option: " + option);
            return;
        } catch (Exception e) {
            throw new RuntimeException("FAIL. Unexpected exception.", e);
        }
        throw new RuntimeException("FAIL. UnsupportedOperationException " +
                "hasn't been thrown. Socket: " + socket + " Option: " + option);
    }

    private static void getOption(Object socket,
                                  SocketOption option) throws IOException {
        if (socket instanceof Socket) {
            ((Socket) socket).getOption(option);
        } else if (socket instanceof ServerSocket) {
            ((ServerSocket) socket).getOption(option);
        } else if (socket instanceof DatagramSocket) {
            ((DatagramSocket) socket).getOption(option);
        } else {
            throw new RuntimeException("Unsupported socket type");
        }
    }

    private static void setOption(Object socket,
                                  SocketOption option) throws IOException {
        if (socket instanceof Socket) {
            ((Socket) socket).setOption(option, null);
        } else if (socket instanceof ServerSocket) {
            ((ServerSocket) socket).setOption(option, null);
        } else if (socket instanceof DatagramSocket) {
            ((DatagramSocket) socket).setOption(option, null);
        } else {
            throw new RuntimeException("Unsupported socket type");
        }
    }
}
