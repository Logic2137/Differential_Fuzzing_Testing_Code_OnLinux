
package nsk.share.jdi;

import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.spi.*;
import java.io.*;
import java.util.*;

public class PlugTransportService extends TransportService {

    String plugTransportServiceName = "Undefined_PlugTransportService_Name";

    String plugTransportServiceDescription = "Undefined_PlugTransportService_Description";

    TransportService.Capabilities plugTransportServiceCapabilities = new TestCapabilities();

    public static class TestCapabilities extends TransportService.Capabilities {

        boolean supportsAcceptTimeout = false;

        boolean supportsAttachTimeout = false;

        boolean supportsHandshakeTimeout = false;

        boolean supportsMultipleConnections = false;

        public TestCapabilities() {
        }

        public TestCapabilities(boolean supportsAcceptTimeout, boolean supportsAttachTimeout, boolean supportsHandshakeTimeout, boolean supportsMultipleConnections) {
            this.supportsAcceptTimeout = supportsAcceptTimeout;
            this.supportsAttachTimeout = supportsAttachTimeout;
            this.supportsHandshakeTimeout = supportsHandshakeTimeout;
            this.supportsMultipleConnections = supportsMultipleConnections;
        }

        public boolean supportsAcceptTimeout() {
            return supportsAcceptTimeout;
        }

        public boolean supportsAttachTimeout() {
            return supportsAttachTimeout;
        }

        public boolean supportsHandshakeTimeout() {
            return supportsHandshakeTimeout;
        }

        public boolean supportsMultipleConnections() {
            return supportsMultipleConnections;
        }
    }

    public static class TestListenKey extends TransportService.ListenKey {

        String address = null;

        public TestListenKey() {
        }

        public TestListenKey(String address) {
            this.address = address;
        }

        public String address() {
            return address;
        }
    }

    public PlugTransportService() {
    }

    public PlugTransportService(String plugTransportServiceName, String plugTransportServiceDescription, TransportService.Capabilities plugTransportServiceCapabilities) {
        this.plugTransportServiceName = plugTransportServiceName;
        this.plugTransportServiceDescription = plugTransportServiceDescription;
        this.plugTransportServiceCapabilities = plugTransportServiceCapabilities;
    }

    public String name() {
        return plugTransportServiceName;
    }

    public String description() {
        return plugTransportServiceDescription;
    }

    public TransportService.Capabilities capabilities() {
        return plugTransportServiceCapabilities;
    }

    public Connection attach(String address, long attachTimeout, long handshakeTimeout) throws IOException {
        String exceptionMessage = "## PlugTransportService: TransportService name = '" + plugTransportServiceName + "';\nNon-authorized call of attach(...) method!";
        if (true) {
            throw new RuntimeException(exceptionMessage);
        }
        return null;
    }

    public TransportService.ListenKey startListening(String address) throws IOException {
        String exceptionMessage = "## PlugTransportService: TransportService name = '" + plugTransportServiceName + "';\nNon-authorized call of startListening(...) method!";
        if (true) {
            throw new RuntimeException(exceptionMessage);
        }
        return null;
    }

    public TransportService.ListenKey startListening() throws IOException {
        String exceptionMessage = "## PlugTransportService: TransportService name = '" + plugTransportServiceName + "';\nNon-authorized call of startListening() method!";
        if (true) {
            throw new RuntimeException(exceptionMessage);
        }
        return null;
    }

    public void stopListening(TransportService.ListenKey listenKey) throws IOException {
        String exceptionMessage = "## PlugTransportService: TransportService name = '" + plugTransportServiceName + "';\nNon-authorized call of stopListening() method!";
        if (true) {
            throw new RuntimeException(exceptionMessage);
        }
    }

    public Connection accept(TransportService.ListenKey listenKey, long acceptTimeout, long handshakeTimeout) throws IOException {
        String exceptionMessage = "## PlugTransportService: TransportService name = '" + plugTransportServiceName + "';\nNon-authorized call of accept(...) method!";
        if (true) {
            throw new RuntimeException(exceptionMessage);
        }
        return null;
    }

    public static class PlugTransportServiceConnection extends Connection {

        public void close() throws IOException {
            String exceptionMessage = "## PlugTransportConnection: \nNon-authorized call of close() method!";
            if (true) {
                throw new RuntimeException(exceptionMessage);
            }
        }

        public boolean isOpen() {
            String exceptionMessage = "## PlugTransportConnection: \nNon-authorized call of isOpen() method!";
            if (true) {
                throw new RuntimeException(exceptionMessage);
            }
            return false;
        }

        public byte[] readPacket() throws IOException {
            String exceptionMessage = "## PlugTransportConnection: \nNon-authorized call of readPacket() method!";
            if (true) {
                throw new ClosedConnectionException(exceptionMessage);
            }
            if (true) {
                throw new ClosedConnectionException();
            }
            return null;
        }

        public void writePacket(byte[] pkt) throws IOException {
            String exceptionMessage = "## PlugTransportConnection: \nNon-authorized call of writePacket(...) method!";
            if (true) {
                throw new ClosedConnectionException(exceptionMessage);
            }
            if (true) {
                throw new ClosedConnectionException();
            }
        }
    }
}
