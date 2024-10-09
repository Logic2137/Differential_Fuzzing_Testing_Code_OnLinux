




package nsk.stress.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class network003 {
    
    static private final boolean DEBUG_MODE = false;

    
    static private PrintStream out = System.out;

    
    static private synchronized void println(Object message) {
        out.println(message.toString());
    }

    
    static private void display(Object report) {
        if (DEBUG_MODE)
            println(report.toString());
    }

    
    private final static int MAX_CONNECTIONS = 128;

    
    private final static int CONNECTIONS_RESERVE = 10;

    
    private static final int CONNECTIONS = detectOSLimitation();

    
    private static final int DATA_PARCELS = 128;

    
    private static final int MAX_PARCEL = 1 << 8;

    
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
            println("# ------------------------- CAUTION: -------------------");
            println("# While checking the OS limitations, the test found that");
            println("# only " + i + " TCP/IP socket connections could be safely open");
            println("# simultaneously. However, possibility to open at least");
            println("# " + MAX_CONNECTIONS + "+" + CONNECTIONS_RESERVE
                    + " connections were expected.");
            println("# ");
            println("# So, the test will check only " + safeConnections + " connection"
                    + (safeConnections == 1 ? "" : "s") + " which seem");
            println("# safe to be open simultaneously.");
            println("# ------------------------------------------------------");
        }
        return safeConnections;
    }

    
    static private class Server extends Thread {
        
        private ServerSocket serverSocket;

        
        public String toString() {
            return serverSocket.toString();
        }

        
        Exception exception = null;

        
        int getPort() {
            return serverSocket.getLocalPort();
        }

        
        Server(InetAddress address) throws IOException {
            int someFreePort = 0;
            int backlog = 50; 
            serverSocket = new ServerSocket(someFreePort, backlog, address);
        }

        
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                display("Server socket: " + socket);

                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();

                Random random = new Random(getPort());

                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        println("Server thread for port #"
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

    
    static private class Client extends Thread {
        
        private Socket socket;

        
        public String toString() {
            return socket.toString();
        }

        
        Exception exception = null;

        
        Client(InetAddress address, int port) throws IOException {
            socket = new Socket(address, port);
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
                        println("Client thread for port #"
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

    
    public static int run(String args[], PrintStream out) {
        network003.out = out;

        
        
        

        InetAddress address = null;
        try {
            switch (args.length) {
                case 0:
                    address = InetAddress.getLocalHost();
                    break;
                case 1:
                    String hostName = args[0];
                    address = InetAddress.getByName(args[0]);
                    break;
                default:
                    println("Use:");
                    println("    java network003");
                    println("or:");
                    println("    java network003 ${IP_ADDRESS}");
                    println("or:");
                    println("    java network003 ${HOST_NAME}");
                    println("or:");
                    println("    java network003 localhost");
                    return 2; 
            }
        } catch (UnknownHostException exception) {
            println(exception);
            return 2; 
        }
        display("Host: " + address);

        
        
        

        Server server[] = new Server[CONNECTIONS];
        Client client[] = new Client[CONNECTIONS];

        for (int i = 0; i < CONNECTIONS; i++) {
            try {
                server[i] = new Server(address);
            } catch (IOException io) {
                println("Failed to create server #" + i + ": " + io);
                return 2;
            }
            display("Server #" + i + ": " + server[i]);
        }

        for (int i = 0; i < CONNECTIONS; i++) {
            int port = server[i].getPort();
            try {
                client[i] = new Client(address, port);
            } catch (IOException io) {
                out.println("Failed to create client #" + i + ": " + io);
                return 2;
            }
            display("Client socket #" + i + ": " + client[i]);
        }

        
        
        

        Exception exception = null;
        try {
            for (int i = 0; i < CONNECTIONS; i++)
                server[i].start();
            for (int i = 0; i < CONNECTIONS; i++)
                client[i].start();
            boolean someIsAlive = true;
            while (someIsAlive) {
                boolean aliveFound = false;
                boolean someBroken = false;
                for (int i = 0; i < CONNECTIONS; i++)
                    if (client[i].isAlive() || server[i].isAlive()) {
                        if ((client[i].exception != null) ||
                                (server[i].exception != null))
                            someBroken = true;
                        aliveFound = true;
                        Thread.yield();
                    }
                someIsAlive = aliveFound;
                if (someBroken)
                    break;
            }
        } catch (TestFailure failure) {
            exception = failure;
        }

        

        Exception problem[] = new Exception[2 * CONNECTIONS + 1];
        problem[0] = exception;
        for (int i = 0; i < CONNECTIONS; i++) {
            problem[2 * i + 1] = server[i].exception;
            problem[2 * i + 2] = client[i].exception;
        }

        int exitCode = 0;

        for (int i = 0; i < 2 * CONNECTIONS + 1; i++)
            if (problem[i] != null) {
                out.println("#### OOPS ! ####");
                problem[i].printStackTrace(out);
                exitCode = 2;
            }

        if (exitCode != 0) {
            out.println("#### OOPS ! ####");
            out.println("# Test failed.");
            return 2; 
        }
        display("Test passed.");
        return 0; 
    }

    
    public static void main(String args[]) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
        
    }

}
