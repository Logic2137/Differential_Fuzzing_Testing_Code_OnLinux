import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.nio.*;
import java.nio.channels.*;

public class SSLEngineService {

    private static String keyStoreFile = "keystore";

    private static String trustStoreFile = "truststore";

    private static char[] passphrase = "passphrase".toCharArray();

    private String pathToStores;

    private String keyFilename;

    private String trustFilename;

    protected SSLEngineService() {
        init("../etc");
    }

    protected SSLEngineService(String pathToStores) {
        init(pathToStores);
    }

    private void init(String pathToStores) {
        this.pathToStores = pathToStores;
        this.keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;
        this.trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;
    }

    protected static void deliver(SSLEngine ssle, SocketChannel sc) throws Exception {
        int appBufferMax = ssle.getSession().getApplicationBufferSize();
        int netBufferMax = ssle.getSession().getPacketBufferSize();
        int length = appBufferMax * (Integer.SIZE / 8);
        ByteBuffer localAppData = ByteBuffer.allocate(length);
        ByteBuffer localNetData = ByteBuffer.allocate(netBufferMax / 2);
        localAppData.putInt(length);
        for (int i = 1; i < appBufferMax; i++) {
            localAppData.putInt(i);
        }
        localAppData.flip();
        while (localAppData.hasRemaining()) {
            localNetData.clear();
            SSLEngineResult res = ssle.wrap(localAppData, localNetData);
            switch(res.getStatus()) {
                case OK:
                    localNetData.flip();
                    while (localNetData.hasRemaining()) {
                        if (sc.write(localNetData) < 0) {
                            throw new IOException("Unable write to socket channel");
                        }
                    }
                    if (res.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                        Runnable runnable;
                        while ((runnable = ssle.getDelegatedTask()) != null) {
                            runnable.run();
                        }
                    }
                    if (res.bytesProduced() >= Short.MAX_VALUE) {
                        System.out.println("Generate a " + res.bytesProduced() + " bytes large packet ");
                    }
                    break;
                case BUFFER_OVERFLOW:
                    int size = ssle.getSession().getPacketBufferSize();
                    if (size > localNetData.capacity()) {
                        System.out.println("resize destination buffer upto " + size + " bytes for BUFFER_OVERFLOW");
                        localNetData = enlargeBuffer(localNetData, size);
                    }
                    break;
                default:
                    throw new IOException("Received invalid" + res.getStatus() + "during transfer application data");
            }
        }
    }

