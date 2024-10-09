import javax.net.ssl.*;
import java.io.*;
import java.net.*;

public class SSLSocketReset {

    public static void main(String[] args) {
        ServerThread serverThread = null;
        Exception clientException = null;
        try {
            SSLServerSocketFactory sslserversocketfactory = SSLContext.getDefault().getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(0);
            serverThread = new ServerThread(sslServerSocket);
            serverThread.start();
            try {
                Socket socket = new Socket(sslServerSocket.getInetAddress(), sslServerSocket.getLocalPort());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String msg = "Hello";
                out.writeUTF(msg);
                out.flush();
                msg = in.readUTF();
            } catch (Exception e) {
                clientException = e;
                e.printStackTrace();
            }
            serverThread.join();
        } catch (Exception e) {
            throw new RuntimeException("Fails to start SSL server");
        }
        if (serverThread.exception instanceof SSLException && serverThread.exception.getMessage().equals("Unsupported or unrecognized SSL message") && !(clientException instanceof SocketException && clientException.getMessage().equals("Connection reset"))) {
            System.out.println("Test PASSED");
        } else {
            throw new RuntimeException("TCP connection reset");
        }
    }

    private static class ServerThread extends Thread {

        private SSLServerSocket sslServerSocket = null;

        private SSLSocket sslSocket = null;

        Exception exception;

        ServerThread(SSLServerSocket sslServerSocket) {
            this.sslServerSocket = sslServerSocket;
        }

        public void run() {
            try {
                SSLSocket sslsocket = null;
                while (true) {
                    sslsocket = (SSLSocket) sslServerSocket.accept();
                    DataInputStream in = new DataInputStream(sslsocket.getInputStream());
                    DataOutputStream out = new DataOutputStream(sslsocket.getOutputStream());
                    String string;
                    while ((string = in.readUTF()) != null) {
                        out.writeUTF(string);
                        out.flush();
                    }
                }
            } catch (Exception e) {
                exception = e;
                e.printStackTrace();
            }
        }
    }
}
