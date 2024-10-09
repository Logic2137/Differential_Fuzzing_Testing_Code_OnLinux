import java.io.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class AcceptInheritHandle {

    enum ServerSocketProducer {

        JAVA_NET(() -> {
            try {
                return new ServerSocket();
            } catch (IOException x) {
                throw new UncheckedIOException(x);
            }
        }), NIO_CHANNELS(() -> {
            try {
                return ServerSocketChannel.open().socket();
            } catch (IOException x) {
                throw new UncheckedIOException(x);
            }
        });

        final Supplier<ServerSocket> supplier;

        ServerSocketProducer(Supplier<ServerSocket> supplier) {
            this.supplier = supplier;
        }

        Supplier<ServerSocket> supplier() {
            return supplier;
        }
    }

    static final String JAVA = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

    static final String CLASSPATH = System.getProperty("java.class.path");

    public static void main(String[] args) throws Exception {
        if (args.length == 1)
            server(ServerSocketProducer.valueOf(args[0]));
        else
            mainEntry();
    }

    static void mainEntry() throws Exception {
        testJavaNetServerSocket();
        testNioServerSocketChannel();
    }

    static void testJavaNetServerSocket() throws Exception {
        test(ServerSocketProducer.JAVA_NET);
        test(ServerSocketProducer.JAVA_NET, "-Djava.net.preferIPv4Stack=true");
    }

    static void testNioServerSocketChannel() throws Exception {
        test(ServerSocketProducer.NIO_CHANNELS);
    }

    static void test(ServerSocketProducer ssp, String... sysProps) throws Exception {
        System.out.println("\nStarting test for " + ssp.name());
        List<String> commands = new ArrayList<>();
        commands.add(JAVA);
        for (String prop : sysProps) commands.add(prop);
        commands.add("-cp");
        commands.add(CLASSPATH);
        commands.add("AcceptInheritHandle");
        commands.add(ssp.name());
        System.out.println("Executing: " + commands);
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process serverProcess = pb.start();
        DataInputStream dis = new DataInputStream(serverProcess.getInputStream());
        int port = dis.readInt();
        System.out.println("Server process listening on " + port + ", connecting...");
        Socket socket = new Socket("localhost", port);
        String s = dis.readUTF();
        System.out.println("Server process said " + s);
        serverProcess.destroy();
        serverProcess.waitFor(30, TimeUnit.SECONDS);
        System.out.println("serverProcess exitCode:" + serverProcess.exitValue());
        try {
            socket.setSoTimeout(10 * 1000);
            socket.getInputStream().read();
        } catch (SocketTimeoutException x) {
            throw new RuntimeException("Failed: should get reset, not " + x);
        } catch (SocketException x) {
            System.out.println("Expected:" + x);
        }
    }

    static void server(ServerSocketProducer producer) throws Exception {
        try (ServerSocket ss = producer.supplier().get()) {
            ss.bind(new InetSocketAddress(0));
            int port = ss.getLocalPort();
            DataOutputStream dos = new DataOutputStream(System.out);
            dos.writeInt(port);
            dos.flush();
            ss.accept();
            Runtime.getRuntime().exec("sleep 20");
            Thread.sleep(3 * 1000);
            dos.writeUTF("kill me!");
            dos.flush();
            Thread.sleep(30 * 1000);
        }
    }
}
