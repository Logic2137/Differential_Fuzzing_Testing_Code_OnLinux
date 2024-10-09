




import sun.net.ftp.FtpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class TestFtpClientNameListWithNull {

    private static volatile boolean commandHasArgs;

    public static void main(String[] args) throws Exception {
        try (FtpServer server = new FtpServer();
             FtpClient client = FtpClient.create()) {
            (new Thread(server)).start();
            int port = server.getPort();
            client.connect(new InetSocketAddress("localhost", port));
            client.nameList(null);
        } finally {
            if (commandHasArgs) {
                throw new RuntimeException("Test failed. NLST shouldn't have " +
                        "args if nameList parameter is null");
            }
        }
    }

    private static class FtpServer implements AutoCloseable, Runnable {
        private final ServerSocket serverSocket;

        FtpServer() throws IOException {
            serverSocket = new ServerSocket(0);
        }

        public void handleClient(Socket client) throws IOException {
            boolean done = false;
            String str;

            client.setSoTimeout(2000);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.
                    getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println("220 FTP serverSocket is ready.");
            while (!done) {
                try {
                    str = in.readLine();
                } catch (SocketException e) {
                    done = true;
                    continue;
                }
                String cmd = str.substring(0, str.indexOf(" ") > 0 ?
                        str.indexOf(" ") : str.length());
                String args = (cmd.equals(str)) ?
                        "" : str.substring(str.indexOf(" "));
                switch (cmd) {
                    case "QUIT":
                        out.println("221 Goodbye.");
                        out.flush();
                        done = true;
                        break;
                    case "EPSV":
                        if ("all".equalsIgnoreCase(args)) {
                            out.println("200 EPSV ALL command successful.");
                            continue;
                        }
                        out.println("229 Entering Extended Passive Mode " +
                                "(|||" + getPort() + "|)");
                        break;
                    case "NLST":
                        if (args.trim().length() != 0) {
                            commandHasArgs = true;
                        }
                        out.println("200 Command okay.");
                        break;
                    default:
                        out.println("500 unsupported command: " + str);
                }
            }
        }

        public int getPort() {
            if (serverSocket != null) {
                return serverSocket.getLocalPort();
            }
            return 0;
        }

        public void close() throws IOException {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }

        @Override
        public void run() {
            try {
                try (Socket client = serverSocket.accept()) {
                    handleClient(client);
                }
            } catch (IOException e) {
                throw new RuntimeException("Problem in test execution", e);
            }
        }
    }
}
