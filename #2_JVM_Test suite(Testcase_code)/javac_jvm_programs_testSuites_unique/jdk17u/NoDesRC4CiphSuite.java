import java.security.Security;
import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class NoDesRC4CiphSuite {

    private static final boolean DEBUG = false;

    private static final byte RECTYPE_HS = 0x16;

    private static final byte HSMSG_CLIHELLO = 0x01;

    private static final List<Integer> DES_CS_LIST = Arrays.asList(0x0009, 0x0015, 0x0012, 0x001A, 0x0008, 0x0014, 0x0011, 0x0019);

    private static final String[] DES_CS_LIST_NAMES = new String[] { "SSL_RSA_WITH_DES_CBC_SHA", "SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA", "SSL_DH_anon_WITH_DES_CBC_SHA", "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", "SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA" };

    private static final List<Integer> RC4_CS_LIST = Arrays.asList(0xC007, 0xC011, 0x0005, 0xC002, 0xC00C, 0x0004, 0xC016, 0x0018, 0x0003, 0x0017);

    private static final String[] RC4_CS_LIST_NAMES = new String[] { "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA", "TLS_ECDHE_RSA_WITH_RC4_128_SHA", "SSL_RSA_WITH_RC4_128_SHA", "TLS_ECDH_ECDSA_WITH_RC4_128_SHA", "TLS_ECDH_RSA_WITH_RC4_128_SHA", "SSL_RSA_WITH_RC4_128_MD5", "TLS_ECDH_anon_WITH_RC4_128_SHA", "SSL_DH_anon_WITH_RC4_128_MD5", "SSL_RSA_EXPORT_WITH_RC4_40_MD5", "SSL_DH_anon_EXPORT_WITH_RC4_40_MD5" };

    private static final ByteBuffer CLIOUTBUF = ByteBuffer.wrap("Client Side".getBytes());

    public static void main(String[] args) throws Exception {
        boolean allGood = true;
        String disAlg = Security.getProperty("jdk.tls.disabledAlgorithms");
        System.err.println("Disabled Algs: " + disAlg);
        allGood &= testDefaultCase(DES_CS_LIST);
        allGood &= testEngAddDisabled(DES_CS_LIST_NAMES, DES_CS_LIST);
        allGood &= testEngOnlyDisabled(DES_CS_LIST_NAMES);
        allGood &= testDefaultCase(RC4_CS_LIST);
        allGood &= testEngAddDisabled(RC4_CS_LIST_NAMES, RC4_CS_LIST);
        allGood &= testEngOnlyDisabled(RC4_CS_LIST_NAMES);
        if (allGood) {
            System.err.println("All tests passed");
        } else {
            throw new RuntimeException("One or more tests failed");
        }
    }

    private static boolean testDefaultCase(List<Integer> disabledSuiteIds) throws Exception {
        System.err.println("\nTest: Default SSLEngine suite set");
        SSLEngine ssle = makeEngine();
        if (DEBUG) {
            listCiphers("Suite set upon creation", ssle);
        }
        SSLEngineResult clientResult;
        ByteBuffer cTOs = makeClientBuf(ssle);
        clientResult = ssle.wrap(CLIOUTBUF, cTOs);
        if (DEBUG) {
            dumpResult("ClientHello: ", clientResult);
        }
        cTOs.flip();
        boolean foundSuite = areSuitesPresentCH(cTOs, disabledSuiteIds);
        if (foundSuite) {
            System.err.println("FAIL: Found disabled suites!");
            return false;
        } else {
            System.err.println("PASS: No disabled suites found.");
            return true;
        }
    }

    private static boolean testEngOnlyDisabled(String[] disabledSuiteNames) throws Exception {
        System.err.println("\nTest: SSLEngine configured with only disabled suites");
        try {
            SSLEngine ssle = makeEngine();
            ssle.setEnabledCipherSuites(disabledSuiteNames);
            if (DEBUG) {
                listCiphers("Suite set upon creation", ssle);
            }
            SSLEngineResult clientResult;
            ByteBuffer cTOs = makeClientBuf(ssle);
            clientResult = ssle.wrap(CLIOUTBUF, cTOs);
            if (DEBUG) {
                dumpResult("ClientHello: ", clientResult);
            }
            cTOs.flip();
        } catch (SSLHandshakeException shse) {
            System.err.println("PASS: Caught expected exception: " + shse);
            return true;
        }
        System.err.println("FAIL: Expected SSLHandshakeException not thrown");
        return false;
    }

    private static boolean testEngAddDisabled(String[] disabledNames, List<Integer> disabledIds) throws Exception {
        System.err.println("\nTest: SSLEngine with disabled suites added");
        SSLEngine ssle = makeEngine();
        String[] initialSuites = ssle.getEnabledCipherSuites();
        String[] plusDisSuites = Arrays.copyOf(initialSuites, initialSuites.length + disabledNames.length);
        System.arraycopy(disabledNames, 0, plusDisSuites, initialSuites.length, disabledNames.length);
        ssle.setEnabledCipherSuites(plusDisSuites);
        if (DEBUG) {
            listCiphers("Suite set upon creation", ssle);
        }
        SSLEngineResult clientResult;
        ByteBuffer cTOs = makeClientBuf(ssle);
        clientResult = ssle.wrap(CLIOUTBUF, cTOs);
        if (DEBUG) {
            dumpResult("ClientHello: ", clientResult);
        }
        cTOs.flip();
        boolean foundDisabled = areSuitesPresentCH(cTOs, disabledIds);
        if (foundDisabled) {
            System.err.println("FAIL: Found disabled suites!");
            return false;
        } else {
            System.err.println("PASS: No disabled suites found.");
            return true;
        }
    }

    private static SSLEngine makeEngine() throws GeneralSecurityException {
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(null, null, null);
        return ctx.createSSLEngine();
    }

    private static ByteBuffer makeClientBuf(SSLEngine ssle) {
        ssle.setUseClientMode(true);
        ssle.setNeedClientAuth(false);
        SSLSession sess = ssle.getSession();
        ByteBuffer cTOs = ByteBuffer.allocateDirect(sess.getPacketBufferSize());
        return cTOs;
    }

    private static void listCiphers(String prefix, SSLEngine ssle) {
        System.err.println(prefix + "\n---------------");
        String[] suites = ssle.getEnabledCipherSuites();
        for (String suite : suites) {
            System.err.println(suite);
        }
        System.err.println("---------------");
    }

    private static boolean areSuitesPresentCH(ByteBuffer clientHello, List<Integer> suiteIdList) throws IOException {
        byte val;
        val = clientHello.get();
        if (val != RECTYPE_HS) {
            throw new IOException("Not a handshake record, type = " + val);
        }
        clientHello.position(clientHello.position() + 4);
        val = clientHello.get();
        if (val != HSMSG_CLIHELLO) {
            throw new IOException("Not a ClientHello handshake message, type = " + val);
        }
        clientHello.position(clientHello.position() + 3);
        clientHello.position(clientHello.position() + 34);
        int len = Byte.toUnsignedInt(clientHello.get());
        if (len > 32) {
            throw new IOException("Session ID is too large, len = " + len);
        }
        clientHello.position(clientHello.position() + len);
        int csLen = Short.toUnsignedInt(clientHello.getShort());
        if (csLen % 2 != 0) {
            throw new IOException("CipherSuite length is invalid, len = " + csLen);
        }
        int csCount = csLen / 2;
        List<Integer> csSuiteList = new ArrayList<>(csCount);
        log("Found following suite IDs in hello:");
        for (int i = 0; i < csCount; i++) {
            int curSuite = Short.toUnsignedInt(clientHello.getShort());
            log(String.format("Suite ID: 0x%04x", curSuite));
            csSuiteList.add(curSuite);
        }
        boolean foundMatch = false;
        for (Integer cs : suiteIdList) {
            if (csSuiteList.contains(cs)) {
                System.err.format("Found match for suite ID 0x%04x\n", cs);
                foundMatch = true;
                break;
            }
        }
        clientHello.rewind();
        return foundMatch;
    }

    private static void dumpResult(String str, SSLEngineResult result) {
        System.err.println("The format of the SSLEngineResult is: \n" + "\t\"getStatus() / getHandshakeStatus()\" +\n" + "\t\"bytesConsumed() / bytesProduced()\"\n");
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        System.err.println(str + result.getStatus() + "/" + hsStatus + ", " + result.bytesConsumed() + "/" + result.bytesProduced() + " bytes");
        if (hsStatus == HandshakeStatus.FINISHED) {
            System.err.println("\t...ready for application data");
        }
    }

    private static void log(String str) {
        if (DEBUG) {
            System.err.println(str);
        }
    }
}
