
package nsk.jdi.VirtualMachineManager.createVirtualMachine;

import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.spi.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class CreateVM005_TranspServ extends TransportService {

    static class SocketListenKey extends ListenKey {

        ServerSocket ss;

        SocketListenKey(ServerSocket ss) {
            this.ss = ss;
        }

        ServerSocket socket() {
            return ss;
        }

        public String address() {
            InetAddress localaddr = ss.getInetAddress();
            int port = ss.getLocalPort();
            if (localaddr.isAnyLocalAddress()) {
                return "" + port;
            } else {
                return localaddr + ":" + port;
            }
        }

        public String toString() {
            return address();
        }
    }

    void handshake(Socket s, long timeout) throws IOException {
        s.setSoTimeout((int) timeout);
        byte[] hello = "JDWP-Handshake".getBytes("UTF-8");
        s.getOutputStream().write(hello);
        byte[] b = new byte[hello.length];
        int received = 0;
        while (received < hello.length) {
            int n;
            try {
                n = s.getInputStream().read(b, received, hello.length - received);
            } catch (SocketTimeoutException x) {
                throw new IOException("##> CreateVM005_TranspServ: Handshake timeout");
            }
            if (n < 0) {
                s.close();
                throw new IOException("##> CreateVM005_TranspServ: Handshake FAILED - connection prematurally closed!");
            }
            received += n;
        }
        for (int i = 0; i < hello.length; i++) {
            if (b[i] != hello[i]) {
                throw new IOException("##> CreateVM005_TranspServ: Handshake FAILED - unrecognized message from target VM");
            }
        }
        s.setSoTimeout(0);
    }

    public CreateVM005_TranspServ() {
    }

    public String name() {
        return "CreateVM005_TranspServ";
    }

    public String description() {
        return "SocketTransportService for nsk/jdi/VirtualMachineManager/createVirtualMachine/createVM005 test.";
    }

    public Capabilities capabilities() {
        return new CreateVM005_TranspServCapabilities();
    }

    public Connection attach(String address, long attachTimeout, long handshakeTimeout) throws IOException {
        if (address == null) {
            throw new NullPointerException("##> CreateVM005_TranspServ: attach() - address is null");
        }
        if (attachTimeout < 0 || handshakeTimeout < 0) {
            throw new IllegalArgumentException("##> CreateVM005_TranspServ: attach() - timeout is negative");
        }
        int splitIndex = address.indexOf(':');
        String host;
        String portStr;
        if (splitIndex < 0) {
            host = InetAddress.getLocalHost().getHostName();
            portStr = address;
        } else {
            host = address.substring(0, splitIndex);
            portStr = address.substring(splitIndex + 1);
        }
        int port;
        try {
            port = Integer.decode(portStr).intValue();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("##> CreateVM005_TranspServ: attach() - unable to parse port number in address");
        }
        InetSocketAddress sa = new InetSocketAddress(host, port);
        Socket s = new Socket();
        try {
            s.connect(sa, (int) attachTimeout);
        } catch (SocketTimeoutException exc) {
            try {
                s.close();
            } catch (IOException x) {
            }
            throw new TransportTimeoutException("##> CreateVM005_TranspServ: attach() - timed out trying to establish connection");
        }
        try {
            handshake(s, handshakeTimeout);
        } catch (IOException exc) {
            try {
                s.close();
            } catch (IOException x) {
            }
            throw exc;
        }
        return new CreateVM005_Connection(s);
    }

    ListenKey startListening(String localaddress, int port) throws IOException {
        InetSocketAddress sa;
        if (localaddress == null) {
            sa = new InetSocketAddress(port);
        } else {
            sa = new InetSocketAddress(localaddress, port);
        }
        ServerSocket ss = new ServerSocket();
        if (port == 0) {
            ss.setReuseAddress(false);
        }
        ss.bind(sa);
        return new SocketListenKey(ss);
    }

    public ListenKey startListening(String address) throws IOException {
        if (address == null || address.length() == 0) {
            address = "0";
        }
        int splitIndex = address.indexOf(':');
        String localaddr = null;
        if (splitIndex >= 0) {
            localaddr = address.substring(0, splitIndex);
            address = address.substring(splitIndex + 1);
        }
        int port;
        try {
            port = Integer.decode(address).intValue();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("##> CreateVM005_TranspServ: startListening() - unable to parse port number in address");
        }
        return startListening(localaddr, port);
    }

    public ListenKey startListening() throws IOException {
        return startListening(null, 0);
    }

    public void stopListening(ListenKey listener) throws IOException {
        if (!(listener instanceof SocketListenKey)) {
            throw new IllegalArgumentException("##> CreateVM005_TranspServ: stopListening() - Invalid listener");
        }
        synchronized (listener) {
            ServerSocket ss = ((SocketListenKey) listener).socket();
            if (ss.isClosed()) {
                throw new IllegalArgumentException("##> CreateVM005_TranspServ: stopListening() - Invalid listener");
            }
            ss.close();
        }
    }

    public Connection accept(ListenKey listener, long acceptTimeout, long handshakeTimeout) throws IOException {
        if (acceptTimeout < 0 || handshakeTimeout < 0) {
            throw new IllegalArgumentException("##> CreateVM005_TranspServ: accept() - timeout is negative");
        }
        if (!(listener instanceof SocketListenKey)) {
            throw new IllegalArgumentException("##> CreateVM005_TranspServ: accept() - Invalid listener");
        }
        ServerSocket ss;
        synchronized (listener) {
            ss = ((SocketListenKey) listener).socket();
            if (ss.isClosed()) {
                throw new IllegalArgumentException("##> CreateVM005_TranspServ: accept() - Invalid listener");
            }
        }
        ss.setSoTimeout((int) acceptTimeout);
        Socket s;
        try {
            s = ss.accept();
        } catch (SocketTimeoutException x) {
            throw new TransportTimeoutException("##> CreateVM005_TranspServ: accept() - timeout waiting for connection");
        }
        handshake(s, handshakeTimeout);
        return new CreateVM005_Connection(s);
    }

    public String toString() {
        return name();
    }
}

