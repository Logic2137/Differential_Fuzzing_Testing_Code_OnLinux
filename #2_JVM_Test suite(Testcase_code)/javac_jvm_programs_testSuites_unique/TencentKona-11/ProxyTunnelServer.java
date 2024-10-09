import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import javax.net.ServerSocketFactory;
import sun.net.www.MessageHeader;

public class ProxyTunnelServer extends Thread {

    private static ServerSocket ss = null;

    private Socket clientSocket = null;

    private InetAddress serverInetAddr;

    private int serverPort;

    public ProxyTunnelServer() throws IOException {
        if (ss == null) {
            ss = (ServerSocket) ServerSocketFactory.getDefault().createServerSocket(0);
        }
    }

    public void run() {
        try {
            clientSocket = ss.accept();
            processRequests();
        } catch (Exception e) {
            System.out.println("Proxy Failed: " + e);
            e.printStackTrace();
            try {
                ss.close();
            } catch (IOException excep) {
                System.out.println("ProxyServer close error: " + excep);
                excep.printStackTrace();
            }
        }
    }

    private void processRequests() throws Exception {
        InputStream in = clientSocket.getInputStream();
        MessageHeader response = new MessageHeader(in);
        String statusLine = response.getValue(0);
        if (statusLine.startsWith("CONNECT")) {
            retrieveConnectInfo(statusLine);
            respondForConnect();
            doTunnel();
            ss.close();
        } else {
            System.out.println("proxy server: processes only " + "CONNECT method requests, recieved: " + statusLine);
        }
    }

    private void respondForConnect() throws Exception {
        OutputStream out = clientSocket.getOutputStream();
        PrintWriter pout = new PrintWriter(out);
        pout.println("HTTP/1.1 200 OK");
        pout.println();
        pout.flush();
    }

    private void doTunnel() throws Exception {
        Socket serverSocket = new Socket(serverInetAddr, serverPort);
        ProxyTunnel clientToServer = new ProxyTunnel(clientSocket, serverSocket, true);
        ProxyTunnel serverToClient = new ProxyTunnel(serverSocket, clientSocket, false);
        clientToServer.start();
        serverToClient.start();
        clientToServer.join();
        serverToClient.join();
        clientToServer.close();
        serverToClient.close();
    }

    class ProxyTunnel extends Thread {

        Socket sockIn;

        Socket sockOut;

        InputStream input;

        OutputStream output;

        boolean delayedWrite;

        public ProxyTunnel(Socket sockIn, Socket sockOut, boolean delayedWrite) throws Exception {
            this.sockIn = sockIn;
            this.sockOut = sockOut;
            input = sockIn.getInputStream();
            output = sockOut.getOutputStream();
            this.delayedWrite = delayedWrite;
        }

        public void run() {
            int BUFFER_SIZE = 40;
            byte[] buf = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            int count = 0;
            try {
                while ((bytesRead = input.read(buf)) >= 0) {
                    if (delayedWrite) {
                        try {
                            this.sleep(1);
                        } catch (InterruptedException excep) {
                            System.out.println(excep);
                        }
                    }
                    output.write(buf, 0, bytesRead);
                    output.flush();
                    count += bytesRead;
                }
            } catch (IOException e) {
                close();
            }
        }

        public void close() {
            try {
                if (!sockIn.isClosed())
                    sockIn.close();
                if (!sockOut.isClosed())
                    sockOut.close();
            } catch (IOException ignored) {
            }
        }
    }

    void retrieveConnectInfo(String connectStr) throws Exception {
        int starti;
        int endi;
        String connectInfo;
        String serverName = null;
        try {
            starti = connectStr.indexOf(' ');
            endi = connectStr.lastIndexOf(' ');
            connectInfo = connectStr.substring(starti + 1, endi).trim();
            endi = connectInfo.indexOf(':');
            serverName = connectInfo.substring(0, endi);
            serverPort = Integer.parseInt(connectInfo.substring(endi + 1));
        } catch (Exception e) {
            throw new IOException("Proxy recieved a request: " + connectStr);
        }
        serverInetAddr = InetAddress.getByName(serverName);
    }

    public int getPort() {
        return ss.getLocalPort();
    }
}
