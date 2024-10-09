





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


public class network002 {
    
    private static int SO_TIMEOUT;

    
    private static final int DATA_PARCELS = 2000;

    
    private static final int MAX_PARCEL = 250;

    
    static private final boolean DEBUG_MODE = false;

    

    
    public static void main(String args[]) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
        
    }

    
    public static int run(String args[], PrintStream out) {
        network002 test = new network002(out);
        int exitCode = test.run(args);
        return exitCode;
    }

    
    private int run(String args[]) {
        
        
        
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
                    complain("    java network002 $JAVA_COMMAND " +
                            "[$IP_ADDRESS | $HOST_NAME | localhost]");
                    return 2; 
            }
        } catch (UnknownHostException exception) {
            complain(exception.toString());
            return 2; 
        }
        display("Host: " + address);

        
        
        
        Server server = null;
        try {
            server = new Server(address);
            server.start();
        } catch (Exception exception) {
            complain("Failed to start server: " + exception);
            return 2;
        }
        display("Server: " + server);

        
        
        
        String IPAddress = server.getIPAddress(); 
        int port = server.getPort();
        String command = args[0] + " " + network002.class.getName() + "$Client " + IPAddress + " " + port;
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

        } catch (Exception exception) {
            complain("Failed to start client: " + exception);
            return 2;
        }

        
        
        
        try {
            client.waitFor();
            if (redirectOut.isAlive())
                redirectOut.join();
            if (redirectErr.isAlive())
                redirectErr.join();

            
            int clientStatus = client.exitValue();
            if (clientStatus != 95) {
                complain("");
                complain("Client VM has crashed: exit status=" + clientStatus);
                if (server.isAlive())
                    complain("Server also should be terminated.");
                complain("Test failed.");
                return 2; 
            }

            
            if (server.isAlive())
                server.join();

        } catch (Exception exception) {
            complain("Test interrupted: " + exception);
            complain("Test failed.");
            return 2; 
        }

        
        
        

        if (server.exception != null) {
            complain("Server exception: " + server.exception);
            complain("Test failed.");
            return 2; 
        }

        display("Test passed.");
        return 0; 
    }

    

    
    private network002(PrintStream out) {
        this.out = out;
    }

    
    private PrintStream out;

    
    private void complain(Object message) {
        out.println("# " + message);
        out.flush();
    }

    
    private void display(Object report) {
        if (DEBUG_MODE)
            out.println(report.toString());
        out.flush();
    }

    

    
    private class Server extends Thread {
        
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

                Random random = new Random(0);

                for (int i = 0; i < DATA_PARCELS; i++) {
                    display("Server: i=" + i);
                    Parcel etalon = new Parcel(random);

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        complain("Server got unexpected parcel:\n"
                                + "sample=" + sample + "\n"
                                + "etalon=" + etalon);
                        throw new TestFailure(
                                "the parcel just read seems wrong for server");
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

    

    
    private static class Client {
        
        private static void complain(Object message) {
            System.err.println("# " + message);
            System.err.flush();
        }

        
        private static void display(Object message) {
            System.out.println(message.toString());
            System.out.flush();
        }

        
        private static void exit(int exitCode) {
            System.exit(exitCode + 95);
        }

        
        public static void main(String args[]) {
            if (args.length != 2) {
                complain("Illegal number of client paramenets, try:");
                complain("    java network002$Client IP-address port");
                exit(2); 
            }

            try {
                InetAddress address = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);

                Socket socket = new Socket(address, port);
                socket.setSoTimeout(SO_TIMEOUT);
                display("Client: " + socket);

                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();

                Random random = new Random(0);

                for (int i = 0; i < DATA_PARCELS; i++) {
                    display("Client: i=" + i);
                    Parcel etalon = new Parcel(random);
                    etalon.send(ostream);
                    ostream.flush();

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        complain("Client got unexpected parcel:\n"
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

            } catch (Exception exception) {
                complain("Client exception: " + exception);
                exit(2); 
            }
            exit(0); 
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
