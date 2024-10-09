import java.net.*;
import java.io.*;

public class CustomSocketImplFactory implements SocketImplFactory {

    @Override
    public SocketImpl createSocketImpl() {
        try {
            SocketImpl s = new CustomSocketImpl();
            System.out.println("Created " + s);
            return s;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Socket.setSocketImplFactory(new CustomSocketImplFactory());
        try (ServerSocket ss = new ServerSocket(0)) {
            ss.setSoTimeout(1);
            ss.accept();
            System.out.println("PASS");
        } catch (SocketTimeoutException | NullPointerException e) {
        }
    }

    class CustomSocketImpl extends SocketImpl {

        public void create(boolean stream) throws IOException {
        }

        public void connect(String host, int port) throws IOException {
        }

        public void connect(InetAddress addr, int port) throws IOException {
        }

        public void connect(SocketAddress addr, int timeout) throws IOException {
        }

        public void bind(InetAddress host, int port) throws IOException {
        }

        public void listen(int backlog) throws IOException {
        }

        public void accept(SocketImpl s) throws IOException {
        }

        public InputStream getInputStream() throws IOException {
            return null;
        }

        public OutputStream getOutputStream() throws IOException {
            return null;
        }

        public int available() throws IOException {
            return 0;
        }

        public void close() throws IOException {
        }

        public void sendUrgentData(int data) throws IOException {
        }

        public Object getOption(int i) throws SocketException {
            return null;
        }

        public void setOption(int i, Object o) throws SocketException {
        }
    }
}
