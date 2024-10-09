



import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MissingTrailingSpace {

    private static final int noMsgCode = 207;
    private static final String someContext = "/context";

    public static void main(String[] args) throws Exception {
        InetAddress loopback = InetAddress.getLoopbackAddress();
        HttpServer server = HttpServer.create(new InetSocketAddress(loopback, 0), 0);
        try {
            server.setExecutor(Executors.newFixedThreadPool(1));
            server.createContext(someContext, new HttpHandler() {
                @Override
                public void handle(HttpExchange msg) {
                    try {
                        try {
                            msg.sendResponseHeaders(noMsgCode, -1);
                        } catch(IOException ioe) {
                            ioe.printStackTrace();
                        }
                    } finally {
                        msg.close();
                    }
                }
            });
            server.start();
            System.out.println("Server started at port "
                               + server.getAddress().getPort());

            runRawSocketHttpClient(loopback, server.getAddress().getPort());
        } finally {
            ((ExecutorService)server.getExecutor()).shutdown();
            server.stop(0);
        }
        System.out.println("Server finished.");
    }

    static void runRawSocketHttpClient(InetAddress address, int port)
        throws Exception
    {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        final String CRLF = "\r\n";
        try {
            socket = new Socket(address, port);
            writer = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()));
            System.out.println("Client connected by socket: " + socket);

            writer.print("GET " + someContext + "/ HTTP/1.1" + CRLF);
            writer.print("User-Agent: Java/"
                + System.getProperty("java.version")
                + CRLF);
            writer.print("Host: " + address.getHostName() + CRLF);
            writer.print("Accept: */*" + CRLF);
            writer.print("Connection: keep-alive" + CRLF);
            writer.print(CRLF); 
            
            writer.flush();
            System.out.println("Client wrote rquest to socket: " + socket);

            reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
            System.out.println("Client start reading from server:"  );
            String line = reader.readLine();
            if ( !line.endsWith(" ") ) {
                throw new RuntimeException("respond to unknown code "
                    + noMsgCode
                    + " doesn't return space at the end of the first header.\n"
                    + "Should be: " + "\"" + line + " \""
                    + ", but returns: " + "\"" + line + "\".");
            }
            for (; line != null; line = reader.readLine()) {
                if (line.isEmpty()) {
                    break;
                }
                System.out.println("\""  + line + "\"");
            }
            System.out.println("Client finished reading from server"  );
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                    logOrIgnore.printStackTrace();
                }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException logOrIgnore) {
                    logOrIgnore.printStackTrace();
                }
            }
        }
        System.out.println("Client finished." );
    }
}
