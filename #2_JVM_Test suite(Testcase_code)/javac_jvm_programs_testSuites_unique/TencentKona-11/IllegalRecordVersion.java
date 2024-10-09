






import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;

public class IllegalRecordVersion {

    public static void main(String args[]) throws Exception {
        SSLContext context = SSLContext.getDefault();

        SSLEngine cliEngine = context.createSSLEngine();
        cliEngine.setUseClientMode(true);
        SSLEngine srvEngine = context.createSSLEngine();
        srvEngine.setUseClientMode(false);

        SSLSession session = cliEngine.getSession();
        int netBufferMax = session.getPacketBufferSize();
        int appBufferMax = session.getApplicationBufferSize();

        ByteBuffer cliToSrv = ByteBuffer.allocateDirect(netBufferMax);
        ByteBuffer srvIBuff = ByteBuffer.allocateDirect(appBufferMax + 50);
        ByteBuffer cliOBuff = ByteBuffer.wrap("I'm client".getBytes());


        System.out.println("client hello (record version(0xa9, 0xa2))");
        SSLEngineResult cliRes = cliEngine.wrap(cliOBuff, cliToSrv);
        System.out.println("Client wrap result: " + cliRes);
        cliToSrv.flip();
        if (cliToSrv.limit() > 5) {
            cliToSrv.put(1, (byte)0xa9);
            cliToSrv.put(2, (byte)0xa2);
        }

        try {
            srvEngine.unwrap(cliToSrv, srvIBuff);
            throw new Exception(
                "Cannot catch the unsupported record version issue");
        } catch (SSLException e) {
            
        }
    }
}
