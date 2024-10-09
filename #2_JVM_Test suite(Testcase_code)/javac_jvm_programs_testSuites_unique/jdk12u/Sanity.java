

import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Enumeration;



public class Sanity {
    public static void main(String[] args) throws Exception {
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            NetworkInterface ni = nifs.nextElement();
            Enumeration<InetAddress> addrs = ni.getInetAddresses();
            while (addrs.hasMoreElements()) {
                InetAddress addr = addrs.nextElement();
                test(addr);
            }
        }
    }

    static void test(InetAddress addr) throws Exception {
        System.out.println(addr.getHostAddress());

        
        ServerSocketChannel ssc = ServerSocketChannel.open();
        try {
            ssc.bind(new InetSocketAddress(addr, 0));
            int port = ((InetSocketAddress)(ssc.getLocalAddress())).getPort();

            
            SocketChannel client = SocketChannel.open();
            try {
                client.connect(new InetSocketAddress(addr, port));
                SocketChannel peer = ssc.accept();
                try {
                    testConnection(Channels.newOutputStream(client),
                                   Channels.newInputStream(peer));
                } finally {
                    peer.close();
                }
            } finally {
                client.close();
            }

            
            client = SocketChannel.open();
            try {
                client.bind(new InetSocketAddress(addr, 0))
                  .connect(new InetSocketAddress(addr, port));
                ssc.accept().close();
            } finally {
                client.close();
            }
        } finally {
            ssc.close();
        }

        
        AsynchronousServerSocketChannel server =
            AsynchronousServerSocketChannel.open();
        try {
            server.bind(new InetSocketAddress(addr, 0));
            int port = ((InetSocketAddress)(server.getLocalAddress())).getPort();

            
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            try {
                client.connect(new InetSocketAddress(addr, port)).get();
                AsynchronousSocketChannel peer = server.accept().get();
                try {
                    testConnection(Channels.newOutputStream(client),
                                   Channels.newInputStream(peer));
                } finally {
                    peer.close();
                }
            } finally {
                client.close();
            }

            
            client = AsynchronousSocketChannel.open();
            try {
                client.bind(new InetSocketAddress(addr, 0))
                  .connect(new InetSocketAddress(addr, port)).get();
                server.accept().get().close();
            } finally {
                client.close();
            }
        } finally {
            server.close();
        }

        
        ServerSocket ss = new ServerSocket();
        try {
            ss.bind(new InetSocketAddress(addr, 0));
            int port = ss.getLocalPort();

            
            Socket s = new Socket();
            try {
                s.connect(new InetSocketAddress(addr, port));
                Socket peer = ss.accept();
                try {
                    testConnection(s.getOutputStream(), peer.getInputStream());
                } finally {
                    peer.close();
                }
            } finally {
                s.close();
            }

            
            s = new Socket();
            try {
                s.bind(new InetSocketAddress(addr, 0));
                s.connect(new InetSocketAddress(addr, port));
                ss.accept().close();
            } finally {
                s.close();
            }
        } finally {
            ss.close();
        }
    }

    static void testConnection(OutputStream out, InputStream in)
        throws IOException
    {
        byte[] msg = "hello".getBytes();
        out.write(msg);

        byte[] ba = new byte[100];
        int nread = 0;
        while (nread < msg.length) {
            int n = in.read(ba);
            if (n < 0)
                throw new IOException("EOF not expected!");
            nread += n;
        }
    }
}
