


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class StateTestService {

    static boolean failed = false;
    static int reply_port;

    static void check(boolean okay) {
        println("check " + okay);
        if (!okay) {
            failed = true;
        }
    }

    static String logDir;
    static PrintStream out;
    static boolean initialized = false;

    
    static void initLogFile() {
        if (initialized)
            return;

        try {
            OutputStream f = Files.newOutputStream(Path.of(logDir, "statetest.txt"), APPEND, CREATE);
            out = new PrintStream(f);
        } catch (Exception e) {}
        initialized = true;
    }

    static void println(String msg) {
        initLogFile();
        out.println(msg);
    }

    private static void reply(String msg) throws IOException {
        println("REPLYING: "  + msg);
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), reply_port);
        SocketChannel sc = SocketChannel.open(isa);
        byte b[] = msg.getBytes("UTF-8");
        ByteBuffer bb = ByteBuffer.wrap(b);
        sc.write(bb);
        sc.close();
    }

    public static void main(String args[]) throws IOException {
        try {
            if (args.length == 0) {
                System.err.println("Usage: StateTestService [reply-port]");
                return;
            }
            reply_port = Integer.parseInt(args[0]);
            logDir = System.getProperty("test.classes");

            Channel c = null;
            try {
                c = System.inheritedChannel();
            } catch (SecurityException se) {
                
            }
            if (c == null) {
                println("c == null");
                reply("FAILED");
                return;
            }

            if (c instanceof SocketChannel) {
                SocketChannel sc = (SocketChannel)c;
                check( sc.isBlocking() );
                check( sc.socket().isBound() );
                check( sc.socket().isConnected() );
            }

            if (c instanceof ServerSocketChannel) {
                ServerSocketChannel ssc = (ServerSocketChannel)c;
                check( ssc.isBlocking() );
                check( ssc.socket().isBound() );
            }

            if (c instanceof DatagramChannel) {
                DatagramChannel dc = (DatagramChannel)c;
                check( dc.isBlocking() );
                check( dc.socket().isBound() );
            }

            if (failed) {
                reply("FAILED");
            } else {
                reply("PASSED");
            }
        } catch (Throwable t) {
            t.printStackTrace(out);
            throw t;
        }
    }
}
