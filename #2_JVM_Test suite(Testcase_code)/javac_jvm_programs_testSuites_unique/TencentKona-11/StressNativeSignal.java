



import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class StressNativeSignal {
    private UDPThread udpThread;
    private ServerSocketThread serverSocketThread;

    StressNativeSignal() {
        try {
            serverSocketThread = new ServerSocketThread();
            serverSocketThread.start();

            udpThread = new UDPThread();
            udpThread.start();
        } catch (Exception z) {
            z.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        StressNativeSignal test = new StressNativeSignal();
        try {
            Thread.sleep(3000);
        } catch (Exception z) {
            z.printStackTrace(System.err);
        }

        test.shutdown();
    }

    public void shutdown() {
        udpThread.terminate();
        try {
            udpThread.join();
        } catch (Exception z) {
            z.printStackTrace(System.err);
        }

        serverSocketThread.terminate();
        try {
            serverSocketThread.join();
        } catch (Exception z) {
            z.printStackTrace(System.err);
        }
    }

    public class ServerSocketThread extends Thread {
        private volatile boolean shouldTerminate;
        private ServerSocket socket;

        public void run() {
            try {
                socket = new ServerSocket(1122);
                Socket client = socket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                shouldTerminate = false;
                while (!shouldTerminate) {
                    String msg = reader.readLine();
                }
            } catch (Exception z) {
                if (!shouldTerminate) {
                    z.printStackTrace(System.err);
                }
            }
        }

        public void terminate() {
            shouldTerminate = true;
            try {
                socket.close();
            } catch (Exception z) {
                z.printStackTrace(System.err);
                
            }
        }
    }

    public class UDPThread extends Thread {
        private DatagramChannel channel;
        private volatile boolean shouldTerminate;

        @Override
        public void run() {
            try {
                channel = DatagramChannel.open();
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 6553600);
                channel.bind(new InetSocketAddress(19870));
            } catch (IOException z) {
                z.printStackTrace(System.err);
            }

            ByteBuffer buf = ByteBuffer.allocate(6553600);
            shouldTerminate = false;
            while (!shouldTerminate) {
                try {
                    buf.rewind();
                    channel.receive(buf);
                } catch (IOException z) {
                    if (!shouldTerminate) {
                        z.printStackTrace(System.err);
                    }
                }
            }
        }

        public void terminate() {
            shouldTerminate = true;
            try {
                channel.close();
            } catch (Exception z) {
                z.printStackTrace(System.err);
                
            }
        }
    }

}