class CreateVM005_Connection extends Connection {

    private Socket socket;

    private boolean closed = false;

    private OutputStream socketOutput;

    private InputStream socketInput;

    private Object receiveLock = new Object();

    private Object sendLock = new Object();

    private Object closeLock = new Object();

    CreateVM005_Connection(Socket socket) throws IOException {
        this.socket = socket;
        socket.setTcpNoDelay(true);
        socketInput = socket.getInputStream();
        socketOutput = socket.getOutputStream();
    }

    public void close() throws IOException {
        synchronized (closeLock) {
            if (closed) {
                return;
            }
            socketOutput.close();
            socketInput.close();
            socket.close();
            closed = true;
        }
    }

    public boolean isOpen() {
        synchronized (closeLock) {
            return !closed;
        }
    }

    public byte[] readPacket() throws IOException {
        if (!isOpen()) {
            throw new ClosedConnectionException("##> CreateVM005_Connection: readPacket() - connection is closed");
        }
        synchronized (receiveLock) {
            int b1, b2, b3, b4;
            try {
                b1 = socketInput.read();
                b2 = socketInput.read();
                b3 = socketInput.read();
                b4 = socketInput.read();
            } catch (IOException ioe) {
                if (!isOpen()) {
                    throw new ClosedConnectionException("##> CreateVM005_Connection: readPacket() - connection is closed");
                } else {
                    throw ioe;
                }
            }
            if (b1 < 0 || b2 < 0 || b3 < 0 || b4 < 0)
                throw new EOFException();
            int len = ((b1 << 24) | (b2 << 16) | (b3 << 8) | (b4 << 0));
            if (len < 0) {
                throw new IOException("##> CreateVM005_Connection: readPacket() - protocol error: invalid Packet's length");
            }
            byte[] b = new byte[len];
            b[0] = (byte) b1;
            b[1] = (byte) b2;
            b[2] = (byte) b3;
            b[3] = (byte) b4;
            int off = 4;
            len -= off;
            while (len > 0) {
                int count;
                try {
                    count = socketInput.read(b, off, len);
                } catch (IOException ioe) {
                    if (!isOpen()) {
                        throw new ClosedConnectionException("##> CreateVM005_Connection: readPacket() - connection is closed");
                    } else {
                        throw ioe;
                    }
                }
                if (count < 0) {
                    throw new EOFException("##> CreateVM005_Connection: readPacket() - read() method returns negative value");
                }
                len -= count;
                off += count;
            }
            return b;
        }
    }

    public void writePacket(byte[] b) throws IOException {
        if (!isOpen()) {
            throw new ClosedConnectionException("##> CreateVM005_Connection: writePacket() - connection is closed");
        }
        if (b.length < 11) {
            throw new IllegalArgumentException("##> CreateVM005_Connection: writePacket() - packet is insufficient size");
        }
        int b0 = b[0] & 0xff;
        int b1 = b[1] & 0xff;
        int b2 = b[2] & 0xff;
        int b3 = b[3] & 0xff;
        int len = ((b0 << 24) | (b1 << 16) | (b2 << 8) | (b3 << 0));
        if (len < 11) {
            throw new IllegalArgumentException("##> CreateVM005_Connection: writePacket() - packet is insufficient size");
        }
        if (len > b.length) {
            throw new IllegalArgumentException("##> CreateVM005_Connection: writePacket() - length mis-match");
        }
        synchronized (sendLock) {
            try {
                socketOutput.write(b, 0, len);
            } catch (IOException ioe) {
                if (!isOpen()) {
                    throw new ClosedConnectionException("##> CreateVM005_Connection: writePacket() - connection is closed");
                } else {
                    throw ioe;
                }
            }
        }
    }
}

class CreateVM005_TranspServCapabilities extends TransportService.Capabilities {

    public boolean supportsMultipleConnections() {
        return true;
    }

    public boolean supportsAttachTimeout() {
        return true;
    }

    public boolean supportsAcceptTimeout() {
        return true;
    }

    public boolean supportsHandshakeTimeout() {
        return true;
    }
}
