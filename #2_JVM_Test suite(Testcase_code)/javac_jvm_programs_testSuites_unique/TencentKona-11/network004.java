




package nsk.stress.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.StringTokenizer;


public class network004 {
    
    private static int SO_TIMEOUT;

    
    private final static int MAX_CONNECTIONS = 128;

    
    private final static int CONNECTIONS_RESERVE = 10;

    
    private final static int DATA_PARCELS = 128;

    
    private final static int MAX_PARCEL = 1 << 8;

    
    static private final boolean DEBUG_MODE = false;

    
    private static int detectOSLimitation() {
        final int CONNECTIONS_TO_TRY = MAX_CONNECTIONS + CONNECTIONS_RESERVE;
        ServerSocket ssoc[] = new ServerSocket[CONNECTIONS_TO_TRY];
        display("--- Trying to open " + CONNECTIONS_TO_TRY + " connections:");
        int i;
        for (i = 0; i < CONNECTIONS_TO_TRY; i++)
            try {
                ssoc[i] = new ServerSocket(0);
                display("--- Open: ssoc[" + i + "] = " + ssoc[i]);
            } catch (IOException ioe) {
                display("--- OOPS! -- failed to open connection #" + i);
                break;
            }
        display("--- Could open " +
                (i < CONNECTIONS_TO_TRY ? "only " : "") + i + " connections.");
        display("--- Closing them:");
        for (int j = 0; j < i; j++)
            try {
                ssoc[j].close();
            } catch (IOException ioe) {
                throw new Error("FATAL error while loading the test: " + ioe);
            }
        display("--- OK.");
        int safeConnections = i - CONNECTIONS_RESERVE;
        if (safeConnections < 1)
            safeConnections = 1;
        if (safeConnections < MAX_CONNECTIONS) {
            complain("------------------------- CAUTION: -------------------");
            complain("While checking the OS limitations, the test found that");
            complain("only " + i + " TCP/IP socket connections could be safely open");
            complain("simultaneously. However, possibility to open at least");
            complain("" + MAX_CONNECTIONS + "+" + CONNECTIONS_RESERVE
                    + " connections were expected.");
            complain("");
            complain("So, the test will check only " + safeConnections + " connection"
                    + (safeConnections == 1 ? "" : "s") + " which seem");
            complain("safe to be open simultaneously.");
            complain("------------------------------------------------------");
        }
        return safeConnections;
    }

    

    
    public static void main(String args[]) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
        
    }

    
    public static int run(String args[], PrintStream out) {
        network004.out = out;

        
        
        
        InetAddress address = null;
        try {
            switch (args.length) {
                case 2:
                    address = InetAddress.getLocalHost();
                    break;
                case 3:
                    address = InetAddress.getByName(args[2]);
                    break;
                default:
                    complain("Illegal arguments number; execute:");
                    complain("    java network004 $JAVA_COMMAND " +
                            "[$IP_ADDRESS | $HOST_NAME | localhost]");
                    return 2; 
            }
        } catch (UnknownHostException exception) {
            complain(exception.toString());
            return 2; 
        }
        display("Host: " + address);

        
        
        
        final int CONNECTIONS = detectOSLimitation();

        
        
        
        Server server[] = new Server[CONNECTIONS];
        for (int i = 0; i < CONNECTIONS; i++) {
            try {
                server[i] = new Server(address);
            } catch (Exception exception) {
                complain("Server #" + i + ": " + exception);
                return 2;
            }
            display("Server #" + i + ": " + server[i]);
            server[i].start();
        }

        
        
        
        String command = args[0] + " " + network004.class.getName() + "$Client";
        try {
            SO_TIMEOUT = Integer.parseInt(args[1]) * 60 * 1000;
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

            
            PrintStream clientIn = new PrintStream(client.getOutputStream());
            clientIn.println(CONNECTIONS);
            for (int i = 0; i < CONNECTIONS; i++)
                clientIn.println(server[i].getIPAddress() + " " + server[i].getPort());
            clientIn.flush();
            clientIn.close();

        } catch (Exception exception) {
            complain("Failed to start client: " + exception);
            return 2;
        }

        
        
        
        boolean testFailed = false;
        try {
            client.waitFor();
            
            if (redirectOut.isAlive())
                redirectOut.join();
            if (redirectErr.isAlive())
                redirectErr.join();

            
            int clientStatus = client.exitValue();
            if (clientStatus != 95) {
                complain("Client VM has failed: exit status=" + clientStatus);
                testFailed = true;
            }

            
            for (int i = 0; i < CONNECTIONS; i++) {
                display("Server: waiting for #" + i);
                while (server[i].isAlive())
                    server[i].join();
                if (server[i].exception != null) {
                    complain("Server thread #" + i + ": " + server[i].exception);
                    testFailed = true;
                }
            }

        } catch (Exception exception) {
            complain("Test interrupted: " + exception);
            testFailed = true;
        }

        if (testFailed)
            complain("Test failed.");
        else
            display("Test passed.");
        return testFailed ? 2 : 0;
    }

    

    
    private static PrintStream out;

    
    private static synchronized void complain(Object message) {
        out.println("# " + message);
        out.flush();
    }

    
    private static synchronized void display(Object report) {
        if (DEBUG_MODE)
            out.println(report.toString());
        out.flush();
    }

    

    
    private static class Server extends Thread {
        
        private ServerSocket serverSocket;

        
        public String toString() {
            return serverSocket.toString();
        }

        
        public String getIPAddress() {
            return serverSocket.getInetAddress().getHostAddress();
        }

        
        int getPort() {
            return serverSocket.getLocalPort();
        }

        
        public Server(InetAddress address) throws IOException {
            int someFreePort = 0;
            int backlog = 50; 
            serverSocket = new ServerSocket(someFreePort, backlog, address);
        }

        
        Exception exception = null;

        
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(SO_TIMEOUT);


                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();

                Random random = new Random(getPort());

                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        complain("Server thread for port #"
                                + getPort() + " got unexpected parcel:\n"
                                + "sample=" + sample + "\n"
                                + "etalon=" + etalon);
                        throw new TestFailure(
                                "server has read unexpected parcel");
                    }

                    etalon.send(ostream);
                    ostream.flush();
                }

                int datum = istream.read(); 
                if (datum >= 0)
                    throw new TestFailure(
                            "server has read ambigous byte: " + datum);

                ostream.close(); 

            } catch (Exception oops) {
                exception = oops;
            }
        }

    }

    

    
    private static class Client extends Thread {
        
        private Socket socket;

        
        public String toString() {
            return socket.toString();
        }

        
        Exception exception = null;

        
        Client(InetAddress address, int port) throws IOException {
            socket = new Socket(address, port);
            socket.setSoTimeout(SO_TIMEOUT);
        }

        
        int getPort() {
            return socket.getPort();
        }

        
        public void run() {
            try {
                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();

                Random random = new Random(getPort());

                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);
                    etalon.send(ostream);
                    ostream.flush();

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        complain("Client thread for port #"
                                + getPort() + " got unexpected parcel:\n"
                                + "sample=" + sample + "\n"
                                + "etalon=" + etalon);
                        throw new TestFailure(
                                "parcel context is unexpected to client");
                    }
                }

                if (istream.available() > 0) {
                    int datum = istream.read();
                    throw new TestFailure(
                            "client has read ambigous byte: " + datum);
                }
                ostream.close(); 

            } catch (Exception oops) {
                exception = oops;
            }
        }

        
        public static void main(String args[]) {
            
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            final int CONNECTIONS;
            try {
                String line = in.readLine();
                if (line == null) {
                    complain("Client expects paramenets passed through stdin:");
                    complain("    actual_number_of_sockets");
                    complain("    IP-address_1 port_1");
                    complain("    IP-address_2 port_2");
                    complain("    .   .   .");
                    complain("    IP-address_N port_N");
                    exit(2); 
                }
                CONNECTIONS = Integer.parseInt(line);
            } catch (IOException ioe) {
                complain("Client failed to read the actual number of CONNECTIONS");
                throw new RuntimeException(ioe.toString());
            }

            Client client[] = new Client[CONNECTIONS];
            for (int i = 0; i < CONNECTIONS; i++)
                try {
                    String line = in.readLine();
                    if (line == null) {
                        complain("Client: failed to read address/port for client #" + i);
                        exit(3);
                    }

                    StringTokenizer tokenz = new StringTokenizer(line);
                    if (tokenz.countTokens() != 2) {
                        complain("Client: illegal input string: " + line);
                        exit(3);
                    }
                    String serverName = (String) tokenz.nextElement();
                    InetAddress address = InetAddress.getByName(serverName);
                    int port = Integer.parseInt((String) tokenz.nextElement());

                    client[i] = new Client(address, port);

                    display("Client #" + i + ": " + client[i]);

                } catch (IOException ioe) {
                    complain("Client #" + i + ": " + ioe);
                    exit(3);
                }

            

            for (int i = 0; i < CONNECTIONS; i++)
                client[i].start();

            int status = 0;
            for (int i = 0; i < CONNECTIONS; i++) {
                display("Client: waiting for #" + i);
                while (client[i].isAlive())
                    yield();
                if (client[i].exception != null) {
                    complain("Client #" + i + ": " + client[i].exception);
                    status = 2;
                }
            }

            exit(status);
        }

        
        private static synchronized void complain(Object message) {
            System.err.println("# " + message);
            System.err.flush();
        }

        
        private static synchronized void display(Object message) {
            if (!DEBUG_MODE)
                return;
            System.out.println(message.toString());
            System.out.flush();
        }

        
        private static void exit(int exitCode) {
            System.exit(exitCode + 95);
        }

    }

    
    private static class IORedirector extends Thread {
        
        InputStream in;
        
        OutputStream out;

        
        public IORedirector(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }

        
        public void run() {
            try {
                for (; ; ) {
                    int symbol = in.read();
                    if (symbol < 0)
                        break; 
                    if (out != null)
                        out.write(symbol);
                }

                if (out != null)
                    out.flush();

            } catch (Exception exception) {
                throw new TestFailure("IORedirector exception: " + exception);
            }
        }
    }

    

    
    static class Parcel {
        private byte[] parcel;

        
        public String toString() {
            if (parcel == null)
                return "null";
            String s = "{";
            for (int i = 0; i < parcel.length; i++)
                s += (i > 0 ? ", " : "") + ((int) parcel[i] & 0xFF);
            return s + "}";
        }

        
        public Parcel(Random random) {
            int size = random.nextInt(MAX_PARCEL) + 1;
            parcel = new byte[size];
            for (int i = 0; i < size; i++)
                parcel[i] = (byte) random.nextInt(256);
        }

        
        private static byte[] readBytes(int size, InputStream istream)
                throws IOException {

            byte data[] = new byte[size];
            for (int i = 0; i < size; i++) {
                int datum = istream.read();
                if (datum < 0)
                    throw new TestFailure(
                            "unexpected EOF: have read: " + i + " bytes of " + size);
                data[i] = (byte) datum;
            }
            return data;
        }

        
        private static int getSize(InputStream istream) throws IOException {
            byte data[] = readBytes(4, istream);
            int data0 = (int) data[0] & 0xFF;
            int data1 = (int) data[1] & 0xFF;
            int data2 = (int) data[2] & 0xFF;
            int data3 = (int) data[3] & 0xFF;
            int sizeWord = data0 + (data1 << 8) + (data2 << 16) + (data3 << 24);
            int size = sizeWord + 1;
            if (size <= 0)
                throw new TestFailure("illegal size: " + size);
            return size;
        }

        
        private static void putSize(OutputStream ostream, int size)
                throws IOException {

            if (size <= 0)
                throw new TestFailure("illegal size: " + size);

            int sizeWord = size - 1;
            byte data[] = new byte[4];
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
            if (this.parcel.length != other.parcel.length)
                return false;
            int size = parcel.length;
            for (int i = 0; i < size; i++)
                if (this.parcel[i] != other.parcel[i])
                    return false;
            return true;
        }

    }

    
    static class TestFailure extends RuntimeException {
        
        public TestFailure(String purpose) {
            super(purpose);
        }

    }

}