    protected static void receive(SSLEngine ssle, SocketChannel sc) throws Exception {
        int appBufferMax = ssle.getSession().getApplicationBufferSize();
        int netBufferMax = ssle.getSession().getPacketBufferSize();
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferMax / 2);
        ByteBuffer peerNetData = ByteBuffer.allocate(netBufferMax / 2);
        int received = -1;
        boolean needToReadMore = true;
        while (received != 0) {
            if (needToReadMore) {
                if (ssle.isInboundDone() || sc.read(peerNetData) < 0) {
                    break;
                }
            }
            peerNetData.flip();
            SSLEngineResult res = ssle.unwrap(peerNetData, peerAppData);
            peerNetData.compact();
            switch(res.getStatus()) {
                case OK:
                    if (res.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                        Runnable runnable;
                        while ((runnable = ssle.getDelegatedTask()) != null) {
                            runnable.run();
                        }
                    }
                    if (received < 0 && res.bytesProduced() < 4) {
                        break;
                    }
                    if (received < 0) {
                        received = peerAppData.getInt(0);
                    }
                    System.out.println("received " + peerAppData.position() + " bytes client application data");
                    System.out.println("\tcomsumed " + res.bytesConsumed() + " byes network data");
                    peerAppData.clear();
                    received -= res.bytesProduced();
                    if (res.bytesConsumed() >= Short.MAX_VALUE) {
                        System.out.println("Consumes a " + res.bytesConsumed() + " bytes large packet ");
                    }
                    needToReadMore = (peerNetData.position() > 0) ? false : true;
                    break;
                case BUFFER_OVERFLOW:
                    int size = ssle.getSession().getApplicationBufferSize();
                    if (size > peerAppData.capacity()) {
                        System.out.println("resize destination buffer upto " + size + " bytes for BUFFER_OVERFLOW");
                        peerAppData = enlargeBuffer(peerAppData, size);
                    }
                    break;
                case BUFFER_UNDERFLOW:
                    size = ssle.getSession().getPacketBufferSize();
                    if (size > peerNetData.capacity()) {
                        System.out.println("resize source buffer upto " + size + " bytes for BUFFER_UNDERFLOW");
                        peerNetData = enlargeBuffer(peerNetData, size);
                    }
                    needToReadMore = true;
                    break;
                default:
                    throw new IOException("Received invalid" + res.getStatus() + "during transfer application data");
            }
        }
    }

    protected static void handshaking(SSLEngine ssle, SocketChannel sc, ByteBuffer additional) throws Exception {
        int appBufferMax = ssle.getSession().getApplicationBufferSize();
        int netBufferMax = ssle.getSession().getPacketBufferSize();
        ByteBuffer localAppData = ByteBuffer.allocate(appBufferMax / 10);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferMax / 10);
        ByteBuffer localNetData = ByteBuffer.allocate(netBufferMax / 10);
        ByteBuffer peerNetData = ByteBuffer.allocate(netBufferMax / 10);
        ssle.beginHandshake();
        SSLEngineResult.HandshakeStatus hs = ssle.getHandshakeStatus();
        byte[] buffer = new byte[0xFF];
        boolean underflow = false;
        do {
            switch(hs) {
                case NEED_UNWRAP:
                    if (peerNetData.position() == 0) {
                        if (additional != null && additional.hasRemaining()) {
                            do {
                                int len = Math.min(buffer.length, peerNetData.remaining());
                                len = Math.min(len, additional.remaining());
                                if (len != 0) {
                                    additional.get(buffer, 0, len);
                                    peerNetData.put(buffer, 0, len);
                                }
                            } while (peerNetData.remaining() > 0 && additional.hasRemaining());
                        } else {
                            if (sc.read(peerNetData) < 0) {
                                ssle.closeInbound();
                                return;
                            }
                        }
                    }
                    if (underflow) {
                        if (sc.read(peerNetData) < 0) {
                            ssle.closeInbound();
                            return;
                        }
                        underflow = false;
                    }
                    peerNetData.flip();
                    SSLEngineResult res = ssle.unwrap(peerNetData, peerAppData);
                    peerNetData.compact();
                    hs = res.getHandshakeStatus();
                    switch(res.getStatus()) {
                        case OK:
                            break;
                        case BUFFER_UNDERFLOW:
                            int size = ssle.getSession().getPacketBufferSize();
                            if (size > peerNetData.capacity()) {
                                System.out.println("resize source buffer upto " + size + " bytes for BUFFER_UNDERFLOW");
                                peerNetData = enlargeBuffer(peerNetData, size);
                            }
                            underflow = true;
                            break;
                        case BUFFER_OVERFLOW:
                            size = ssle.getSession().getApplicationBufferSize();
                            if (size > peerAppData.capacity()) {
                                System.out.println("resize destination buffer upto " + size + " bytes for BUFFER_OVERFLOW");
                                peerAppData = enlargeBuffer(peerAppData, size);
                            }
                            break;
                        default:
                            throw new IOException("Received invalid" + res.getStatus() + "during initial handshaking");
                    }
                    break;
                case NEED_WRAP:
                    localNetData.clear();
                    res = ssle.wrap(localAppData, localNetData);
                    hs = res.getHandshakeStatus();
                    switch(res.getStatus()) {
                        case OK:
                            localNetData.flip();
                            while (localNetData.hasRemaining()) {
                                if (sc.write(localNetData) < 0) {
                                    throw new IOException("Unable write to socket channel");
                                }
                            }
                            break;
                        case BUFFER_OVERFLOW:
                            int size = ssle.getSession().getPacketBufferSize();
                            if (size > localNetData.capacity()) {
                                System.out.println("resize destination buffer upto " + size + " bytes for BUFFER_OVERFLOW");
                                localNetData = enlargeBuffer(localNetData, size);
                            }
                            break;
                        default:
                            throw new IOException("Received invalid" + res.getStatus() + "during initial handshaking");
                    }
                    break;
                case NEED_TASK:
                    Runnable runnable;
                    while ((runnable = ssle.getDelegatedTask()) != null) {
                        runnable.run();
                    }
                    hs = ssle.getHandshakeStatus();
                    break;
                default:
            }
        } while (hs != SSLEngineResult.HandshakeStatus.FINISHED && hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING);
    }

    private static ByteBuffer enlargeBuffer(ByteBuffer buffer, int size) {
        ByteBuffer bb = ByteBuffer.allocate(size);
        buffer.flip();
        bb.put(buffer);
        return bb;
    }

    protected SSLEngine createSSLEngine(boolean mode) throws Exception {
        SSLEngine ssle;
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyFilename), passphrase);
        ts.load(new FileInputStream(trustFilename), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);
        SSLContext sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        ssle = sslCtx.createSSLEngine();
        ssle.setUseClientMode(mode);
        return ssle;
    }
}
