import java.io.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;

public class TestMaxLengthDER {

    public static void main(String[] args) throws Exception {
        String message = "Message";
        Signature sig = Signature.getInstance("SHA256withDSA");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom rnd = new SecureRandom();
        rnd.setSeed(1);
        kpg.initialize(2048, rnd);
        KeyPair kp = kpg.generateKeyPair();
        sig.initSign(kp.getPrivate());
        sig.update(message.getBytes());
        byte[] sigData = sig.sign();
        int lengthPos = sigData[3] + 5;
        byte[] modifiedSigData = new byte[sigData.length + 4];
        System.arraycopy(sigData, 0, modifiedSigData, 0, lengthPos);
        System.arraycopy(sigData, lengthPos + 1, modifiedSigData, lengthPos + 5, sigData.length - (lengthPos + 1));
        modifiedSigData[1] += 4;
        modifiedSigData[lengthPos] = (byte) 0x84;
        modifiedSigData[lengthPos + 1] = (byte) 0x7F;
        modifiedSigData[lengthPos + 2] = (byte) 0xFF;
        modifiedSigData[lengthPos + 3] = (byte) 0xFF;
        modifiedSigData[lengthPos + 4] = (byte) 0xFF;
        sig.initVerify(kp.getPublic());
        sig.update(message.getBytes());
        try {
            sig.verify(modifiedSigData);
            throw new RuntimeException("No exception on misencoded signature");
        } catch (SignatureException ex) {
            if (ex.getCause() instanceof EOFException) {
            } else {
                throw ex;
            }
        }
    }
}
