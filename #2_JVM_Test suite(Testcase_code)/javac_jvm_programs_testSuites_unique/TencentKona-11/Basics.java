



import java.security.*;
import java.io.*;
import java.nio.*;
import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;

public class Basics {

    private static String pathToStores = "../etc";
    private static String keyStoreFile = "keystore";
    private static String trustStoreFile = "truststore";
    private static String passwd = "passphrase";

    private static String keyFilename =
            System.getProperty("test.src", "./") + "/" + pathToStores +
                "/" + keyStoreFile;
    private static String trustFilename =
            System.getProperty("test.src", "./") + "/" + pathToStores +
                "/" + trustStoreFile;

    public static void main(String args[]) throws Exception {

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");
        char[] passphrase = "passphrase".toCharArray();

        ks.load(new FileInputStream(keyFilename), passphrase);
        ts.load(new FileInputStream(trustFilename), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslCtx = SSLContext.getInstance("TLS");

        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLEngine ssle = sslCtx.createSSLEngine();

        System.out.println(ssle);

        String [] suites = ssle.getSupportedCipherSuites();
        String secondSuite = suites[1];
        String [] oneSuites = new String [] { secondSuite };

        printStrings("Supported Ciphersuites", suites);
        printStrings("Enabled Ciphersuites", ssle.getEnabledCipherSuites());
        ssle.setEnabledCipherSuites(oneSuites);
        printStrings("Set Ciphersuites", ssle.getEnabledCipherSuites());

        suites = ssle.getEnabledCipherSuites();
        if ((ssle.getEnabledCipherSuites().length != 1) ||
                !(suites[0].equals(secondSuite))) {
            throw new Exception("set ciphers not what was expected");
        }

        System.out.println();

        String [] protocols = ssle.getSupportedProtocols();
        String secondProtocol = protocols[1];
        String [] oneProtocols = new String [] { protocols[1] };

        printStrings("Supported Protocols", protocols);
        printStrings("Enabled Protocols", ssle.getEnabledProtocols());
        ssle.setEnabledProtocols(oneProtocols);
        printStrings("Set Protocols", ssle.getEnabledProtocols());

        protocols = ssle.getEnabledProtocols();
        if ((ssle.getEnabledProtocols().length != 1) ||
                !(protocols[0].equals(secondProtocol))) {
            throw new Exception("set protocols not what was expected");
        }

        System.out.println("Checking get/setUseClientMode");

        ssle.setUseClientMode(true);
        if (ssle.getUseClientMode() != true) {
            throw new Exception("set/getUseClientMode false");
        }

        ssle.setUseClientMode(false);
        if (ssle.getUseClientMode() != false) {
            throw new Exception("set/getUseClientMode true");
        }


        System.out.println("Checking get/setClientAuth");

        ssle.setNeedClientAuth(false);
        if (ssle.getNeedClientAuth() != false) {
            throw new Exception("set/getNeedClientAuth true");
        }

        ssle.setNeedClientAuth(true);
        if (ssle.getNeedClientAuth() != true) {
            throw new Exception("set/getNeedClientAuth false");
        }

        ssle.setWantClientAuth(true);

        if (ssle.getNeedClientAuth() == true) {
            throw new Exception("set/getWantClientAuth need = true");
        }

        if (ssle.getWantClientAuth() != true) {
            throw new Exception("set/getNeedClientAuth false");
        }

        ssle.setWantClientAuth(false);
        if (ssle.getWantClientAuth() != false) {
            throw new Exception("set/getNeedClientAuth true");
        }

        
        ssle.setUseClientMode(true);

        System.out.println("checking session creation");

        ssle.setEnableSessionCreation(false);
        if (ssle.getEnableSessionCreation() != false) {
            throw new Exception("set/getSessionCreation true");
        }

        ssle.setEnableSessionCreation(true);
        if (ssle.getEnableSessionCreation() != true) {
            throw new Exception("set/getSessionCreation false");
        }

        
        ByteBuffer smallBB = ByteBuffer.allocate(10);

        if (ssle.wrap(smallBB, smallBB).getStatus() !=
                Status.BUFFER_OVERFLOW) {
            throw new Exception("wrap should have overflowed");
        }

        
        
        
        
        
        
        
        
        

        SSLSession ssls = ssle.getSession();

        ByteBuffer appBB =
            ByteBuffer.allocate(ssls.getApplicationBufferSize());
        ByteBuffer netBB =
            ByteBuffer.allocate(ssls.getPacketBufferSize());
        appBB.position(10);

        
        if (ssle.wrap(appBB, netBB).getHandshakeStatus() !=
                HandshakeStatus.NEED_UNWRAP) {
            throw new Exception("initial client hello needs unwrap");
        }

        

        if (ssle.wrap(appBB, netBB).getStatus() !=
                Status.BUFFER_OVERFLOW) {
            throw new Exception("unwrap should have overflowed");
        }

        ByteBuffer ro = appBB.asReadOnlyBuffer();

        System.out.println("checking for wrap/unwrap on RO Buffers");
        try {
            ssle.wrap(netBB, ro);
            throw new Exception("wrap wasn't ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            System.out.println("Caught the ReadOnlyBuffer: " + e);
        }

        try {
            ssle.unwrap(netBB, ro);
            throw new Exception("unwrap wasn't ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            System.out.println("Caught the ReadOnlyBuffer: " + e);
        }

        appBB.position(0);
        System.out.println("Check various UNDERFLOW conditions");

        SSLEngineResult sslER;

        if ((sslER =
                ssle.unwrap(ByteBuffer.wrap(smallSSLHeader),
                appBB)).getStatus() !=
                Status.BUFFER_UNDERFLOW) {
            System.out.println(sslER);
            throw new Exception("unwrap should underflow");
        }

        if ((sslER =
                ssle.unwrap(ByteBuffer.wrap(incompleteSSLHeader),
                appBB)).getStatus() !=
                Status.BUFFER_UNDERFLOW) {
            System.out.println(sslER);
            throw new Exception("unwrap should underflow");
        }

        if ((sslER =
                ssle.unwrap(ByteBuffer.wrap(smallv2Header),
                appBB)).getStatus() !=
                Status.BUFFER_UNDERFLOW) {
            System.out.println(sslER);
            throw new Exception("unwrap should underflow");
        }

        
        try {
            ssle.unwrap(ByteBuffer.wrap(gobblydegook), appBB);
            throw new Exception("Didn't catch the nasty SSLException");
        } catch (SSLException e) {
            System.out.println("caught the nasty SSLException: " + e);
        }

        System.out.println("Test PASSED");

    }

    static byte [] smallSSLHeader = new byte [] {
        (byte) 0x16, (byte) 0x03, (byte) 0x01,
        (byte) 0x05 };

    static byte [] incompleteSSLHeader = new byte [] {
        (byte) 0x16, (byte) 0x03, (byte) 0x01,
        (byte) 0x00, (byte) 0x5,  
        (byte) 0x16, (byte) 0x03, (byte) 0x01, (byte) 0x00 };

    static byte [] smallv2Header = new byte [] {
        (byte) 0x80, (byte) 0x03, (byte) 0x01,
        (byte) 0x00 };

    static byte [] gobblydegook = new byte [] {
        
        (byte) 0x48, (byte) 0x45, (byte) 0x4C, (byte) 0x4C, (byte) 0x20,
        (byte) 0x48, (byte) 0x45, (byte) 0x4C, (byte) 0x4C };

    static void printStrings(String label, String [] strs) {
        System.out.println(label);

        for (int i = 0; i < strs.length; i++) {
            System.out.println("    " + strs[i]);
        }
    }
}
