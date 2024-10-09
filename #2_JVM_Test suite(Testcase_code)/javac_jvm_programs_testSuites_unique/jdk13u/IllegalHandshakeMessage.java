import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;

public class IllegalHandshakeMessage {

    public static void main(String[] args) throws Exception {
        SSLContext context = SSLContext.getDefault();
        SSLEngine cliEngine = context.createSSLEngine();
        cliEngine.setUseClientMode(true);
        SSLEngine srvEngine = context.createSSLEngine();
        srvEngine.setUseClientMode(false);
        SSLSession session = cliEngine.getSession();
        int netBufferMax = session.getPacketBufferSize();
        int appBufferMax = session.getApplicationBufferSize();
        ByteBuffer cliToSrv = ByteBuffer.allocateDirect(netBufferMax);
        ByteBuffer srvToCli = ByteBuffer.allocateDirect(netBufferMax);
        ByteBuffer srvIBuff = ByteBuffer.allocateDirect(appBufferMax + 50);
        ByteBuffer cliOBuff = ByteBuffer.wrap("I'm client".getBytes());
        ByteBuffer srvOBuff = ByteBuffer.wrap("I'm server".getBytes());
        System.out.println("client hello (handshake type(0xAB))");
        SSLEngineResult cliRes = cliEngine.wrap(cliOBuff, cliToSrv);
        System.out.println("Client wrap result: " + cliRes);
        cliToSrv.flip();
        if (cliToSrv.limit() > 7) {
            cliToSrv.put(5, (byte) 0xAB);
            cliToSrv.put(7, (byte) 0x80);
        } else {
            throw new Exception("No handshage message generated.");
        }
        try {
            SSLEngineResult srvRes = srvEngine.unwrap(cliToSrv, srvIBuff);
            System.out.println("Server unwrap result: " + srvRes);
            runDelegatedTasks(srvRes, srvEngine);
            srvRes = srvEngine.wrap(srvOBuff, srvToCli);
            System.out.println("Server wrap result: " + srvRes);
            throw new Exception("Unsupported handshake message is not handled properly.");
        } catch (SSLException e) {
            System.out.println("Expected exception: " + e);
        }
    }

    private static void runDelegatedTasks(SSLEngineResult result, SSLEngine engine) throws Exception {
        if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            while ((runnable = engine.getDelegatedTask()) != null) {
                System.out.println("\trunning delegated task...");
                runnable.run();
            }
            HandshakeStatus hsStatus = engine.getHandshakeStatus();
            if (hsStatus == HandshakeStatus.NEED_TASK) {
                throw new Exception("handshake shouldn't need additional tasks");
            }
            System.out.println("\tnew HandshakeStatus: " + hsStatus);
        }
    }
}
