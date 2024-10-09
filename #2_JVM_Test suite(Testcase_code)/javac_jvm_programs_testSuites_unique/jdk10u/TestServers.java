



import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestServers {

    private TestServers() { }

    
    static abstract class AbstractServer {

        private AbstractServer() {
        }

        public abstract int getPort();

        public abstract InetAddress getAddress();
    }

    
    static class RefusingServer extends AbstractServer {

        final InetAddress address;
        final int port;

        private RefusingServer(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public InetAddress getAddress() {
            return address;
        }

        public static RefusingServer newRefusingServer() throws IOException {
            
            
            
            
            return new RefusingServer(InetAddress.getLocalHost(), 1);
        }
    }

    
    static abstract class AbstractTcpServer extends AbstractServer
            implements Runnable, Closeable {

        protected final long linger; 
        private Thread acceptThread; 
        
        private List<TcpConnectionThread> connections = new ArrayList<>();
        private ServerSocket serverSocket; 
        private boolean started = false; 
        Throwable error = null;

        
        protected AbstractTcpServer(long linger) {
            this.linger = linger;
        }

        
        @Override
        public final synchronized int getPort() {
            if (!started) {
                throw new IllegalStateException("Not started");
            }
            return serverSocket.getLocalPort();
        }

        
        @Override
        public final synchronized InetAddress getAddress() {
            if (!started) {
                throw new IllegalStateException("Not started");
            }
            return serverSocket.getInetAddress();
        }

        
        public final synchronized boolean isStarted() {
            return started;
        }

        
        protected ServerSocket newServerSocket(int port, int backlog,
                InetAddress address)
                throws IOException {
            return new ServerSocket(port, backlog, address);
        }

        
        public final synchronized void start() throws IOException {
            if (started) {
                return;
            }
            final ServerSocket socket =
                    newServerSocket(0, 100, InetAddress.getLocalHost());
            serverSocket = socket;
            acceptThread = new Thread(this);
            acceptThread.setDaemon(true);
            acceptThread.start();
            started = true;
        }

        
        protected final void lingerIfRequired() {
            if (linger > 0) {
                try {
                    Thread.sleep(linger);
                } catch (InterruptedException x) {
                    Thread.interrupted();
                    final ServerSocket socket = serverSocket();
                    if (socket != null && !socket.isClosed()) {
                        System.err.println("Thread interrupted...");
                    }
                }
            }
        }

        final synchronized ServerSocket serverSocket() {
            return this.serverSocket;
        }

        
        @Override
        public final void run() {
            final ServerSocket sSocket = serverSocket();
            try {
                Socket s;
                while (isStarted() && !Thread.interrupted()
                        && (s = sSocket.accept()) != null) {
                    lingerIfRequired();
                    listen(s);
                }
            } catch (Exception x) {
                error = x;
            } finally {
                synchronized (this) {
                    if (!sSocket.isClosed()) {
                        try {
                            sSocket.close();
                        } catch (IOException x) {
                            System.err.println("Failed to close server socket");
                        }
                    }
                    if (started && this.serverSocket == sSocket) {
                        started = false;
                        this.serverSocket = null;
                        this.acceptThread = null;
                    }
                }
            }
        }

        
        protected abstract class TcpConnectionThread extends Thread {

            protected final Socket socket;

            protected TcpConnectionThread(Socket socket) {
                this.socket = socket;
                this.setDaemon(true);
            }

            public void close() throws IOException {
                socket.close();
                interrupt();
            }
        }

        
        protected abstract TcpConnectionThread createConnection(Socket s);

        
        private synchronized void listen(Socket s) {
            TcpConnectionThread c = createConnection(s);
            c.start();
            addConnection(c);
        }

        
        protected synchronized void addConnection(
                TcpConnectionThread connection) {
            connections.add(connection);
        }

        
        protected synchronized void removeConnection(
                TcpConnectionThread connection) {
            connections.remove(connection);
        }

        
        @Override
        public synchronized void close() throws IOException {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (acceptThread != null) {
                acceptThread.interrupt();
            }
            int failed = 0;
            for (TcpConnectionThread c : connections) {
                try {
                    c.close();
                } catch (IOException x) {
                    
                    failed++;
                }
            }
            connections.clear();
            if (failed > 0) {
                throw new IOException("Failed to close some connections");
            }
        }
    }

    
    static class EchoServer extends AbstractTcpServer {

        public EchoServer() {
            this(0L);
        }

        public EchoServer(long linger) {
            super(linger);
        }

        @Override
        protected TcpConnectionThread createConnection(Socket s) {
            return new EchoConnection(s);
        }

        private final class EchoConnection extends TcpConnectionThread {

            public EchoConnection(Socket socket) {
                super(socket);
            }

            @Override
            public void run() {
                try {
                    final InputStream is = socket.getInputStream();
                    final OutputStream out = socket.getOutputStream();
                    byte[] b = new byte[255];
                    int n;
                    while ((n = is.read(b)) > 0) {
                        lingerIfRequired();
                        out.write(b, 0, n);
                    }
                } catch (IOException io) {
                    
                } finally {
                    if (!socket.isClosed()) {
                        try {
                            socket.close();
                        } catch (IOException x) {
                            System.err.println(
                                    "Failed to close echo connection socket");
                        }
                    }
                    removeConnection(this);
                }
            }
        }

        public static EchoServer startNewServer() throws IOException {
            return startNewServer(0);
        }

        public static EchoServer startNewServer(long linger) throws IOException {
            final EchoServer echoServer = new EchoServer(linger);
            echoServer.start();
            return echoServer;
        }
    }

    
    static final class NoResponseServer extends EchoServer {
        public NoResponseServer() {
            super(Long.MAX_VALUE);
        }

        public static NoResponseServer startNewServer() throws IOException {
            final NoResponseServer noResponseServer = new NoResponseServer();
            noResponseServer.start();
            return noResponseServer;
        }
    }

    
    static final class DayTimeServer extends AbstractTcpServer {

        public DayTimeServer() {
            this(0L);
        }

        public DayTimeServer(long linger) {
            super(linger);
        }

        @Override
        protected TcpConnectionThread createConnection(Socket s) {
            return new DayTimeServerConnection(s);
        }

        @Override
        protected void addConnection(TcpConnectionThread connection) {
            
        }

        @Override
        protected void removeConnection(TcpConnectionThread connection) {
            
        }

        private final class DayTimeServerConnection extends TcpConnectionThread {

            public DayTimeServerConnection(Socket socket) {
                super(socket);
            }

            @Override
            public void run() {
                try {
                    final OutputStream out = socket.getOutputStream();
                    lingerIfRequired();
                    out.write(new Date(System.currentTimeMillis())
                            .toString().getBytes("US-ASCII"));
                    out.flush();
                } catch (IOException io) {
                    
                } finally {
                    if (!socket.isClosed()) {
                        try {
                            socket.close();
                        } catch (IOException x) {
                            System.err.println(
                                    "Failed to close echo connection socket");
                        }
                    }
                }
            }
        }

        public static DayTimeServer startNewServer()
                throws IOException {
            return startNewServer(0);
        }

        public static DayTimeServer startNewServer(long linger)
                throws IOException {
            final DayTimeServer daytimeServer = new DayTimeServer(linger);
            daytimeServer.start();
            return daytimeServer;
        }
    }

    
    static abstract class AbstractUdpServer extends AbstractServer
            implements Runnable, Closeable {

        protected final long linger; 
        private Thread acceptThread; 
        private DatagramSocket serverSocket; 
        private boolean started = false; 
        Throwable error = null;

        
        protected AbstractUdpServer(long linger) {
            this.linger = linger;
        }

        
        @Override
        public final synchronized int getPort() {
            if (!started) {
                throw new IllegalStateException("Not started");
            }
            return serverSocket.getLocalPort();
        }

        
        @Override
        public final synchronized InetAddress getAddress() {
            if (!started) {
                throw new IllegalStateException("Not started");
            }
            return serverSocket.getLocalAddress();
        }

        
        public final synchronized boolean isStarted() {
            return started;
        }

        
        protected DatagramSocket newDatagramSocket(int port,
                InetAddress address)
                throws IOException {
            return new DatagramSocket(port, address);
        }

        
        public final synchronized void start() throws IOException {
            if (started) {
                return;
            }
            final DatagramSocket socket =
                    newDatagramSocket(0, InetAddress.getLocalHost());
            serverSocket = socket;
            acceptThread = new Thread(this);
            acceptThread.setDaemon(true);
            acceptThread.start();
            started = true;
        }

        
        protected final void lingerIfRequired() {
            if (linger > 0) {
                try {
                    Thread.sleep(linger);
                } catch (InterruptedException x) {
                    Thread.interrupted();
                    final DatagramSocket socket = serverSocket();
                    if (socket != null && !socket.isClosed()) {
                        System.err.println("Thread interrupted...");
                    }
                }
            }
        }

        final synchronized DatagramSocket serverSocket() {
            return this.serverSocket;
        }

        final synchronized boolean send(DatagramSocket socket,
                DatagramPacket response) throws IOException {
            if (!socket.isClosed()) {
                socket.send(response);
                return true;
            } else {
                return false;
            }
        }

        
        @Override
        public final void run() {
            final DatagramSocket sSocket = serverSocket();
            try {
                final int size = Math.max(1024, sSocket.getReceiveBufferSize());
                if (size > sSocket.getReceiveBufferSize()) {
                    sSocket.setReceiveBufferSize(size);
                }
                while (isStarted() && !Thread.interrupted() && !sSocket.isClosed()) {
                    final byte[] buf = new byte[size];
                    final DatagramPacket packet =
                            new DatagramPacket(buf, buf.length);
                    lingerIfRequired();
                    sSocket.receive(packet);
                    
                    
                    handle(sSocket, packet);
                }
            } catch (Exception x) {
                error = x;
            } finally {
                synchronized (this) {
                    if (!sSocket.isClosed()) {
                        sSocket.close();
                    }
                    if (started && this.serverSocket == sSocket) {
                        started = false;
                        this.serverSocket = null;
                        this.acceptThread = null;
                    }
                }
            }
        }

        
        protected abstract class UdpRequestThread extends Thread {

            protected final DatagramPacket request;
            protected final DatagramSocket socket;

            protected UdpRequestThread(DatagramSocket socket, DatagramPacket request) {
                this.socket = socket;
                this.request = request;
                this.setDaemon(true);
            }
        }

        
        protected abstract UdpRequestThread createConnection(DatagramSocket socket,
                DatagramPacket request);

        
        private synchronized void handle(DatagramSocket socket,
                DatagramPacket request) {
            UdpRequestThread c = createConnection(socket, request);
            
            if (c != null) {
                c.start();
            }
        }

        
        @Override
        public synchronized void close() throws IOException {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (acceptThread != null) {
                acceptThread.interrupt();
            }
        }
    }

    
    static final class UdpDiscardServer extends AbstractUdpServer {

        public UdpDiscardServer() {
            this(0L);
        }

        public UdpDiscardServer(long linger) {
            super(linger);
        }

        @Override
        protected UdpRequestThread createConnection(DatagramSocket socket,
                DatagramPacket request) {
            
            return null;
        }

        public static UdpDiscardServer startNewServer() throws IOException {
            return startNewServer(0);
        }

        public static UdpDiscardServer startNewServer(long linger) throws IOException {
            final UdpDiscardServer discardServer = new UdpDiscardServer(linger);
            discardServer.start();
            return discardServer;
        }
    }

    
    static final class UdpEchoServer extends AbstractUdpServer {

        public UdpEchoServer() {
            this(0L);
        }

        public UdpEchoServer(long linger) {
            super(linger);
        }

        @Override
        protected UdpEchoRequest createConnection(DatagramSocket socket,
                DatagramPacket request) {
            return new UdpEchoRequest(socket, request);
        }

        private final class UdpEchoRequest extends UdpRequestThread {

            public UdpEchoRequest(DatagramSocket socket, DatagramPacket request) {
                super(socket, request);
            }

            @Override
            public void run() {
                try {
                    lingerIfRequired();
                    final DatagramPacket response =
                            new DatagramPacket(request.getData(),
                                    request.getOffset(), request.getLength(),
                                    request.getAddress(), request.getPort());
                    send(socket, response);
                } catch (IOException io) {
                    System.err.println("Failed to send response: " + io);
                    io.printStackTrace(System.err);
                }
            }
        }

        public static UdpEchoServer startNewServer() throws IOException {
            return startNewServer(0);
        }

        public static UdpEchoServer startNewServer(long linger) throws IOException {
            final UdpEchoServer echoServer = new UdpEchoServer(linger);
            echoServer.start();
            return echoServer;
        }
    }

    
    static final class UdpDayTimeServer extends AbstractUdpServer {

        public UdpDayTimeServer() {
            this(0L);
        }

        public UdpDayTimeServer(long linger) {
            super(linger);
        }

        @Override
        protected UdpDayTimeRequestThread createConnection(DatagramSocket socket,
                DatagramPacket request) {
            return new UdpDayTimeRequestThread(socket, request);
        }

        private final class UdpDayTimeRequestThread extends UdpRequestThread {

            public UdpDayTimeRequestThread(DatagramSocket socket,
                    DatagramPacket request) {
                super(socket, request);
            }

            @Override
            public void run() {
                try {
                    lingerIfRequired();
                    final byte[] data = new Date(System.currentTimeMillis())
                            .toString().getBytes("US-ASCII");
                    final DatagramPacket response =
                            new DatagramPacket(data, 0, data.length,
                                    request.getAddress(), request.getPort());
                    send(socket, response);
                } catch (IOException io) {
                    System.err.println("Failed to send response: " + io);
                    io.printStackTrace(System.err);
                }
            }
        }

        public static UdpDayTimeServer startNewServer() throws IOException {
            return startNewServer(0);
        }

        public static UdpDayTimeServer startNewServer(long linger)
                throws IOException {
            final UdpDayTimeServer echoServer = new UdpDayTimeServer(linger);
            echoServer.start();
            return echoServer;
        }
    }
}
