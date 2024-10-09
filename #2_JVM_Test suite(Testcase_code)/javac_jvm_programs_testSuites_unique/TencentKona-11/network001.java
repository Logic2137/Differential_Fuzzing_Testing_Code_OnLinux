



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


public class network001 {
    
    private static final int DATA_PARCELS = 2000;

    
    private static final int MAX_PARCEL = 250;

    
    static private final boolean DEBUG_MODE = false;

    
    static private PrintStream out = System.out;

    
    static private synchronized void println(Object message) {
        out.println(message.toString());
    }

    
    static private void display(Object report) {
        if (DEBUG_MODE)
            println(report.toString());
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

                Random random = new Random(0);

                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        println("Server thread got unexpected parcel:");
                        println("sample=" + sample);
                        println("etalon=" + etalon);
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

        
        public void run() {
            try {
                InputStream istream = socket.getInputStream();
                OutputStream ostream = socket.getOutputStream();

                Random random = new Random(0);

                for (int i = 0; i < DATA_PARCELS; i++) {
                    Parcel etalon = new Parcel(random);
                    etalon.send(ostream);
                    ostream.flush();

                    Parcel sample = new Parcel(istream); 
                    if (!sample.equals(etalon)) {
                        println("Client thread got unexpected parcel:");
                        println("sample=" + sample);
                        println("etalon=" + etalon);
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
        network001.out = out;

        
        
        

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
                    println("    java network001");
                    println("or:");
                    println("    java network001 ${IP_ADDRESS}");
                    println("or:");
                    println("    java network001 ${HOST_NAME}");
                    println("or:");
                    println("    java network001 localhost");
                    return 2; 
            }
        } catch (UnknownHostException exception) {
            println(exception);
            return 2; 
        }
        display("Host: " + address);

        
        
        

        Server server = null;
        try {
            server = new Server(address);
        } catch (IOException io) {
            println("Failed to create server: " + io);
            return 2;
        }
        display("Server: " + server);

        int port = server.getPort();

        Client client = null;
        try {
            client = new Client(address, port);
        } catch (IOException io) {
            out.println("Failed to create client: " + io);
            return 2;
        }
        display("Client socket: " + client);

        
        
        

        Exception exception = null;
        try {
            server.start();
            client.start();
            while (client.isAlive() || server.isAlive())
                if (client.exception == null && server.exception == null)
                    Thread.yield();
                else
                    break;
        } catch (TestFailure failure) {
            exception = failure;
        }

        

        Exception problem[] = new Exception[3];
        problem[0] = exception;
        problem[1] = server.exception;
        problem[2] = client.exception;

        int exitCode = 0;

        for (int i = 0; i < 3; i++)
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
