import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class HugeDataTransferTest {

    private static int SO_TIMEOUT;

    private final static int MAX_CONNECTIONS = 128;

    private final static int CONNECTIONS_RESERVE = 10;

    private final static int BACKLOG_QUEUE_LENGTH = MAX_CONNECTIONS;

    private final static int DATA_PARCELS = 128;

    private final static int MAX_PARCEL = 1 << 8;

    static private final boolean DEBUG_MODE = false;

    private static int detectOSLimitation() {
        final int CONNECTIONS_TO_TRY = MAX_CONNECTIONS + CONNECTIONS_RESERVE;
        display("--- Trying to open " + CONNECTIONS_TO_TRY + " connections:");
        InetAddress address;
        ServerSocket serverSocket;
        try {
            address = InetAddress.getLocalHost();
            int anyPort = 0;
            int defaultBacklog = BACKLOG_QUEUE_LENGTH;
            serverSocket = new ServerSocket(anyPort, defaultBacklog, address);
        } catch (IOException ioe) {
            throw new Error("FATAL error while loading the test: " + ioe);
        }
        display(serverSocket.toString());
        Socket[] server = new Socket[CONNECTIONS_TO_TRY];
        Socket[] client = new Socket[CONNECTIONS_TO_TRY];
        int i, port = serverSocket.getLocalPort();
        for (i = 0; i < CONNECTIONS_TO_TRY; i++) {
            try {
                client[i] = new Socket(address, port);
                display(">Open: client[" + i + "] = " + client[i]);
                server[i] = serverSocket.accept();
                display(">Open: server[" + i + "] = " + server[i]);
            } catch (IOException ioe) {
                display(">OOPS! -- failed to open connection #" + i);
                break;
            }
        }
        display("> Could open " + (i < CONNECTIONS_TO_TRY ? "only " : "") + i + " connections.");
        display(">Closing them:");
        for (int j = 0; j < i; j++) {
            try {
                server[j].close();
                client[j].close();
            } catch (IOException ioe) {
                throw new Error("FATAL error while loading the test: " + ioe);
            }
        }
        display(">OK.");
        int safeConnections = i - CONNECTIONS_RESERVE;
        if (safeConnections < 1) {
            safeConnections = 1;
        }
        if (safeConnections < MAX_CONNECTIONS) {
            complain("------------------------- CAUTION: -------------------");
            complain("While checking the OS limitations, the test found that");
            complain("only " + i + " TCP/IP socket connections could be safely open");
            complain("simultaneously. However, possibility to open at least");
            complain("" + MAX_CONNECTIONS + "+" + CONNECTIONS_RESERVE + " connections were expected.");
            complain("");
            complain("So, the test will check only " + safeConnections + " connection" + (safeConnections == 1 ? "" : "s") + " which seem");
            complain("safe to be open simultaneously.");
            complain("------------------------------------------------------");
        }
        return safeConnections;
    }

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        HugeDataTransferTest.out = out;
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException exception) {
            complain(exception.toString());
            return 2;
        }
        display("Host: " + address);
        final int CONNECTIONS = detectOSLimitation();
        ServerSocket serverSocket;
        try {
            final int anyPort = 0;
            final int defaultBacklog = BACKLOG_QUEUE_LENGTH;
            serverSocket = new ServerSocket(anyPort, defaultBacklog, address);
        } catch (IOException exception) {
            complain("Cannot assign a ServerSocket on: " + address);
            return 2;
        }
        String jdkPath = System.getProperty("test.jdk");
        Path toolName = Paths.get("bin", "java" + (isWindows() ? ".exe" : ""));
        Path jdkTool = Paths.get(jdkPath, toolName.toString());
        String IPAddress = address.getHostAddress();
        int localPort = serverSocket.getLocalPort();
        String arguments = " " + CONNECTIONS + " " + IPAddress + " " + localPort;
        String command = jdkTool.toAbsolutePath().toString() + " " + Client.class.getName() + " " + arguments;
        try {
            SO_TIMEOUT = Integer.parseInt(args[0]) * 60 * 1000;
        } catch (NumberFormatException e) {
            complain("Wrong timeout argument: " + e);
            return 2;
        }
        Runtime runtime = Runtime.getRuntime();
        Process client = null;
        IORedirector redirectOut = null;
        IORedirector redirectErr = null;
        try {
            client = runtime.exec(command);
            InputStream clientOut = client.getInputStream();
            InputStream clientErr = client.getErrorStream();
            redirectOut = new IORedirector(clientOut, DEBUG_MODE ? out : null);
            redirectErr = new IORedirector(clientErr, out);
            redirectOut.start();
            redirectErr.start();
        } catch (IOException exception) {
            complain("Failed to start client: " + exception);
            return 2;
        }
        Server[] server = new Server[CONNECTIONS];
        for (int i = 0; i < CONNECTIONS; i++) {
            server[i] = new Server(serverSocket);
            display("Server #" + i + ": " + server[i]);
            server[i].start();
        }
        boolean testFailed = false;
        try {
            client.waitFor();
            int clientStatus = client.exitValue();
            display("Client VM exitCode=" + clientStatus);
            if (redirectOut.isAlive()) {
                redirectOut.join();
            }
            if (redirectErr.isAlive()) {
                redirectErr.join();
            }
            if (clientStatus != 95) {
                complain("Client VM has crashed: exit status=" + clientStatus);
                testFailed = true;
            }
            for (int i = 0; i < CONNECTIONS; i++) {
                display("Server: waiting for #" + i);
                if (server[i].isAlive()) {
                    display("Server #" + i + ": (joining...)" + server[i]);
                    server[i].join();
                }
                if (server[i].exception != null) {
                    if (server[i].message != null) {
                        complain("Server #" + i + "(finished): with message:" + server[i].message);
                    }
                    complain("Server #" + i + "(finished): " + server[i].exception);
                    server[i].exception.printStackTrace(out);
                    out.flush();
                    testFailed = true;
                }
            }
        } catch (InterruptedException exception) {
            complain("Test interrupted: " + exception);
            testFailed = true;
        }
        if (testFailed) {
            complain("Test failed.");
        } else {
            display("Test passed.");
        }
        return testFailed ? 2 : 0;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    private static PrintStream out;

    private static synchronized void complain(Object message) {
        out.println("# " + message);
        out.flush();
    }

    private static synchronized void display(Object report) {
        if (DEBUG_MODE) {
            out.println(report.toString());
        }
        out.flush();
    }

    private static class Server extends Thread {

        private ServerSocket serverSocket;

        private Socket socket;

        @Override
        public String toString() {
            return "ServerSocket: " + serverSocket.toString();
        }

        int getPort() {
            return serverSocket.getLocalPort();
        }

        public Server(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        Exception exception = null;

        String message = null;

        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
                socket.setSoTimeout(SO_TIMEOUT);
                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();
                Random random = new Random(getPort());
                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);
                    message = "reading parcel number " + i;
                    Parcel sample = new Parcel(istream);
                    if (!sample.equals(etalon)) {
                        complain("Server thread for port #" + getPort() + " got unexpected parcel:\n" + "sample=" + sample + "\n" + "etalon=" + etalon);
                        throw new TestFailure("server has read unexpected parcel");
                    }
                    message = "sending parcel number " + i;
                    etalon.send(ostream);
                    ostream.flush();
                }
                int datum = istream.read();
                if (datum >= 0) {
                    throw new TestFailure("server has read ambigous byte: " + datum);
                }
                ostream.close();
            } catch (Exception oops) {
                exception = oops;
            }
        }
    }

    private static class Client extends Thread {

        private Socket socket;

        @Override
        public String toString() {
            return socket.toString();
        }

        Exception exception = null;

        String message = null;

        public static java.io.PrintStream complainStream = System.out;

        public static java.io.PrintStream displayStream = System.err;

        Client(InetAddress address, int port) throws IOException {
            socket = new Socket(address, port);
            socket.setSoTimeout(SO_TIMEOUT);
        }

        int getPort() {
            return socket.getPort();
        }

        @Override
        public void run() {
            try {
                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();
                Random random = new Random(getPort());
                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);
                    message = "sending parcel number: " + i;
                    etalon.send(ostream);
                    ostream.flush();
                    message = "reading parcel number: " + i;
                    Parcel sample = new Parcel(istream);
                    if (!sample.equals(etalon)) {
                        complain("Client thread for port #" + getPort() + " got unexpected parcel:\n" + "sample=" + sample + "\n" + "etalon=" + etalon);
                        throw new TestFailure("parcel context is unexpected to client");
                    }
                }
                if (istream.available() > 0) {
                    int datum = istream.read();
                    throw new TestFailure("client has read ambigous byte: " + datum);
                }
                ostream.close();
            } catch (Exception oops) {
                exception = oops;
            }
        }

        public static void main(String[] args) {
            if (DEBUG_MODE) {
                try {
                    String filename = "Client" + ((args.length == 3) ? args[2] : "new");
                    displayStream = new PrintStream(filename + ".out");
                    complainStream = new PrintStream(filename + ".err");
                } catch (FileNotFoundException exception) {
                    complain(exception);
                }
            }
            if (args.length != 3) {
                complain("Client expects 3 paramenets:");
                complain("    java " + Client.class.getName() + " connections_to_try address port");
                exit(1);
            }
            int CONNECTIONS = Integer.parseInt(args[0]);
            display("Client VM: will try " + CONNECTIONS + " connections.");
            InetAddress address;
            try {
                address = InetAddress.getByName(args[1]);
            } catch (UnknownHostException exception) {
                address = null;
                complain("Client: cannot find host: \"" + args[1] + "\"");
                exit(4);
            }
            display("Client: host to contact: " + address);
            int port = Integer.parseInt(args[2]);
            display("Client: port to contact: " + port);
            Client[] client = new Client[CONNECTIONS];
            for (int i = 0; i < CONNECTIONS; i++) {
                try {
                    client[i] = new Client(address, port);
                    display("Client #" + i + ": " + client[i]);
                } catch (IOException ioe) {
                    complain("Client #" + i + "(creation): " + ioe);
                    ioe.printStackTrace(complainStream);
                    complainStream.flush();
                    exit(3);
                }
            }
            for (int i = 0; i < CONNECTIONS; i++) {
                client[i].start();
            }
            int status = 0;
            for (int i = 0; i < CONNECTIONS; i++) {
                display("Client: waiting for #" + i);
                if (client[i].isAlive()) {
                    display("Client #" + i + ": (joining...)" + client[i]);
                    try {
                        client[i].join();
                    } catch (InterruptedException ie) {
                        complain("Client #" + i + ": " + ie);
                        status = 3;
                    }
                }
                if (client[i].exception != null) {
                    if (client[i].message != null) {
                        complain("Client #" + i + "(finished) with message: " + client[i].message);
                    }
                    complain("Client #" + i + "(finished): " + client[i].exception);
                    client[i].exception.printStackTrace(complainStream);
                    complainStream.flush();
                    if (status == 0) {
                        status = 2;
                    }
                }
            }
            exit(status);
        }

        private static synchronized void complain(Object message) {
            complainStream.println("# " + message);
            complainStream.flush();
        }

        private static synchronized void display(Object message) {
            if (!DEBUG_MODE) {
                return;
            }
            displayStream.println(message.toString());
            displayStream.flush();
        }

        private static void exit(int exitCode) {
            int status = exitCode + 95;
            System.exit(status);
        }
    }

    private static class IORedirector extends Thread {

        InputStream in;

        OutputStream out;

        public IORedirector(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                for (; ; ) {
                    int symbol = in.read();
                    if (symbol < 0) {
                        break;
                    }
                    if (out != null) {
                        out.write(symbol);
                    }
                }
                if (out != null) {
                    out.flush();
                }
            } catch (IOException exception) {
                throw new TestFailure("IORedirector exception: " + exception);
            }
        }
    }

    static class Parcel {

        private final byte[] parcel;

        @Override
        public String toString() {
            if (parcel == null) {
                return "null";
            }
            String s = "{";
            for (int i = 0; i < parcel.length; i++) {
                s += (i > 0 ? ", " : "") + ((int) parcel[i] & 0xFF);
            }
            return s + "}";
        }

        public Parcel(Random random) {
            int size = random.nextInt(MAX_PARCEL) + 1;
            parcel = new byte[size];
            for (int i = 0; i < size; i++) {
                parcel[i] = (byte) random.nextInt(256);
            }
        }

        private static byte[] readBytes(int size, InputStream istream) throws IOException {
            byte[] data = new byte[size];
            for (int i = 0; i < size; i++) {
                int datum = istream.read();
                if (datum < 0) {
                    throw new TestFailure("unexpected EOF: have read: " + i + " bytes of " + size);
                }
                data[i] = (byte) datum;
            }
            return data;
        }

        private static int getSize(InputStream istream) throws IOException {
            byte[] data = readBytes(4, istream);
            int data0 = (int) data[0] & 0xFF;
            int data1 = (int) data[1] & 0xFF;
            int data2 = (int) data[2] & 0xFF;
            int data3 = (int) data[3] & 0xFF;
            int sizeWord = data0 + (data1 << 8) + (data2 << 16) + (data3 << 24);
            int size = sizeWord + 1;
            if (size <= 0) {
                throw new TestFailure("illegal size: " + size);
            }
            return size;
        }

        private static void putSize(OutputStream ostream, int size) throws IOException {
            if (size <= 0) {
                throw new TestFailure("illegal size: " + size);
            }
            int sizeWord = size - 1;
            byte[] data = new byte[4];
            data[0] = (byte) sizeWord;
            data[1] = (byte) (sizeWord >> 8);
            data[2] = (byte) (sizeWord >> 16);
            data[3] = (byte) (sizeWord >> 24);
            ostream.write(data);
        }

        public Parcel(InputStream istream) throws IOException {
            int size = getSize(istream);
            parcel = readBytes(size, istream);
        }

        public void send(OutputStream ostream) throws IOException {
            int size = parcel.length;
            putSize(ostream, size);
            ostream.write(parcel);
        }

        public boolean equals(Parcel other) {
            if (this.parcel.length != other.parcel.length) {
                return false;
            }
            int size = parcel.length;
            for (int i = 0; i < size; i++) {
                if (this.parcel[i] != other.parcel[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    static class TestFailure extends RuntimeException {

        public TestFailure(String purpose) {
            super(purpose);
        }
    }
}
