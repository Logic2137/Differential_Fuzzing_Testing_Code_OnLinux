import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;

public class ArgCheck {

    private static boolean debug = false;

    private static String pathToStores = "../etc";

    private static String keyStoreFile = "keystore";

    private static String trustStoreFile = "truststore";

    private static String passwd = "passphrase";

    private static String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;

    private static String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;

    private static void tryNull(SSLEngine ssle, ByteBuffer appData, ByteBuffer netData) throws Exception {
        try {
            ssle.wrap(appData, netData);
            throw new Exception();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught right exception");
        }
        try {
            ssle.unwrap(netData, appData);
            throw new Exception();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught right exception");
        }
    }

    private static void tryNullArray(SSLEngine ssle, ByteBuffer[] appData, ByteBuffer netData) throws Exception {
        try {
            ssle.wrap(appData, netData);
            throw new Exception();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught right exception");
        }
        try {
            ssle.unwrap(netData, appData);
            throw new Exception();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught right exception");
        }
    }

    private static void tryNullArrayLen(SSLEngine ssle, ByteBuffer[] appData, int offset, int len, ByteBuffer netData) throws Exception {
        try {
            ssle.wrap(appData, offset, len, netData);
            throw new Exception();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught right exception");
        }
        try {
            ssle.unwrap(netData, appData, offset, len);
            throw new Exception();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught right exception");
        }
    }

    private static void tryReadOnly(SSLEngine ssle, ByteBuffer[] appData, int offset, int len, ByteBuffer netData) throws Exception {
        try {
            if (netData.isReadOnly()) {
                ssle.wrap(appData, offset, len, netData);
                throw new Exception();
            }
        } catch (ReadOnlyBufferException e) {
            System.out.println("Caught right exception");
        }
        try {
            if (!netData.isReadOnly()) {
                ssle.unwrap(netData, appData, offset, len);
                throw new Exception();
            }
        } catch (ReadOnlyBufferException e) {
            System.out.println("Caught right exception");
        }
    }

    private static void tryOutOfBounds(SSLEngine ssle, ByteBuffer[] appData, int offset, int len, ByteBuffer netData) throws Exception {
        try {
            ssle.wrap(appData, offset, len, netData);
            throw new Exception();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught right exception");
        }
        try {
            ssle.unwrap(netData, appData, offset, len);
            throw new Exception();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught right exception");
        }
    }

    private static void trySmallBufs(SSLEngine ssle, ByteBuffer appBB, ByteBuffer smallNetBB, ByteBuffer smallAppBB, ByteBuffer netBB) throws Exception {
        SSLEngineResult res = ssle.wrap(appBB, smallNetBB);
        if (res.getStatus() != Status.BUFFER_OVERFLOW) {
            throw new Exception();
        }
    }

    private static void trySmallBufsArray(SSLEngine ssle, ByteBuffer[] appBB, ByteBuffer smallNetBB, ByteBuffer[] smallAppBB, ByteBuffer netBB) throws Exception {
        SSLEngineResult res = ssle.wrap(appBB, 0, appBB.length, smallNetBB);
        if (res.getStatus() != Status.BUFFER_OVERFLOW) {
            throw new Exception();
        }
    }

    private static void runTest(SSLEngine ssle) throws Exception {
        ByteBuffer[] bufs;
        ByteBuffer roBB = ByteBuffer.allocate(40).asReadOnlyBuffer();
        ByteBuffer bb1K = ByteBuffer.allocate(1024);
        ByteBuffer bb2K = ByteBuffer.allocate(2048);
        ByteBuffer bb4K = ByteBuffer.allocate(5096);
        ByteBuffer bb8K = ByteBuffer.allocate(10192);
        SSLSession ssls = ssle.getSession();
        ByteBuffer netBBMinus1 = ByteBuffer.allocate(ssls.getPacketBufferSize() - 1);
        ByteBuffer appBBMinus1 = ByteBuffer.allocate(ssls.getApplicationBufferSize() - 1);
        ByteBuffer bbNet = ByteBuffer.allocate(ssls.getPacketBufferSize());
        ByteBuffer bbApp = ByteBuffer.allocate(ssls.getApplicationBufferSize());
        bufs = new ByteBuffer[] { bb1K, bb2K, bb4K, bb8K };
        tryNull(ssle, null, null);
        tryNull(ssle, bb1K, null);
        tryNull(ssle, null, bb1K);
        tryNullArray(ssle, null, null);
        tryNullArray(ssle, bufs, null);
        tryNullArray(ssle, null, bb1K);
        tryNullArrayLen(ssle, null, 0, 4, null);
        tryNullArrayLen(ssle, bufs, 0, 4, null);
        tryNullArrayLen(ssle, null, 0, 4, bb1K);
        bufs[2] = null;
        tryNullArray(ssle, bufs, bb1K);
        bufs[2] = bb4K;
        tryReadOnly(ssle, bufs, 0, 4, roBB);
        bufs[2] = roBB;
        tryReadOnly(ssle, bufs, 0, 4, bb4K);
        bufs[2] = bb4K;
        tryOutOfBounds(ssle, bufs, -1, 0, bb1K);
        tryOutOfBounds(ssle, bufs, 0, -1, bb1K);
        tryOutOfBounds(ssle, bufs, 0, bufs.length + 1, bb1K);
        tryOutOfBounds(ssle, bufs, bufs.length, 1, bb1K);
        bufs[3].position(bufs[3].limit());
        trySmallBufs(ssle, bb1K, netBBMinus1, appBBMinus1, bb1K);
        trySmallBufsArray(ssle, bufs, netBBMinus1, bufs, bb1K);
        bufs[3].rewind();
    }

    public static void main(String[] args) throws Exception {
        SSLEngine ssle = createSSLEngine(keyFilename, trustFilename);
        runTest(ssle);
        System.out.println("Test Passed.");
    }

    static private SSLEngine createSSLEngine(String keyFile, String trustFile) throws Exception {
        SSLEngine ssle;
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");
        char[] passphrase = "passphrase".toCharArray();
        ks.load(new FileInputStream(keyFile), passphrase);
        ts.load(new FileInputStream(trustFile), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);
        SSLContext sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        ssle = sslCtx.createSSLEngine("client", 1001);
        ssle.setUseClientMode(true);
        return ssle;
    }
}
